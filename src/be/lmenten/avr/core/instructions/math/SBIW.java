package be.lmenten.avr.core.instructions.math;

import be.lmenten.avr.assembler.def.AvrRegistersUpperPairs;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx2_K6;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SBIW
	extends AbstractInstruction_Rx2_K6
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SBIW( InstructionSet entry, int data )
	{
		super( InstructionSet.SBIW, data );
	}

	// ------------------------------------------------------------------------

	public SBIW( AvrRegistersUpperPairs rd, int K )
	{
		super( InstructionSet.SBIW, rd, K );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
