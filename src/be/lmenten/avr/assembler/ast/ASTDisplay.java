package be.lmenten.avr.assembler.ast;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.*;

import be.lmenten.avr.assembler.parser.ExpressionValue;
import be.lmenten.avr.assembler.parser.ExpressionValue.Type;
import be.lmenten.avr.assembler.parser.analysis.Analysis;
import be.lmenten.avr.assembler.parser.analysis.DepthFirstAdapter;
import be.lmenten.avr.assembler.parser.node.EOF;
import be.lmenten.avr.assembler.parser.node.Node;
import be.lmenten.avr.assembler.parser.node.Start;
import be.lmenten.avr.assembler.parser.node.Token;
import be.lmenten.avr.project.AvrSource;
import net.java.linoleum.jlfgr.Jlfgr;

public class ASTDisplay
	extends DepthFirstAdapter
{
	private final AvrSource src;
	private final Analysis a;

	// ------------------------------------------------------------------------

	private JFrame frame;
	private JTree astTree;
	private JTable symbolTable;
	
	private Stack<DefaultMutableTreeNode> parents = new Stack<>();

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public ASTDisplay( AvrSource src, Analysis a )
	{
		this.src = src;
		this.a = a;

		// --------------------------------------------------------------------

		frame = new JFrame( src.getFileName() );
		frame.addWindowListener( new WindowAdapter ()
		{
			public void windowClosing( WindowEvent e )
			{
				System.exit(0);
			}
		} );

		// --------------------------------------------------------------------

		astTree = new JTree();
		astTree.setCellRenderer( renderer );
		astTree.setBackground( Color.LIGHT_GRAY );

		Dimension astTreeSize = new Dimension( 450, 900 );

		JScrollPane astTreeScrollPane = new JScrollPane( astTree );
		astTreeScrollPane.setPreferredSize( astTreeSize );
		frame.getContentPane().add( astTreeScrollPane, BorderLayout.WEST );

		// --------------------------------------------------------------------

		symbolTable = new JTable();
		symbolTable.setModel( tableModel );
		symbolTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

		symbolTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		symbolTable.getColumnModel().getColumn(1).setPreferredWidth(75);
		symbolTable.getColumnModel().getColumn(2).setPreferredWidth(25);
		symbolTable.getColumnModel().getColumn(3).setPreferredWidth(240);

		Dimension symbolTableSize = new Dimension( 540, 900 );

		JScrollPane symbolTableScrollPane = new JScrollPane( symbolTable );
		symbolTableScrollPane.setPreferredSize( symbolTableSize );
		frame.getContentPane().add( symbolTableScrollPane, BorderLayout.CENTER );

		// --------------------------------------------------------------------

		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable( false );

		toolbar.add( expandAll );
//		toolbar.add( collapeAll );

		frame.getContentPane().add( toolbar, BorderLayout.NORTH );

		// --------------------------------------------------------------------

		frame.pack();
		frame.setVisible( true );
	}

	// ------------------------------------------------------------------------
	// ---
	// ------------------------------------------------------------------------

	@SuppressWarnings("serial")
	private Action expandAll = new AbstractAction()
	{
		public Action construct()
		{
			putValue( NAME, "Expand all" );
			putValue( LARGE_ICON_KEY, Jlfgr.GENERAL_ZOOM_IN.getIcon24() );
			putValue( SMALL_ICON, Jlfgr.GENERAL_ZOOM_IN.getIcon16() );
			putValue( SHORT_DESCRIPTION, "Expand all tree nodes" );
			putValue( MNEMONIC_KEY, Integer.valueOf( KeyEvent.VK_E ) );

			return this;
		}

		@Override
		public void actionPerformed( ActionEvent e )
		{
			expandAll( astTree );
		}
	}.construct() ;

	@SuppressWarnings({ "serial", "unused" })
	private Action collapeAll = new AbstractAction()
	{
		public Action construct()
		{
			putValue( NAME, "Collapse all" );
			putValue( LARGE_ICON_KEY, Jlfgr.GENERAL_ZOOM_OUT.getIcon24() );
			putValue( SMALL_ICON, Jlfgr.GENERAL_ZOOM_OUT.getIcon16() );
			putValue( SHORT_DESCRIPTION, "Expand all tree nodes" );
			putValue( MNEMONIC_KEY, Integer.valueOf( KeyEvent.VK_C ) );

			return this;
		}

		@Override
		public void actionPerformed( ActionEvent e )
		{
			collapseAll( astTree );
		}
	}.construct() ;

	// ========================================================================
	// ===
	// ========================================================================

	public void outStart( Start node )
	{
		TreeModel model = astTree.getModel();
		if( model instanceof DefaultTreeModel )
		{
		    DefaultTreeModel treeModel = (DefaultTreeModel) model;
		    DefaultMutableTreeNode root = (DefaultMutableTreeNode) parents.pop();
		    treeModel.setRoot( root );
		}
    }

	/**
	 * Push non-terminals on stack
	 */
	public void defaultIn(Node node)
    {
		String t = node.getClass().getSimpleName();

       	t += infos( node );
  
		DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode( t );
		parents.push( thisNode );
    }
   
	/**
	 * Pop non-terminal and to is parent (before it on stack)
	 */
	public void defaultOut(Node node)
    {
		DefaultMutableTreeNode thisNode = (DefaultMutableTreeNode) parents.pop ();
		((DefaultMutableTreeNode) parents.peek ()).add ( thisNode );
    }

	/**
	 * And terminal to is parent (on top of stack)
	 */
	public void defaultCase(Node node)
    {
    	String t = ((Token) node).getText ();

       	t += infos( node );
  
		DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode( t );
		((DefaultMutableTreeNode) parents.peek ()).add (thisNode);
    }
    
	public void caseEOF(EOF node)
	{
		symbolTable.setModel( tableModel );
	}
    
    // ------------------------------------------------------------------------

	private String infos( Node node )
	{
		StringBuilder s = new StringBuilder();

		Object o = a.getIn( node );
	    if( o != null )
	    {
	    	s.append( String.format( " in=%s", o ) );
	    }
	 
	    o = a.getOut( node );
	    if( o != null )
	    {
    		s.append( String.format( " out=%s", o ) );
	    }

	    return s.toString();
	}

    // ========================================================================
    // ===
    // ========================================================================

	public void expandAll( JTree tree )
	{
		Object root = tree.getModel().getRoot();
		if( root != null )
		{
			treeAction( tree, new TreePath( root ), true );
		}
	}

	public void collapseAll( JTree tree )
	{
		Object root = tree.getModel().getRoot();
		if( root != null )
		{
			treeAction( tree, new TreePath( root ), false );
		}
	}

	private void treeAction( JTree tree, TreePath path, boolean expand )
    {
		Iterator<TreePath> i = extremalPaths( tree.getModel(), path, new HashSet<TreePath>() ).iterator();
		for(  ; i.hasNext() ; )
		{
			if( expand )
			{
				tree.expandPath( (TreePath) i.next() );
			}
			else
			{
				tree.collapsePath( (TreePath) i.next() );
			}
		}
	}

	// ------------------------------------------------------------------------
	
	/** The "extremal paths" of the tree model's subtree starting at
        path. The extremal paths are those paths that a) are non-leaves
        and b) have only leaf children, if any. It suffices to know
        these to know all non-leaf paths in the subtree, and those are
        the ones that matter for expansion (since the concept of expan-
        sion only applies to non-leaves).
        The extremal paths of a leaves is an empty collection.
        This method uses the usual collection filling idiom, i.e.
        clear and then fill the collection that it is given, and
        for convenience return it. The extremal paths are stored
        in the order in which they appear in pre-order in the
        tree model.
    */
    public static Collection<TreePath> extremalPaths(TreeModel data,
										   TreePath path, 
										   Collection<TreePath> result)
    {
        result.clear();
		
        if (data.isLeaf(path.getLastPathComponent()))
        {
            return result; // should really be forbidden (?)
        }
        
        extremalPathsImpl(data, path, result);

        return result;
	}
	
	private static void extremalPathsImpl(TreeModel data, 
										  TreePath path,
										  Collection<TreePath> result)
    {
        Object node = path.getLastPathComponent();
        
        boolean hasNonLeafChildren = false;

        int count = data.getChildCount(node);
        
        for (int i = 0; i < count; i++)
            if (!data.isLeaf(data.getChild(node, i)))
                hasNonLeafChildren = true;

        if (!hasNonLeafChildren)
            result.add(path);
        else
        {
            for (int i = 0; i < count; i++)
            {
                Object child = data.getChild(node, i);
                
                if (!data.isLeaf(child))
                    extremalPathsImpl(data,
									  path.pathByAddingChild(child),
									  result);
            }
        }
	}

	// ========================================================================
	// ===
	// ========================================================================

	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);

			setOpaque( true );
			setBackground( Color.LIGHT_GRAY );

		    String node = (String) ((DefaultMutableTreeNode) value).getUserObject();

			if( node.contains( "APrologLine" ) || node.contains( "AEndprologLine" ) )
			{
				setBackground( Color.YELLOW );
			}
			if( node.contains( "AEquLine" ) || node.contains( "ASetLine" ) || node.contains( "AUnsetLine" ) )
			{
				setBackground( Color.CYAN );					
			}
			if( node.contains( "ALabelLine" )  )
			{
				setBackground( Color.MAGENTA );
			}
			else if( node.contains( "ACsegLine" ) || node.contains( "ADsegLine" ) || node.contains( "AEsegLine" )
						|| node.contains( "AOrgLine" ) )
			{
        		setBackground( Color.RED );
		    }
			else if( node.contains( "Instruction") )
			{
				setForeground( Color.BLUE );
			}

		       return this;
		}
	};

	// ========================================================================
	// ===
	// ========================================================================

	@SuppressWarnings("serial")
	TableModel tableModel = new AbstractTableModel()
	{
		private int symbolsCount;
		private ArrayList<String> names;
		private ArrayList<ExpressionValue> values;

		private void update()
		{
			symbolsCount = src.getSymbolsCount();

			names = new ArrayList<>();
			values = new ArrayList<>();

	    	src.listSymbols( (k,v) ->
	    	{
	    		names.add( k );
	    		values.add( v );
	    	} );
		}

		// --------------------------------------------------------------------

		private final String [] columns =
		{
			"Name",
			"T",
			"L",
			"Value(s)"
		};

		@Override
		public int getColumnCount()
		{
			return columns.length;
		}

		public String getColumnName( int column )
		{
			return columns[ column ];
		}

		// --------------------------------------------------------------------

		@Override
		public int getRowCount()
		{
			update();

			return symbolsCount;
		}

		// --------------------------------------------------------------------

		@Override
		public Object getValueAt( int rowIndex, int columnIndex )
		{
			switch( columnIndex )
			{
				case 0:
					return names.get( rowIndex );

				case 1:
					return values.get( rowIndex ).getType();

				case 2:
					return values.get( rowIndex ).isProtected() ? "🔒" : "" ;

				case 3:
				{
					if( values.get( rowIndex ).getType() == Type.STRING )
					{
						return values.get( rowIndex ).getString();
					}

					StringBuilder  s = new StringBuilder();
					s.append( String.format( "0x%08X", values.get( rowIndex ).getQWord() ) )
					 .append( ", " )
					 .append( String.format( "%f", values.get( rowIndex ).getDouble() ) );
					 ;

					 return s.toString();
				}
			}

			return null;
		}
	};
}



