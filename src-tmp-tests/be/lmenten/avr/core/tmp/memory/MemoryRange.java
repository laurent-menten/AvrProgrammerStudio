package be.lmenten.avr.core.tmp.memory;

public class MemoryRange
{
	private final int address;
	private final int size;

	// ========================================================================
	// ===
	// ========================================================================

	public MemoryRange( int address, int size )
	{
		this.address = address;
		this.size = size;
	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 * @return
	 */
	public int getAddress()
	{
		return address;
	}

	/**
	 * 
	 * @return
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * 
	 * @return
	 */
	public int getLimit()
	{
		return address + size - 1;
	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 * @param address
	 * @return
	 */
	public boolean contains( int address )
	{
		return (address >= getAddress()) && (address <= getLimit());
	}

	/**
	 * 
	 * @param address
	 * @return
	 */
	public int offset( int address )
	{
		return address - getAddress();
	}
}
