package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class CLS
	extends BCLR
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CLS( InstructionSet entry, int data )
	{
		super( InstructionSet.CLS, data );
	}

	// ------------------------------------------------------------------------

	public CLS()
	{
		super( InstructionSet.CLS, AvrStatusRegisterBits.S );
	}
}
