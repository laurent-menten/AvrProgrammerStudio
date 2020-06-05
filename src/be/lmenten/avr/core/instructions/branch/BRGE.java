package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRGE
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRGE( InstructionSet entry, int data )
	{
		super( InstructionSet.BRGE, data );
	}

	// ------------------------------------------------------------------------

	public BRGE( int k )
	{
		super( InstructionSet.BRGE, AvrStatusRegisterBits.S, k );
	}
}
