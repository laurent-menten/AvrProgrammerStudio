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

package be.lmenten.avr.assembler;

/**
 * This interface describes an AVR core register from an assembler point of
 * view. It is primarily used to ensure strong typing of instructions instances
 * construction.
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public interface AvrRegister
{
	/**
	 * Get the index of this register in the General Registers File.
	 * 
	 * @return the index
	 */
	public int getIndex();

	/**
	 * Get the operand code of this register as it is found in an opcode.
	 * 
	 * @return the operand code
	 */
	public int getOperandCode();
}
