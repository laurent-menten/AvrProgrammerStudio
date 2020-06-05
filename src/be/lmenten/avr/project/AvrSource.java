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
import java.io.PushbackReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.function.BiConsumer;

import be.lmenten.avr.AvrProgrammerStudio;
import be.lmenten.avr.assembler.ast.ASTDisplay;
import be.lmenten.avr.assembler.ast.AvrSourceAnalyser;
import be.lmenten.avr.assembler.ast.AvrSourceAssembler;
import be.lmenten.avr.assembler.ast.AvrSourceResolver;
import be.lmenten.avr.assembler.parser.AvrSourceLexer;
import be.lmenten.avr.assembler.parser.AvrSourceParser;
import be.lmenten.avr.assembler.parser.ExpressionValue;
import be.lmenten.avr.assembler.parser.analysis.Analysis;
import be.lmenten.avr.assembler.parser.lexer.LexerException;
import be.lmenten.avr.assembler.parser.node.Node;
import be.lmenten.avr.assembler.parser.node.Start;
import be.lmenten.avr.assembler.parser.parser.ParserException;
import be.lmenten.avr.core.descriptor.CoreDescriptor;

public abstract class AvrSource
{
	// ------------------------------------------------------------------------
	// --- Builtins -----------------------------------------------------------
	// ------------------------------------------------------------------------

	public static final String SYM_AVP_VERSION = "__AVP_VERSION__";

	public static final String SYM_AVP_CORE_DESCRIPTOR_VERSION
		= "__AVP_CORE_DESCRIPTOR_VERSION__";

	// -------------------------------------------------------------------------

	public static final String SYM_PART_NAME = "__PART_NAME__";
	public static final String SYM_PARTNAME = "__partname__";
	public static final String SYM_CORE_VERSION = "__CORE_VERSION__";
	public static final String SYM_CORE_COREVERSION = "__CORE_coreversion__";

	public static final String SYM_DATE = "__DATE__";			// mmm dd yyyy
	public static final String SYM_DAY = "__DAY__";			// 1 .. 31
	public static final String SYM_MONTH = "__MONTH__";		// 1 .. 12
	public static final String SYM_YEAR = "__YEAR__";			// 0 .. 99
	public static final String SYM_CENTURY = "__CENTURY__";	// 20

	public static final String SYM_TIME = "__TIME__";			// hh:mm:ss
	public static final String SYM_SECOND = "__SECOND__";		// 0 .. 29
 	public static final String SYM_MINUTE = "__MINUTE__";		// 0 .. 59
	public static final String SYM_HOUR = "__HOUR__";			// 0 .. 23

	// -------------------------------------------------------------------------

	public static final String SYM_FILE = "__FILE__";
	public static final String SYM_LINE = "__LINE__";

	// ------------------------------------------------------------------------
	// ---
	// ------------------------------------------------------------------------

	private final String fileName;
	private final CoreDescriptor cdesc;

    private final HashMap<Node,Object> in;
    private final HashMap<Node,Object> out;

    private final LinkedList<String> symbolsSource;
    private final LinkedHashMap<String,ExpressionValue> symbols;
  
    // ========================================================================
    // === CONSTRUCTOR(s) =====================================================
    // ========================================================================

    public AvrSource( String fileName, CoreDescriptor cdesc )
    {
    	this.cdesc = cdesc;
    	this.fileName = fileName;

    	// --------------------------------------------------------------------

    	in = new HashMap<>();
    	out = new HashMap<>();

    	// --------------------------------------------------------------------

    	symbolsSource = new LinkedList<>();

    	symbolsSource.add( ".prolog" );
    	cdesc.exportSymbols( (name,expr) ->
    	{
    		symbolsSource.add( ".equ " + name + " = " + expr.getValue() + "" );
    	} );
    	symbolsSource.add( ".endprolog" );

    	symbols = new LinkedHashMap<>();
    }

    // ========================================================================
	// === 
	// ========================================================================

	public CoreDescriptor getCoreDescriptor()
    {
    	return cdesc;
    }

	public String getFileName()
	{
		return fileName;
	}

	// ========================================================================
	// === 
	// ========================================================================

	public int getPrologLineCount()
	{
		return symbolsSource.size();
	}

	public String getPrologLine( int line )
	{
		if( line < getPrologLineCount() )
		{
			return symbolsSource.get( line );
		}

		return null;
	}

	// ------------------------------------------------------------------------

	public abstract int getSourceLineCount();

	public abstract String getSourceLine( int line );

    // ========================================================================
    // === AST data ===========================================================
    // ========================================================================

	/**
	 * 
	 * @param node
	 * @param o
	 */
    public void setIn( Node node, Object o )
    {
        if( o != null )
        {
            this.in.put( node, o );
        }
        else
        {
            this.in.remove( node );
        }
    }

    /**
     * 
     * @param node
     * @return
     */
    public Object getIn( Node node )
    {
        return this.in.get( node );
    }

    // ------------------------------------------------------------------------

    /**
     * 
     * @param node
     * @param o
     */
    public void setOut( Node node, Object o )
    {
        if( o != null )
        {
            this.out.put( node, o );
        }
        else
        {
            this.out.remove( node );
        }
    }

    /**
     * 
     * @param node
     * @return
     */
    public Object getOut( Node node )
    {
        return this.out.get( node );
    }

    // ------------------------------------------------------------------------
    
