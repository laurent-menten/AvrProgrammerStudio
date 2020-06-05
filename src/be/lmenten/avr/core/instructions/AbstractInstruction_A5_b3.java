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
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;

/**
 * Rx (R16..31, 5-bits operand).
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public abstract class AbstractInstruction_A5_b3
	extends Instruction
{
	private final int A;
	private final int b;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AbstractInstruction_A5_b3( InstructionSet entry, int data )
	{
		super( entry, data );

		this.A = entry.getOperand( OperandType.A, data );			
		this.b = entry.getOperand( OperandType.b, data );			
		check();
	}

	private void check()
	{
		if( (A < 0) || (A > 31) )
		{
			throw new RuntimeException( "A value " + A + " out of range [0..31]" );
		}

		if( (b < 0) || (b > 7) )
		{
			throw new RuntimeException( "b value " + b + " out of range [0..7]" );
		}
	}

	// ------------------------------------------------------------------------

	public AbstractInstruction_A5_b3( InstructionSet entry, int A, int b )
	{
		super( entry );

		this.A = A;
		this.b = b;
		check();
	}

	// ========================================================================
	// ===
	// ========================================================================

	public int getA()
	{
		return A;
	}

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
		
		StringBuilder comment = new StringBuilder();
		String address = String.format( "0x%02X", (getA() & getDataMask() ) );
		String bit = String.format( "%d", getB() );

		if( core != null )
		{
			CoreRegister rdesc = core.getIORegister( getA() );
			if( rdesc != null )
			{
				comment.append( "Address = " + address );
				address = rdesc.getName();

				String bitName = rdesc.getBitName( getB() );
				if( bitName != null )
				{
					comment.append( ", bit = " + bit );
					bit = bitName;
				}
			}
			else
			{
				parsedLine.setComment( "*** unknown I/O location !!!" );				
			}
		}

		parsedLine.setOperand1( address );
		parsedLine.setOperand2( bit );
		parsedLine.setComment( comment.toString() );
	}
}
