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

package be.lmenten.avr.assembler.ast;

import be.lmenten.avr.assembler.parser.ExpressionValue;
import be.lmenten.avr.assembler.parser.analysis.DepthFirstAdapter;
import be.lmenten.avr.assembler.parser.node.Node;
import be.lmenten.avr.core.descriptor.CoreDescriptor;
import be.lmenten.avr.project.AvrSource;

/**
 * A DepthFirstAdapter for staged analysis of an AST. This allow splitting
 * tree processing among severals classes.
 * 
 * @author Laurent Menten
 */
public abstract class AvrStagedDepthFirstAdapter
	extends DepthFirstAdapter
{
	private final AvrSource source;
	private final CoreDescriptor cdesc;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AvrStagedDepthFirstAdapter( AvrSource source )
	{
		if( source == null )
		{
			throw new NullPointerException();
		}

		this.source = source;
		this.cdesc = source.getCoreDescriptor();
	}

	// ========================================================================
	// ===
	// ========================================================================

	public CoreDescriptor getCoreDescriptor()
	{
		return cdesc;
	}

	// ========================================================================
	// === Link to source stage ===============================================
	// ========================================================================

	@Override
	public void setIn( Node node, Object o )
	{
		source.setIn( node, o );
	}

	@Override
	public Object getIn( Node node )
	{
		return source.getIn( node );
	}

	// ------------------------------------------------------------------------

	@Override
	public void setOut( Node node, Object o )
	{
		source.setOut( node, o );
	}

	@Override
	public Object getOut( Node node )
	{
		return source.getOut( node );
	}

	// ------------------------------------------------------------------------

	public void setSymbol( String name, ExpressionValue value )
	{
		source.setSymbol( name, value );
	}

	public ExpressionValue getSymbol( String name )
	{
		return source.getSymbol( name );
	}
}
