package be.lmenten.avr.core.tmp;

public class IORegisterEvent
	extends CoreEvent
{
	private byte oldValue;
	private byte newValue;

	// ========================================================================
	// ===
	// ========================================================================

	public IORegisterEvent( Core core, int registerAddress )
	{
		super( core, registerAddress );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public void setOldValue( byte oldValue )
	{
		this.oldValue = oldValue;
	}

	public byte getOldValue()
	{
		return oldValue;
	}

	// ------------------------------------------------------------------------

	public void setNewValue( byte newValue )
	{
		this.newValue = newValue;
	}

	public byte getNewValue()
	{
		return newValue;
	}
}
