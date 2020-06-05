package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRCC
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRCC( InstructionSet entry, int data )
	{
		super( InstructionSet.BRCC, data );
	}

	// ------------------------------------------------------------------------

	public BRCC( int k )
	{
		super( InstructionSet.BRCC, AvrStatusRegisterBits.C, k );
	}
}
