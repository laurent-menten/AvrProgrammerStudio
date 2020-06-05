package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;

public class ICALL
	extends Instruction
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public ICALL( InstructionSet entry, int data )
	{
		super( InstructionSet.ICALL, data );
	}

	// ------------------------------------------------------------------------

	public ICALL()
	{
		super( InstructionSet.ICALL, 0x0000 );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		int address = core.getInstructionIndexRegisterValue( false );

		core.pushProgramCounter();
		core.setProgramCounter( address );

		core.updateClockCyclesCounter( 1l );
	}
}
