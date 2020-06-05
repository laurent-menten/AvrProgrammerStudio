package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRLO
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRLO( InstructionSet entry, int data)
	{
		super( InstructionSet.BRLO, data );
	}

	// ------------------------------------------------------------------------

	public BRLO( int k )
	{
		super( InstructionSet.BRLO, AvrStatusRegisterBits.C, k );
	}
}
