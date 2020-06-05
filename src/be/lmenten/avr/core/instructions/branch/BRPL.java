package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRPL
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRPL( InstructionSet entry, int data )
	{
		super( InstructionSet.BRPL, data );
	}

	// ------------------------------------------------------------------------

	public BRPL( int k )
	{
		super( InstructionSet.BRPL, AvrStatusRegisterBits.N, k );
	}
}
