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

import be.lmenten.avr.core.descriptor.CoreRegisterDescriptor;

/**
 * 
 * @author Laurent Menten
 * @since 1.0
 */
public class CoreRegister
	extends CoreData
{
	private final CoreRegisterDescriptor desc;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public CoreRegister( CoreRegisterDescriptor desc )
	{
		this( (byte) 0, desc );
	}

	public CoreRegister( byte value, CoreRegisterDescriptor desc )
	{
		super( value, desc.getDefaultValue() );

		this.desc = desc;
	}
	
	// ========================================================================
	// === ACCESSOR(s) ========================================================
	// ========================================================================

	public CoreRegisterDescriptor getRegisterDescriptor()
	{
		return desc;
	}

	// ------------------------------------------------------------------------

	public String getName()
	{
		return desc.getName();
	}

	public String getBitName( int bit )
	{
		return desc.getBitName( bit );
	}

	public int getBitByName( String name )
	{
		return desc.getBitByName( name );
	}

	// ========================================================================
	// === ACTION(s) ==========================================================
	// ========================================================================

	public byte mask( String ... names )
	{
		if( names == null )
		{
			throw new NullPointerException();
		}

		byte mask = 0b0000_0000;
		for( String name : names )
		{
			if( name == null )
			{
				throw new NullPointerException();
			}

			int bit = getBitByName( name );
			if( bit < 0 )
			{
				throw new IllegalArgumentException( "Bit " + desc.getName() + "." + name + " not found !!!" );
			}

			mask |= (1 << bit);			
		}

		return mask( mask );
	}

	// ------------------------------------------------------------------------

	public boolean bit( String name )
	{
		if( name == null )
		{
			throw new NullPointerException();
		}

		int bit = getBitByName( name );
		if( bit < 0 )
		{
			throw new IllegalArgumentException( "Bit " + desc.getName() + "." + name + " not found !!!" );
		}

		return bit( bit );
	}

	public void bit( String name, boolean state )
	{
		if( name == null )
		{
			throw new NullPointerException();
		}

		int bit = getBitByName( name );
		if( bit < 0 )
		{
			throw new IllegalArgumentException( "Bit " + desc.getName() + "." + name + " not found !!!" );
		}

		bit( bit, state );
	}

	// ========================================================================
	// === Object =============================================================
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( super.toString() )
		 .append( " ")
		 .append( getName() );

		if( desc.hasBitNames() )
		{
			s.append( " { " );
			for( int i = 7 ; i >= 0 ; i-- )
			{
				if( getBitName(i) != null )
				{
					s.append( getBitName(i) );
				}
				else
				{
					s.append( "-" );
				}
				s.append( " " );
			}
			s.append( "}" );
		}

		return s.toString();
	}
}
