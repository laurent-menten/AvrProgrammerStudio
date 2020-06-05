package be.lmenten.avr.core.instructions.math;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rd_Rr;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SBC
	extends AbstractInstruction_Rd_Rr
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SBC( InstructionSet entry, int data )
	{
		super( InstructionSet.SBC, data );
	}

	// ------------------------------------------------------------------------

	public SBC( AvrRegisters rd, AvrRegisters rr )
	{
		super( InstructionSet.SBC, rd, rr );
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
