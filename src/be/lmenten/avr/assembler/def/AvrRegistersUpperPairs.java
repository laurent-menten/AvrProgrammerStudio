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

import static be.lmenten.avr.assembler.def.AvrRegisters.*;

import be.lmenten.avr.assembler.AvrRegister;
import be.lmenten.avr.assembler.AvrRegisterPair;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public enum AvrRegistersUpperPairs
	implements AvrRegisterPair
{
	R24_R25	( R25, R24 ),
	R26_R27	( R27, R26 ),
	R28_R29	( R29, R28 ),
	R30_R31	( R31, R30 ),
	;

	public static final AvrRegistersUpperPairs Z = AvrRegistersUpperPairs.R30_R31;
	public static final AvrRegistersUpperPairs Y = AvrRegistersUpperPairs.R28_R29;
	public static final AvrRegistersUpperPairs X = AvrRegistersUpperPairs.R26_R27;

	// ------------------------------------------------------------------------

	private final AvrRegisters upperRegister;
	private final AvrRegisters lowerRegister;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	private AvrRegistersUpperPairs( AvrRegisters upperRegister, AvrRegisters lowerRegister )
	{
		this.upperRegister = upperRegister;
		this.lowerRegister = lowerRegister;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getIndex()
	{
		return ordinal() * 2;
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
	public static AvrRegistersUpperPairs lookupByOperandCode( int code )
	{
		return values() [ code ];
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public AvrRegister getUpperRegister()
	{
		return upperRegister;
	}

	@Override
	public AvrRegister getLowerRegister()
	{
		return lowerRegister;
	}
}
