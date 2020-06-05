package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SET
	extends BSET
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SET( InstructionSet entry, int data )
	{
		super( InstructionSet.SET, data );
	}

	// ------------------------------------------------------------------------

	public SET()
	{
		super( InstructionSet.SET, AvrStatusRegisterBits.T );
	}
}
