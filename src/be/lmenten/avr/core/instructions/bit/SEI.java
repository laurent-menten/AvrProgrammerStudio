package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SEI
	extends BSET
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SEI( InstructionSet entry, int data )
	{
		super( InstructionSet.SEI, data );
	}

	// ------------------------------------------------------------------------

	public SEI()
	{
		super( InstructionSet.SEI, AvrStatusRegisterBits.I );
	}
}
