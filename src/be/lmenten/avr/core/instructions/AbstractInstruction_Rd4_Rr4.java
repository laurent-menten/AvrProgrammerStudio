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
 * Rd, Rr (Upper registers: R16..31, 4-bits operand).
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class AbstractInstruction_Rd4_Rr4
	extends Instruction
{
	private final AvrUpperRegisters rd;
	private final AvrUpperRegisters rr;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AbstractInstruction_Rd4_Rr4( InstructionSet entry, int data )
	{
		super( entry, data );

		int rdOperanCode = entry.getOperand( OperandType.d, data );
		this.rd = AvrUpperRegisters.lookupByOperandCode( rdOperanCode );

		int rrOperandCode = entry.getOperand( OperandType.r, data );
		this.rr = AvrUpperRegisters.lookupByOperandCode( rrOperandCode );
	}

	// ------------------------------------------------------------------------

	public AbstractInstruction_Rd4_Rr4( InstructionSet entry, AvrUpperRegisters rd, AvrUpperRegisters rr )
	{
		super( entry );

		this.rd = rd;
		this.rr = rr;

		// --------------------------------------------------------------------

		int opcode = entry.getOpcode();

		opcode = entry.setOperand( opcode, OperandType.d, getRd().getOperandCode() );
		opcode = entry.setOperand( opcode, OperandType.r, getRr().getOperandCode() );

		setOpcode( opcode );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public AvrUpperRegisters getRd()
	{
		return rd;
	}

	public AvrUpperRegisters getRr()
	{
		return rr;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		parsedLine.setOperand1( getRd().toString() );
		parsedLine.setOperand2( getRr().toString() );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( super.toString() )
		 .append( " " )

		 .append( getRd() )
		 .append( ", " )
		 .append( getRr() )
		 ;
		 
		return s.toString();
	}
}
