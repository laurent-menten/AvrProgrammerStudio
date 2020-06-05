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

package be.lmenten.avr.core.instructions.logic;

import be.lmenten.avr.assembler.def.AvrUpperRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.Value;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx4_K8;
import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class ORI
	extends AbstractInstruction_Rx4_K8
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public ORI( InstructionSet entry, int data )
	{
		super( entry, data );
	}

	public ORI( InstructionSet entry, AvrUpperRegisters rd, byte K )
	{
		super( entry, rd, K );
	}

	// ------------------------------------------------------------------------

	public ORI( AvrUpperRegisters rd, byte K )
	{
		this( InstructionSet.ORI, rd, K );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister _rd = core.getGeneralRegister( getRx() );
		Value rd = new Value( (byte) _rd.getData() );
		Value K = new Value( (byte) getK() );

		Value r = rd.compute( (a,b) -> { return (byte)(a | b); }, K );
		
		core.SREG.v( false );
		core.SREG.n( r.bit7() );
		core.SREG.s( core.SREG.n() ^ core.SREG.v() );
		core.SREG.z( r.zero() );

		_rd.setData( r.getValue() );

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
