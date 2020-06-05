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

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.assembler.def.AvrRegistersXYZ;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreData;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx;
import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class LPM
	extends AbstractInstruction_Rx
{
	private boolean zInc;

	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public LPM( InstructionSet entry, int data )
	{
		super( entry, data );

		switch( entry )
		{
			case LPM_Z:
			{
				this.zInc = false;
				break;
			}

			case LPM_ZP:
			{
				this.zInc = true;
				break;
			}

			default:
				throw new RuntimeException(
					"INTERNAL ERROR: "
					+ "invalid InstructionSet entry '" + entry + "' "
					+ "for LPM instruction handler."
				);
		}
	}

	// ------------------------------------------------------------------------

	public LPM( AvrRegisters rd )
	{
		this( rd, false );
	}

	public LPM( AvrRegisters rd, boolean zInc )
	{
		super( zInc ? InstructionSet.LPM_ZP : InstructionSet.LPM_Z, rd );

		this.zInc = zInc;
	}
	
	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rd = core.getGeneralRegister( getRx() );

		int zAddress = core.getIndexRegisterValue( AvrRegistersXYZ.Z, false );
		CoreData zCell = core.getSramCell( zAddress );

		// --------------------------------------------------------------------

		rd.setData( zCell.getData() );

		if( zInc )
		{
			zAddress++;
			core.setIndexRegisterValue( AvrRegistersXYZ.Z, zAddress, false );
		}

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 3l );
	}
}
