package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class CLI
	extends BCLR
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CLI( InstructionSet entry, int data )
	{
		super( InstructionSet.CLI, data );
	}

	// ------------------------------------------------------------------------

	public CLI()
	{
		super( InstructionSet.CLI, AvrStatusRegisterBits.I );
	}
}
