package be.lmenten.avr.project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import be.lmenten.avr.core.descriptor.CoreDescriptor;

public class AvrExternalSource
	extends AvrSource
{
	private ArrayList<String> content
		= new ArrayList<>();

	// ========================================================================
	// === CONSTRUTOR(s) ======================================================
	// ========================================================================

	public AvrExternalSource( String fileName, CoreDescriptor cdesc )
		throws FileNotFoundException, IOException
	{
		super( fileName, cdesc );

		try( BufferedReader br = new BufferedReader( new FileReader( fileName ) ) )
		{
		    while( br.ready() )
		    {
		        content.add( br.readLine() );
		    }
		}
	}

    // ========================================================================
	// ===
    // ========================================================================

	@Override
	public int getSourceLineCount()
	{
		return content.size();
	}

	@Override
	public String getSourceLine( int line )
	{
		if( line < getSourceLineCount() )
		{
			return content.get( line );
		}

		return null;
	}
}
