package be.lmenten.avr.core.descriptor;

import java.io.InputStream;
import java.util.ResourceBundle;

public enum SupportedCore
{
	ATMEGA2560( "ATmega2560" ),
	ATMEGA328( "ATmega328" ),
	ATMEGA328P( "ATmega328P" ),
	;

	// ------------------------------------------------------------------------

	private final String id;
	private CoreDescriptor cdesc;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	private SupportedCore( String id )
	{
		this.id = id;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public CoreDescriptor getDescriptor()
	{
		if( cdesc == null )
		{
			cdesc = new CoreDescriptor( this );
		}

		return cdesc;
	}

	public InputStream getDescriptorDataAsStream()
	{
		return  getClass().getResourceAsStream( "data/" + id + ".json" );
	}

	public ResourceBundle getDescriptorResourceBundle()
	{
		return ResourceBundle.getBundle(
			CoreDescriptor.class.getPackageName() + ".data." + id );
	}
}
