package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SEN
	extends BSET
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SEN( InstructionSet entry, int data )
	{
		super( InstructionSet.SEN, data );
	}

	// ------------------------------------------------------------------------

	public SEN()
	{
		super( InstructionSet.SEN, AvrStatusRegisterBits.N );
	}
}
