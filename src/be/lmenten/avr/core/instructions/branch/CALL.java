package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.OperandType;

public class CALL
	extends Instruction
{
	private int k;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CALL( InstructionSet entry, int data )
	{
		super( InstructionSet.CALL, data );

		this.k = entry.getOperand( OperandType.k, data );
	}

	// ------------------------------------------------------------------------

	public CALL( int k )
	{
		super( InstructionSet.CALL );

		this.k = k;
	}

	// ========================================================================
	// === 
	// ========================================================================

	public int getK()
	{
		return k;
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		core.updateProgramCounter( getOpcodeSize() );
		core.pushProgramCounter();

		core.setProgramCounter( getK() );		

		core.updateClockCyclesCounter( 2l );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		parsedLine.setOperand1( String.format( "0x%06X", getK()*2 ) );
	}
}
