package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;

public class IJMP
	extends Instruction
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public IJMP( InstructionSet entry, int data )
	{
		super( InstructionSet.IJMP, data );
	}

	// ------------------------------------------------------------------------

	public IJMP()
	{
		super( InstructionSet.IJMP, 0x0000 );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		int address = core.getInstructionIndexRegisterValue( false );

		core.setProgramCounter( address );

		core.updateClockCyclesCounter( 1l );
	}
}
