package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRMI
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRMI( InstructionSet entry, int data )
	{
		super( InstructionSet.BRMI, data );
	}

	// ------------------------------------------------------------------------

	public BRMI( int k )
	{
		super( InstructionSet.BRMI, AvrStatusRegisterBits.N, k );
	}
}
