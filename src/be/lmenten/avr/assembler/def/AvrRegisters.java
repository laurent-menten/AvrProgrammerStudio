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
public enum AvrRegisters
	implements AvrRegister
{
	R0,
	R1,
	R2,
	R3,
	R4,
	R5,
	R6,
	R7,
	R8,
	R9,
	R10,
	R11,
	R12,
	R13,
	R14,
	R15,
	R16,
	R17,
	R18,
	R19,
	R20,
	R21,
	R22,
	R23,
	R24,
	R25,
	R26,
	R27,
	R28,
	R29,
	R30,
	R31,
	;

	public static final AvrRegisters ZH = R31;
	public static final AvrRegisters ZL = R30;
	public static final AvrRegisters YH = R29;
	public static final AvrRegisters YL = R28;
	public static final AvrRegisters XH = R27;
	public static final AvrRegisters XL = R26;

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
	public static AvrRegisters lookupByOperandCode( int code )
	{
		return values() [ code ];
	}
}
