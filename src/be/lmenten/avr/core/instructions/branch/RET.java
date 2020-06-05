package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;

public class RET
	extends Instruction
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public RET( InstructionSet entry, int data )
	{
		super( InstructionSet.RET, data );
	}

	// ------------------------------------------------------------------------

	public RET()
	{
		super( InstructionSet.RET, 0x0000 );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		core.popProgramCounter();
		core.updateClockCyclesCounter( 1l );
	}
}
