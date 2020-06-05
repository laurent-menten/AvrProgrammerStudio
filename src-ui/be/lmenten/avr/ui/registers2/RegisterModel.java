package be.lmenten.avr.ui.registers2;

import java.util.List;

public interface RegisterModel
{
	public String getRegisterName();
	public int getRegisterAddress();
	public String getRegisterDescription();

	public byte getRegisterValue();

	public List<RegisterBitModel> getBitsList();
}
