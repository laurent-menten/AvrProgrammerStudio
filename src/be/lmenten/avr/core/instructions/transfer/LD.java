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
public class LD
	extends AbstractInstruction_Rx
{
	private AvrRegistersXYZ rr;
	private boolean preDec;
	private boolean postInc;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public LD( InstructionSet entry, int data )
	{
		super( entry, data );

		switch( entry )
		{
			case LD_X:
			case LD_DX:
			case LD_XP:
			{
				this.rr = AvrRegistersXYZ.X;
				this.preDec = (entry == InstructionSet.LD_DX) ? true : false;
				this.postInc = (entry == InstructionSet.LD_XP) ? true : false;
				break;
			}

			case LD_DY:
			case LD_YP:
			{
				this.rr = AvrRegistersXYZ.Y;
				this.preDec = (entry == InstructionSet.LD_DY) ? true : false;
				this.postInc = (entry == InstructionSet.LD_YP) ? true : false;
				break;
			}

			case LD_DZ:
			case LD_ZP:
			{
				this.rr = AvrRegistersXYZ.Z;
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

	public LD( AvrRegisters rd, AvrRegistersXYZ rr )
	{
		super( analyse( rr ), rd );

		this.rr = rr;
		this.preDec = false;
		this.postInc = false;
	}

	public LD( AvrRegisters rd, boolean preDec, AvrRegistersXYZ rr )
	{
		super( analyse( preDec, rr ), rd );

		this.rr = rr;
		this.preDec = preDec;
		this.postInc = false;
	}

	public LD( AvrRegisters rd, AvrRegistersXYZ rr, boolean postInc )
	{
		super( analyse( rr, postInc ), rd );

		this.rr = rr;
		this.preDec = false;
		this.postInc = postInc;
	}

	// ------------------------------------------------------------------------

	private static InstructionSet analyse( AvrRegistersXYZ rr )
	{
		switch( rr )
		{
			case X:
				return InstructionSet.LD_X;

			case Y:
				return InstructionSet.LD_Y;

			case Z:
				return InstructionSet.LD_Z;
		}

		return null;	// never reached !!!
	}

	private static InstructionSet analyse( boolean preDec, AvrRegistersXYZ rr )
	{
		if( preDec )
		{
			switch( rr )
			{
				case X:
					return InstructionSet.LD_DX;

				case Y:
					return InstructionSet.LD_DY;

				case Z:
					return InstructionSet.LD_DZ;
			}

			return null;	// never reached !!!
		}

		return analyse( rr );
	}

	private static InstructionSet analyse( AvrRegistersXYZ rr, boolean postInc )
	{
		if( postInc )
		{
			switch( rr )
			{
				case X:
					return InstructionSet.LD_XP;

				case Y:
					return InstructionSet.LD_YP;

				case Z:
					return InstructionSet.LD_ZP;
			}

			return null;	// never reached !!!
		}

		return analyse( rr );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister rd = core.getGeneralRegister( getRx() );

		int address = core.getIndexRegisterValue( this.rr, true );
		CoreData cell = core.getSramCell( address );

		// --------------------------------------------------------------------

		if( preDec )
		{
			--address;
		}

		rd.setData( cell.getData() );

		if( postInc )
		{
			address++;
		}

		// --------------------------------------------------------------------

		core.setIndexRegisterValue( this.rr, address, true );

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
	
		parsedLine.setOperand1( getRx().toString() );

		switch( getInstructionSetEntry() )
		{
			case LD_X:
				parsedLine.setOperand2( "X" );
				break;

			case LD_DX:
				parsedLine.setOperand2( "-X" );
				break;

			case LD_XP:
				parsedLine.setOperand2( "X+" );
				break;

			case LD_DY:
				parsedLine.setOperand2( "-Y" );
				break;

			case LD_YP:
				parsedLine.setOperand2( "Y+" );
				break;

			case LD_DZ:
				parsedLine.setOperand2( "-Z" );
				break;

			case LD_ZP:
				parsedLine.setOperand2( "Z+" );
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
