package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BREQ
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BREQ( InstructionSet entry, int data )
	{
		super( InstructionSet.BREQ, data );
	}

	// ------------------------------------------------------------------------

	public BREQ( int k )
	{
		super( InstructionSet.BREQ, AvrStatusRegisterBits.Z, k );
	}
}
