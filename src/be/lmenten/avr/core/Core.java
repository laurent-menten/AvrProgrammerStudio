// = ======================================================================== =
// = === AVR Programmer Studio ======= Copyright (c) 2020+ Laurent Menten === =
// = ======================================================================== =
// = = This program is free software: you can redistribute it and/or modify = =
// = = it under the terms of the GNU General Public License as published by = =
// = = the Free Software Foundation, either version 3 of the License, or    = =
// = = (at your option) any later version.                                  = =
// = =                                                                      = =
// = = This program is distributed in the hope that it will be useful, but  = =
// = = WITHOUT ANY WARRANTY; without even the implied warranty of           = =
// = = MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU    = =
// = = General Public License for more details.                             = =
// = =                                                                      = =
// = = You should have received a copy of the GNU General Public License    = =
// = = along with this program. If not, see                                 = =
// = = <https://www.gnu.org/licenses/>.                                     = =
// = ======================================================================== =

package be.lmenten.avr.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import be.lmenten.avr.assembler.AvrRegister;
import be.lmenten.avr.assembler.AvrRegisterIndex;
import be.lmenten.avr.assembler.def.AvrRegistersXYZ;
import be.lmenten.avr.core.descriptor.CoreDescriptor;
import be.lmenten.avr.core.descriptor.CoreFeatures;
import be.lmenten.avr.core.descriptor.CoreMemory;
import be.lmenten.avr.core.descriptor.CoreRegisterDescriptor;
import be.lmenten.avr.core.event.CoreEvent;
import be.lmenten.avr.core.event.CoreEventListener;
import be.lmenten.avr.core.event.CoreEventType;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionData;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.branch.JMP;
import be.lmenten.ihex.IntelHexReader;
import be.lmenten.utils.StringUtils;
import be.lmenten.utils.logging.LogFormatter;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class Core
	implements CoreModel
{
	public static final String SYSTEM_CONFIG_TRACE_LEVEL
		= "be.lmenten.avr.core.traceLevel";

	public static final String CONFIG_EXTERNAL_SRAM
		= "external.sram.size";

	// -------------------------------------------------------------------------

	private final CoreDescriptor cdesc;

	// -------------------------------------------------------------------------
	// - Memories --------------------------------------------------------------
	// -------------------------------------------------------------------------

	private final CoreRegister [] fuses;

	private final CoreRegister [] lockBits;

	// -------------------------------------------------------------------------

	private final Instruction [] flash;	
	private final Map<Integer,String> flashSymbolsByAddresse
		= new HashMap<>();

	private final CoreData sram [];
	private final Map<Integer,String> sramSymbolsByAddresse
		= new HashMap<>();

	private final CoreData eeprom [];
	private final Map<Integer,String> eepromSymbolsByAddresse
		= new HashMap<>();

	// -------------------------------------------------------------------------
	// - Registers shortcuts ---------------------------------------------------
	// -------------------------------------------------------------------------

	public final CoreStatusRegister SREG;
	public final CoreRegister MCUSR;
	public final CoreRegister MCUCR;

	public final CoreRegister SPH;
	public final CoreRegister SPL;

	// -------------------------------------------------------------------------

	public final CoreRegister XMCRA;
	public final CoreRegister XMCRB;

	public final CoreRegister OSCCAL;
	public final CoreRegister CLKPR;
	
	public final CoreRegister SMCR;
	public final CoreRegister PRR0;
	public final CoreRegister PRR1;
	
	// -------------------------------------------------------------------------

	public final CoreRegister RAMPD;
	public final CoreRegister RAMPX;
	public final CoreRegister RAMPY;
	public final CoreRegister RAMPZ;
	public final CoreRegister EIND;

	// -------------------------------------------------------------------------
	// - Configuration ---------------------------------------------------------
	// -------------------------------------------------------------------------

	private Level traceLevel = Level.FINE;

	// -------------------------------------------------------------------------
	
	private int externalSramSize = 0;

	// -------------------------------------------------------------------------
	// - Runtime ---------------------------------------------------------------
	// -------------------------------------------------------------------------

	private boolean ioDebugRegisterDirty = false;

	private long clockCyclesCounter;
	
	private int programCounter;

	private int [] pendingInterrupts;

	private RunningMode coreMode = RunningMode.STOPPED;

	// -------------------------------------------------------------------------
	// - Events ----------------------------------------------------------------
	// -------------------------------------------------------------------------

	private final List<CoreEventListener> coreEventListener
		= new ArrayList<>();

	// =========================================================================
	// === CONSTRUCTOR(S) ======================================================
	// =========================================================================

	/**
	 * 
	 * @param cdesc
	 */
	public Core( CoreDescriptor cdesc )
	{
		this( cdesc, null );
	}

	/**
	 * 
	 * @param cdesc
	 * @param config
	 */
	public Core( CoreDescriptor cdesc, Properties config )
	{
		this.cdesc = cdesc;

		log.info( "Core: " + cdesc.getPartName() );

		StringJoiner sj = new StringJoiner( ", " );
		for( CoreFeatures feature : cdesc.getFeatures() )
		{
			sj.add( feature.toString() );
		}
		log.fine( " > features : " + sj );
		
		// ----------------------------------------------------------------------
		// - GENERAL CONFIGURATION ----------------------------------------------
		// ----------------------------------------------------------------------

		parseSystemConfig();

		LogFormatter.setLevel( traceLevel );

		// ----------------------------------------------------------------------
		// - FUSES / LOCKBITS ---------------------------------------------------
		// ----------------------------------------------------------------------

		log.fine( " > fuse bytes : " + cdesc.getFusesCount() );

		fuses = new CoreRegister [ cdesc.getFusesCount() ];
		cdesc.exportFuses( (addr, rdesc) -> 
		{
			fuses[ addr ] = new CoreRegister( rdesc );
			fuses[ addr ].setAddress( addr );
		} );

		// ----------------------------------------------------------------------

		log.fine( " > lockbits bytes : " + cdesc.getLockBitsCount() );

		lockBits = new CoreRegister [ cdesc.getLockBitsCount() ];
		cdesc.exportLockBits( (addr, rdesc) ->
		{
			lockBits[ addr ] = new CoreRegister( rdesc );
			lockBits[ addr ].setAddress( addr );
		} );
		
		// ----------------------------------------------------------------------
		// - CORE RUNTIME CONFIGURATION -----------------------------------------
		// ----------------------------------------------------------------------

		parseConfig( config );

		// ----------------------------------------------------------------------
		// - FLASH --------------------------------------------------------------
		// ----------------------------------------------------------------------

		log.info( "Flash size = " + cdesc.getFlashSize() + " bytes" );

		flash = new Instruction [ cdesc.getFlashSize() / 2 ];
		for( int addr = 0 ; addr < flash.length ; addr++ )
		{
			flash[ addr ] = null;
		}

		if( supportsBootLoaderSection() )
		{
			log.fine( " > BLS address = " + String.format( "0x%06X", getBootLoaderSectionBase() ) );
			log.fine( " > BLS size = " + getBootLoaderSectionSize() + " bytes" );
		}

		// ----------------------------------------------------------------------
		// - SRAM ---------------------------------------------------------------
		// ----------------------------------------------------------------------

		if( supportsExternalMemoryFeature() )
		{
			log.info( "Sram size = " + cdesc.getOnChipSramSize()
				+ " + " + externalSramSize + " bytes" );
		}
		else
		{
			log.info( "Sram size = " + cdesc.getOnChipSramSize() + " bytes" );
		}

		sram = new CoreData [ cdesc.getOnChipSramSize() + externalSramSize ];
		for( int addr = 0 ; addr < sram.length ; addr++ )
		{
			sram[ addr ] = null;
		}

		// - R0 .. Rxx ----------------------------------------------------------

		cdesc.exportRegisters( ( addr, rdesc ) ->
		{
			CoreRegister reg = new CoreRegister( rdesc ); 
			reg.setAddress( addr );

			sram[ addr ] = reg;			
		} );
		
		log.fine( " > " + cdesc.getRegistersCount() + " general registers");
		
		// - I/O registers ------------------------------------------------------

		cdesc.exportIoRegisters( ( addr, rdesc ) ->
		{
			CoreRegister reg;
	
			if( rdesc.getName().equals( "SREG" ) )
				reg = new CoreStatusRegister( rdesc );
			else
				reg = new CoreRegister( rdesc ); 

			reg.setAddress( addr );

			sram[ addr ] = reg;
		} );

		log.fine( " > " + cdesc.getIoRegistersCount() + " I/O registers");

		// - Extended I/O registers ---------------------------------------------

		cdesc.exportExtendedIoRegisters( ( addr, rdesc ) ->
		{
			CoreRegister reg = new CoreRegister( rdesc ); 
			reg.setAddress( addr );

			sram[ addr ] = reg;
		} );

		log.fine( " > " + cdesc.getExtendedIoRegistersCount() + " extended I/O registers");

		// - Internal memory ----------------------------------------------------

		for( int addr = 0 ; addr < cdesc.getSramSize() ; addr++ )
		{
			CoreData data = new CoreData(); 
			data.setAddress( cdesc.getSramBase() + addr );

			sram[ cdesc.getSramBase() + addr ] = data;
		}

		// - External (off-chip) memory -----------------------------------------

		if( supportsExternalMemoryFeature() )
		{
			for( int addr = 0 ; addr < externalSramSize ; addr++ )
			{
				CoreData data = new CoreData(); 
				data.setAddress( cdesc.getExternalSramBase() + addr );

				sram[ cdesc.getExternalSramBase() + addr ] = data;
			}
		}

		// ----------------------------------------------------------------------
		// - EEPROM -------------------------------------------------------------
		// ----------------------------------------------------------------------

		log.info( "Eeprom size = " + cdesc.getEepromSize() + " bytes" );

		eeprom = new CoreData [ cdesc.getEepromSize() ];
		for( int addr = 0 ; addr < eeprom.length ; addr++ )
		{
			CoreData data = new CoreData(); 
			data.setAddress( cdesc.getExternalSramBase() + addr );

			eeprom[ addr ] = data;
		}

		// ----------------------------------------------------------------------
		// - Registers shortcuts ------------------------------------------------
		// ----------------------------------------------------------------------

		CoreRegisterDescriptor rdesc;

		rdesc = cdesc.getRegisterDescriptor( "SREG" );
		SREG = (CoreStatusRegister) sram[ rdesc.getAddress() ];

		rdesc = cdesc.getRegisterDescriptor( "MCUCR" );
		MCUCR = (CoreRegister) sram[ rdesc.getAddress() ];

		rdesc = cdesc.getRegisterDescriptor( "MCUSR" );
		MCUSR = (CoreRegister) sram[ rdesc.getAddress() ];

		// ----------------------------------------------------------------------

		rdesc = cdesc.getRegisterDescriptor( "SPL" );
		SPL = (CoreRegister) sram[ rdesc.getAddress() ];

		rdesc = cdesc.getRegisterDescriptor( "SPH" );
		if( rdesc != null )
		{
			SPH = (CoreRegister) sram[ rdesc.getAddress() ];
		}
		else
			SPH = null;

		// TODO add CCP from Xmega architecture

		// ----------------------------------------------------------------------

		rdesc = cdesc.getRegisterDescriptor( "OSCCAL" );
		OSCCAL = (CoreRegister) sram[ rdesc.getAddress() ];

		rdesc = cdesc.getRegisterDescriptor( "CLKPR" );
		CLKPR = (CoreRegister) sram[ rdesc.getAddress() ];
		
		rdesc = cdesc.getRegisterDescriptor( "SMCR" );
		SMCR = (CoreRegister) sram[ rdesc.getAddress() ];

		rdesc = cdesc.getRegisterDescriptor( "PRR0" );
		PRR0 = (rdesc != null) ? (CoreRegister) sram[ rdesc.getAddress() ] : null;

		rdesc = cdesc.getRegisterDescriptor( "PRR1" );
		PRR1 = (rdesc != null) ? (CoreRegister) sram[ rdesc.getAddress() ] : null;

		// ----------------------------------------------------------------------

		if( supportsExternalMemoryFeature() )
		{
			rdesc = cdesc.getRegisterDescriptor( "XMCRA" );
			XMCRA = (CoreRegister) sram[ rdesc.getAddress() ];

			rdesc = cdesc.getRegisterDescriptor( "XMCRB" );
			XMCRB = (CoreRegister) sram[ rdesc.getAddress() ];
		}
		else
		{
			XMCRA = null;
			XMCRB = null;
		}

		// ----------------------------------------------------------------------

		rdesc = cdesc.getRegisterDescriptor( "RAMPD" );
		if( rdesc != null )
		{
			RAMPD = (CoreRegister) sram[ rdesc.getAddress() ];
		}
		else
			RAMPD = null;
		
		rdesc = cdesc.getRegisterDescriptor( "RAMPX" );
		if( rdesc != null )
		{
			RAMPX = (CoreRegister) sram[ rdesc.getAddress() ];
		}
		else
			RAMPX = null;

		rdesc = cdesc.getRegisterDescriptor( "RAMPY" );
		if( rdesc != null )
		{
			RAMPY = (CoreRegister) sram[ rdesc.getAddress() ];
		}
		else
			RAMPY = null;

		rdesc = cdesc.getRegisterDescriptor( "RAMPZ" );
		if( rdesc != null )
		{
			RAMPZ = (CoreRegister) sram[ rdesc.getAddress() ];
		}
		else
			RAMPZ = null;

		rdesc = cdesc.getRegisterDescriptor( "EIND" );
		if( rdesc != null )
		{
			EIND = (CoreRegister) sram[ rdesc.getAddress() ];
		}
		else
			EIND = null;

		// ----------------------------------------------------------------------
		// - Finalisation -------------------------------------------------------
		// ----------------------------------------------------------------------

		reset( ResetSources.POWER_ON );
	}

	/**
	 * 
	 */
	private void parseSystemConfig()
	{
		String tmp;

		// ----------------------------------------------------------------------

		tmp = System.getProperty( SYSTEM_CONFIG_TRACE_LEVEL );
		if( tmp != null )
		{
			traceLevel = Level.parse( tmp );
		}
	}

	/**
	 * 
	 * @param config
	 */
	private void parseConfig( Properties config )
	{
		if( config == null )
		{
			return;
		}

		String tmp;

		// ----------------------------------------------------------------------

		if( supportsExternalMemoryFeature() )
		{
			tmp = config.getProperty( CONFIG_EXTERNAL_SRAM );
			if( tmp != null )
			{
				externalSramSize = (int) StringUtils.parseNumber( tmp );
				if( (externalSramSize < 0)
						|| (externalSramSize > cdesc.getMaxExternalSramSize()) )
				{
					System.out.println( "external sram size !!!" );
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public Properties getDefaultConfig()
	{
		Properties config = new Properties();

		if( supportsExternalMemoryFeature() )
		{
			config.put( CONFIG_EXTERNAL_SRAM, 0 );
		}

		return config;
	}
	
	// =========================================================================
	// === DEBUG HELPERS =======================================================
	// =========================================================================

	public void ioDebugRegisterDirty( boolean ioDebugRegisterDirty )
	{
		this.ioDebugRegisterDirty = ioDebugRegisterDirty;
	}

	public boolean ioDebugRegisterDirty()
	{
		return ioDebugRegisterDirty;
	}

	// =========================================================================
	// === Events ==============================================================
	// =========================================================================

	public void addCoreEventListener( CoreEventListener listener )
	{
		if( coreEventListener.contains( listener ) )
		{
			return;
		}

		coreEventListener.add( listener );
	}

	public void removeCoreEventListener( CoreEventListener listener )
	{
		if( ! coreEventListener.contains( listener ) )
		{
			return;
		}

		coreEventListener.remove( listener );
	}

	public void fireCoreEvent( CoreEvent event )
	{
		coreEventListener.forEach( listener -> listener.onEvent( event ) );

		if( event.shouldAbort() )
		{
			// TODO throw a better exception to abort
			throw new RuntimeException( "Aborted" );
		}
	}

	// =========================================================================
	// === Descriptor ==========================================================
	// =========================================================================

	@Override
	public CoreDescriptor getDescriptor()
	{
		return cdesc;
	}

	// -------------------------------------------------------------------------

	@Override
	public boolean supportsBootLoaderSection()
	{
		return cdesc.hasFeature( CoreFeatures.BOOT_LOADER );
	}

	@Override
	public int getApplicationSectionBase()
	{
		if( supportsBootLoaderSection() )
		{
			return 0x00000;
		}

		throw new UnsupportedOperationException( "MCU has no bootloader support" );
	}

	@Override
	public int getApplicationSectionSize()
	{
		if( supportsBootLoaderSection() )
		{
			return cdesc.getFlashSize() - getBootLoaderSectionSize();
		}

		throw new UnsupportedOperationException( "MCU has no bootloader support" );
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getBootLoaderSectionBase()
	{
		if( supportsBootLoaderSection() )
		{
			return cdesc.getFlashSize() - getBootLoaderSectionSize();
		}

		throw new UnsupportedOperationException( "MCU has no bootloader support" );
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getBootLoaderSectionSize()
	{
		if( supportsBootLoaderSection() )
		{
			CoreRegister fuse = getFuseByte( "HIGH_BYTE" ) ;

			int bootsz = fuse.mask( "BOOTSZ1", "BOOTSZ0" ) >> 1;

			return cdesc.getBootLoaderSection( bootsz ).getRangeSize();
		}

		throw new UnsupportedOperationException( "MCU has no bootloader support" );
	}

	// -------------------------------------------------------------------------

	@Override
	public boolean supportsExternalMemoryFeature()
	{
		return cdesc.hasFeature( CoreFeatures.EXTERNAL_SRAM );
	}

	@Override
	public int getExternalSramSize()
	{
		if( supportsExternalMemoryFeature() )
		{
			return externalSramSize;
		}

		throw new UnsupportedOperationException( "MCU has no external memory support" );
	}

	// =========================================================================
	// === PROGRAM =============================================================
	// =========================================================================

	@Override
	public int getProgramCounter()
	{
		return programCounter;
	}

	public void setProgramCounter( int programCounter )
	{
		this.programCounter = programCounter;
	}

	public void updateProgramCounter( int offset )
	{
		this.programCounter += offset ;
	}

	// -------------------------------------------------------------------------

	@Override
	public Instruction getCurrentInstruction()
	{
		return flash[ programCounter ];
	}

	public Instruction getFollowingInstruction()
	{
		Instruction current = getCurrentInstruction();
		if( current != null )
		{
			return flash[ programCounter + current.getOpcodeSize() ];
		}

		return null;
	}

	// -------------------------------------------------------------------------

	public int getInterruptVectorAddress( int vector )
	{
		int address = 0x00000;
		if( MCUCR.bit( "IVSEL" ) )
		{
			CoreRegister fuse = getFuseByte( "HIGH_BYTE" ) ;

			int bootsz = fuse.mask( "BOOTSZ1", "BOOTSZ0" ) >> 1;
			address = cdesc.getBootLoaderSection( bootsz ).getRangeBase();
		}

		return address + (vector * cdesc.getInterruptVectorSize());
	}

	// -------------------------------------------------------------------------

	public void pushProgramCounter()
	{
		int value = programCounter;

		for( int i = cdesc.getProgramCounterWidth() ; i > 0 ; i -= 8 )
		{
			byte b = (byte) (value & 0xFF);
			push( b );
			
			value >>= 8;
		}
	}

	public void popProgramCounter()
	{
		int value = 0x00000;

		for( int i = cdesc.getProgramCounterWidth() ; i > 0 ; i -= 8 )
		{
			byte b = pop();
			
			value = (value << 8) | (b & 0xFF);
		}

		programCounter = value;
	}

	// =========================================================================
	// === LOADER ==============================================================
	// =========================================================================

	/**
	 * 
	 * @param address
	 */
	public void installFakeBootLoader()
	{
		int blsAddress = getBootLoaderSectionBase();
		int pc =  blsAddress / 2;

		if( flash[pc] == null )
		{
			JMP jumpToZero = new JMP( 0x000000 );
			jumpToZero.setAddress( blsAddress );

			flash[pc++] = jumpToZero;
			flash[pc++] = jumpToZero.getSecondWord();
		}
	}

	// -------------------------------------------------------------------------

	@Deprecated
	public void loadProgram( Consumer<Instruction []> loader )
	{
		loader.accept( flash );
	}

	// -------------------------------------------------------------------------

	public void loadFlash( File file )
		throws IOException
	{
		try( IntelHexReader r = new IntelHexReader( file ) )
		{
			while( ! r.eof() )
			{
				int address = (int) r.getAddress();
				int opcode = r.readWord();

				InstructionSet entry = InstructionSet.getInstructionSetEntry( opcode );
				if( entry != null )
				{
					if( entry.getOpcodeWidth() == 32 )
					{
						opcode = ((opcode & 0xFFFF) << 16) + (r.readWord() & 0xFFFF);
					}

					Instruction instruction = entry.getInstance( opcode );
					instruction.setAddress( address );

					if( flash[(address/2)] != null )
					{
						log.warning( "Overwritting flash at address "
							+ String.format( "%06X", address ) );

						CoreEvent event
							= new CoreEvent( CoreEventType.FLASH_MEMORY_OVERWRITTING, this );

						fireCoreEvent( event );
					}

					flash[(address/2)] = instruction;

					if( entry.getOpcodeWidth() == 32 )
					{
						Instruction instruction2 = instruction.getSecondWord();
						instruction2.setAddress( address + 1 );

						flash[(address/2) + 1] = instruction2;
					}
				}
				else
				{
					flash[(address/2)] = new InstructionData( opcode );
					flash[(address/2)].setAddress( address );
				}
			}
		}
	}

	/**
	 * 
	 * @param file
	 */
	public void loadEeprom( File file )
		throws IOException
	{
		try( IntelHexReader r = new IntelHexReader( file ) )
		{
			while( ! r.eof() )
			{
				int address = (int) r.getAddress();
				int value = r.readByte();

				CoreData data = new CoreData( (byte) value );
				data.setAddress( address );

				eeprom[address] = data;
			}
		}
	}

	// ========================================================================
	// === STACK ==============================================================
	// ========================================================================

	public void push( byte value )
	{
		int address = 0x0000;		
		if( SPH != null )
		{
			address = (SPH.privGetData() & 0xFF) << 8;
		}
		address |= (SPL.privGetData() & 0xFF);

		sram[ address-- ].privSetData( value );

		SPL.privSetData( (byte) (address & 0xFF) ); 
		if( SPH != null )
		{
			SPH.privSetData( (byte) ((address >> 8) & 0xFF) );
		} 
	}

	public byte pop()
	{
		int address = 0x0000;		
		if( SPH != null )
		{
			address = (SPH.privGetData() & 0xFF) << 8;
		}
		address |= (SPL.privGetData() & 0xFF);

		byte value = (byte) sram[ ++address ].privGetData();

		SPL.privSetData( (byte) (address & 0xFF) ); 
		if( SPH != null )
		{
			SPH.privSetData( (byte) ((address >> 8) & 0xFF) );
		} 

		return value;
	}

	// ========================================================================
	// === CLOCK ==============================================================
	// ========================================================================

	@Override
	public long getClockCyclesCounter()
	{
		return clockCyclesCounter;
	}

	public void updateClockCyclesCounter( long clockCyclesCount )
	{
		this.clockCyclesCounter += clockCyclesCount;
	}

	// ========================================================================
	// === RUNNING MODE =======================================================
	// ========================================================================

	@Override
	public RunningMode getCoreMode()
	{
		return coreMode;
	}

	public synchronized void setCoreMode( RunningMode coreMode )
	{
		this.coreMode = coreMode;
	}

	// ========================================================================
	// === EXECUTION ==========================================================
	// ========================================================================

	public void step()
	{
		// --------------------------------------------------------------------
		// - Interrupts -------------------------------------------------------
		// --------------------------------------------------------------------

		if( SREG.bit( "I" ) )
		{
			// FIXME handle interrupts
		}

		// --------------------------------------------------------------------
		// - 
		// --------------------------------------------------------------------

		Instruction current = getCurrentInstruction();
		if( current != null )
		{
			current.execute( this );
		}
	}

	public void run()
	{
		while( coreMode == RunningMode.RUNNING )
		{
			step();
		}
	}

	// ------------------------------------------------------------------------

	public void interrupt( int vector )
	{
		pendingInterrupts[vector] ++;
	}

	// ------------------------------------------------------------------------

	public void reset( ResetSources resetSource )
	{
		clockCyclesCounter = 0l;

		// --------------------------------------------------------------------
		// --- RESET ISR address ----------------------------------------------	
		// --------------------------------------------------------------------

		if( supportsBootLoaderSection() )
		{
			CoreRegister fuse = getFuseByte( "HIGH_BYTE" ) ;

			if( fuse.bit( "BOOTRST" ) )	// BOOTRST not programmed
			{
				programCounter = 0x0000;
			}

			else // BOOTRST programmed
			{
				int address = getBootLoaderSectionBase();

				programCounter = address / 2;
			}
		}
		else
		{
			programCounter = 0x0000;
		}

		// --------------------------------------------------------------------
		// --- SRAM default values --------------------------------------------
		// --------------------------------------------------------------------

		for( int i = 0 ; i < sram.length ; i++ )
		{
			if( sram[i] != null )
			{
				sram[i].reset();
			}
		}

		// --------------------------------------------------------------------
		// --- Set reset source flag in MCUSR --------------------------------- 
		// --------------------------------------------------------------------

		switch( resetSource )
		{
			case POWER_ON:
				MCUSR.bit( "PORF", true );
				break;
				
			case EXTERNAL:
				MCUSR.bit( "EXTRF", true );
				break;
				
			case BROWN_OUT:
				MCUSR.bit( "BORF", true );
				break;
				
			case WATCHDOG:
				MCUSR.bit( "WDRF", true );
				break;
				
			case JTAG:
				MCUSR.bit( "JTRF", true );
				break;				
		}

		// --------------------------------------------------------------------
		// ---
		// --------------------------------------------------------------------

		SREG.bit( "I", false );

		// --------------------------------------------------------------------

		// TODO reset components

		// --------------------------------------------------------------------
		// ---
		// --------------------------------------------------------------------

		coreMode = RunningMode.RUNNING;
	}

	// ========================================================================
	// === REGISTERS ==========================================================
	// ========================================================================

	/**
	 * Get a General Purpose Register (R0-R31) by it register number
	 * 
	 * @param r
	 * @return
	 */
	public CoreRegister getGeneralRegister( AvrRegister r )
	{
		int rIndex = r.getIndex();
		if( rIndex >= cdesc.getRegistersCount() )
		{
			throw new IllegalArgumentException();
		}

		return (CoreRegister) sram[ cdesc.getRegistersBase() + rIndex ];
	}

	/**
	 * Get an I/O Register by its address as passed to IN/OUT (0..63) or
	 * CBI/SBI/SBIC/SBIS (0..31) instructions.
	 * 
	 * @param address
	 * @return
	 */
	public CoreRegister getIORegisterByAddress( int address )
	{
		if( (address < 0) || (address >= cdesc.getIoRegistersCount()) )
		{
			throw new IllegalArgumentException();
		}

		return (CoreRegister) sram[ cdesc.getIoRegistersBase() + address ];
	}

	/**
	 * Get an I/O Register by its physical address.
	 * 
	 * @param address
	 * @return
	 */
	public CoreRegister getIORegisterByPhysicalAddress( int address )
	{
		if( (address < cdesc.getIoRegistersBase())
				|| (address >= cdesc.getSramBase()) )
		{
			throw new IllegalArgumentException( "address = " + address );
		}

		return (CoreRegister) sram[ address ];
	}

	/**
	 * Get a register by its name (General, IO or extended IO).
	 * 
	 * @param name
	 * @return
	 */
	public CoreRegister getRegisterByName( String name )
	{
		CoreRegisterDescriptor rdesc = cdesc.getRegisterDescriptor( name );
		if( rdesc == null )
		{
			return null;
		}
		
		return (CoreRegister) sram[ rdesc.getAddress() ];
	}

	// ------------------------------------------------------------------------

	/**
	 * Get Fuse byte by its name.
	 * 
	 * @param name
	 * @return
	 */
	public CoreRegister getFuseByte( String name )
	{
		CoreRegisterDescriptor rdesc = cdesc.getFuseDescriptor( name );
		if( rdesc == null )
		{
			return null;
		}

		return fuses[ rdesc.getAddress() ];
	}

	/**
	 * Get Lock Bits byte by its name.
	 * 
	 * @param name
	 * @return
	 */
	public CoreRegister getLockBits( String name )
	{
		CoreRegisterDescriptor rdesc = cdesc.getLockBitsDescriptor( name );
		if( rdesc == null )
		{
			return null;
		}

		return fuses[ rdesc.getAddress() ];
	}

	// ========================================================================
	// === MEMORY ACCESS ======================================================
	// ========================================================================

	public Instruction getFlashCell( int address )
	{
		if( ! checkFlashAddress(address) )
		{
			throw new IndexOutOfBoundsException(address);
		}
		
		if( flash[address] == null )
		{
			flash[address] = new InstructionData();
		}

		return flash[address];
	}

	public boolean checkFlashAddress( int address )
	{
		return (address >= 0) && (address < (cdesc.getFlashSize()/2));		
	}

	// ------------------------------------------------------------------------

	public CoreData getSramCell( int address )
	{
		if( ! checkSramAddress(address) )
		{
			throw new IndexOutOfBoundsException(address);
		}
		
		if( sram[address] == null )
		{
			sram[address] = new CoreData();
		}

		return sram[address];
	}

	public boolean checkSramAddress( int address )
	{
		if( supportsExternalMemoryFeature() )
		{	
			if( XMCRA.bit( "SRE" ) )
			{
				return (address >= 0) && (address < sram.length);
			}
		}

		return (address >= 0) && (address < cdesc.getOnChipSramSize());
	}

	public int maskExternalMemoryAddress( int address )
	{
		if( supportsExternalMemoryFeature() )
		{	
			if( XMCRA.bit( "SRE" ) )
			{
				@SuppressWarnings("unused")
				byte mask = XMCRB.mask( "XMM2", "XMM1", "XMM0" );

				// TODO Handle external memory masking.

				return address;
			}
		}

		return address;
	}

	// ------------------------------------------------------------------------

	public CoreData getEepromCell( int address )
	{
		if( ! checkSramAddress(address) )
		{
			throw new IndexOutOfBoundsException(address);
		}

		if( eeprom[address] == null )
		{
			eeprom[address] = new CoreData();
		}

		return eeprom[address];
	}

	public boolean checkEepromAddress( int address )
	{
		return (address >= 0) && (address < cdesc.getEepromSize());
	}

	// ========================================================================
	// === INDEX REGISTERS ====================================================
	// ========================================================================

	public int getInstructionIndexRegisterValue( boolean ext )
	{
		int zAddress = 0;

		if( ext )
		{
			CoreRegister eind = getRegisterByName( "EIND" );
			if( eind != null )
			{
				zAddress = (eind.privGetData() & 0xFF) << 16;
			}
		}

		CoreRegister h = getGeneralRegister( AvrRegistersXYZ.Z.getUpperRegister() );
		CoreRegister l = getGeneralRegister( AvrRegistersXYZ.Z.getLowerRegister() );
		zAddress += ((h.privGetData() & 0xFF) << 8) + (l.privGetData() & 0xFF);

		return zAddress;
	}

	public int getIndexRegisterValue( AvrRegisterIndex r, boolean ext )
	{
		int zAddress = 0;

		if( ext )
		{
			CoreRegister ramp = getRegisterByName( r.getExtendedRegisterName() );
			if( ramp != null )
			{
				zAddress = (ramp.privGetData() & 0xFF) << 16;
			}
		}

		CoreRegister h = getGeneralRegister( r.getUpperRegister() );
		CoreRegister l = getGeneralRegister( r.getLowerRegister() );
		zAddress += ((h.privGetData() & 0xFF) << 8) + (l.privGetData() & 0xFF);

		return zAddress;
	}

	public void setIndexRegisterValue( AvrRegistersXYZ r, int value, boolean ext )
	{
		if( ext )
		{
			CoreRegister ramp = getRegisterByName( r.getExtendedRegisterName() );
			if( ramp != null )
			{
				ramp.privSetData( (byte) (value >> 16) );
			}
		}

		CoreRegister h = getGeneralRegister( r.getUpperRegister() );
		CoreRegister l = getGeneralRegister( r.getLowerRegister() );

		h.setData( (byte) (value >> 8) );
		l.setData( (byte) (value & 0xFF) );
	}

	// ========================================================================
	// === SYMBOLS ============================================================
	// ========================================================================

	/**
	 * 
	 * @param memory
	 */
	public void flushSymbols(  CoreMemory memory )
	{
		switch( memory )
		{
			case FLASH:
				flashSymbolsByAddresse.clear();
				break;

			case SRAM:
				sramSymbolsByAddresse.clear();
				break;

			case EEPROM:
				eepromSymbolsByAddresse.clear();
				break;
		}
	}

	/**
	 * 
	 * @param memory
	 * @param address
	 * @param symbol
	 */
	public void addSymbol( CoreMemory memory, int address, String symbol )
	{	
		switch( memory )
		{
			case FLASH:
				flashSymbolsByAddresse.put( address, symbol );
				break;

			case SRAM:
				sramSymbolsByAddresse.put( address, symbol );
				break;

			case EEPROM:
				eepromSymbolsByAddresse.put( address, symbol );
				break;
		}
	}

	/**
	 * 
	 * @param memory
	 * @param address
	 */
	public void removeSymbol( CoreMemory memory, int address )
	{
		switch( memory )
		{
			case FLASH:
				flashSymbolsByAddresse.remove( address );
				break;

			case SRAM:
				sramSymbolsByAddresse.remove( address );
				break;

			case EEPROM:
				eepromSymbolsByAddresse.remove( address );
				break;
		}
	}

	/**
	 * 
	 * @param memory
	 * @param address
	 * @return
	 */
	public String getSymbol( CoreMemory memory, int address )
	{
		switch( memory )
		{
			case FLASH:
				return flashSymbolsByAddresse.get( address );

			case SRAM:
				return sramSymbolsByAddresse.get( address );

			case EEPROM:
				return eepromSymbolsByAddresse.get( address );
		}

		return null;
	}

	// ========================================================================
	// === LOGGING ============================================================
	// ========================================================================

	private static final Logger log
		= Logger.getLogger( Core.class.getName() );

}
