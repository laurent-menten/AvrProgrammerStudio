package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.OperandType;

public class BCLR
	extends Instruction
{
	private AvrStatusRegisterBits s;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BCLR( InstructionSet entry, int data )
	{
		super( entry, data );

		int sOperandCode = InstructionSet.BCLR.getOperand( OperandType.s, data );
		this.s = AvrStatusRegisterBits.lookupByOperandCode( sOperandCode );
	}

	// for CLI, CLT, CLH, CLS, CLV, CLN, CLZ, CLC
	protected BCLR(  InstructionSet entry, AvrStatusRegisterBits s )
	{
		super( entry );

		this.s = s;
	}

	// ------------------------------------------------------------------------

	public BCLR( AvrStatusRegisterBits s )
	{
		this( InstructionSet.BCLR, s );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public AvrStatusRegisterBits getS()
	{
		return s;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		core.SREG.bit( s.getIndex(), false );

		// --------------------------------------------------------------------

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		if( getInstructionSetEntry() == InstructionSet.BCLR )
		{
			parsedLine.setOperand1( String.format( "%d", getS().getIndex() ) );
		}
	}
}
