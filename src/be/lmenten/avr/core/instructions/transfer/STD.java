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

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.assembler.def.AvrRegistersXYZ;
import be.lmenten.avr.assembler.def.AvrRegistersYZ;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreData;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx_q6;
import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class STD
	extends AbstractInstruction_Rx_q6
{
	private final AvrRegistersXYZ rd;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public STD( InstructionSet entry, int data )
	{
		super( entry, data );

		switch( entry )
		{
			case ST_Y:
				this.rd = AvrRegistersXYZ.Y;
				break;

			case STD_YQ:
				this.rd = AvrRegistersXYZ.Y;
				break;

			case ST_Z:
				this.rd = AvrRegistersXYZ.Z;
				break;

			case STD_ZQ:
				this.rd = AvrRegistersXYZ.Z;
				break;

			default:
				throw new RuntimeException();
		}
	}

	// ------------------------------------------------------------------------

	public STD( AvrRegistersYZ rd, int q, AvrRegisters rr )
	{
		super( analyse( rd ), rr, q );

		this.rd = AvrRegistersXYZ.lookupByOperandCode( rd.getOperandCode() );
	}

	// ------------------------------------------------------------------------

	private static final InstructionSet analyse( AvrRegistersYZ rd  )
	{
		switch( rd )
		{
			case Y: 
				return InstructionSet.LDD_YQ;

			case Z: 
				return InstructionSet.LDD_ZQ;
		}

		return null;
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rr = core.getGeneralRegister( getRx() );

		int address = core.getIndexRegisterValue( this.rd, true ) + getQ();
		CoreData cell = core.getSramCell( address );

		// --------------------------------------------------------------------

		cell.setData( rr.getData() );

		// --------------------------------------------------------------------

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );
	
		parsedLine.setOperand2( getRx().toString() );

		switch( getInstructionSetEntry() )
		{
			case ST_Y:
				parsedLine.setOperand1( "Y" );
				break;

			case STD_YQ:
				parsedLine.setOperand1( String.format( "Y+%d", getQ() ) );
				break;

			case ST_Z:
				parsedLine.setOperand1( "Z" );
				break;

			case STD_ZQ:
				parsedLine.setOperand1( String.format( "Z+%d", getQ() ) );
				break;

			default:
				throw new RuntimeException(
					"INTERNAL ERROR: "
					+ "invalid InstructionSet entry '" + getInstructionSetEntry() + "' "
					+ "for LD instruction handler."
				);
		}
	}
}
