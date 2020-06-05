package be.lmenten.avr.core.instructions;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.core.Core;

/**
 * 2nd word of 2 words instructions
 * data in program memory
 * unknown instruction
 */
public class InstructionData
	extends Instruction
{
	private final Instruction rel;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public InstructionData( InstructionSet entry, int data )
	{
		super( InstructionSet.DATA, data );

		this.rel = null;
	}

	/*package*/ InstructionData( Instruction rel )
	{
		super( InstructionSet.DATA, 0x0000 );

		this.rel = rel;
	}

	// ------------------------------------------------------------------------

	public InstructionData( int data )
	{
		this( InstructionSet.DATA, data );
	}

	public InstructionData()
	{
		this( InstructionSet.DATA, 0x0000 );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public boolean hasRelativeInstruction()
	{
		return (rel != null);
	}

	public Instruction getRelativeInstruction()
	{
		return rel;
	}

	public InstructionSet getRelaviteInstructionSetEntry()
	{
		return (rel == null) ? null : rel.getInstructionSetEntry();
	}

	@Override
	public int getOpcodeSize()
	{	
		return (rel == null) ? 1 : rel.getOpcodeSize();
	}
	
	// --------------------------------------------------------------------

	@Override
	protected void setOpcode( int opcode )
	{
		if( rel == null )
		{
			super.setOpcode( opcode );
		}
		else
		{
			rel.setOpcode( opcode );
		}
	}

	@Override
	public int getOpcode()
	{
		return (rel == null) ? super.getOpcode() : rel.getOpcode();
	}

	// --------------------------------------------------------------------

	@Override
	public byte getHighByteValue()
	{
		int data = (rel == null) ? super.getOpcode() : rel.getOpcode();

		return (byte) ((data >> 24) & 0xFF);
	}

	@Override
	public byte getLowByteValue()
	{
		int data = (rel == null) ? super.getOpcode() : rel.getOpcode();

		return (byte) ((data >> 16) & 0xFF);
	}		

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine(core, parsedLine);

		parsedLine.setOperand1( String.format( "0x%04X", (internGetData() & getDataMask()) ) );
	}
}
