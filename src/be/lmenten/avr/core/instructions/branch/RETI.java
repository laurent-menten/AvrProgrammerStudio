package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;

public class RETI
	extends Instruction
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public RETI( InstructionSet entry, int data )
	{
		super( InstructionSet.RETI, data );
	}

	// ------------------------------------------------------------------------

	public RETI()
	{
		super( InstructionSet.RETI, 0x0000 );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		core.SREG.bit( AvrStatusRegisterBits.I.getIndex(), true );
		core.popProgramCounter();
		core.updateClockCyclesCounter( 1l );
	}
}
