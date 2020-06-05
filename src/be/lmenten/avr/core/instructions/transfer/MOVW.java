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

package be.lmenten.avr.core.instructions.transfer;

import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rd4_Rr4_pairs;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.assembler.def.AvrRegisterPairs;
import be.lmenten.avr.core.Core;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class MOVW
	extends AbstractInstruction_Rd4_Rr4_pairs
{
	// ========================================================================
	// === CONSTRUTOR(S) ======================================================
	// ========================================================================

	public MOVW( InstructionSet entry, int data )
	{
		super( InstructionSet.MOVW, data );
	}

	// ------------------------------------------------------------------------

	public MOVW( AvrRegisterPairs rd, AvrRegisterPairs rr )
	{
		super( InstructionSet.MOVW, rd, rr );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rd = core.getGeneralRegister( getRd().getLowerRegister() );
		CoreRegister rd1 = core.getGeneralRegister( getRd().getUpperRegister() );

		CoreRegister rr = core.getGeneralRegister( getRr().getLowerRegister() );
		CoreRegister rr1 = core.getGeneralRegister( getRr().getUpperRegister() );

		// --------------------------------------------------------------------

		rd.setData( rr.getData() );
		rd1.setData( rr1.getData() );

		// --------------------------------------------------------------------

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
