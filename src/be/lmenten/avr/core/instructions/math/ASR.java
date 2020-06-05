package be.lmenten.avr.core.instructions.math;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx;
import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class ASR
	extends AbstractInstruction_Rx
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public ASR( InstructionSet entry, int data )
	{
		super( InstructionSet.ASR, data );
	}

	// ------------------------------------------------------------------------

	public ASR( AvrRegisters rd )
	{
		super( InstructionSet.ASR, rd );
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
