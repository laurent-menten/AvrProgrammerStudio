package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx_b3;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SBRS
	extends AbstractInstruction_Rx_b3
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SBRS( InstructionSet entry, int data )
	{
		super( InstructionSet.SBRS, data );
	}

	// ------------------------------------------------------------------------

	public SBRS( AvrRegisters rr, int b )
	{
		super( InstructionSet.SBRS, rr, b );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister r = core.getGeneralRegister( getRx() );

		// --------------------------------------------------------------------

		int offset = getOpcodeSize();

		if( r.bit( getB() ) )
		{
			offset += core.getFollowingInstruction().getOpcodeSize();
		}
		
		// --------------------------------------------------------------------

		core.updateProgramCounter( offset );
		core.updateClockCyclesCounter( 1l );
	}
}
