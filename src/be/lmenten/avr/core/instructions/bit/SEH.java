package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SEH
	extends BSET
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SEH( InstructionSet entry, int data )
	{
		super( InstructionSet.SEH, data );
	}

	// ------------------------------------------------------------------------

	public SEH()
	{
		super( InstructionSet.SEH, AvrStatusRegisterBits.H );
	}
}
