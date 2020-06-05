package be.lmenten.avr.ui.registers;

public abstract class AbstractRegisterModel
	implements RegisterModel
{

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public int getWidth()
	{
		return 0;
	}

	@Override
	public int getValue()
	{
		return 0;
	}

	@Override
	public RegisterBitModel getBitModel( int bit )
	{
		return null;
	}
}
