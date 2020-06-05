package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SEC
	extends BSET
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SEC( InstructionSet entry, int data )
	{
		super( InstructionSet.SEC, data );
	}

	// ------------------------------------------------------------------------

	public SEC()
	{
		super( InstructionSet.SEC, AvrStatusRegisterBits.C );
	}
}
