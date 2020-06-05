package be.lmenten.avr.core.instructions.math;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rd_Rr;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SUB
	extends AbstractInstruction_Rd_Rr
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SUB( InstructionSet entry, int data )
	{
		super( InstructionSet.SUB, data );
	}

	// ------------------------------------------------------------------------

	public SUB( AvrRegisters rd, AvrRegisters rr )
	{
		super( InstructionSet.SUB, rd, rr );
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
