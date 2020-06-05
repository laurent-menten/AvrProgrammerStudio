package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class CLT
	extends BCLR
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CLT( InstructionSet entry, int data )
	{
		super( InstructionSet.CLT, data );
	}

	// ------------------------------------------------------------------------

	public CLT()
	{
		super( InstructionSet.CLT, AvrStatusRegisterBits.T );
	}
}
