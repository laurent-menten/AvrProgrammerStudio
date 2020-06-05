package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SES
	extends BSET
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SES( InstructionSet entry, int data )
	{
		super( InstructionSet.SES, data );
	}

	// ------------------------------------------------------------------------

	public SES()
	{
		super( InstructionSet.SES, AvrStatusRegisterBits.S );
	}
}
