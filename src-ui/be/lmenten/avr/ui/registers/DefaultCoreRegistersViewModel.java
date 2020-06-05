package be.lmenten.avr.ui.registers;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.descriptor.CoreRegistersFile;

public class DefaultCoreRegistersViewModel
	extends AbstractCoreRegistersViewModel
{
	private static final long serialVersionUID = 1L;

	// ========================================================================
	// ===
	// ========================================================================

	public DefaultCoreRegistersViewModel( Core core, CoreRegistersFile file )
	{
		super( core, file );
	}
}