    /**
     * 
     * @param name
     * @param value
     */
	public void setSymbol( String name, ExpressionValue value )
	{
		ExpressionValue oldValue = symbols.get( name );
		if( (oldValue != null) && oldValue.isProtected() )
		{
			if( value != null )
			{
				// FIXME handle this correctly
				System.out.println( "Trying to redefine constant '" + name + "'." );				
			}
			else
			{
				// FIXME handle this correctly
				System.out.println( "Trying to remove constant '" + name + "'." );				
			}

			return;
		}

		if( value != null )
		{
			this.symbols.put( name, value );			
		}
		else
		{
			if( oldValue == null )
			{
				// FIXME handle this correctly
				System.out.println( "Trying to remove unknown constant '" + name + "'." );
				return;
			}

			this.symbols.remove( name );
		}
	}

 	public ExpressionValue getSymbol( String name )
 	{
 System.out.println( "get " + name + " = " + this.symbols.get( name ) );
 		return this.symbols.get( name );
 	}


	// -------------------------------------------------------------------------

    public int getSymbolsCount()
    {
    	return symbols.size();
    }

    public void listSymbols( BiConsumer<String,ExpressionValue> c )
    {
    	symbols.forEach( (k,v) ->
    	{
    		c.accept( k, v );
    	} );
    }

	// ========================================================================
	// === BUILTINS ===========================================================
	// ========================================================================

	private void setBuiltins()
	{
		LocalDateTime now = LocalDateTime.now();

		// --------------------------------------------------------------------
		// - Internals --------------------------------------------------------
		// --------------------------------------------------------------------

		ExpressionValue avpVersion = new ExpressionValue(
			(AvrProgrammerStudio.VERSION.feature() * 1000)
			 + AvrProgrammerStudio.VERSION.interim() );
		avpVersion.setProtected();
		symbols.put( SYM_AVP_VERSION, avpVersion );

		ExpressionValue cdescVersion = new ExpressionValue(
			(cdesc.getFileVersion().feature() *1000)
			+ cdesc.getFileVersion().interim() );
		cdescVersion.setProtected();
		symbols.put( SYM_AVP_CORE_DESCRIPTOR_VERSION, cdescVersion );

		ExpressionValue file = new ExpressionValue( fileName );
		file.setProtected();
		symbols.put( SYM_FILE, file );

		// --------------------------------------------------------------------
		// - Part -------------------------------------------------------------
		// --------------------------------------------------------------------

		String stringPartName = "__" + cdesc.getPartName() + "__";
		ExpressionValue partName = new ExpressionValue( stringPartName );
		partName.setProtected();

		String stringCoreVersion = "__CORE_" + cdesc.getCoreVersion() + "__";
		ExpressionValue coreVersion = new ExpressionValue( stringCoreVersion );
		coreVersion.setProtected();

		symbols.put( SYM_PART_NAME, partName );
		symbols.put( SYM_CORE_VERSION, coreVersion );

		symbols.put( SYM_PARTNAME, partName );
		symbols.put( SYM_CORE_COREVERSION, coreVersion );

		// --------------------------------------------------------------------
		// - Date -------------------------------------------------------------
		// --------------------------------------------------------------------

		DateTimeFormatter df = DateTimeFormatter.ofPattern( "MMM dd yyyy" );

		ExpressionValue date = new ExpressionValue( df.format( now ) );
		date.setProtected();
		symbols.put( SYM_DATE, date );

		ExpressionValue day = new ExpressionValue(  now.getDayOfMonth() );
		day.setProtected();
		symbols.put( SYM_DAY, day );

		ExpressionValue month = new ExpressionValue( now.getMonthValue() );
		month.setProtected();
		symbols.put( SYM_MONTH, month );

		ExpressionValue year = new ExpressionValue( now.getYear() - 2000 );
		year.setProtected();
		symbols.put( SYM_YEAR, year );

		ExpressionValue century = new ExpressionValue( 20 );
		century.setProtected();
		symbols.put( SYM_CENTURY, century );

		// --------------------------------------------------------------------
		// - Time -------------------------------------------------------------
		// --------------------------------------------------------------------

		DateTimeFormatter tf = DateTimeFormatter.ofPattern( "HH:mm:ss" );

		ExpressionValue time = new ExpressionValue( tf.format( now ) );
		time.setProtected();
		symbols.put( SYM_TIME, time );
		
		ExpressionValue second = new ExpressionValue( now.getSecond() );
		second.setProtected();
		symbols.put( SYM_SECOND, second );

		ExpressionValue minute = new ExpressionValue( now.getMinute() );
		minute.setProtected();
		symbols.put( SYM_MINUTE, minute );

		ExpressionValue hour = new ExpressionValue( now.getHour() );
		hour.setProtected();
		symbols.put( SYM_HOUR, hour );
	}

    // ========================================================================
	// === Parse this source ==================================================
	// ========================================================================

    public void parse()
    	throws LexerException, ParserException, IOException
    {
		Reader r = new AvrSourceReader( this );
		PushbackReader pbr = new PushbackReader( r, 1024 );

		AvrSourceLexer l = new AvrSourceLexer( pbr );
		AvrSourceParser p = new AvrSourceParser( l );

		setBuiltins();

		Start tree = p.parse();

		// --------------------------------------------------------------------
		// - Stage 1 ----------------------------------------------------------
		// --------------------------------------------------------------------

		Analysis stage1 = new AvrSourceAnalyser( this );
		tree.apply( stage1 );

		// --------------------------------------------------------------------
		// - Stage 2 ----------------------------------------------------------
		// --------------------------------------------------------------------

		Analysis stage2 = new AvrSourceResolver( this );
		tree.apply( stage2 );

		// --------------------------------------------------------------------
		// - Stage 3 ----------------------------------------------------------
		// --------------------------------------------------------------------

		Analysis stage3 = new AvrSourceAssembler( this );
		tree.apply( stage3 );

		if( AvrProgrammerStudio.DEVELOPPER_DEBUG )
		{
			ASTDisplay display3 = new ASTDisplay( this, stage3 );
			tree.apply( display3 );
		}
    }
}
