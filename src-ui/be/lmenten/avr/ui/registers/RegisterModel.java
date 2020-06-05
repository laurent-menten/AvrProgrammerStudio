package be.lmenten.avr.ui.registers;

public interface RegisterModel
{
	public String getName();
	public int getWidth();
	public int getValue();

	public RegisterBitModel getBitModel( int bit );
}
