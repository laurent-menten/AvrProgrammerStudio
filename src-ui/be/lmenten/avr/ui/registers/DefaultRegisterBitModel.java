package be.lmenten.avr.ui.registers;

import be.lmenten.avr.core.descriptor.CoreRegisterDescriptor;

public class DefaultRegisterBitModel
	extends AbstractRegisterBitModel
{
	private final CoreRegisterDescriptor rdesc;
	private final int bit;

	// ========================================================================
	// ===
	// ========================================================================

	public DefaultRegisterBitModel( CoreRegisterDescriptor rdesc, int bit )
	{
		this.rdesc = rdesc;
		this.bit = bit;
	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 * @return
	 */
	public CoreRegisterDescriptor getRegisterDescriptor()
	{
		return rdesc;
	}

	/**
	 * 
	 * @return
	 */
	public int getBit()
	{
		return bit;
	}
}
