package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class CLH
	extends BCLR
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CLH( InstructionSet entry, int data )
	{
		super( InstructionSet.CLH, data );
	}

	// ------------------------------------------------------------------------

	public CLH()
	{
		super( InstructionSet.CLH, AvrStatusRegisterBits.H );
	}
}
