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

package be.lmenten.avr.core.analysis;

import be.lmenten.avr.core.instructions.Instruction;

/**
 * 
 *
 * @author Laurent Menten
 * @version 1.0, (12 Jul 2020)
 * @since 1.0
 */
public class Access
{
	private final long tick;
	private final Instruction instruction;
	private final AccessType accessType;

	// =========================================================================
	// ===
	// =========================================================================

	public Access( long tick, Instruction instruction, AccessType accessType )
	{
		this.tick = tick;
		this.instruction = instruction;
		this.accessType = accessType;
	}

	// =========================================================================
	// ===
	// =========================================================================

	public long getTick()
	{
		return tick;
	}

	public Instruction getInstruction()
	{
		return instruction;
	}

	public AccessType getAccessType()
	{
		return accessType;
	}
}
