package be.lmenten.avr.assembler;

public class ParsedAssemblerLine
{
	private boolean hasAddress;
	private int address;

	private int lineNumber;

	// ------------------------------------------------------------------------

	private String label;
	private String opcode;
	private String mnemonic;
	private String operand1;
	private String operand2;
	private String comment;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public ParsedAssemblerLine()
	{
		hasAddress = false;
		address = 0x00000;

		lineNumber = 0;

		label = null;
		opcode = null;
		mnemonic = null;
		operand1 = null;
		operand2 = null;
		comment = null;
	}

	// ========================================================================
	// === GETTER(s) / SETTER(s) ==============================================
	// ========================================================================

	public boolean hasAddress()
	{
		return hasAddress;
	}

	public void setAddress( int address )
	{
		this.address = address;
		this.hasAddress = true;
	}

	public int getAddress()
	{
		return address;
	}

	// ------------------------------------------------------------------------

	public void setLineNumber( int lineNumber )
	{
		this.lineNumber = lineNumber;
	}

	public int getLineNumber()
	{
		return lineNumber;
	}

	// ------------------------------------------------------------------------

	public void setLabel( String label )
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}

	// ------------------------------------------------------------------------

	public void setOpcode( String opcode )
	{
		this.opcode = opcode;
	}

	public String getOpcode()
	{
		return opcode;
	}

	// ------------------------------------------------------------------------

	public void setMnemonic( String mnemonic )
	{
		this.mnemonic = mnemonic;
	}

	public String getMnemonic()
	{
		return mnemonic;
	}

	// ------------------------------------------------------------------------

	public void setOperand1( String operand1 )
	{
		this.operand1 = operand1;
	}

	public String getOperand1()
	{
		return operand1;
	}

	// ------------------------------------------------------------------------

	public void setOperand2( String operand2 )
	{
		this.operand2 = operand2;
	}

	public String getOperand2()
	{
		return operand2;
	}

	// ------------------------------------------------------------------------

	public void setComment( String comment )
	{
		this.comment = comment;
	}

	public String getComment()
	{
		return comment;
	}

	// ========================================================================
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		if( hasAddress )
		{
			s.append( String.format( "%05X: ", getAddress() * 2 ) );
		}

		if( opcode != null )
		{
			s.append( opcode );
			s.append( ' ' );
		}

		s.append( mnemonic );

		if( operand1 != null )
		{
			s.append( ' ' );
			s.append( operand1 );

			if( operand2 != null )
			{
				s.append( ", " );
				s.append( operand2 );
			}
		}

		if( comment != null )
		{
			s.append( "     ; " );
			s.append( comment );
		}

		return s.toString();
	}
}
