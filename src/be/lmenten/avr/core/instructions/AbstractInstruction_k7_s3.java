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

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public abstract class AbstractInstruction_k7_s3
	extends Instruction
{
	private final int k;
	private final int s;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AbstractInstruction_k7_s3( InstructionSet entry, int data )
	{
		super( entry, data );

		int tmpK = entry.getOperand( OperandType.k, data );
		if( (tmpK & 0b0100_0000) == 0b0100_0000 )
		{
			tmpK |= ~0b0111_1111;
		}
		this.k = tmpK;

		this.s = entry.getOperand( OperandType.s, data );			
		check();
	}

	private void check()
	{
		if( (k < -64) || (k > 63) )
		{
			throw new RuntimeException( "k value " + k + " out of range [-64..63]" );
		}

		if( (s < 0) || (s > 7) )
		{
			throw new RuntimeException( "s value " + s + " out of range [0..7]" );
		}
	}

	// ------------------------------------------------------------------------

	public AbstractInstruction_k7_s3( InstructionSet entry, int k, int s )
	{
		super( entry );

		this.k = k;
		this.s = s;
		check();
	}

	// ========================================================================
	// ===
	// ========================================================================

	public int getK()
	{
		return k;
	}

	public int getS()
	{
		return s;
	}
	
	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );

		int address = getK();
		if( parsedLine.hasAddress() )
		{
			address += 1 + parsedLine.getAddress();
		}


		if( (getInstructionSetEntry() == InstructionSet.BRBC)
				|| (getInstructionSetEntry() == InstructionSet.BRBS) )
		{
			if( core != null )
			{
				parsedLine.setOperand1( core.SREG.getBitName( getS() ) );
				parsedLine.setOperand2( String.format( "0x%06X", address*2 ) );
				parsedLine.setComment( "bit = " + getS() );
			}
			else
			{
				parsedLine.setOperand1( String.format( "%d", getS() ) );
				parsedLine.setOperand2( String.format( "0x%06X", address*2 ) );
			}
		}
		else
		{
			parsedLine.setOperand1( String.format( "0x%06X", address*2 ) );			
		}
	}
}
