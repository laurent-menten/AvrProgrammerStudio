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

package be.lmenten.avr.core.instructions.system;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.RunningMode;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.core.CoreRegister;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class SLEEP
	extends Instruction
{
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public SLEEP( InstructionSet entry, int data )
	{
		super( InstructionSet.SLEEP, data );
	}

	// ------------------------------------------------------------------------

	public SLEEP()
	{
		super( InstructionSet.SLEEP, 0x0000 );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void execute( Core core )
	{
		CoreRegister SMCR = core.getRegisterByName( "SMCR" );
		if( SMCR.bit( "SE" ) )
		{
			int sleepMode = SMCR.mask( "SM2", "SM1", "SM0" ) >> 1;
			switch( sleepMode )
			{
				case	0b000:
					core.setCoreMode( RunningMode.IDLE );
					break;
					
				case	0b001:
					core.setCoreMode( RunningMode.ADC_NOISE_REDUCTION );
					break;
					
				case	0b010:
					core.setCoreMode( RunningMode.POWER_DOWN );
					break;
					
				case	0b011:
					core.setCoreMode( RunningMode.POWDER_SAVE );
					break;

				case	0b100:
					// RESERVED
					break;

				case	0b101:
					// RESERVED
					break;

				case	0b110:
					core.setCoreMode( RunningMode.STANDBY );
					break;
					
				case	0b111:
					core.setCoreMode( RunningMode.EXTENDED_STANDBY );
					break;		
			}
		}

		core.updateProgramCounter( getOpcodeSize() );
		core.updateClockCyclesCounter( 1l );
	}
}
