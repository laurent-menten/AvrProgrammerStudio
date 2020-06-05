package be.lmenten.avr.core.tmp;

public abstract class CoreEvent
{
	private final Core core;
	private final long clockCounter;
	private final int programCounter;
	private final int physicalAddress;

	// ========================================================================
	// ===
	// ========================================================================

	public CoreEvent( Core core, int physicalAddress )
	{
		this.core = core;

		this.clockCounter = core.getClockCounter();
		this.programCounter = core.getProgramCounter();
		this.physicalAddress = physicalAddress;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public Core getCore()
	{
		return core;
	}

	public long getClockCounter()
	{
		return clockCounter;
	}

	public int getProgramCounter()
	{
		return programCounter;
	}

	public int getPhysicalAddress()
	{
		return physicalAddress;
	}
}
