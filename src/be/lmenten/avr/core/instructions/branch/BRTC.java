package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BRTC
	extends BRBC
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRTC( InstructionSet entry, int data )
	{
		super( InstructionSet.BRTC, data );
	}

	// ------------------------------------------------------------------------

	public BRTC( int k )
	{
		super( InstructionSet.BRTC, AvrStatusRegisterBits.T, k );
	}
}
