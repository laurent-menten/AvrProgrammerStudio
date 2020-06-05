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

import java.util.function.Function;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreMemoryCell;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public abstract class Instruction
	extends CoreMemoryCell
{
	private final InstructionSet entry;
	private final InstructionData secondWord;

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * <P>
	 * Construct an Instruction with no known opcode.
	 * 
	 * <P>
	 * This is usually used when constructing an instruction instance
	 * programmatically.
	 * 
	 * @param entry
	 */
	public Instruction( InstructionSet entry )
	{
		this.entry = entry;

		privSetData( entry.getOpcode() );

		secondWord = (entry.getOpcodeWidth()) == 32 ? new InstructionData( this ) : null;
	}

	// ------------------------------------------------------------------------

	/**
	 * <P>
	 * Construct an Instruction with a known opcode.
	 * 
	 * <P>
	 * This is usually used when constructing an instruction instance
	 * from a binary image.
	 * 
	 * @param entry
	 * @param opcode
	 */
	protected Instruction( InstructionSet entry, int opcode )
	{
		this.entry = entry;

		privSetData( opcode );

		secondWord = (entry.getOpcodeWidth()) == 32 ? new InstructionData( this ) : null;
	}

	/**
	 * <P>
	 * Construct an Instruction with a known opcode.
	 * 
	 * <P>
	 * This is usually used when constructing an instruction instance
	 * from a binary image but the {@link InstructionSet} entry is not yet
	 * exactly determined.
	 * 
	 * @param f
	 * @param opcode
	 */
	protected Instruction( Function<Integer,InstructionSet> f, int opcode )
	{
		this( f.apply( opcode ), opcode );
	}

	@Override
	public int getDataWidth()
	{
		return 16;
	}
		
	// ========================================================================
	// ===
	// ========================================================================

	public InstructionSet getInstructionSetEntry()
	{
		return entry;
	}

	public int getOpcodeSize()
	{
		return getInstructionSetEntry().getOpcodeSize();
	}

	public int getOpcodeWidth()
	{
		return getInstructionSetEntry().getOpcodeWidth();
	}

	// ------------------------------------------------------------------------

	protected void setOpcode( int opcode )
	{
		privSetData( opcode );
	}

	public int getOpcode()
	{
		return privGetData();
	}

	// ------------------------------------------------------------------------

	public byte getHighByteValue()
	{
		return (byte) ((getOpcode() >> 8) & 0xFF);
	}
	
	public byte getLowByteValue()
	{
		return (byte) (getOpcode() & 0xFF);
	}

	// ------------------------------------------------------------------------

	public InstructionData getSecondWord()
	{
		if( entry.getOpcodeWidth() == 32 )
		{
			return secondWord;
		}

		throw new UnsupportedOperationException();
	}

	// ========================================================================
	// ===
	// ========================================================================

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 * @param core
	 */
	public void execute( Core core )
	{
		throw new UnsupportedOperationException();
	}

	// ------------------------------------------------------------------------

	/**
	 * Assemble this instruction.
	 * 
	 * @return
	 */
	public int assemble()
	{
		return assemble( null, 0x0000 );
	}

	/**
	 * Assemble this instruction and write its binary form to the given byte
	 * array.
	 * 
	 * @param data
	 * @param address
	 * @return
	 */
	public int assemble( byte [] data, int address )
	{
		throw new UnsupportedOperationException();
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param data
	 */
	public void disassemble( int data )
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param data
	 * @param address
	 */
	public void disassemble( byte [] data, int address )
	{
		throw new UnsupportedOperationException();
	}

	// ========================================================================
	// === DEBUGGER & DISASSEMLER SUPPORT =====================================
	// ========================================================================

	/**
	 * 
	 * @param core
	 * @param parsedLine
	 * @return
	 */
	public void toParsedLine( Core core, ParsedAssemblerLine parsedLine )
	{
		String fmt = (getOpcodeSize() == 1) ? "%04x" : "%08x";
		parsedLine.setOpcode( String.format( fmt, getOpcode() ) );

		parsedLine.setMnemonic( getInstructionSetEntry().getMnemonic() );
	}

	// ========================================================================
	// === Object =============================================================
	// ========================================================================

	@Override
	public String toString()
	{
		ParsedAssemblerLine parsedLine = new ParsedAssemblerLine();
		toParsedLine( parsedLine );

		return parsedLine.toString();
	}
}

