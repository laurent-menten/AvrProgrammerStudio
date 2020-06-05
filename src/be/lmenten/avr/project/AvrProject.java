package be.lmenten.avr.project;

public class AvrProject
{
	// ------------------------------------------------------------------------

	private String authorName;
	private String authorEmail;

	// ------------------------------------------------------------------------
	
	public final String path;
	public final String name;

	public AvrProject( String path, String name )
	{
		this.path = path;
		this.name = name;
	}

	public AvrSource getSource()
	{
		return new AvrInternalSource( null, null );
	}
}
