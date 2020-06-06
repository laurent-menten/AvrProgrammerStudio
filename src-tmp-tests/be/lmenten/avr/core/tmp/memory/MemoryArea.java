package be.lmenten.avr.core.tmp.memory;

import java.util.Map;

public class MemoryArea
{
	private final Map<String,MemoryRange>	map;
	private final byte [] data;

	public MemoryArea()
	{
		this.map = null;
		this.data = new byte [0];
	}

	
}
