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
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CoreData( int address )
	{
		this( address, (byte) 0, (byte) 0 ); 
	}

	public CoreData( int address, byte value )
	{
		this( address, value, (byte) 0 );
	}

	public CoreData( int address, byte value, byte defaultValue )
	{
		super( address, defaultValue );

		internSetData( value );
	}

	// ------------------------------------------------------------------------

	@Override
	public int getDataWidth()
	{
		return 8;
	}


	// ========================================================================
	// === ACTION(s) ==========================================================
	// ========================================================================

	/**
	 * Reset this data cell to its default value.
	 */
	@Override
	public void reset()
	{
		super.reset();

		
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

	/**
	 * several locations in the opcode, the value is packed and can be directly
	 * used.
	 * 
	 * @param mask
	 * @return
	 */
	public byte bits( byte mask )
	{
		byte rc = 0;

		for( int from=0, to=0 ; from < 8 ; from++ )
		{
			if( (mask & (1 << from)) == (1 << from) )
			{
				if( (internGetData() & (1 << from)) == (1 << from) )
				{
					rc |= (1 << to);
				}
				else
				{
					rc &= ~(1 << to);
				}

				to++;
			}
		}

		return rc;
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
		 .append( ") [initial=0x" )
		 .append( String.format( "%02X", getInitialData() ) )
		 .append( "]" )
		 ;

		return s.toString();
	}
}
