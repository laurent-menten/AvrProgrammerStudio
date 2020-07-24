package be.lmenten.avr.project;

//
// Projet
//		- Flash
//			- Application
//				- Reset
//				- interrupts
//				- * sections
//			- BLS
//				- Reset
//				- interrupts
//				- * sections
//		- Sram
//				- * sections
//		- Eeprom
//				- * sections
//
public class AvrProject
{
	// ------------------------------------------------------------------------

	private String authorName;
	private String authorEmail;

	// ------------------------------------------------------------------------
	
	public final String path;
	public final String name;

	// =========================================================================
	// === CONSTRUCTOR(s) ======================================================
	// =========================================================================

	public AvrProject( String path, String name )
	{
		this.path = path;
		this.name = name;
	}

	// -------------------------------------------------------------------------

	public String getAuthorName()
	{
		return authorName;
	}

	public String getAuthorEmail()
	{
		return authorEmail;
	}

	// -------------------------------------------------------------------------

	public AvrSource getSource()
	{
		return new AvrInternalSource( null, null );
	}
}
