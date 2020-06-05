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

package be.lmenten.avr.core.instructions.register;

import be.lmenten.avr.assembler.def.AvrUpperRegisters;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.logic.ANDI;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class CBR
	extends ANDI
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CBR( InstructionSet entry, int data )
	{
		super( InstructionSet.CBR, data );
	}

	// ------------------------------------------------------------------------

	public CBR( AvrUpperRegisters rd, byte K )
	{
		super( InstructionSet.CBR, rd, (byte)(1 - K) );
	}
}
