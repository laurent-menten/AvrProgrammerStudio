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
public class LDD
	extends AbstractInstruction_Rx_q6
{
	private final AvrRegistersXYZ rr;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public LDD( InstructionSet entry, int data )
	{
		super( entry, data );

		switch( entry )
		{
			case LD_Y:
				this.rr = AvrRegistersXYZ.Y;
				break;

			case LDD_YQ:
				this.rr = AvrRegistersXYZ.Y;
				break;

			case LD_Z:
				this.rr = AvrRegistersXYZ.Z;
				break;

			case LDD_ZQ:
				this.rr = AvrRegistersXYZ.Z;
				break;

			default:
				throw new RuntimeException();
		}
	}

	// ------------------------------------------------------------------------

	public LDD( AvrRegisters rd, AvrRegistersYZ rr, int q )
	{
		super( analyse( rr ), rd, q );

		this.rr = AvrRegistersXYZ.lookupByOperandCode( rr.getIndex() );
	}

	// ------------------------------------------------------------------------

	private static final InstructionSet analyse( AvrRegistersYZ rr  )
	{
		switch( rr )
		{
			case Y: 
				return InstructionSet.LDD_YQ;

			case Z: 
				return InstructionSet.LDD_ZQ;
		}

		throw new RuntimeException();
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rd = core.getGeneralRegister( getRx() );

		int address = core.getIndexRegisterValue( this.rr, true ) + getQ();

		CoreData cell = core.getSramCell( address );

		// --------------------------------------------------------------------

		rd.setData( cell.getData() );

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
	
		parsedLine.setOperand1( getRx().toString() );

		switch( getInstructionSetEntry() )
		{
			case LD_Y:
				parsedLine.setOperand2( "Y" );
				break;

			case LDD_YQ:
				parsedLine.setOperand2( String.format( "Y+%d", getQ() ) );
				break;

			case LD_Z:
				parsedLine.setOperand2( "Z" );
				break;

			case LDD_ZQ:
				parsedLine.setOperand2( String.format( "Z+%d", getQ() ) );
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
