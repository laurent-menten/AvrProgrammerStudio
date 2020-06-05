package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRID
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRID( InstructionSet entry, int data )
	{
		super( InstructionSet.BRID, data );
	}

	// ------------------------------------------------------------------------

	public BRID( int k )
	{
		super( InstructionSet.BRID, AvrStatusRegisterBits.I, k );
	}
}
