package be.lmenten.avr.assembler.parser;

public class ExpressionValue
	implements Comparable<ExpressionValue>
{
	public static final ExpressionValue zero = new ExpressionValue( 0 );
	public static final ExpressionValue one = new ExpressionValue( 1 );

	public static final ExpressionValue pi = new ExpressionValue( Math.PI );
	public static final ExpressionValue e = new ExpressionValue( Math.E );

	// ========================================================================
	// ===
	// ========================================================================

	public enum Type
	{
		INTEGER,
		DECIMAL,
		STRING,
		;
	}

	// ------------------------------------------------------------------------

	private final Type type;
	private final long ivalue;
	private final double dvalue;
	private final String svalue;

	private boolean isProtected;
	
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	// ------------------------------------------------------------------------
	// - Integer --------------------------------------------------------------
	// ------------------------------------------------------------------------

	public ExpressionValue( long ivalue )
	{
		this.type = Type.INTEGER;

		this.ivalue = ivalue;
		this.dvalue = (double) ivalue;
		this.svalue = null;

		this.isProtected = false;
	}

	public ExpressionValue( int ivalue )
	{
		this.type = Type.INTEGER;

		this.ivalue = (long) ivalue;
		this.dvalue = (double) ivalue;
		this.svalue = null;

		this.isProtected = false;
	}

	public ExpressionValue( short ivalue )
	{
		this.type = Type.INTEGER;

		this.ivalue = (long) ivalue;
		this.dvalue = (double) ivalue;
		this.svalue = null;

		this.isProtected = false;
	}

	public ExpressionValue( byte ivalue )
	{
		this.type = Type.INTEGER;

		this.ivalue = (long) ivalue;
		this.dvalue = (double) ivalue;
		this.svalue = null;

		this.isProtected = false;
	}

	// ------------------------------------------------------------------------
	// - Decimal --------------------------------------------------------------
	// ------------------------------------------------------------------------

	public ExpressionValue( double dvalue )
	{
		this.type = Type.DECIMAL;

		this.dvalue = dvalue;
		this.ivalue = (long) dvalue;
		this.svalue = null;

		this.isProtected = false;
	}

	public ExpressionValue( float dvalue )
	{
		this.type = Type.DECIMAL;

		this.dvalue = dvalue;
		this.ivalue = (long) dvalue;
		this.svalue = null;

		this.isProtected = false;
	}

	// ------------------------------------------------------------------------
	// - String ---------------------------------------------------------------
	// ------------------------------------------------------------------------

	public ExpressionValue( String svalue )
	{
		if( svalue == null )
		{
			throw new NullPointerException();
		}

		// --------------------------------------------------------------------

		this.type = Type.STRING;

		this.svalue = svalue;
		this.dvalue = 0.0d;
		this.ivalue = 0l;

		this.isProtected = false;
	}

	// ========================================================================
	// ===
	// ========================================================================

	private void checkString()
	{
		if( this.type != Type.STRING )
		{
			throw new UnsupportedOperationException();
		}
	}


	private void checkStringOperation( ExpressionValue value )
	{
		if( value == null )
		{
			throw new NullPointerException();
		}

		if( this.type != Type.STRING && value.type != Type.STRING  )
		{
			throw new UnsupportedOperationException();
		}
	}

	// ------------------------------------------------------------------------

	private void checkNotString()
	{
		if( this.type == Type.STRING )
		{
			throw new UnsupportedOperationException();
		}		
	}

	private void checkNotStringOperation( ExpressionValue value )
	{
		if( value == null )
		{
			throw new NullPointerException();
		}

		if( this.type == Type.STRING || value.type == Type.STRING )
		{
			throw new UnsupportedOperationException();
		}		
	}

	// ========================================================================
	// ===
	// ========================================================================

	public Type getType()
	{
		return type;
	}

	// ------------------------------------------------------------------------

	public void setProtected()
	{
		this.isProtected = true;
	}

	public boolean isProtected()
	{
		return isProtected;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public byte getByte()
	{
		checkNotString();

		return (byte) ivalue;
	}

	public int getWord()
	{
		checkNotString();

		return (int) (ivalue & 0xFFFF);
	}

	public int getDWord()
	{
		checkNotString();

		return (int) (ivalue & 0xFFFFFFFF);
	}

	public long getQWord()
	{
		checkNotString();

		return ivalue;
	}

	// ------------------------------------------------------------------------

	public float getFloat()
	{
		checkNotString();

		return (float) dvalue;
	}

	public double getDouble()
	{
		checkNotString();

		return dvalue;
	}

	// ------------------------------------------------------------------------

	public String getString()
	{
		checkString();

		return svalue;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public ExpressionValue low()
	{
		checkNotString();

		return new ExpressionValue( ivalue & 0xFF );
	}

	public ExpressionValue high()
	{
		checkNotString();

		return new ExpressionValue( (ivalue >> 8) & 0xFF );
	}

	public ExpressionValue page()
	{
		checkNotString();

		return new ExpressionValue( (ivalue >> 16) & 0x3F );
	}
	
	public ExpressionValue byte3()
	{
		checkNotString();

		return new ExpressionValue( (ivalue >> 16) & 0xFF );
	}

	public ExpressionValue byte4()
	{
		checkNotString();

		return new ExpressionValue( (ivalue >> 24) & 0xFF );
	}

	// ------------------------------------------------------------------------

	public ExpressionValue lwrd()
	{
		checkNotString();

		return new ExpressionValue( ivalue & 0xFFFF );
	}

	public ExpressionValue hwrd()
	{
		checkNotString();

		return new ExpressionValue( (ivalue >> 16) & 0xFFFF );
	}

	// ========================================================================
	// ===
	// ========================================================================
	
	public ExpressionValue not()
	{
		checkNotString();

		return new ExpressionValue( ~ this.ivalue );
	}

	public ExpressionValue negative()
	{
		checkNotString();

		return new ExpressionValue( - this.dvalue );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public ExpressionValue add( ExpressionValue value )
	{
		if( this.type == Type.STRING )
		{
			if( value.type != Type.STRING )
			{
				throw new UnsupportedOperationException();
			}

			return new ExpressionValue( this.svalue + value.svalue );			
		}
		else
		{
			return new ExpressionValue( this.dvalue + value.dvalue );
		}
	}

	public ExpressionValue sub( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.dvalue - value.dvalue );
	}

	public ExpressionValue and( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.ivalue & value.ivalue );
	}

	// ------------------------------------------------------------------------

	public ExpressionValue mul( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.dvalue * value.dvalue );
	}

	public ExpressionValue div( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.dvalue / value.dvalue );
	}

	public ExpressionValue mod( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.dvalue % value.dvalue );
	}

	// ------------------------------------------------------------------------

	public ExpressionValue or( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.ivalue | value.ivalue );
	}

	public ExpressionValue xor( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.ivalue ^ value.ivalue );
	}

	public ExpressionValue lshift( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.ivalue << value.ivalue );
	}

	public ExpressionValue rshift( ExpressionValue value )
	{
		checkNotStringOperation( value );

		return new ExpressionValue( this.ivalue >> value.ivalue );
	}

	// ------------------------------------------------------------------------

	public ExpressionValue q7()
	{
		checkNotString();

		// FIXME make q7
		return new ExpressionValue( 0x00000000 );
	}

	public ExpressionValue q15()
	{
		checkNotString();

		// FIXME make q15
		return new ExpressionValue( 0x00000000 );
	}

	public ExpressionValue integer()
	{
		checkNotString();

		return new ExpressionValue( this.ivalue );
	}

	public ExpressionValue frac()
	{
		checkNotString();

		return new ExpressionValue( this.dvalue - this.ivalue );
	}

	// ------------------------------------------------------------------------

	public ExpressionValue abs()
	{
		checkNotString();

		return new ExpressionValue( Math.abs( this.dvalue ) );
	}

	public ExpressionValue exp2()
	{
		checkNotString();

		return new ExpressionValue( Math.pow( 2, this.dvalue ) );
	}

	public ExpressionValue log2()
	{
		checkNotString();

		return new ExpressionValue( Math.log10( this.dvalue ) / Math.log10( 2 ) );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public ExpressionValue strlen()
	{
		checkString();

		return new ExpressionValue( svalue.length() );
	}

	public ExpressionValue concat( String value )
	{
		checkString();

		if( value == null )
		{
			throw new NullPointerException();
		}

		return new ExpressionValue( svalue + value );
	}

	// ========================================================================
	// === Comparison =========================================================
	// ========================================================================

	@Override
	public int compareTo( ExpressionValue value )
	{
		switch( this.type )
		{
			case INTEGER:
				checkNotStringOperation( value );
				
				return compareTo( value.ivalue );

			case DECIMAL:
				checkNotStringOperation( value );
				
				return compareTo( value.dvalue );

			case STRING:
				checkStringOperation( value );
				
				return compareTo( value.svalue );
		}

		throw new RuntimeException( "*** unaccessible code ***" );
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param value
	 * @return
	 */
	public int compareTo( long value )
	{
		checkNotString();

		if( ivalue == value )
			return 0;

		if( ivalue < value )
			return 1;
		else
			return -1;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public int compareTo( double value )
	{
		checkNotString();

		if( ivalue == value )
			return 0;

		if( ivalue < value )
			return 1;
		else
			return -1;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public int compareTo( String value )
	{
		checkString();

		if( svalue == null )
		{
			throw new NullPointerException();
		}

		return svalue.compareTo( value );
	}
		
	// ========================================================================
	// === Object =============================================================
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( "[" );

		switch( type )
		{
			case INTEGER:	s.append( 'I' ); break;
			case DECIMAL:	s.append( 'D' ); break;
			case STRING:	s.append( 'S' ); break;
		}

		s.append( isProtected ? '*' : ' ' )
		 .append( ' ' );

		if( svalue == null )
		{
			s.append( String.format( "0x%08X", ivalue ) )
			 .append( ", " )
			 .append( String.format( "%f", dvalue ) );
		}
		else
		{
			s.append( '"' )
			 .append( svalue )
			 .append( '"' );
		}
		
		s.append( "]" );

		return s.toString();
	}
}
