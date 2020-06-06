package be.lmenten.avr.core.tmp;

import java.util.Properties;

public abstract class AbstractCoreComponent
	implements CoreComponent
{
	protected final Core core;
	
	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public AbstractCoreComponent( Core core, Properties config )
	{
		this.core = core;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void tick()
	{
	}
}
