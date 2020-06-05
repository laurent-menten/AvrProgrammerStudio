package be.lmenten.avr.core.components;

import java.util.Properties;

import be.lmenten.avr.core.tmp.AbstractCoreComponent;
import be.lmenten.avr.core.tmp.Core;
import be.lmenten.avr.core.tmp.IORegisterEvent;
import be.lmenten.avr.core.tmp.IORegisterListener;
import be.lmenten.avr.core.utils.CoreIORegisters;
import be.lmenten.avr.core.utils.CoreInterrupts;

public class Watchdog
	extends AbstractCoreComponent
	implements IORegisterListener
{
	private final int MCUSR_ADDR;	
	private final int WDTCSR_ADDR;
	private final int WDT_VECTOR;

	// ========================================================================

	public Watchdog( Core core, Properties config )
	{
		super( core, config );

		// --------------------------------------------------------------------
		// - core dependent configuration -------------------------------------
		// --------------------------------------------------------------------

		WDTCSR_ADDR = core
			.getCoreDescriptor()
			.getIORegisterOffset( CoreIORegisters.WDTSCR );

		MCUSR_ADDR = core
			.getCoreDescriptor()
			.getIORegisterOffset( CoreIORegisters.MCUSR );

		WDT_VECTOR = core
			.getCoreDescriptor()
			.getInterruptVector( CoreInterrupts.WDT );

		// --------------------------------------------------------------------
		// - component configuration ------------------------------------------
		// --------------------------------------------------------------------

		core.registerIORegisterListener( WDTCSR_ADDR,  this );
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
		core.interrupt( WDT_VECTOR );
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
		byte mcusr = core.readDataMemory( MCUSR_ADDR );

		mcusr |= 0b00001000;

		core.writeDataMemory( MCUSR_ADDR, mcusr );
	}
}
