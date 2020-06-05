package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRSH
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRSH( InstructionSet entry, int data )
	{
		super( InstructionSet.BRSH, data );
	}

	// ------------------------------------------------------------------------

	public BRSH( int k )
	{
		super( InstructionSet.BRSH, AvrStatusRegisterBits.C, k );
	}
}
