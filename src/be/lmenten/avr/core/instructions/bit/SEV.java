package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SEV
	extends BSET
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SEV( InstructionSet entry, int data )
	{
		super( InstructionSet.SEV, data );
	}

	// ------------------------------------------------------------------------

	public SEV()
	{
		super( InstructionSet.SEV, AvrStatusRegisterBits.V );
	}
}
