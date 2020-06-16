package be.lmenten.avr.core.descriptor;

import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import be.lmenten.utils.StringUtils;

/**
 * 
 * @author lmenten
 */
public class CoreRegisterDescriptor
{
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_NAME = "name";
	private static final String KEY_DEFAULT = "default";
	private static final String KEY_BITS = "bits";
	private static final String KEY_MASK = "mask";

	// ------------------------------------------------------------------------

	private final boolean hasAddress;
	private final int address;

	private final boolean hasName;
	private final String name;
	private final String description;
	
	private final boolean hasDefaultValue;
	private final byte defaultValue;
	private final byte mask;

	private final boolean hasBitNames;
	private final String [] bitNames
		= new String [8];
	private final String [] bitDescriptions
	= new String [8];


	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	/*package*/ CoreRegisterDescriptor( int address, String name )
	{
		this.hasAddress = true;
		this.address = address;

		this.hasName = true;		
		this.name = name;
		this.description = null;

		this.defaultValue = (byte) 0;
		this.hasDefaultValue = false;
		this.mask = (byte) 0b1111_1111;

		this.hasBitNames = false;
	}

	public CoreRegisterDescriptor( JSONObject o )
	{
		this( o, null );
	}

	public CoreRegisterDescriptor( JSONObject o, ResourceBundle res )
	{
		String tmp;

		tmp = (String) o.get( KEY_ADDRESS );
		if( tmp == null )
		{
			address = 0x0000;
			hasAddress = false;
		}
		else
		{
			address = (int) StringUtils.parseNumber( tmp );
			hasAddress = true;
		}

		name = (String) o.get( KEY_NAME );
		description = (res != null) ? res.getString( name ) : null;
		hasName = (name != null);

		tmp = (String) o.get( KEY_DEFAULT );
		if( tmp == null )
		{
			defaultValue = 0b0000_0000;
			hasDefaultValue = false;
		}
		else
		{
			defaultValue = (byte) StringUtils.parseNumber( tmp );
			hasDefaultValue = true;
		}

		boolean hasBitNames = false;
		JSONArray bits = (JSONArray) o.get( KEY_BITS );
		if( bits != null)
		{
			for( int i = 0 ; i < 8 ; i++ )
			{
				if( bits.get( i ) != null )
				{
					hasBitNames = true;
				}

				bitNames[i] = (String) bits.get( i );
				if( bitNames[i] != null )
				{
					String resEntryName = name + "." + bitNames[i];
					bitDescriptions[i] = (res != null) ? res.getString( resEntryName ) : null;
				}
			}
		}
		this.hasBitNames = hasBitNames;

		tmp = (String) o.get( KEY_MASK );
		if( tmp == null )
		{
			mask = (byte) 0b1111_1111;
		}
		else
		{
			mask = (byte) StringUtils.parseNumber( tmp );
		}
	}

	// ========================================================================
	// ===
	// ========================================================================

	public boolean hasAddress()
	{
		return hasAddress;
	}
	
	public int getAddress()
	{
		return address;
	}

	// ------------------------------------------------------------------------

	public boolean hasName()
	{
		return hasName;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	// ------------------------------------------------------------------------

	public boolean hasDefaultValue()
	{
		return hasDefaultValue;
	}

	public byte getDefaultValue()
	{
		return defaultValue;
	}

	public byte getMask()
	{
		return mask;
	}

	// ------------------------------------------------------------------------

	public boolean hasBitNames()
	{
		return hasBitNames;
	}

	public String getBitName( int bit )
	{
		if( (bit < 0) || (bit > 7) )
		{
			throw new IllegalArgumentException();
		}

		return bitNames[ 7 - bit ];
	}

	public String getBitDescription( int bit )
	{
		if( (bit < 0) || (bit > 7) )
		{
			throw new IllegalArgumentException();
		}

		return bitDescriptions[ bit ];
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getBitByName( String name )
	{
		for( int i = 0 ; i < 8 ; i++ )
		{
			if( (bitNames[i] != null) && bitNames[i].equalsIgnoreCase( name ) )
			{
				return 7 - i;
			}
		}

		return -1;
	}

	public boolean getDefaultBitValue( int bit )
	{
		if( (bit < 0) || (bit > 7) )
		{
			throw new IllegalArgumentException();
		}

		return (defaultValue & (1 << bit)) == (1 << bit);
	}

	public boolean isBitUsed( int bit )
	{
		if( (bit < 0) || (bit > 7) )
		{
			throw new IllegalArgumentException();
		}

		return (mask & (1 << bit)) == (1 << bit);
	}

	// ========================================================================
	// === Object =============================================================
	// ========================================================================

}
