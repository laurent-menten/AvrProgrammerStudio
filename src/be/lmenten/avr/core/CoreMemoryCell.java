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

package be.lmenten.avr.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.core.analysis.Access;
import be.lmenten.avr.core.analysis.AccessEvent;
import be.lmenten.avr.core.analysis.AccessEventListener;
import be.lmenten.avr.core.analysis.AccessType;
import be.lmenten.avr.core.instructions.Instruction;

/**
 * <p>
 * The root of any data (or instruction) held in one of the memories of an Avr MCU.
 * 
 * <p>
 * Features:
 * <ul>
 * 	<li>cleanness</li>
 * 	<li>access count</li>
 * 	<li>access event</li>
 * 	<li>bit level access</li>
 * </ul>
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public abstract class CoreMemoryCell
{
	private boolean hasAddress;
	private int address;

	private boolean hasInitialData;
	private final int initialData;
	private boolean hasData = false;
	private int data = 0;

	private boolean dirty = false;

	// -------------------------------------------------------------------------

	private final List<Access> accessList
		= new ArrayList<>();
	
	private final List<AccessEventListener> accessEventListeners
		= new ArrayList<>();

	// =========================================================================
	// === CONSTRUCTOR(s) ======================================================
	// =========================================================================

	/**
	 * 
	 * @param address
	 * @param data
	 */
	public CoreMemoryCell( int address, int data )
	{
		this.hasAddress = true;
		this.address = address;

		this.hasInitialData = true;
		this.initialData = data;
		this.hasData = true;
		this.data = data;
	}

	/**
	 * 
	 * @param address
	 */
	public CoreMemoryCell( int address )
	{
		this.hasAddress = true;
		this.address = address;

		this.hasInitialData = false;
		this.initialData = 0;
		this.hasData = false;
		this.data = 0;
	}

	public CoreMemoryCell()
	{
		this.hasAddress = false;
		this.address = 0;

		this.hasInitialData = false;
		this.initialData = 0;
		this.hasData = false;
		this.data = 0;
	}

	// =========================================================================
	// === INTERNALS ===========================================================
	// =========================================================================

	/**
	 * <p>
	 * Reset this memory cell to its default state.
	 * 
	 * <p>
	 * This method does not alter the data and address. This is left to the
	 * implementing class.
	 */
	public void reset()
	{
		dirty = false;

		accessList.clear();
	}

	// -------------------------------------------------------------------------

	/**
	 * Get the size in bits of this memory cell.
	 * 
	 * @return
	 */
	public abstract int getDataWidth();

	/**
	 * 
	 * @return
	 */
	public final int getDataMask()
	{
		return ((1 << getDataWidth()) - 1);
	}

	// -------------------------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	public boolean hasInitialData()
	{
		return hasInitialData;
	}

	/**
	 * 
	 * @return
	 */
	public int getInitialData()
	{
		return initialData;
	}

	// =========================================================================
	// === ACCESSES ============================================================
	// =========================================================================

	/**
	 * 
	 * @param tick
	 * @param instruction
	 * @param accessType
	 */
	public void addAccess( long tick, Instruction instruction, AccessType accessType )
	{
		accessList.add( new Access( tick, instruction, accessType ) );
	}

	/**
	 * 
	 * @return
	 */
	public ListIterator<Access> getAccessesIterator()
	{
		return accessList.listIterator();
	}

	/**
	 * 
	 * @return
	 */
	public int getAccessesCount()
	{
		return accessList.size();
	}

	/**
	 * 
	 * @return
	 */
	public boolean wasAccessed()
	{
		return getAccessesCount() != 0;
	}


	// =========================================================================
	// === EVENTS ==============================================================
	// =========================================================================

	/**
	 * 
	 * @param listener
	 */
	public void addAccessListener( AccessEventListener listener )
	{
		if( ! accessEventListeners.contains( listener ) )
		{
			accessEventListeners.add( listener );
		}
	}

	/**
	 * 
	 * @param listener
	 */
	public void removeAccessListener( AccessEventListener listener )
	{
		if( accessEventListeners.contains( listener ) )
		{
			accessEventListeners.remove( listener );
		}
	}

	/**
	 * 
	 * @param event
	 */
	protected void fireAccessEvent( AccessEvent event )
	{
		for( AccessEventListener listener : accessEventListeners )
		{
			listener.onAccessEvent( event );
		}
	}

	// ========================================================================
	// === GETTER(s) / SETTER(s) = ADDRESS ====================================
	// ========================================================================

	/**
	 * 
	 * @param address
	 */
	public void setAddress( int address )
	{
		this.hasAddress = true;
		this.address = address;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasAddress()
	{
		return hasAddress;
	}

	/**
	 * 
	 * @return
	 */
	public int getAddress()
	{
		return address;
	}

	// ========================================================================
	// === GETTER(s) / SETTER(s) = DATA =======================================
	// ========================================================================

	/**
	 * 
	 * @return
	 */
	public boolean hasData()
	{
		return hasData;
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	public boolean isDirty()
	{
		return dirty;
	}

	/**
	 * 
	 * @param dirty
	 */
	public void setDirty( boolean dirty )
	{
		this.dirty = dirty;
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param data
	 */
	public void setData( int data )
	{
		AccessEvent event = new AccessEvent( this );
		event.setAccessMode( AccessEvent.ACCESS_WRITE );
		event.setNewData( data );
		fireAccessEvent( event );

		privSetData( data );
	}

	/**
	 * 
	 * @return
	 */
	public int getData()
	{
		AccessEvent event = new AccessEvent( this );
		event.setAccessMode( AccessEvent.ACCESS_READ );
		fireAccessEvent( event );

		return privGetData();
	}

	// ------------------------------------------------------------------------

	/**
	 * <P>
	 * Set this cell content.
	 * 
	 * <P>
	 * This version is designed for internal use and does not trigger access
	 * events.
	 * 
	 * @param data
	 */
	public void privSetData( int data )
	{
		this.data = data;
		this.hasData = true;
		this.dirty = true;
	}

	/**
	 * <P>
	 * Get this cell content.
	 * 
	 * <P>
	 * This version is designed for internal use and does not trigger access
	 * events.
	 * 
	 * @return
	 */
	public int privGetData()
	{
		return data;
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param data
	 */
	public void internSetData( int data )
	{
		this.data = data;
	}

	/**
	 * 
	 * @return
	 */
	public int internGetData()
	{
		return this.data;
	}

	// ========================================================================
	// === BITS ===============================================================
	// ========================================================================

	/**
	 * Get the state of a single bit.
	 * 
	 * @param bit
	 * @return
	 */
	public boolean bit( int bit )
	{
		if( (bit < 0) || (bit >= getDataWidth()) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range" );
		}

		return ((getData() & (1<<bit)) == (1<<bit));
	}

	public boolean bit0()	{ return bit( 0 ); }
	public boolean bit1()	{ return bit( 1 ); }
	public boolean bit2()	{ return bit( 2 ); }
	public boolean bit3()	{ return bit( 3 ); }
	public boolean bit4()	{ return bit( 4 ); }
	public boolean bit5()	{ return bit( 5 ); }
	public boolean bit6()	{ return bit( 6 ); }
	public boolean bit7()	{ return bit( 7 ); }

	/**
	 * Set the state of a single bit.
	 * 
	 * @param bit
	 * @param state
	 */
	public void bit( int bit, final boolean state )
	{
		if( (bit < 0) || (bit >= getDataWidth()) )
		{
			throw new IllegalArgumentException( "Bit number " + bit + " out of range" );
		}

		if( state )
		{
			setData( getData() | (1<<bit) );
		}
		else
		{
			setData( getData() & ~(1<<bit) );
		}
	}

	public void bit0( boolean state )	{ bit( 0, state ); }
	public void bit1( boolean state )	{ bit( 1, state ); }
	public void bit2( boolean state )	{ bit( 2, state ); }
	public void bit3( boolean state )	{ bit( 3, state ); }
	public void bit4( boolean state )	{ bit( 4, state ); }
	public void bit5( boolean state )	{ bit( 5, state ); }
	public void bit6( boolean state )	{ bit( 6, state ); }
	public void bit7( boolean state )	{ bit( 7, state ); }

	// ========================================================================
	// === DEBUGGER & DISASSEMLER SUPPORT =====================================
	// ========================================================================

	/**
	 * 
	 * @param parsedLine
	 * @return
	 */
	public final void toParsedLine( ParsedAssemblerLine parsedLine )
	{
		toParsedLine( null, parsedLine );
	}

	/**
	 * 
	 * @param core
	 * @param parsedLine
	 * @return
	 */
	public abstract void toParsedLine( Core core, ParsedAssemblerLine parsedLine );

	// ========================================================================
	// === Object =============================================================
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( String.format( "0x%05X:", address ) );

		return s.toString();
	}
}
