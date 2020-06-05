package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SEZ
	extends BSET
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SEZ( InstructionSet entry, int data )
	{
		super( InstructionSet.SEZ, data );
	}

	// ------------------------------------------------------------------------

	public SEZ()
	{
		super( InstructionSet.SEZ, AvrStatusRegisterBits.Z );
	}
}
