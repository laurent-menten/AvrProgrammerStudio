package be.lmenten.avr.core.event;

import be.lmenten.avr.core.Core;

public class CoreEvent
{
	private final CoreEventType type;
	private final Core core;
	private final long clockCyclesCount;
	private final int programCounter;

	private boolean shouldAbort;

	// ========================================================================
	// === CONSTRUTOR(s) ======================================================
	// ========================================================================

	public CoreEvent( CoreEventType type, Core core )
	{
		this.type = type;
		this.core = core;
		this.clockCyclesCount = core.getClockCyclesCounter();
		this.programCounter = core.getProgramCounter();

		this.shouldAbort = false;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public CoreEventType getEventType()
	{
		return type;
	}

	public Core getCore()
	{
		return core;
	}

	public long getClockCyclesCount()
	{
		return clockCyclesCount;
	}

	public int getProgramCounter()
	{
		return programCounter;
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public void abort()
	{
		this.shouldAbort = true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean shouldAbort()
	{
		return shouldAbort;
	}
}
