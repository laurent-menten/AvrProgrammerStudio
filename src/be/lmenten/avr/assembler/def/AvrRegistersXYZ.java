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
import be.lmenten.avr.assembler.AvrRegisterIndex;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public enum AvrRegistersXYZ
	implements AvrRegisterIndex
{
	X( "RAMPX", R27, R26 )
	{
		@Override
		public int getOperandCode()
		{
			return 0b11;
		}
	},

	Y( "RAMPY", R29, R28 )
	{
		@Override
		public int getOperandCode()
		{
			return 0b10;
		}
	},

	Z( "RAMPZ", R31, R30 )
	{
		@Override
		public int getOperandCode()
		{
			return 0b00;
		}
	},
	;

	// ------------------------------------------------------------------------

	private final String extendedRegisterName;
	private final AvrRegisters upperRegister;
	private final AvrRegisters lowerRegister;

	// ========================================================================
	// ===
	// ========================================================================

	private AvrRegistersXYZ( String extendedRegisterName, AvrRegisters upperRegister, AvrRegisters lowerRegister  )
	{
		this.extendedRegisterName = extendedRegisterName;
		this.upperRegister = upperRegister;
		this.lowerRegister = lowerRegister;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getIndex()
	{
		return 26 + (ordinal() * 2);
	}

	@Override
	public abstract int getOperandCode();

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static AvrRegistersXYZ lookupByOperandCode( int code )
	{
		for( AvrRegistersXYZ r : values() )
		{
			if( r.getOperandCode() == code )
			{
				return r;
			}
		}

		return null;
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

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public String getExtendedRegisterName()
	{
		return extendedRegisterName;
	}
}
