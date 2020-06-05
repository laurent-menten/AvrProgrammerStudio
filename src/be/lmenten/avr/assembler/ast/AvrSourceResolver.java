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
import be.lmenten.avr.assembler.parser.node.AAbsExpr;
import be.lmenten.avr.assembler.parser.node.AAddExpr;
import be.lmenten.avr.assembler.parser.node.AAndExpr;
import be.lmenten.avr.assembler.parser.node.AByte3Expr;
import be.lmenten.avr.assembler.parser.node.AByte4Expr;
import be.lmenten.avr.assembler.parser.node.ADivExpr;
import be.lmenten.avr.assembler.parser.node.AEquLine;
import be.lmenten.avr.assembler.parser.node.AExp2Expr;
import be.lmenten.avr.assembler.parser.node.AFracExpr;
import be.lmenten.avr.assembler.parser.node.AHighExpr;
import be.lmenten.avr.assembler.parser.node.AHwrdExpr;
import be.lmenten.avr.assembler.parser.node.AIntExpr;
import be.lmenten.avr.assembler.parser.node.ALog2Expr;
import be.lmenten.avr.assembler.parser.node.ALowExpr;
import be.lmenten.avr.assembler.parser.node.ALshiftExpr;
import be.lmenten.avr.assembler.parser.node.ALwrdExpr;
import be.lmenten.avr.assembler.parser.node.AModExpr;
import be.lmenten.avr.assembler.parser.node.AMulExpr;
import be.lmenten.avr.assembler.parser.node.ANotExpr;
import be.lmenten.avr.assembler.parser.node.AOrExpr;
import be.lmenten.avr.assembler.parser.node.AOrgLine;
import be.lmenten.avr.assembler.parser.node.APageExpr;
import be.lmenten.avr.assembler.parser.node.AQ15Expr;
import be.lmenten.avr.assembler.parser.node.AQ7Expr;
import be.lmenten.avr.assembler.parser.node.ARshiftExpr;
import be.lmenten.avr.assembler.parser.node.ASetLine;
import be.lmenten.avr.assembler.parser.node.ASubExpr;
import be.lmenten.avr.assembler.parser.node.AUnsetLine;
import be.lmenten.avr.assembler.parser.node.AXorExpr;
import be.lmenten.avr.assembler.parser.node.Node;
import be.lmenten.avr.assembler.parser.node.PExpr;
import be.lmenten.avr.project.AvrSource;

/**
 * AvrSourceResolver walks the AST and compute values of the expressions.
 * Values are stored into the 'in' hashmap. 
 * 
 * @author Laurent Menten
 */
