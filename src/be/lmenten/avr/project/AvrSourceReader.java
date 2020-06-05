// = ======================================================================== =
// = === AVR Programmer Studio ======= Copyright (c) 2020+ Laurent Menten === =
// = ======================================================================== =
// = = This program is free software: you can redistribute it and/or modify = =
// = = it under the terms of the GNU General Public License as published by = =
// = = the Free Software Foundation, either version 3 of the License, or    = =
// = = (at your option) any later version.                                  = =
// = =                                                                      = =
// = = This program is distributed in the hope that it will be useful, but  = =
// = = WITHOUT ANY WARRANTY; without even the implied warranty of           = =
// = = MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU    = =
// = = General Public License for more details.                             = =
// = =                                                                      = =
// = = You should have received a copy of the GNU General Public License    = =
// = = along with this program. If not, see                                 = =
// = = <https://www.gnu.org/licenses/>.                                     = =
// = ======================================================================== =

package be.lmenten.avr.project;

import java.io.IOException;
import java.io.Reader;

/**
 * 
 * @author Laurent Menten
 */
public class AvrSourceReader
	extends Reader
{
	private final AvrSource source;

	private boolean doProglog;
	private int currentLineNumber;
	private String currentLine;
	private int currentLineLength;

	private int next = 0;
	private int mark = 0;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AvrSourceReader( AvrSource source )
	{
		this.source = source;

		doProglog = true;
		currentLine = null;
		currentLineNumber = 0;
		currentLineLength = 0;

		nextLine();
	}

	// ------------------------------------------------------------------------

	private void ensureOpen()
		throws IOException
	{
		if( currentLine == null )
			throw new IOException("Stream closed");
	}

	// ========================================================================
	// ===
	// ========================================================================

	private boolean nextLine()
	{
		if( doProglog )
		{
			return newLineProlog();
		}
		else
		{
			return newLineSource();
		}
	}

	// ------------------------------------------------------------------------

	private boolean newLineProlog()
	{		
		if( currentLineNumber < source.getPrologLineCount() )
		{
			currentLine = source.getPrologLine( currentLineNumber++ );
			currentLineLength = currentLine.length();
			next = 0;
			mark = 0;
		}
		else
		{
			doProglog = false;
			currentLine = null;
			currentLineNumber = 0;

			newLineSource();
		}

		return currentLine != null;
	}

	private boolean newLineSource()
	{	
		if( currentLineNumber < source.getSourceLineCount() )
		{
			currentLine = source.getSourceLine( currentLineNumber++ );
			currentLineLength = currentLine.length();
			next = 0;
			mark = 0;
		}
		else
		{
			currentLine = null;
		}

		return currentLine != null;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public boolean ready()
			throws IOException
		{
			synchronized( lock )
			{
				ensureOpen();

				return true;
			}
		}

	// ========================================================================
	// ===
	// ========================================================================

	public int read()
		throws IOException
	{
		synchronized( lock )
		{
			ensureOpen();

			if( next >= currentLineLength )
			{
				if( ! nextLine() )
				{
					return -1;
				}

				return '\n';
			}

			return currentLine.charAt( next++ );
		}
	}

	public int read( char cbuf[], int off, int len )
		throws IOException
	{
		throw new UnsupportedOperationException( "" );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public long skip( long ns )
    	throws IOException
    {
		throw new UnsupportedOperationException( "" );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public boolean markSupported()
	{
		return false;
	}

	public void mark( int readAheadLimit )
		throws IOException
	{
		if( readAheadLimit < 0 )
		{
			throw new IllegalArgumentException("Read-ahead limit < 0");
		}

		synchronized( lock )
		{
			ensureOpen();

			mark = next;
		}
	}

	public void reset()
		throws IOException
	{
		synchronized( lock )
		{
			ensureOpen();

			next = mark;
		}
	}

	// ========================================================================
	// ===
	// ========================================================================

	public void close()
	{
		currentLine = null;
	}
}
