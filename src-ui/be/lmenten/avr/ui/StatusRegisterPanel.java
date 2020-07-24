package be.lmenten.avr.ui;

import javax.swing.JPanel;

import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.analysis.AccessEvent;
import be.lmenten.avr.core.analysis.AccessEventListener;

public class StatusRegisterPanel
	extends JPanel
	implements AccessEventListener
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	private final CoreRegister sreg;

	// ========================================================================
	// ===
	// ========================================================================

	public StatusRegisterPanel( CoreRegister sreg )
	{
		this.sreg = sreg;

		// --------------------------------------------------------------------

		sreg.addAccessListener( this );
	}

	public CoreRegister getSreg()
	{
		return sreg;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void onAccessEvent( AccessEvent event )
	{
		if( event.getAccessMode() == AccessEvent.ACCESS_WRITE )
		{
			// TODO report change
		}
	}
}
