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

package be.lmenten.avr.core.instructions.branch;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_A5_b3;
import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class SBIC
	extends AbstractInstruction_A5_b3
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SBIC( InstructionSet entry, int data )
	{
		super( InstructionSet.SBIC, data );
	}

	// ------------------------------------------------------------------------

	public SBIC( int A, int b )
	{
		super( InstructionSet.SBIC, A, b );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister r = core.getIORegister( getA() );

		// --------------------------------------------------------------------

		int offset = getOpcodeSize();

		if( ! r.bit( getB() ) )
		{
			offset += core.getFollowingInstruction().getOpcodeSize();
		}
		
		// --------------------------------------------------------------------

		core.updateProgramCounter( offset );
		core.updateClockCyclesCounter( 1l );
	}
}
