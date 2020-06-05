package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRHC
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRHC( InstructionSet entry, int data )
	{
		super( InstructionSet.BRHC, data );
	}

	// ------------------------------------------------------------------------

	public BRHC( int k )
	{
		super( InstructionSet.BRHC, AvrStatusRegisterBits.H, k );
	}
}
