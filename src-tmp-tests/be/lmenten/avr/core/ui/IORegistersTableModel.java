package be.lmenten.avr.core.ui;

import javax.swing.table.AbstractTableModel;

import be.lmenten.avr.core.tmp.Core;
import be.lmenten.avr.core.tmp.memory.MemoryRange;

public class IORegistersTableModel
	extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	private static final int COL_ADDRESS	= 0;
	private static final int COL_NAME		= 1;
	private static final int COL_HEX_VALUE	= 2;
	private static final int COL_BIT_7		= 3;
	private static final int COL_BIT_6		= 4;
	private static final int COL_BIT_5		= 5;
	private static final int COL_BIT_4		= 6;
	private static final int COL_BIT_3		= 7;
	private static final int COL_BIT_2		= 8;
	private static final int COL_BIT_1		= 9;
	private static final int COL_BIT_0		= 10;
	private static final int COL_COUNT		= 11; // ***

	private final MemoryRange range;
	private final int ROW_COUNT;

	private final byte [] lastValues;

	// ========================================================================
	// ===
	// ========================================================================

	public IORegistersTableModel( Core core, MemoryRange range )
	{
		this.range = range;

		ROW_COUNT = range.getSize();

		lastValues = new byte [ROW_COUNT];
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getRowCount()
	{
		return range.getSize();
	}

	@Override
	public int getColumnCount()
	{
		return COL_COUNT;
	}

	@Override
	public String getColumnName( int column )
	{
		switch( column )
		{
			case COL_ADDRESS:
				return "addr";

			case COL_NAME:
				return "name";

			case COL_HEX_VALUE:
				return "hex";

			default:
				return "";
		}
	}

	// ------------------------------------------------------------------------

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		int address = range.getAddress() + rowIndex;
		int bit = COL_BIT_0 - columnIndex;
		byte data = 0;
		boolean state = false;

		switch( columnIndex )
		{
			case COL_ADDRESS:
				return address;

			case COL_NAME:
				return "";

			case COL_HEX_VALUE:
				return data;

			case COL_BIT_7:
			case COL_BIT_6:
			case COL_BIT_5:
			case COL_BIT_4:
			case COL_BIT_3:
			case COL_BIT_2:
			case COL_BIT_1:
			case COL_BIT_0:
				state = (data & (1<<bit)) == (1<<bit);

				return state;
		}

		return null;
	}
}
