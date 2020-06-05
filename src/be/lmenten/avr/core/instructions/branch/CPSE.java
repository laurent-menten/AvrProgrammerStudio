package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rd_Rr;
import be.lmenten.avr.core.instructions.InstructionSet;

public class CPSE
	extends AbstractInstruction_Rd_Rr
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CPSE( InstructionSet entry, int data )
	{
		super( InstructionSet.CPSE, data );
	}

	// ------------------------------------------------------------------------

	public CPSE( AvrRegisters rd, AvrRegisters rr )
	{
		super( InstructionSet.CPSE, rd, rr );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rd = core.getGeneralRegister( getRd() );
		CoreRegister rr = core.getGeneralRegister( getRr() );

		// --------------------------------------------------------------------

		int offset = getOpcodeSize();

		if( rd.getData() == rr.getData() )
		{
			offset += core.getFollowingInstruction().getOpcodeSize();
		}
		
		// --------------------------------------------------------------------

		core.updateProgramCounter( offset );
		core.updateClockCyclesCounter( 1l );
	}
}
