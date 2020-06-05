package be.lmenten.avr.ui.registers2;

import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.descriptor.CoreRegisterDescriptor;

public class DefaultRegisterModel
	extends AbstractRegisterModel
{
	private CoreRegisterDescriptor rdesc;
	private CoreRegister reg;

	// ========================================================================
	// ===
	// ========================================================================

	public DefaultRegisterModel( CoreRegisterDescriptor rdesc, CoreRegister reg )
	{
		super( rdesc.getAddress(), rdesc.getName() );

		this.rdesc = rdesc;
		this.reg = reg;

		setDescription( rdesc.getDescription() );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public CoreRegisterDescriptor getRegisterDescripto()
	{
		return rdesc;
	}

	public CoreRegister getRegister()
	{
		return reg;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public byte getRegisterValue()
	{
		return (byte) reg.internGetData();
	}
}
