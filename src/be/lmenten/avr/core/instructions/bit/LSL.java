package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.math.ADD;

public class LSL
	extends ADD
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public LSL( InstructionSet entry, int data )
	{
		super( InstructionSet.LSL, data );
	}

	// ------------------------------------------------------------------------

	public LSL( AvrRegisters rd )
	{
		super( InstructionSet.LSL, rd, rd );
	}
}
