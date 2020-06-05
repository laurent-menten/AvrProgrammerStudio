package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRVC
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRVC( InstructionSet entry, int data )
	{
		super( InstructionSet.BRVC, data );
	}

	// ------------------------------------------------------------------------

	public BRVC( int k )
	{
		super( InstructionSet.BRVC, AvrStatusRegisterBits.V, k );
	}
}
