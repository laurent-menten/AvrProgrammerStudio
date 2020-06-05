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

import be.lmenten.avr.assembler.ParsedAssemblerLine;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class CoreData
	extends CoreMemoryCell
{
	private byte defaultValue;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CoreData()
	{
		this( (byte) 0, (byte) 0 ); 
	}

	public CoreData( byte value )
	{
		this( value, (byte) 0 );
	}

	public CoreData( byte value, byte defaultValue )
	{
		setData( value );

		this.defaultValue = defaultValue;
	}

	// ------------------------------------------------------------------------

	@Override
	public int getDataWidth()
	{
		return 8;
	}

	// ========================================================================
	// === ACCESSOR(s) ========================================================
	// ========================================================================


	/**
	 * Get the default value of this data cell.
	 * 
	 * @return
	 */
	public byte getDefaultValue()
	{
		return defaultValue;
	}

	// ========================================================================
	// === ACTION(s) ==========================================================
	// ========================================================================

	/**
	 * Reset this data cell to its default value.
	 */
	public void reset()
	{
		privSetData( getDefaultValue() );
	}

	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Return the value of this data cell ANDed with the given mask.
	 * 
	 * <p>
	 * !!! Does not trigger event or record accesses.
	 * 
	 * @param mask
	 * @return
	 */
	public byte mask( byte mask )
	{
		return (byte) (internGetData() & mask);
	}

	// ========================================================================
	// === DEBUGGER & DISASSEMLER SUPPORT =====================================
	// ========================================================================

	/**
	 * 
	 * @param core
	 * @param parsedLine
	 * @return
	 */
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		parsedLine.setMnemonic( "DB" );
		parsedLine.setOperand1( String.format( "%02X", (internGetData() & getDataMask()) ) );
	}

	// ========================================================================
	// === Object =============================================================
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( super.toString() )
		 .append( " ")

		 .append( String.format( "0x%02X", (internGetData() & getDataMask()) ) )
		 .append( " (" )
		 .append( bit7() ? '1' : '0' )
		 .append( bit6() ? '1' : '0' )
		 .append( bit5() ? '1' : '0' )
		 .append( bit4() ? '1' : '0' )
		 .append( bit3() ? '1' : '0' )
		 .append( bit2() ? '1' : '0' )
		 .append( bit1() ? '1' : '0' )
		 .append( bit0() ? '1' : '0' )
		 .append( ") [default=0x" )
		 .append( String.format( "%02X", defaultValue ) )
		 .append( "]" )
		 ;

		return s.toString();
	}
}
