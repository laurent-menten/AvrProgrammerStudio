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

package be.lmenten.avr.core.instructions.bit;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx;
import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class LSR
	extends AbstractInstruction_Rx
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public LSR( InstructionSet entry, int data )
	{
		super( InstructionSet.LSR, data );
	}

	// ------------------------------------------------------------------------

	public LSR( AvrRegisters rd )
	{
		super( InstructionSet.LSR, rd );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rd = core.getGeneralRegister( getRx() );

		// --------------------------------------------------------------------

		core.SREG.bit( AvrStatusRegisterBits.C.getIndex(), rd.bit0() );

		rd.setData( (byte) ((rd.getData() >> 7) & 0x7F) );

		// --------------------------------------------------------------------

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
