package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRCS
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRCS( InstructionSet entry, int data )
	{
		super( InstructionSet.BRCS, data );
	}

	// ------------------------------------------------------------------------

	public BRCS( int k )
	{
		super( InstructionSet.BRCS, AvrStatusRegisterBits.C, k );
	}
}
