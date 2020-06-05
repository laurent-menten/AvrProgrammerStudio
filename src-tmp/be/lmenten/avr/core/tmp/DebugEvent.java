package be.lmenten.avr.core.tmp;

public class DebugEvent
	extends CoreEvent
{

	public DebugEvent( Core core, int physicalAddress )
	{
		super( core, physicalAddress );
	}
}
