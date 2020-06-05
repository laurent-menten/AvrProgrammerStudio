package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx_b3;
import be.lmenten.avr.core.instructions.InstructionSet;

public class SBRC
	extends AbstractInstruction_Rx_b3
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SBRC( InstructionSet entry, int data )
	{
		super( InstructionSet.SBRC, data );
	}

	// ------------------------------------------------------------------------

	public SBRC( AvrRegisters rr, int b )
	{
		super( InstructionSet.SBRC, 0x0000 );
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

		if( ! r.bit( getB() ) )
		{
			offset += core.getFollowingInstruction().getOpcodeSize();
		}
		
		// --------------------------------------------------------------------

		core.updateProgramCounter( offset );
		core.updateClockCyclesCounter( 1l );
	}
}
