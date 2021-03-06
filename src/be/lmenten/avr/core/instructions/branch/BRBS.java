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

import be.lmenten.avr.assembler.def.AvrStatusRegisterBits;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.AbstractInstruction_k7_s3;
import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class BRBS
	extends AbstractInstruction_k7_s3
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BRBS( InstructionSet entry, int data )
	{
		super( entry, data );
	}

	// for BRCS, BRHS, BRIE, BRLO, BRLT BRMI, BRTS, BRVS
	protected BRBS( InstructionSet entry, AvrStatusRegisterBits s, int k )
	{
		super( entry, k, s.getIndex() );
	}

	// ------------------------------------------------------------------------

	public BRBS( AvrStatusRegisterBits s, int k )
	{
		this( InstructionSet.BRBS, s, k );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		if( ! core.SREG.bit( getS() ) )
		{
			core.setProgramCounter( getK() );
		}
		else
		{
			core.updateProgramCounter( getOpcodeSize() );
		}

		core.updateClockCyclesCounter( 1l );
	}
}
