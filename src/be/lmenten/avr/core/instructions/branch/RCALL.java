package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.OperandType;

public class RCALL
	extends Instruction
{
	private int k;

	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public RCALL( InstructionSet entry, int data )
	{
		super( InstructionSet.RCALL, data );

		this.k = entry.getOperand( OperandType.k, data );

		if( (this.k & 0b0000_1000_0000_0000) == 0b0000_1000_0000_0000 )
		{
			this.k |= ~0b0000_1111_1111_1111;
		}
	}

	// ------------------------------------------------------------------------

	public RCALL( int k )
	{
		super( InstructionSet.RCALL );

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

		core.updateProgramCounter( k );

		core.updateClockCyclesCounter( 2l );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		int address = getK();
		if( parsedLine.hasAddress() )
		{
			address += 1 + parsedLine.getAddress();
		}

		parsedLine.setOperand1( String.format( "0x%06X", address*2 ) );			
	}
}
