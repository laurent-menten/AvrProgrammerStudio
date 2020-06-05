package be.lmenten.avr.ui.registers;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.descriptor.CoreRegistersFile;

public class JCoreRegistersView
	extends JTable
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public JCoreRegistersView( Core core, CoreRegistersFile file )
	{
		DefaultCoreRegistersViewModel model
			= new DefaultCoreRegistersViewModel( core, file );
		setModel( model );

		// --------------------------------------------------------------------

	}

	// ========================================================================
	// ===
	// ========================================================================

	public void setModel( CoreRegistersViewModel dataModel )
	{
		super.setModel( dataModel );
	}

	// ------------------------------------------------------------------------

}
