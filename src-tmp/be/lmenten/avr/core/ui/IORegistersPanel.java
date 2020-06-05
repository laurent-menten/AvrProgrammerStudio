package be.lmenten.avr.core.ui;

import java.awt.Panel;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import be.lmenten.avr.core.tmp.Core;

public class IORegistersPanel
	extends Panel
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	// 0x0000 || Name || hex | 0 0 0 0 0 0 0 0
	private static final int TABLE_WIDTH = 1 + 1 + 1 + 8;

	private static final String [] COLUMN_TITLES =
	{
		"Name",
		"Hex",
		"", "", "", "",		"", "", "", ""
	};

	// ------------------------------------------------------------------------

	private final int ioCount;
	private final JTable ioDisplay;
	private final JScrollPane ioScroll;

	private final int extIoCount;
	private final JTable extIoDisplay;
	private final JScrollPane extIoScroll;

	private final JSplitPane splitPane;

	// ========================================================================
	// ===
	// ========================================================================

	public IORegistersPanel( Core core )
	{
		ioCount = core.getCoreDescriptor().getIOResistersCount();
		ioDisplay = new JTable( ioCount, TABLE_WIDTH );
		ioDisplay.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		ioScroll = new JScrollPane( ioDisplay );

		extIoCount = core.getCoreDescriptor().getExtendedIOResistersCount();
		extIoDisplay = new JTable( extIoCount, TABLE_WIDTH );
		extIoDisplay.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		extIoScroll = new JScrollPane( extIoDisplay );		

		splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, ioDisplay, extIoDisplay );

		add( splitPane );
	}
}
