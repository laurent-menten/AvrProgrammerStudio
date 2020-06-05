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

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;

/**
 * Rx (R16..31, 5-bits operand).
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public abstract class AbstractInstruction_Rx_b3
	extends AbstractInstruction_Rx
{
	private final int b;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AbstractInstruction_Rx_b3( InstructionSet entry, int data )
	{
		super( entry, data );

		this.b = entry.getOperand( OperandType.b, data );			
		check();
	}

	private void check()
	{
		if( (b < 0) || (b > 7) )
		{
			throw new RuntimeException( "b value " + b + " out of range [0..7]" );
		}
	}

	// ------------------------------------------------------------------------

	public AbstractInstruction_Rx_b3( InstructionSet entry, AvrRegisters rx, int b )
	{
		super( entry, rx );

		this.b = b;
		check();
	}

	// ========================================================================
	// ===
	// ========================================================================

	public int getB()
	{
		return b;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		parsedLine.setOperand1( getRx().toString() );
		parsedLine.setOperand2( String.format( "%d", getB() ) );
	}
}
