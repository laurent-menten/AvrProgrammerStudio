package be.lmenten.avr.ui.registers;

public abstract class AbstractRegisterBitModel
	implements RegisterBitModel
{

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean hasChanged()
	{
		return false;
	}

	@Override
	public boolean getState()
	{
		return false;
	}
}
