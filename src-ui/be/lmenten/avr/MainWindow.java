package be.lmenten.avr;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import be.lmenten.avr.project.AvrProject;
import be.lmenten.avr.ui.AvrProjectRenderer;
import net.java.linoleum.jlfgr.Jlfgr;

public class MainWindow
	extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final Dimension windowSize = new Dimension( 800, 450 );

	// ------------------------------------------------------------------------

	private JList<AvrProject> recentsList;
	private DefaultListModel<AvrProject> recentsListModel;
	private JScrollPane recentsListScrollPane;
	private JSplitPane splitPane;
	private JPanel mainMenu;

	// ========================================================================
	// ===
	// ========================================================================

	public MainWindow()
	{
		setTitle( "Avr Programmer Studio V" + AvrProgrammerStudio.VERSION );
		setIconImage( Jlfgr.DEV_APPLICATION.getImage16() );

		setResizable( false );

		// --------------------------------------------------------------------

		recentsListModel = new DefaultListModel<AvrProject>();
		recentsListModel.addElement( new AvrProject( "f://ici/ou/là", "projet" ) );
		recentsListModel.addElement( new AvrProject( "d://mon/chemin", "autre projet" ) );

		recentsList = new JList<>( recentsListModel );
		recentsList.setCellRenderer( new AvrProjectRenderer() );

		recentsListScrollPane = new JScrollPane( recentsList );

		// --------------------------------------------------------------------

		mainMenu = new JPanel();
		mainMenu.setLayout( new BoxLayout( mainMenu, BoxLayout.PAGE_AXIS ) );
		
		
		mainMenu.setSize( 150,  100 );

		// --------------------------------------------------------------------

		splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		splitPane.setContinuousLayout( true );
		splitPane.setOneTouchExpandable( true );
		splitPane.setLeftComponent( recentsListScrollPane );
		splitPane.setRightComponent( mainMenu );
		splitPane.setDividerSize( 1 );
//		splitPane.setDividerLocation( 0.33d );

		// --------------------------------------------------------------------

		getContentPane().add( splitPane, BorderLayout.CENTER );

		// --------------------------------------------------------------------

		setSize( windowSize );
		setPreferredSize( windowSize );
		setLocationRelativeTo( null );
	}
}
