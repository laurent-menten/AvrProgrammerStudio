package be.lmenten.avr.ui.registers2;

import java.util.Collections;
import java.util.List;

public abstract class AbstractRegisterModel
	implements RegisterModel
{
	private String name;
	private int address;
	private String description;

	private List<RegisterBitModel> bits
		= Collections.emptyList();

	// ========================================================================
	// ===
	// ========================================================================

	public AbstractRegisterModel( String name )
	{
		this( -1, name );
	}

	public AbstractRegisterModel( int address )
	{
		this( address, null );
	}

	public AbstractRegisterModel( int address, String name )
	{
		this.name = name;
		this.address = address;
	}

	// ========================================================================
	// ===
	// ========================================================================

	protected void setName( String name )
	{
		this.name = name;
	}

	@Override
	public String getRegisterName()
	{
		return name;
	}

	protected void setAddress( int address )
	{
		this.address = address;
	}

	@Override
	public int getRegisterAddress()
	{
		return address;
	}

	protected void setDescription( String description )
	{
		this.description = description;
	}

	@Override
	public String getRegisterDescription()
	{
		return description;
	}

	// ========================================================================
	// ===
	// ========================================================================

	protected void addRegisterBit( RegisterBitModel bit )
	{
		bits.add( bit );
	}

	@Override
	public List<RegisterBitModel> getBitsList()
	{
		return bits;
	}
}
