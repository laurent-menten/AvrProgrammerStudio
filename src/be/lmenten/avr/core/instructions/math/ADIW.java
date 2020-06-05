package be.lmenten.avr.core.instructions.math;

import be.lmenten.avr.assembler.def.AvrRegistersUpperPairs;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx2_K6;
import be.lmenten.avr.core.instructions.InstructionSet;

public class ADIW
	extends AbstractInstruction_Rx2_K6
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public ADIW( InstructionSet entry, int data )
	{
		super( InstructionSet.ADIW, data );
	}

	// ------------------------------------------------------------------------

	public ADIW( AvrRegistersUpperPairs rd, int K )
	{
		super( InstructionSet.ADIW, rd, K );
	}

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
