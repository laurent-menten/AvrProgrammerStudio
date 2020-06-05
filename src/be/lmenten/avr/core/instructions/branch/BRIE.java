package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRIE
	extends BRBS
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRIE( InstructionSet entry, int data )
	{
		super( InstructionSet.BRIE, data );
	}

	// ------------------------------------------------------------------------

	public BRIE( int k )
	{
		super( InstructionSet.BRIE, AvrStatusRegisterBits.I, k );
	}
}
