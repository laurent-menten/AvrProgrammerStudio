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

import be.lmenten.avr.assembler.AvrRegister;
import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;

/**
 * Rx (R0..31, 5-bits operand).
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class AbstractInstruction_Rx
	extends Instruction
{
	private final AvrRegisters rx;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AbstractInstruction_Rx( InstructionSet entry, int data )
	{
		super( entry, data );

		int rxOperanCode = 0;
		if( entry.hasOperand( OperandType.r ) )
		{
			rxOperanCode = entry.getOperand( OperandType.r, data );
		}
		else if( entry.hasOperand( OperandType.d ) )
		{
			rxOperanCode = entry.getOperand( OperandType.d, data );
		}
		else
		{
			throw new RuntimeException( "RUNTIME ERROR: "
					+ entry.getMnemonic()
					+ " has no register operand." );			
		}

		this.rx = AvrRegisters.lookupByOperandCode( rxOperanCode );
	}

	// ------------------------------------------------------------------------

	public AbstractInstruction_Rx( InstructionSet entry, AvrRegisters rx )
	{
		super( entry );

		this.rx = rx;

		// --------------------------------------------------------------------

		int opcode = entry.getOpcode();

		if( entry.hasOperand( OperandType.r ) )
		{
			opcode = entry.setOperand( opcode, OperandType.r, getRx().getOperandCode() );
		}
		else if( entry.hasOperand( OperandType.d ) )
		{
			opcode = entry.setOperand( opcode, OperandType.d, getRx().getOperandCode() );
		}

		setOpcode( opcode );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public AvrRegister getRx()
	{
		return rx;
	}
	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		parsedLine.setOperand1( getRx().toString() );
	}
}
