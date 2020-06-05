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
import be.lmenten.avr.core.CoreRegister;

/**
 * Rx (R16..31, 5-bits operand).
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public abstract class AbstractInstruction_Rx_A6
	extends AbstractInstruction_Rx
{
	private final int A;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AbstractInstruction_Rx_A6( InstructionSet entry, int data )
	{
		super( entry, data );

		this.A = entry.getOperand( OperandType.A, data );			
		check();
	}

	private void check()
	{
		if( (A < 0) || (A > 63) )
		{
			throw new RuntimeException( "A value " + A + " out of range [0..63]" );
		}
	}

	// ------------------------------------------------------------------------

	public AbstractInstruction_Rx_A6( InstructionSet entry, AvrRegisters rx, int A )
	{
		super( entry, rx );

		this.A = A;
		check();
	}

	// ========================================================================
	// ===
	// ========================================================================

	public int getA()
	{
		return A;
	}
	
	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		super.toParsedLine( core, parsedLine );
		
		String address = String.format( "0x%02X", (getA() & getDataMask() ) );
		if( core != null )
		{
			CoreRegister rdesc = core.getIORegister( getA() );
			if( rdesc != null )
			{
				parsedLine.setComment( " addr=" + address
					+ ",  " + rdesc.getRegisterDescriptor().getDescription() );

				address = rdesc.getName();
			}
			else
			{
				parsedLine.setComment( "*** unknown I/O location !!!" );				
			}
		}
		
		if( getInstructionSetEntry().isStore() )
		{
			parsedLine.setOperand1( address );
			parsedLine.setOperand2( getRx().toString() );
		}
		else
		{
			parsedLine.setOperand1( getRx().toString() );			
			parsedLine.setOperand2( address );			
		}
	}
}
