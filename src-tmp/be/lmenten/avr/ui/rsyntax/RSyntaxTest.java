package be.lmenten.avr.ui.rsyntax;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class RSyntaxTest
	extends JFrame
{
	private static final long serialVersionUID = 1L;

	public RSyntaxTest()
	{
		setTitle( "Assembler editor" );

		setRootPane( new AvrAssemblerEditorPane() );
		setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		pack();
	}

	public static void main( String[] args )
	{
		SwingUtilities.invokeLater(() ->
		{
			try
			{
				UIManager.setLookAndFeel( NimbusLookAndFeel.class.getName() );
			}
			catch( Exception e )
			{
				e.printStackTrace(); // Never happens
			}

			Toolkit.getDefaultToolkit().setDynamicLayout( true );

			new RSyntaxTest().setVisible(true);
		} );
	}
}
