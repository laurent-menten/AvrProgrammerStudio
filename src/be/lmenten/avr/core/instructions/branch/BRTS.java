package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRTS
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRTS( InstructionSet entry, int data )
	{
		super( InstructionSet.BRTS, data );
	}

	// ------------------------------------------------------------------------

	public BRTS( int k )
	{
		super( InstructionSet.BRTS, AvrStatusRegisterBits.T, k );
	}
}
