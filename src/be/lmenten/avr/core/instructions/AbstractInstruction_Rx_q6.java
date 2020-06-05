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

package be.lmenten.avr.core.instructions;

import be.lmenten.avr.assembler.def.AvrRegisters;

/**
 * Rx (R16..31, 5-bits operand).
 * q 0..63
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public abstract class AbstractInstruction_Rx_q6
	extends AbstractInstruction_Rx
{
	private final int q;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AbstractInstruction_Rx_q6( InstructionSet entry, int data )
	{
		super( entry, data );

		this.q = entry.getOperand( OperandType.q, data );			
		check();
	}

	private void check()
	{
		if( (q < 0) || (q > 63) )
		{
			throw new RuntimeException( "q value " + q + " out of range [0..63]" );
		}
	}

	// ------------------------------------------------------------------------

	public AbstractInstruction_Rx_q6( InstructionSet entry, AvrRegisters rx, int q )
	{
		super( entry, rx );

		this.q = q;
		check();
	}

	// ========================================================================
	// ===
	// ========================================================================

	public int getQ()
	{
		return q;
	}
}
