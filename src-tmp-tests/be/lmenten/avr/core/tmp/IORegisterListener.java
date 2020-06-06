package be.lmenten.avr.core.tmp;

public interface IORegisterListener
{
	public void registerRead( IORegisterEvent event );
	public void registerWrite( IORegisterEvent event );
}
