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
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreData;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.instructions.AbstractInstruction_Rx;
import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class ST
	extends AbstractInstruction_Rx
{
	private AvrRegistersXYZ rd;
	private boolean preDec;
	private boolean postInc;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public ST( InstructionSet entry, int data )
	{
		super( entry, data );

		switch( entry )
		{
			case ST_X:
			case ST_DX:
			case ST_XP:
			{
				this.rd = AvrRegistersXYZ.X;
				this.preDec = (entry == InstructionSet.LD_DX) ? true : false;
				this.postInc = (entry == InstructionSet.LD_XP) ? true : false;
				break;
			}

			case ST_DY:
			case ST_YP:
			{
				this.rd = AvrRegistersXYZ.Y;
				this.preDec = (entry == InstructionSet.LD_DY) ? true : false;
				this.postInc = (entry == InstructionSet.LD_YP) ? true : false;
				break;
			}

			case ST_DZ:
			case ST_ZP:
			{
				this.rd = AvrRegistersXYZ.Z;
				this.preDec = (entry == InstructionSet.LD_DZ) ? true : false;
				this.postInc = (entry == InstructionSet.LD_ZP) ? true : false;
				break;
			}

			default:
				throw new RuntimeException(
					"INTERNAL ERROR: "
					+ "invalid InstructionSet entry '" + entry + "' "
					+ "for LD instruction handler."
				);
		}
	}

	// ------------------------------------------------------------------------

	public ST( AvrRegistersXYZ rd, AvrRegisters rr )
	{
		super( analyse( rd ), rr );

		this.rd = rd;
		this.preDec = false;
		this.postInc = false;
	}

	public ST( boolean preDec, AvrRegistersXYZ rd, AvrRegisters rr )
	{
		super( analyse( preDec, rd ), rr );

		this.rd = rd;
		this.preDec = preDec;
		this.postInc = false;
	}

	public ST( AvrRegistersXYZ rd, boolean postInc, AvrRegisters rr )
	{
		super( analyse( rd, postInc ), rr );

		this.rd = rd;
		this.preDec = false;
		this.postInc = postInc;
	}

	// ------------------------------------------------------------------------

	private static InstructionSet analyse( AvrRegistersXYZ rd )
	{
		switch( rd )
		{
			case X:
				return InstructionSet.ST_X;

			case Y:
				return InstructionSet.ST_Y;

			case Z:
				return InstructionSet.ST_Z;
		}

		return null;	// never reached !!!
	}

	private static InstructionSet analyse( boolean preDec, AvrRegistersXYZ rd )
	{
		if( preDec )
		{
			switch( rd )
			{
				case X:
					return InstructionSet.ST_DX;

				case Y:
					return InstructionSet.ST_DY;

				case Z:
					return InstructionSet.ST_DZ;
			}

			return null;	// never reached !!!
		}

		return analyse( rd );
	}

	private static InstructionSet analyse( AvrRegistersXYZ rd, boolean postInc )
	{
		if( postInc )
		{
			switch( rd )
			{
				case X:
					return InstructionSet.ST_XP;

				case Y:
					return InstructionSet.ST_YP;

				case Z:
					return InstructionSet.ST_ZP;
			}

			return null;	// never reached !!!
		}

		return analyse( rd );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rr = core.getGeneralRegister( getRx() );

		int address = core.getIndexRegisterValue( this.rd, true );
		CoreData cell = core.getSramCell( address );

		// --------------------------------------------------------------------

		if( preDec )
		{
			--address;
		}

		cell.setData( rr.getData() );

		if( postInc )
		{
			address++;
		}

		// --------------------------------------------------------------------

		core.setIndexRegisterValue( this.rd, address, true );

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 2l );
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
			case ST_X:
				parsedLine.setOperand1( "X" );
				break;

			case ST_DX:
				parsedLine.setOperand1( "-X" );
				break;

			case ST_XP:
				parsedLine.setOperand1( "X+" );
				break;

			case ST_DY:
				parsedLine.setOperand1( "-Y" );
				break;

			case ST_YP:
				parsedLine.setOperand1( "Y+" );
				break;

			case ST_DZ:
				parsedLine.setOperand1( "-Z" );
				break;

			case ST_ZP:
				parsedLine.setOperand1( "Z+" );
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
