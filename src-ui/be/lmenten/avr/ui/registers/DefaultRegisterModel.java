package be.lmenten.avr.ui.registers;

import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.descriptor.CoreRegisterDescriptor;

public class DefaultRegisterModel
	extends AbstractRegisterModel
{
	private final CoreRegister reg;
	private final CoreRegisterDescriptor rdesc;

	// ========================================================================
	// ===
	// ========================================================================

	public DefaultRegisterModel( CoreRegister reg )
	{
		this.reg = reg;
		this.rdesc = reg.getRegisterDescriptor();
	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 * @return
	 */
	public CoreRegister getRegister()
	{
		return reg;
	}

	/**
	 * 
	 * @return
	 */
	public CoreRegisterDescriptor getRegisterDescriptor()
	{
		return rdesc;
	}
}
