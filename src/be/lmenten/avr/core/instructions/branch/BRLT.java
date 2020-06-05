package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRLT
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRLT( InstructionSet entry, int data )
	{
		super( InstructionSet.BRLT, data );
	}

	// ------------------------------------------------------------------------

	public BRLT( int k )
	{
		super( InstructionSet.BRLT, AvrStatusRegisterBits.S, k );
	}
}
