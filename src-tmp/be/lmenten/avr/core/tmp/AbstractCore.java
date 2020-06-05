package be.lmenten.avr.core.tmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

import be.lmenten.avr.core.RunningMode;
import be.lmenten.avr.core.descriptor.CoreFeatures;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.ResetSources;
import be.lmenten.avr.core.ui.CoreUI;
import be.lmenten.avr.core.utils.CoreIORegisters;
import be.lmenten.avr.core.utils.DebugCondition;
import be.lmenten.avr.core.utils.CoreFuses;

public abstract class AbstractCore
	implements Core, CoreComponent, CoreUI
{
	private final int SREG_ADDR;
	private final int SPH_ADDR;
	private final int SPL_ADDR;
	
	private final int MCUCR_ADDR;
	private final int MCUSR_ADDR;

	@SuppressWarnings("unused")
	private final int SMCR_ADDR;
	@SuppressWarnings("unused")
	private final int PRR0_ADDR;
	@SuppressWarnings("unused")
	private final int PRR1_ADDR;

	// ========================================================================
	// === EXPORTS TO THE WORLD ===============================================
	// ========================================================================

	public final boolean hasSPH;
	public final boolean hasEIND;
	public final boolean hasRAMPZ;
	public final boolean hasRAMPY;
	public final boolean hasRAMPX;
	public final boolean hasRAMPD;

	public final boolean hasBootLoader;
	public final boolean hasExternalMemory;

	// ========================================================================
	// === EXPORTS TO SUB-CLASSES =============================================
	// ========================================================================

	protected final Instruction [] programMemory;
	protected final byte [] internalDataMemory;
	protected final byte [] externalDataMemory;
	protected final byte [] eepromMemory;

	// ========================================================================
	// === NO EXPORT ==========================================================
	// ========================================================================

	private final CoreDescriptor coreDescriptor;

	private final Map<CoreFuses,Boolean> coreFuses =
			new HashMap<>();

	// ------------------------------------------------------------------------

	private final Set<CoreComponent> coreComponents =
		new HashSet<>();

	private final Map<Integer, List<IORegisterListener>> ioPortListeners =
		new HashMap<>();

	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------

	private int programCounter;
	private long clockCounter;

	private RunningMode coreMode;

	// ------------------------------------------------------------------------

	private boolean debugMode = false;

	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	protected AbstractCore( Properties config, String coreId )
	{
		coreDescriptor = CoreSimulator.descriptorsList.get( coreId );

		// --------------------------------------------------------------------
		// - fuses ------------------------------------------------------------
		// --------------------------------------------------------------------

		coreDescriptor
			.importFusesValues( (b, v) -> coreFuses.put( b, v ) );

		// --------------------------------------------------------------------
		// - register addresses -----------------------------------------------
		// --------------------------------------------------------------------

		SREG_ADDR = coreDescriptor
			.getIORegisterOffset( CoreIORegisters.SREG );
		SPH_ADDR = coreDescriptor
			.getIORegisterOffset( CoreIORegisters.SREG );
		SPL_ADDR = coreDescriptor
			.getIORegisterOffset( CoreIORegisters.SREG );
			
		MCUSR_ADDR = coreDescriptor
			.getIORegisterOffset( CoreIORegisters.MCUSR );
		MCUCR_ADDR = coreDescriptor
			.getIORegisterOffset( CoreIORegisters.MCUCR );

		SMCR_ADDR = coreDescriptor
			.getIORegisterOffset( CoreIORegisters.SMCR );
		PRR0_ADDR = coreDescriptor
			.getIORegisterOffset( CoreIORegisters.PRR0 );
		PRR1_ADDR = coreDescriptor
			.getIORegisterOffset( CoreIORegisters.PRR1 );

		// --------------------------------------------------------------------
		// - core dependent configuration -------------------------------------
		// --------------------------------------------------------------------

		hasSPH = coreDescriptor.hasFeature( CoreFeatures.SPH );
		hasEIND = coreDescriptor.hasFeature( CoreFeatures.EIND );
		hasRAMPZ = coreDescriptor.hasFeature( CoreFeatures.RAMPZ );
		hasRAMPY = coreDescriptor.hasFeature( CoreFeatures.RAMPY );
		hasRAMPX = coreDescriptor.hasFeature( CoreFeatures.RAMPX );
		hasRAMPD = coreDescriptor.hasFeature( CoreFeatures.RAMPD );

		hasBootLoader = coreDescriptor.hasFeature( CoreFeatures.BOOT_LOADER );
		hasExternalMemory = coreDescriptor.hasFeature( CoreFeatures.EXTERNAL_SRAM );

		// --------------------------------------------------------------------

		programMemory = new Instruction [ coreDescriptor.getProgramMemorySize() ];

		internalDataMemory = new byte [ coreDescriptor.getOnChipDataMemorySize() ];

		eepromMemory = new byte [ coreDescriptor.getEepromSize() ];

		int externalDataMemorySize = 0;
		if( hasExternalMemory && (config != null) )
		{
			String tmp = config.getProperty( CONFIG_EXTERNAL_MEMORY_SIZE );
			if( tmp != null )
			{
				externalDataMemorySize = Integer.parseInt( tmp );
			}
		}

		externalDataMemory = new byte [ externalDataMemorySize ];

		// --------------------------------------------------------------------
		// - runtime configuration --------------------------------------------
		// --------------------------------------------------------------------

		if( config != null )
		{
			String configValue = config.getProperty( CONFIG_DEBUG );
			if( configValue != null )
			{
				debugMode = Boolean.parseBoolean( configValue );
			}
		}

	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public final CoreDescriptor getCoreDescriptor()
	{
		return coreDescriptor;
	}

	@Override
	public CoreUI getCoreUI()
	{
		return this;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public final void registerCoreComponent( CoreComponent coreComponent )
	{
		if( ! coreComponents.contains( coreComponent ) )
		{
			coreComponents.add( coreComponent );
		}
	}

	@Override
	public void registerIORegisterListener( int address, IORegisterListener listener )
	{
		List<IORegisterListener> portListeners;
		if( ioPortListeners.containsKey( address ) )
		{
			portListeners = ioPortListeners.get( address );
		}
		else
		{
			portListeners = new ArrayList<>();
			ioPortListeners.put( address, portListeners );
		}

		if( ! portListeners.contains(listener) )
		{
			portListeners.add( listener );
		}
	}

	// ------------------------------------------------------------------------

	@Override
	public void registerDebugListener( DebugListener listener )
	{
	}

	public void setBreakPoint( DebugCondition condition, int address, DebugListener listener )
	{
	}

	// ========================================================================
	// === Features ===========================================================
	// ========================================================================

	@Override
	public boolean hasFeature( CoreFeatures feature )
	{
		return coreDescriptor.hasFeature( feature );
	}

	// ========================================================================
	// === Fuses ==============================================================
	// ========================================================================

	@Override
	public void programFuse( CoreFuses fuse )
	{
		coreFuses.put( fuse, CoreFuses.PROGRAMMED );
	}

	@Override
	public void unprogramFuse( CoreFuses fuse )
	{
		coreFuses.put( fuse, CoreFuses.UNPROGRAMMED );
	}

	@Override
	public boolean getFuseState( CoreFuses fuse )
	{
		return coreFuses.get( fuse );
	}

	// ========================================================================
	// === Boot Loader Section ================================================
	// ========================================================================

	@Override
	public int getApplicationSectionOffset()
	{
		return 0x0000;
	}

	@Override
	public int getApplicationSectionSize()
	{
		if( hasBootLoader )
		{
			return getCoreDescriptor().getProgramMemorySize()
					- getBootLoaderSectionSize();
		}

		return getCoreDescriptor().getProgramMemorySize();
	}

	// ------------------------------------------------------------------------

	@Override
	public boolean isInBootLoaderSection( int address )
	{
		if( hasBootLoader )
		{
			return (address >= getBootLoaderSectionOffset());
		}

		return false;
	}

	/**
	 * Check if the address held by the program counter resides in the BLS.
	 * 
	 * @return
	 */
	public boolean isInBootLoaderSection()
	{
		return isInBootLoaderSection( getProgramCounter() );
	}
	
	@Override
	public int getBootLoaderSectionOffset()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getBootLoaderSectionSize()
	{
		throw new UnsupportedOperationException();
	}

	// ========================================================================
	// === Run mode ===========================================================
	// ========================================================================

	@Override
	public void setCoreMode( RunningMode coreMode )
	{
		this.coreMode = coreMode;
	}

	@Override
	public RunningMode getCoreMode()
	{
		return coreMode;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void initProgramMemory( Consumer<Instruction []> loader )
	{
		loader.accept( programMemory );
	}

	// ------------------------------------------------------------------------

	@Override
	public Instruction getCurrentInstruction()
	{
		return programMemory[ programCounter ];
	}

	@Override
	public Instruction getFollowingInstruction()
	{
		if( getCurrentInstruction() == null )
		{
			return programMemory[ programCounter + 1 ];
		}

		int size = getCurrentInstruction().getOpcodeSize();

		return programMemory[ programCounter + size ];
	}

	@Override
	public void step()
	{
		Instruction currentInstruction = getCurrentInstruction();

//		currentInstruction.execute( this );

		switch( currentInstruction.getInstructionSetEntry() )
		{
			case BREAK:
				break;

			case SLEEP:
				break;

			default:
		}

		// check execution breakpoint
	}

	// ========================================================================
	// ===
	// ========================================================================

	// ------------------------------------------------------------------------

	public boolean isDebugModeEnaled()
	{
		return debugMode;
	}

	// ========================================================================
	// ===
	// ========================================================================

	protected void fireIOPortRead(  IORegisterEvent event  )
	{
		List<IORegisterListener> portListeners
			= ioPortListeners.get( event.getPhysicalAddress() );

		if( portListeners != null )
		{
			portListeners.forEach(
				listener -> listener.registerRead( event )
			);
		}
	}

	protected void fireIOPortWrite( IORegisterEvent event )
	{		
		List<IORegisterListener> portListeners
			= ioPortListeners.get( event.getPhysicalAddress() );

		if( portListeners != null )
		{
			portListeners.forEach(
				listener -> listener.registerWrite( event )
			);
		}
	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 */
	@Override
	public final void reset( ResetSources source )
	{
		clockCounter = 0l;
		coreMode = RunningMode.RUNNING;
	
		// --------------------------------------------------------------------

		byte mcusr = readDataMemory( MCUSR_ADDR );

		switch( source )
		{
			case POWER_ON:
				mcusr = 0b00000001;		// PORF
				break;
				
			case EXTERNAL:
				mcusr = 0b00000010;		// EXTRF
				break;
				
			case BROWN_OUT:
				mcusr = 0b00000100;		// BORF
				break;
				
			case WATCHDOG:
				mcusr = 0b00001000;		// WDRF
				break;
				
			case JTAG:
				mcusr = 0b00010000;		// JTRF
				break;				
		}

		writeDataMemory( MCUSR_ADDR, mcusr );

		// --------------------------------------------------------------------

		// BOOTRST unprogrammed PC = application section base
		// BOOTRST programmed PC = boot loader section base
		
		// --------------------------------------------------------------------

		reset();
		coreComponents.forEach( component -> component.reset() );
	}

	// ------------------------------------------------------------------------

	/**
	 * This should not be called directly, use reset( ResetSoures ) instead.
	 * 
	 * IMPORTANT CALLS for implementations:
	 * <pre>
	 * setStackPointer();
	 * </pre>
	 */
	@Override
	public abstract void reset();

	@Override
	public void tick()
	{
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getProgramCounter()
	{
		return programCounter;
	}

	@Override
	public void setProgramCounter( int address )
	{
		programCounter = address;
	}

	@Override
	public void updateProgramCounter( int offset )
	{
		programCounter += offset;
	}

	@Override
	public void queueInterrupt( int level )
	{	
	}

	// ------------------------------------------------------------------------

	@Override
	public void pushProgramCounter()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void popProgramCounter()
	{
		throw new UnsupportedOperationException();
	}

	// ------------------------------------------------------------------------

	@Override
	public void interrupt( int vector )
	{
		int address = vector * coreDescriptor.getInterruptVectorSize();

		if( hasBootLoader )
		{
			byte mcucr = internalDataMemory[ MCUCR_ADDR ];
			if( (mcucr & 0b00000010) == 0b00000010 ) // IVSEL
			{
				address += 0x0000;
			}
		}

		pushProgramCounter();
		i( false );
		setProgramCounter( address );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void setStackPointer( int address )
	{
		int spl = address & 0xFF;
		internalDataMemory[ SPL_ADDR ] = (byte)spl;

		if( hasSPH )
		{
			int sph = (address >> 8) & 0xFF;
			internalDataMemory[ SPH_ADDR ] = (byte)sph;
		}
	}

	@Override
	public int getStackPointer()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void push( int value )
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int pop()
	{
		throw new UnsupportedOperationException();
	}

	// ------------------------------------------------------------------------

	@Override
	public long getClockCounter()
	{
		return clockCounter;
	}

	@Override
	public void setClockCounter( long value )
	{
		clockCounter = value;
	}

	@Override
	public void updateClockCounter( long increment )
	{
		clockCounter += increment;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int readProgramMemory( int address )
	{
		return 0;
	}

	@Override
	public int writeProgramMemory( int address, int value )
	{
		return 0;
	}

	// ------------------------------------------------------------------------

	@Override
	public byte readDataMemory( int address )
	{
		byte rc;

		if( address < coreDescriptor.getExternalDataMemoryBaseOffset() )
		{
			rc = internalDataMemory[ address ];

			if( isIOPortAddress( address ) )
			{
				IORegisterEvent event = new IORegisterEvent( this, address );
				event.setOldValue( rc );

				fireIOPortRead( event );

				rc = event.getOldValue();
			}
		}

		else // EXTERNAL DATA MEMORY
		{
			int externalOffset = address
					- coreDescriptor.getExternalDataMemoryBaseOffset();

				rc = externalDataMemory [ externalOffset ];
		}

		return rc;
	}

	@Override
	public byte writeDataMemory( int address, byte value )
	{
		byte rc = value;

		if( address < coreDescriptor.getExternalDataMemoryBaseOffset() )
		{
			if( isIOPortAddress( address ) )
			{
				IORegisterEvent event = new IORegisterEvent( this, address );
				event.setOldValue( internalDataMemory[ address ] );
				event.setNewValue( value );

				fireIOPortWrite( event );

				internalDataMemory[ address ] = event.getNewValue();

				rc = event.getOldValue();
			}
		}

		else // EXTERNAL DATA MEMORY
		{
			int externalOffset = address
					- coreDescriptor.getExternalDataMemoryBaseOffset();

			externalDataMemory [ externalOffset ] = value;
		}

		return rc;
	}

	// ------------------------------------------------------------------------

	@Override
	public byte readEeprom( int address )
	{
		return eepromMemory[ address ];
	}

	@Override
	public byte writeEeprom( int address, byte value )
	{
		byte oldValue = eepromMemory[ address ];

		eepromMemory[ address ] = value;
		
		return oldValue;
	}

	// ========================================================================
	// === UTILITIES ==========================================================
	// ========================================================================

	// ========================================================================
	// === STATUS REGISTER HELPERS ============================================
	// ========================================================================

	public boolean c()
	{
		return (internalDataMemory [ SREG_ADDR ] & 0b00000001) == 0b00000001;
	}

	public boolean z()
	{
		return (internalDataMemory [ SREG_ADDR ] & 0b00000010) == 0b00000010;
	}

	public boolean n()
	{
		return (internalDataMemory [ SREG_ADDR ] & 0b00000100) == 0b00000100;
	}

	public boolean v()
	{
		return (internalDataMemory [ SREG_ADDR ] & 0b00001000) == 0b00001000;
	}

	public boolean s()
	{
		return (internalDataMemory [ SREG_ADDR ] & 0b00010000) == 0b00010000;
	}

	public boolean h()
	{
		return (internalDataMemory [ SREG_ADDR ] & 0b00100000) == 0b00100000;
	}

	public boolean t()
	{
		return (internalDataMemory [ SREG_ADDR ] & 0b01000000) == 0b01000000;
	}

	public boolean i()
	{
		return (internalDataMemory [ SREG_ADDR ] & 0b10000000) == 0b10000000;
	}

	// ------------------------------------------------------------------------

	public void c( boolean c )
	{
		byte tmp = internalDataMemory [ SREG_ADDR ];
		tmp = (byte) (c ? (tmp | 0b00000001) : (tmp & 0b11111110));
		internalDataMemory [ SREG_ADDR ] = tmp;
	}

	public void z( boolean z )
	{
		byte tmp = internalDataMemory [ SREG_ADDR ];
		tmp = (byte) (z ? (tmp | 0b00000010) : (tmp & 0b11111101));
		internalDataMemory [ SREG_ADDR ] = tmp;
	}

	public void n( boolean n )
	{
		byte tmp = internalDataMemory [ SREG_ADDR ];
		tmp = (byte) (n ? (tmp | 0b00000100) : (tmp & 0b11111011));
		internalDataMemory [ SREG_ADDR ] = tmp;
	}

	public void v( boolean v )
	{
		byte tmp = internalDataMemory [ SREG_ADDR ];
		tmp = (byte) (v ? (tmp | 0b00001000) : (tmp & 0b11110111));
		internalDataMemory [ SREG_ADDR ] = tmp;
	}

	public void s( boolean s )
	{
		byte tmp = internalDataMemory [ SREG_ADDR ];
		tmp = (byte) (s ? (tmp | 0b00010000) : (tmp & 0b11110111));
		internalDataMemory [ SREG_ADDR ] = tmp;
	}

	public void h( boolean h )
	{
		byte tmp = internalDataMemory [ SREG_ADDR ];
		tmp = (byte) (h ? (tmp | 0b00100000) : (tmp & 0b11011111));
		internalDataMemory [ SREG_ADDR ] = tmp;
	}

	public void t( boolean t )
	{
		byte tmp = internalDataMemory [ SREG_ADDR ];
		tmp = (byte) (t ? (tmp | 0b01000000) : (tmp & 0b10111111));
		internalDataMemory [ SREG_ADDR ] = tmp;
	}

	public void i( boolean i )
	{
		byte tmp = internalDataMemory [ SREG_ADDR ];
		tmp = (byte) (i ? (tmp | 0b10000000) : (tmp & 0b01111111));
		internalDataMemory [ SREG_ADDR ] = tmp;
	}
}
