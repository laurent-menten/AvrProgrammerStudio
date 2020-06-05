package be.lmenten.avr.core.components;

import java.util.Properties;

import be.lmenten.avr.core.tmp.AbstractCoreComponent;
import be.lmenten.avr.core.tmp.Core;
import be.lmenten.avr.core.tmp.IORegisterEvent;
import be.lmenten.avr.core.tmp.IORegisterListener;
import be.lmenten.avr.core.utils.CoreIORegisters;
import be.lmenten.avr.core.utils.CoreInterrupts;

// read
//	wait EEPE = 0 in EECR
//	write EEARH
//	write EEARL
//	set EERE = 1 in EECR
//		!!! 4 cycles
//	read EEDR
//
// write
//	wait EEPE = 0 in EECR
//	wait SPMEN = 0 in SPMCSR		** if bootloader can update flash
//	write EEARH
//	write EEARL
//	write EEDR
//	set EEMPE = 1 and EEPE = 0 in EECR
//	set EEPE = 1 in EECR (within 4 clock cycle, EEMPE auto cleared)

// EECR
//	0: EERE		read enable
//	1: EEPE		programming enable
//	2: EEMPE	master programming enable
//	3: EERIE	eeprom ready interrupt enable
//	4: EEPM0	programming mode
//	5: EEPM1		"	"
//	6:
//	7:
//
// modes:
//	00	atomic erase & write
//	01	erase only
//	10	write only
//	11	*** reserved ***

public class Eeprom
	extends AbstractCoreComponent
	implements IORegisterListener
{
	private final int EECR_ADDR;
	private final int EEDR_ADDR;
	private final int EEARL_ADDR;
	private final int EEARH_ADDR;
	private final int EE_READY_VECTOR;

	private boolean waitTick;
	private long clockValue;

	// ========================================================================
	// ===
	// ========================================================================

	public Eeprom( Core core, Properties config )
	{
		super( core, config );

		// --------------------------------------------------------------------
		// - core dependent configuration -------------------------------------
		// --------------------------------------------------------------------

		EECR_ADDR = core
			.getCoreDescriptor()
			.getIORegisterOffset( CoreIORegisters.EECR );

		EEDR_ADDR = core
			.getCoreDescriptor()
			.getIORegisterOffset( CoreIORegisters.EEDR );

		EEARL_ADDR = core
			.getCoreDescriptor()
			.getIORegisterOffset( CoreIORegisters.EEARH );

		EEARH_ADDR = core
			.getCoreDescriptor()
			.getIORegisterOffset( CoreIORegisters.EEARH );

		EE_READY_VECTOR = core
			.getCoreDescriptor()
			.getInterruptVector( CoreInterrupts.EE_READY );

		// --------------------------------------------------------------------
		// - component configuration ------------------------------------------
		// --------------------------------------------------------------------

		waitTick = false;
		clockValue = 0;

		core.registerIORegisterListener( EECR_ADDR, this );
	}

	// ========================================================================
	// === CoreComponent ======================================================
	// ========================================================================

	@Override
	public void reset()
	{
	}

	@Override
	public void tick()
	{
		if( waitTick && (core.getClockCounter() >= clockValue) )
		{
			byte eecr = core.readDataMemory( EECR_ADDR );

			eecr &= 0b11111011;

			core.writeDataMemory( EECR_ADDR, eecr );
			waitTick = false;
		}
	}
	
	// ========================================================================
	// === IOPortListener =====================================================
	// ========================================================================

	@Override
	public void registerRead( IORegisterEvent event )
	{
	}

	@Override
	public void registerWrite( IORegisterEvent event )
	{
		if( event.getPhysicalAddress() == EECR_ADDR )
		{
			byte eecr = event.getNewValue();

			// ----------------------------------------------------------------
			// --- eeprom read ------------------------------------------------
			// ----------------------------------------------------------------

			// EERE
			if( (eecr & 0b00000001) == 0b00000001 )
			{
				byte eearl = core.readDataMemory( EEARL_ADDR );
				byte eearh = core.readDataMemory( EEARH_ADDR );
				int address = ((eearh &0xFF) << 8) + (eearl & 0xFF);

				byte value = core.readEeprom( address );
				core.updateClockCounter( 4 );

				core.writeDataMemory( EEDR_ADDR, value );

				event.setNewValue( (byte) (eecr & 0b11111110) );
			}

			// ----------------------------------------------------------------
			// --- eeprom write -----------------------------------------------
			// ----------------------------------------------------------------

			// EEMPE & ~EEPE
			else if( (eecr & 0b00000110) == 0b00000100 )
			{
				clockValue = core.getClockCounter() + 4;
				waitTick = true;
			}
			// EEMPE & EEPE
			else if( (eecr & 0b00000110) == 0b00000110 )
			{
				byte eearl = core.readDataMemory( EEARL_ADDR );
				byte eearh = core.readDataMemory( EEARH_ADDR );
				int address = ((eearh &0xFF) << 8) + (eearl & 0xFF);

				byte value = core.readDataMemory( EEDR_ADDR );

				// EEPM[0..1]
				switch( ((eecr & 0b00110000) >> 4) )
				{
					case 0b00:	// atomic erase & write
						core.writeEeprom( address, (byte) 0 );
						core.writeEeprom( address, value );

					case 0b01:	// erase only
						core.writeEeprom( address, (byte) 0 );
						break;

					case 0b10:	// write only
						core.writeEeprom( address, value );
						break;

					case 0b11:
						// reserved
						break;
				}

				event.setNewValue( (byte) (eecr & 0b11111001) );

				// EERIE
				if( ((eecr & 0b00001000) == 0b00001000) && core.i() )
				{
					if( core.i() )
					{
						core.queueInterrupt( EE_READY_VECTOR );
					}
				}
			}
		}
	}
}
