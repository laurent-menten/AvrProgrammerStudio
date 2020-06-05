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

package be.lmenten.avr.assembler.def;

import be.lmenten.avr.assembler.AvrRegister;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public enum AvrRegisterBits
	implements AvrRegister
{
	BIT0,
	BIT1,
	BIT2,
	BIT3,
	BIT4,
	BIT5,
	BIT6,
	BIT7,
	;

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getIndex()
	{
		return ordinal();
	}

	@Override
	public int getOperandCode()
	{
		return ordinal();
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static AvrRegisterBits lookupByOperandCode( int code )
	{
		return values() [ code ];
	}
}
