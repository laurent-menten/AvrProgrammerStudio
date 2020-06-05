package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRNE
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRNE( InstructionSet entry, int data )
	{
		super( InstructionSet.BRNE, data );
	}

	// ------------------------------------------------------------------------

	public BRNE( int k )
	{
		super( InstructionSet.BRNE, AvrStatusRegisterBits.Z, k );
	}
}
