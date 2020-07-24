package be.lmenten.avr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.Runtime.Version;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import be.lmenten.avr.assembler.ParsedAssemblerLine;
import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.ResetSources;
import be.lmenten.avr.core.analysis.AccessEvent;
import be.lmenten.avr.core.analysis.AccessEventListener;
import be.lmenten.avr.core.descriptor.CoreDescriptor;
import be.lmenten.avr.core.descriptor.SupportedCore;
import be.lmenten.avr.core.event.CoreEvent;
import be.lmenten.avr.core.event.CoreEventListener;
import be.lmenten.avr.core.event.CoreEventType;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.ui.ByteDisplayPanel;
import be.lmenten.avr.ui.CoreUIDefaults;
import be.lmenten.utils.app.Application;
import be.lmenten.utils.prefs.PrefsUtils;
import be.lmenten.utils.swing.SwingUtils;

/**
 * 1. New development project
 * 2. New Reverse engineering project
 * -
 * x. Preferences
 * 
 * @author Laurent Menten
 */
public class AvrProgrammerStudio
	extends Application<AvrProgrammerStudio>
{
	public static final boolean DEVELOPPER_DEBUG = true;

	public static final Version VERSION = 
		Version.parse( "1.0.1-ea+1-20200524" );

	// -------------------------------------------------------------------------

	// =========================================================================
	// === CONSTRUCTOR(s) ======================================================
	// =========================================================================

	public AvrProgrammerStudio()
	{
		log.info( "AVR Programmer Studio (version " + VERSION + ")" );

		// ----------------------------------------------------------------------

		int runCount = prefs.getInt( "run.count", 0 );
	
		try
		{
			prefs.putInt( "run.count", ++runCount );
			prefs.flush();
		}
		catch( BackingStoreException e )
		{
			log.log( Level.WARNING, "Failed to save preferences", e  );
		}

	}

	// =========================================================================
	// === class: Application ==================================================
	// =========================================================================

	@Override
	protected void initialize()
	{
		// ----------------------------------------------------------------------
		// - UI pre-configuration -----------------------------------------------
		// ----------------------------------------------------------------------

		splashScreenUpdate( "Setting Nimbus Look & Feel" );

		SwingUtils.setNimbusLookAndFeel();

		// ----------------------------------------------------------------------
		// - Install UI defaults ------------------------------------------------
		// ----------------------------------------------------------------------

		splashScreenUpdate( "Setting UI defaults" );

		CoreUIDefaults.installDefaults();

		// ----------------------------------------------------------------------
		// - Core descriptors ---------------------------------------------------
		// ----------------------------------------------------------------------

		for( SupportedCore core : SupportedCore.values() )
		{
			splashScreenUpdate( "Loading core descriptor : " + core.getId() );;

			core.getDescriptor();
		}
	}

	@Override
	protected void cleanup()
	{
	}

	// =========================================================================
	// === SPLASHSCREEN ========================================================
	// =========================================================================

	/**
	 * <pre>
	 * size : 620, 300
	 * logo : 20, 20, 160, 260
	 * panel : 200, 20, 400, 160
	 * text area : 200, 260, 400, 20
	 * </pre>
	 */
	@Override
	public void splashScreenUpdate( String text )
	{
		if( hasSplashScreen )
		{
			splashScreenGr.setColor( Color.WHITE );
			splashScreenGr.fillRect( 200, 260, 400, 20 );
			splashScreenGr.setColor( Color.LIGHT_GRAY );
			splashScreenGr.drawRect( 200, 260, 400, 20 );

			splashScreenGr.setColor( Color.BLACK );
			splashScreenGr.drawString( text, 205, 275 );

			splashScreen.update();
		}
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	protected void run()
	{
		CoreDescriptor cdesc = SupportedCore.ATMEGA2560.getDescriptor();

		Properties config = new Properties();
		config.put( Core.CONFIG_EXTERNAL_SRAM, "16K" );
		
		Core core = new Core( cdesc, config );

		core.addCoreEventListener( new CoreEventListener()
		{
			@Override
			public void onEvent( CoreEvent event )
			{
				System.out.println( event );

				if( event.getEventType() == CoreEventType.FLASH_MEMORY_OVERWRITTING )
				{
					event.abort();
				}
			}
		} );

		core.reset( ResetSources.POWER_ON );

		// --------------------------------------------------------------------

		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle( "Cancel for self-test" );
		fc.setCurrentDirectory( new File( "." ) );
        fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter( new FileFilter()
		{
			public String getDescription()
			{
		        return "iHex program (*.hex)";
		    }

		    public boolean accept( File f )
		    {
		    	if( f.isDirectory() )
		    	{
		    		return true;
		        }
		    	else
		    	{
		            return f.getName().toLowerCase().endsWith( ".hex" );
		        }
		    }
		} );

		int returnVal = fc.showOpenDialog( null );
		if( returnVal == JFileChooser.APPROVE_OPTION )
		{
			try
			{
				core.loadFlash( new File( "./bootloader.ATmega2560.03E000.hex" ) );
				core.loadFlash(  fc.getSelectedFile() );
			}
			catch( IOException e )
			{
				log.log( Level.SEVERE, "Failed to load program", e );
				System.exit( 0 );
			}
		}
		else
		{
			finish( false );
			return;
		}

		// --------------------------------------------------------------------

		JFrame frame = new JFrame( "CoreMemoryMap test" );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );


		JPanel workspace = new JPanel();
		workspace.setLayout( new BoxLayout( workspace, BoxLayout.PAGE_AXIS ) );
		frame.getContentPane().add( workspace, BorderLayout.CENTER );
		
		workspace.add( getRegistersGrid( core ) );

/*
		JPanel mcu = new JPanel();
		mcu.setLayout( new FlowLayout() );
		workspace.add( mcu );

		JCoreMemoryMap memoryMap1 = new JCoreMemoryMap();
		CoreMemoryMapModel model1 = new DefaultCoreMemoryMapModel( CoreMemory.SRAM, core );
		memoryMap1.setModel( model1 );
		mcu.add( memoryMap1 );

		JCoreMemoryMap memoryMap2 = new JCoreMemoryMap();
		CoreMemoryMapModel model2 = new DefaultCoreMemoryMapModel( CoreMemory.FLASH, core );
		memoryMap2.setModel( model2 );
		mcu.add( memoryMap2 );

		JCoreMemoryMap memoryMap3 = new JCoreMemoryMap();
		CoreMemoryMapModel model3 = new DefaultCoreMemoryMapModel( CoreMemory.EEPROM, core );
		memoryMap3.setModel( model3 );
		mcu.add( memoryMap3 );
*/

		Instruction i = core.getCurrentInstruction();
		ParsedAssemblerLine parsedLine = new ParsedAssemblerLine();
		parsedLine.setAddress( core.getProgramCounter() );
		i.toParsedLine( core, parsedLine );

		JTextField line = new JTextField();
		line.setText( parsedLine.toString() );
		line.setEditable( false );
		workspace.add( line );

		JButton button = new JButton( "step" );
		button.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				core.step();

				Instruction i = core.getCurrentInstruction();
				if( i == null )
				{
					line.setText( "No more instructions ..." );
					return;
				}

				ParsedAssemblerLine parsedLine = new ParsedAssemblerLine();
				parsedLine.setAddress( core.getProgramCounter() );
				i.toParsedLine( core, parsedLine );

				line.setText( parsedLine.toString() );
			}
		} );

		workspace.add( button );

		frame.getContentPane().add( workspace, BorderLayout.CENTER );

		// --------------------------------------------------------------------

		frame.pack();
		SwingUtils.showOnScreen( 1, frame );
		frame.setVisible( true );
	}

	// ------------------------------------------------------------------------

	private JPanel getRegistersGrid( Core core )
	{
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 17, 4 ) );

		JLabel l;
		JTextField t;

		for( int i = 0 ; i < 16 ; i++ )
		{
			CoreRegister r = core.getGeneralRegister( AvrRegisters.lookupByOperandCode( i ) );

			
			l = new JLabel( r.getName() );
			panel.add( l );

			t = new JTextField( 4 );
			t.setText( String.format( "0x%02X", r.getData() ) );
			t.setEditable( false );
			r.addAccessListener( new AccessEventListener()
			{
				JTextField tt;
				
				@Override
				public void onAccessEvent( AccessEvent event )
				{
					if( event.getAccessMode() == AccessEvent.ACCESS_WRITE )
					{
						tt.setText( String.format( "0x%02X", event.getNewData() & 0xFF ) ) ;
					}
				}

				public AccessEventListener param( JTextField t )
				{
					tt = t;
					return this;
				}
			}.param( t ) );
			panel.add( t );

			// ----------------------------------------------------------------

			r = core.getGeneralRegister( AvrRegisters.lookupByOperandCode( 16 + i ) );

			l = new JLabel( r.getName() );
			panel.add( l );

			t = new JTextField( 4 );
			t.setText( String.format( "0x%02X", r.getData() ) );
			r.addAccessListener( new AccessEventListener()
			{
				JTextField tt;
				
				@Override
				public void onAccessEvent( AccessEvent event )
				{
					if( event.getAccessMode() == AccessEvent.ACCESS_WRITE )
					{
						tt.setText( String.format( "0x%02X", event.getNewData() & 0xFF ) ) ;
					}
				}

				public AccessEventListener param( JTextField t )
				{
					tt = t;
					return this;
				}
			}.param( t ) );
			panel.add( t );
		}

		l = new JLabel( core.SREG.getName() );
		panel.add( l );

		ByteDisplayPanel panel1 = new ByteDisplayPanel( (byte)0, new String [] { "I", "T", "H", "S", "V", "N", "Z", "C" } );
		panel.add( panel1 );

		core.SREG.addAccessListener( new AccessEventListener()
		{
			ByteDisplayPanel  tt;
			
			@Override
			public void onAccessEvent( AccessEvent event )
			{
				if( event.getAccessMode() == AccessEvent.ACCESS_WRITE )
				{
					tt.update( (byte) event.getNewData() );
				}
			}

			public AccessEventListener param( ByteDisplayPanel t )
			{
				tt = t;
				return this;
			}
		}.param( panel1 ) );

		return panel;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@SuppressWarnings("unused")
	private void testGUI( Core core )
	{
		JFrame frame = new JFrame( "core registers" );
		frame.setLayout( new BoxLayout( frame.getContentPane(), BoxLayout.Y_AXIS ) );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		ByteDisplayPanel panel1 = new ByteDisplayPanel( (byte)0, new String [] { "I", "T", "H", "S", "V", "N", "Z", "C" } );
		frame.add( panel1 );

		ByteDisplayPanel panel2 = new ByteDisplayPanel( (byte)0, new String [] { "JTD", null, null, "PUD", null, null, "IVSEL", "IVCE" } );
		frame.add( panel2 );

		frame.pack();
		frame.setVisible( true );

//		showOnScreen( 1, frame );

		// ------------------------------------------------------------

		for( int i = 0 ; i < 256 ; i++ )
		{
	        panel1.update( (byte) i );
	        panel2.update( (byte) ~i );	

	        try
	        {
				Thread.sleep( 500 );
			}
	        catch( InterruptedException e )
	        {
				e.printStackTrace();
			}
		}
	}

	// ========================================================================
	// === LOGGING ============================================================
	// ========================================================================

	private static final Logger log
		= Logger.getLogger( AvrProgrammerStudio.class.getName() );

	private final Preferences prefs
		= PrefsUtils.getNode( AvrProgrammerStudio.class );
}
