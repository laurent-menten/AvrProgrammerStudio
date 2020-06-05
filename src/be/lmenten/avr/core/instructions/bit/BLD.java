package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrRegisterBits;
import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx_b3;
import be.lmenten.avr.core.instructions.InstructionSet;

public class BLD
	extends AbstractInstruction_Rx_b3
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BLD( InstructionSet entry, int data )
	{
		super( InstructionSet.BLD, data );
	}

	// ------------------------------------------------------------------------

	public BLD( AvrRegisters rd, AvrRegisterBits b )
	{
		super( InstructionSet.BLD, rd, b.getIndex() );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rd = core.getGeneralRegister( getRx() );

		// --------------------------------------------------------------------

		boolean tValue = core.SREG.bit( AvrStatusRegisterBits.T.getIndex() );
		rd.bit( getB(), tValue );

		// --------------------------------------------------------------------

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
