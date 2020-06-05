// = ======================================================================== =
// = === AVR Programmer Studio ======= Copyright (c) 2020+ Laurent Menten === =
// = ======================================================================== =
// = = This program is free software: you can redistribute it and/or modify = =
// = = it under the terms of the GNU General Public License as published by = =
// = = the Free Software Foundation, either version 3 of the License, or    = =
// = = (at your option) any later version.                                  = =
// = =                                                                      = =
// = = This program is distributed in the hope that it will be useful, but  = =
// = = WITHOUT ANY WARRANTY; without even the implied warranty of           = =
// = = MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU    = =
// = = General Public License for more details.                             = =
// = =                                                                      = =
// = = You should have received a copy of the GNU General Public License    = =
// = = along with this program. If not, see                                 = =
// = = <https://www.gnu.org/licenses/>.                                     = =
// = ======================================================================== =

package be.lmenten.avr.core;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class Value
{
	private byte value;

	// ------------------------------------------------------------------------

	public interface Operation
	{
		public byte compute( byte value1, byte value2 );
	};

	public interface Operation2
	{
		public byte compute( byte value1, byte value2, byte c );
	};

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public Value( byte value )
	{
		this.value = value;
	}

	// ------------------------------------------------------------------------

	public Value compute( Operation operation, Value operand )
	{
		return new Value( operation.compute( this.value, operand.value ) );
	}

	public Value compute( Operation2 operation, Value operand, boolean c )
	{
		return new Value( operation.compute( this.value, operand.value, c ? (byte)1 : (byte)0 ) );
	}

	// ========================================================================
	// === GETTER(s) ==========================================================
	// ========================================================================

	/**
	 * 
	 * @return
	 */
	public byte getValue()
	{
		return value;
	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 * @return
	 */
	public boolean zero()
	{
		return value == 0;
	}

	// ========================================================================
	// === BIT LEVEL ACCESS ===================================================
	// ========================================================================

	/**
	 * Get the state of a single bit.
	 * 
	 * @param bit
	 * @return
	 */
	public boolean bit( int bit )
	{
		if( (bit < 0) || (bit >= 8) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range" );
		}

		return ((value & (1<<bit)) == (1<<bit));
	}

	public boolean bit0() { return bit( 0 ); }
	public boolean bit1() { return bit( 1 ); }
	public boolean bit2() { return bit( 2 ); }
	public boolean bit3() { return bit( 3 ); }
	public boolean bit4() { return bit( 4 ); }
	public boolean bit5() { return bit( 5 ); }
	public boolean bit6() { return bit( 6 ); }
	public boolean bit7() { return bit( 7 ); }

	/**
	 * Set the state of a single bit.
	 * 
	 * @param bit
	 * @param state
	 */
	public void bit( int bit, final boolean state )
	{
		if( (bit < 0) || (bit >= 8) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range" );
		}

		if( state )
		{
			value |= (1<<bit);
		}
		else
		{
			value &= ~(1<<bit);
		}
	}

	public void bit0( boolean state ) { bit( 0, state ); }
	public void bit1( boolean state ) { bit( 1, state ); }
	public void bit2( boolean state ) { bit( 2, state ); }
	public void bit3( boolean state ) { bit( 3, state ); }
	public void bit4( boolean state ) { bit( 4, state ); }
	public void bit5( boolean state ) { bit( 5, state ); }
	public void bit6( boolean state ) { bit( 6, state ); }
	public void bit7( boolean state ) { bit( 7, state ); }
}
