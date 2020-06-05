package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;

public class EICALL
	extends Instruction
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public EICALL( InstructionSet entry, int data )
	{
		super( InstructionSet.EICALL, data );
	}

	// ------------------------------------------------------------------------

	public EICALL()
	{
		super( InstructionSet.EICALL, 0x0000 );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		int address = core.getInstructionIndexRegisterValue( true );

		core.pushProgramCounter();
		core.setProgramCounter( address );

		core.updateClockCyclesCounter( 1l );
	}
}
