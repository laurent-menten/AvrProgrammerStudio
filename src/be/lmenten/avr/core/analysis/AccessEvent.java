package be.lmenten.avr.core.analysis;

import be.lmenten.avr.core.CoreMemoryCell;

public class AccessEvent
{
	public static final int ACCESS_READ = 1;
	public static final int ACCESS_WRITE = 2;
	
	// ------------------------------------------------------------------------

	private final CoreMemoryCell cell;
	private int accessMode;
	private int newData;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AccessEvent( CoreMemoryCell cell )
	{
		this.cell = cell;
	}

	// ========================================================================
	// === GETTER(s) / SETTER(s) ==============================================
	// ========================================================================

	public CoreMemoryCell getCell() 
	{
		return cell;
	}

	// ------------------------------------------------------------------------

	public void setAccessMode( int accessMode )
	{
		this.accessMode = accessMode;
	}

	public int getAccessMode()
	{
		return accessMode;
	}

	// ------------------------------------------------------------------------

	public void setNewData( int newData )
	{
		this.newData = newData;
	}

	public int getNewData()
	{
		return newData;
	}
}
