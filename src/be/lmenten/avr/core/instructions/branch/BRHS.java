package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRHS
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRHS( InstructionSet entry, int data )
	{
		super( InstructionSet.BRHS, data );
	}

	// ------------------------------------------------------------------------

	public BRHS( int k )
	{
		super( InstructionSet.BRHS, AvrStatusRegisterBits.H, k );
	}
}