public class AvrSourceResolver
	extends AvrStagedDepthFirstAdapter
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AvrSourceResolver( AvrSource source )
	{
		super( source );
	}

	// ========================================================================
	// === CONSTANTS / VARIABLES ==============================================
	// ========================================================================

	/**
	 * Create a constant
	 */
	@Override
	public void outAEquLine( AEquLine node )
	{
		String name = node.getName().getText();
		Node valueNode = node.getValue();
		
		ExpressionValue oldValue = getSymbol( name );
		if( oldValue == null )
		{
			ExpressionValue value = (ExpressionValue)getIn( valueNode );
			if( value != null )
			{
				value.setProtected();

				setSymbol( name, value );
			}
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Create a variable
	 */
	@Override
	public void outASetLine( ASetLine node )
	{
		String name = node.getName().getText();
		Node valueNode = node.getValue();

		ExpressionValue value = (ExpressionValue)getIn( valueNode );
		setSymbol( name, value );
	}

	/**
	 * Destroy a variable
	 */
	@Override
	public void outAUnsetLine( AUnsetLine node )
	{
		String name = node.getName().getText();

		setSymbol( name, null );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void outANotExpr( ANotExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.not() );
	}

	// ========================================================================
	// === EXPR ===============================================================
	// ========================================================================

	@Override
	public void outAAddExpr( AAddExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.add( right ) );
	}

	@Override
	public void outASubExpr( ASubExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.sub( right ) );
	}

	// ------------------------------------------------------------------------

	@Override
	public void outAAndExpr( AAndExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.and( right ) );
	}

	// ========================================================================
	// === FACTOR =============================================================
	// ========================================================================

	@Override
	public void outAMulExpr( AMulExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.mul( right ) );
	}

	@Override
	public void outADivExpr( ADivExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.div( right ) );
	}

	@Override
	public void outAModExpr( AModExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.mod( right ) );
	}

	// ------------------------------------------------------------------------

	@Override
	public void outAOrExpr( AOrExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.or( right ) );
	}

	@Override
	public void outAXorExpr( AXorExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.xor( right ) );
	}

	@Override
	public void outALshiftExpr( ALshiftExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.lshift( right ) );
	}

	@Override
	public void outARshiftExpr( ARshiftExpr node )
	{
		PExpr leftNode = node.getLeft();
		PExpr rightNode = node.getRight();

		ExpressionValue left = (ExpressionValue) getIn( leftNode );
		ExpressionValue right = (ExpressionValue) getIn( rightNode );
	
		setIn( node, left.rshift( right ) );
	}

	// ========================================================================
	// === VALUE ==============================================================
	// ========================================================================

	/**
	 * LOW: returns the low byte of an expression
	 */
	@Override
	public void outALowExpr( ALowExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.low() );
	}

	/**
	 * HIGH & BYTE2: returns the high byte of an expression
	 */
	@Override
	public void outAHighExpr( AHighExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.high() );
	}

	/**
	 * PAGE: returns bits 16-21 of an expression
	 */
	@Override
	public void outAPageExpr( APageExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.page() );
	}

	/**
	 * BYTE3: returns the third byte of an expression
	 */
	@Override
	public void outAByte3Expr( AByte3Expr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.byte3() );
	}

	/**
	 * BYTE4: returns the fourth byte of an expression
	 */
	@Override
	public void outAByte4Expr( AByte4Expr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.byte4() );
	}

	// ------------------------------------------------------------------------

	/**
	 * LWRD: returns bits 0-15 of an expression
	 */
	@Override
	public void outALwrdExpr( ALwrdExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.lwrd() );
	}

	/**
	 * HWRD: returns bits 16-31 of an expression
	 */
	@Override
	public void outAHwrdExpr( AHwrdExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.hwrd() );
	}

	// ------------------------------------------------------------------------

	/**
	 * Q7: converts a fractional floating point expression to a form suitable
	 * for he FMUL/FMULU/FMULSU instruction (sign + 7-bit fraction)
	 */
	@Override
	public void outAQ7Expr( AQ7Expr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.q7() );
	}

	/**
	 * Q15: converts a fractional floating point expression to a form suitable
	 * for he FMUL/FMULU/FMULSU instruction (sign + 15-bit fraction)
	 */
	@Override
	public void outAQ15Expr( AQ15Expr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.q15() );
	}

	/**
	 * INT: truncates a floating point expression to integer
	 */
	@Override
	public void outAIntExpr( AIntExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.integer() );
	}

	/**
	 * FRAC: extracts fractional part of a floating point expression
	 */
	@Override
	public void outAFracExpr( AFracExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.frac() );
	}

	// ------------------------------------------------------------------------

	/**
	 * ABS: returns the absolute value of a constant expression
	 */
	@Override
	public void outAAbsExpr( AAbsExpr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.abs() );
	}

	/**
	 * EXP2: returns 2 to the power of expression
	 */
	@Override
	public void outAExp2Expr( AExp2Expr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.exp2() );
	}

	/**
	 * LOG2: returns the integer part of log2(expression)
	 */
	@Override
	public void outALog2Expr( ALog2Expr node )
	{
		PExpr argNode = node.getArg();

		ExpressionValue arg = (ExpressionValue) getIn( argNode );

		setIn( node, arg.log2() );
	}

	// ========================================================================µ
	// ===
	// ========================================================================

	@Override
	public void outAOrgLine( AOrgLine node )
	{
		PExpr value = node.getValue();

		setIn( node, getIn( value ) );
	}
}
