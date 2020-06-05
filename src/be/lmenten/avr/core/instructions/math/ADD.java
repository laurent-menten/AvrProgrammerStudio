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

package be.lmenten.avr.core.instructions.math;

import be.lmenten.avr.core.instructions.AbstractInstruction_Rd_Rr;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.instructions.bit.LSL;
import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.Value;

/**
 * @See {@link LSL}
 * @author Laurent Menten
 * @since 1.0
 */
public class ADD
	extends AbstractInstruction_Rd_Rr
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public ADD( InstructionSet entry, int data )
	{
		super( entry, data );
	}

	// for LSL
	protected ADD( InstructionSet entry, AvrRegisters rd, AvrRegisters rr )
	{
		super( entry, rd, rr );
	}

	// ------------------------------------------------------------------------

	public ADD( AvrRegisters rd, AvrRegisters rr )
	{
		this( InstructionSet.ADD, rd, rr );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister _rd = core.getGeneralRegister( this.getRd() );
		Value rd = new Value( (byte) _rd.getData() );

		CoreRegister _rr = core.getGeneralRegister( this.getRr() );
		Value rr = new Value( (byte) _rr.getData() );

		// --------------------------------------------------------------------
		
		Value r = rd.compute( (a,b) -> { return (byte)(a + b); }, rr );

		core.SREG.c( rd.bit7() & rr.bit7() | rr.bit7() & !r.bit7() | !r.bit7() & rd.bit7() );
		core.SREG.h( rd.bit3() & rr.bit3() | rr.bit3() & !r.bit3() | !r.bit3() & !rd.bit3() );
		core.SREG.v( rd.bit7() & rr.bit7() & !r.bit7() | !rd.bit7() & !rr.bit7() & r.bit7() );
		core.SREG.n( r.bit7() );
		core.SREG.s( core.SREG.n() ^ core.SREG.v() );
		core.SREG.z( r.zero() );

		_rd.setData( r.getValue() );

		// --------------------------------------------------------------------
		
		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
