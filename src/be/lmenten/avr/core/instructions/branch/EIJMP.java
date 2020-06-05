package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;

public class EIJMP
	extends Instruction
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public EIJMP( InstructionSet entry, int data )
	{
		super( InstructionSet.EIJMP, data );
	}

	// ------------------------------------------------------------------------

	public EIJMP()
	{
		super( InstructionSet.EIJMP, 0x0000 );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		int address = core.getInstructionIndexRegisterValue( true );

		core.setProgramCounter( address );

		core.updateClockCyclesCounter( 1l );
	}
}
