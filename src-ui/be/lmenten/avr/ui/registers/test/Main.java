package be.lmenten.avr.ui.registers.test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.descriptor.CoreDescriptor;
import be.lmenten.avr.core.descriptor.CoreRegistersFile;
import be.lmenten.avr.core.descriptor.SupportedCore;
import be.lmenten.avr.ui.registers.JCoreRegistersView;

public class Main {

	public static void main( String[] args )
	{
		CoreDescriptor desc = new CoreDescriptor( SupportedCore.ATMEGA2560 );
		Core core = new Core( desc );
	
		// --------------------------------------------------------------------

		JFrame frame1 = new JFrame( "Registers view" );
		frame1.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

		JCoreRegistersView view1 = new JCoreRegistersView(core, CoreRegistersFile.IO );
		JScrollPane scrollPane1 = new JScrollPane( view1 );
		frame1.add( scrollPane1 );

		frame1.setVisible( true );

		// --------------------------------------------------------------------

		JFrame frame2 = new JFrame( "Registers view" );
		frame2.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

		JCoreRegistersView view2 = new JCoreRegistersView(core, CoreRegistersFile.EXTENDED_IO );
		JScrollPane scrollPane2 = new JScrollPane( view2 );
		frame2.add( scrollPane2 );

		frame2.setVisible( true );

	}

}
