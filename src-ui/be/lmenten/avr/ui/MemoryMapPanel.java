package be.lmenten.avr.ui;

import java.util.List;

import be.lmenten.avr.core.descriptor.CoreMemoryRange;

public class MemoryMapPanel
{
	@SuppressWarnings("unused")
	private final List<CoreMemoryRange> map;

	// ========================================================================
	// ===
	// ========================================================================

	public MemoryMapPanel( List<CoreMemoryRange> map )
	{
		this.map = map;
	}
}
