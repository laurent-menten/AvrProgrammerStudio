package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrRegisterBits;
import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx_b3;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BST
	extends AbstractInstruction_Rx_b3
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BST( InstructionSet entry, int data )
	{
		super( InstructionSet.BST, data );
	}

	// ------------------------------------------------------------------------

	public BST( AvrRegisters rd, AvrRegisterBits b )
	{
		super( InstructionSet.BST, rd, b.getIndex() );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rr = core.getGeneralRegister( getRx() );

		// --------------------------------------------------------------------

		boolean bitValue = rr.bit( getB() );
		core.SREG.bit( AvrStatusRegisterBits.T.getIndex(), bitValue );

		// --------------------------------------------------------------------

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
