package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class CLN
	extends BCLR
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CLN( InstructionSet entry, int data )
	{
		super( InstructionSet.CLN, data );
	}

	// ------------------------------------------------------------------------

	public CLN()
	{
		super( InstructionSet.CLN, AvrStatusRegisterBits.N );
	}
}
