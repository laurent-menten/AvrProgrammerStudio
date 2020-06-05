package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class CLV
	extends BCLR
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CLV( InstructionSet entry, int data )
	{
		super( InstructionSet.CLV, data );
	}

	// ------------------------------------------------------------------------

	public CLV()
	{
		super( InstructionSet.CLV, AvrStatusRegisterBits.V );
	}
}
