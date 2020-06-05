package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class CLC
	extends BCLR
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CLC( InstructionSet entry, int data )
	{
		super( InstructionSet.CLC, data );
	}

	// ------------------------------------------------------------------------
	
	public CLC()
	{
		super( InstructionSet.CLC, AvrStatusRegisterBits.C );
	}
}
