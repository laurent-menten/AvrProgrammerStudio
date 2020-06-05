package be.lmenten.avr.core.ui;

import be.lmenten.avr.core.utils.CoreIORegisters;
import be.lmenten.avr.core.utils.CoreInterrupts;
import be.lmenten.avr.core.utils.CoreFuses;

public interface CoreUI
{
	CoreFuses [][] getRawLockBitsList();
	CoreInterrupts [] getRawInterruptsList();
	CoreIORegisters [] getRawIORegistersList();
}
