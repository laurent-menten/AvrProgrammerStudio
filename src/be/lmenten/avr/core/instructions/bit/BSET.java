package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.OperandType;

public class BSET
	extends Instruction
{
	private AvrStatusRegisterBits s;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BSET( InstructionSet entry, int data )
	{
		super( entry, data );

		int sOperandCode = InstructionSet.BSET.getOperand( OperandType.s, data );
		this.s = AvrStatusRegisterBits.lookupByOperandCode( sOperandCode );
	}

	// for SEI, SET, SEH, SES, SEV, SEN, SEZ, SEC
	protected BSET(  InstructionSet entry, AvrStatusRegisterBits s )
	{
		super( entry );

		this.s = s;
	}

	// ------------------------------------------------------------------------

	public BSET( AvrStatusRegisterBits s )
	{
		this( InstructionSet.BSET, s );
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
		core.SREG.bit( s.getIndex(), true );

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

		if( getInstructionSetEntry() == InstructionSet.BSET )
		{
			parsedLine.setOperand1( String.format( "%d", getS().getIndex() ) );
		}
	}
}
