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
import be.lmenten.avr.assembler.def.AvrUpperRegisters;
import be.lmenten.avr.core.Core;

/**
 * Rx (R16..31, 5-bits operand).
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public abstract class AbstractInstruction_Rx4_K8
	extends AbstractInstruction_Rx4
{
	private final int k;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AbstractInstruction_Rx4_K8( InstructionSet entry, int data )
	{
		super( entry, data );

		this.k = entry.getOperand( OperandType.K, data );			
		check();
	}

	private void check()
	{
		if( ((k & 0xFF) < 0) || ((k & 0xFF) > 255) )
		{
			throw new RuntimeException( "K value " + k + " out of range [0..255]" );
		}
	}

	// ------------------------------------------------------------------------

	public AbstractInstruction_Rx4_K8( InstructionSet entry, AvrUpperRegisters rx, int k )
	{
		super( entry, rx );

		this.k = k;
		check();
	}

	// ========================================================================
	// ===
	// ========================================================================

	public int getK()
	{
		return k;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		parsedLine.setOperand2( String.format( "0x%02X", (getK() & getDataMask() ) ) );
	}
}
