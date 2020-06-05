package be.lmenten.avr.core.instructions.math;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.OperandType;

public class DES
	extends Instruction
{
	private final int K;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public DES( InstructionSet entry, int data )
	{
		super( InstructionSet.DES, data );

		this.K = entry.getOperand( OperandType.K, data );
		check();
	}

	private void check()
	{
		if( (K < 0) || (K > 15) )
		{
			throw new RuntimeException( "K value " + K + " out of range [0..15]" );
		}
	}


	// ------------------------------------------------------------------------

	public DES( int K )
	{
		super( InstructionSet.DES, K );

		this.K = K;
		check();
	}

	// ========================================================================
	// === 
	// ========================================================================

	public int getK()
	{
		return K;
	}
	
	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		if( core.SREG.h() )
		{
			// R15..R0 <-- encrypt( R15..R0, K )
		}
		else
		{
			// R15..R0 <-- decrypt( R15..R0, K )
		}

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void toParsedLine(Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		parsedLine.setOperand1( String.format( "0x%1X", getK() ) );
	}
}
