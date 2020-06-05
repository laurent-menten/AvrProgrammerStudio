package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRVS
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRVS( InstructionSet entry, int data )
	{
		super( InstructionSet.BRVS, data );
	}

	// ------------------------------------------------------------------------

	public BRVS( int k )
	{
		super( InstructionSet.BRVS, AvrStatusRegisterBits.V, k );
	}
}
