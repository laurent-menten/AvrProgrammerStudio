package be.lmenten.avr.ui.registers;

public interface RegisterBitModel
{
	public String getName();
	public boolean hasChanged();
	public boolean getState();
}
