package be.lmenten.avr.ui.registers;

import javax.swing.table.AbstractTableModel;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.CoreRegister;
import be.lmenten.avr.core.descriptor.CoreDescriptor;
import be.lmenten.avr.core.descriptor.CoreRegistersFile;

public abstract class AbstractCoreRegistersViewModel
	extends AbstractTableModel
	implements CoreRegistersViewModel
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	private static final int COL_ADDRESS = 0;
	private static final int COL_NAME = 1;
	private static final int COL_VALUE = 2;
	private static final int COL_VALUE_BIT7 = 3;
	private static final int COL_VALUE_BIT6 = 4;
	private static final int COL_VALUE_BIT5 = 5;
	private static final int COL_VALUE_BIT4 = 6;
	private static final int COL_VALUE_BIT3 = 7;
	private static final int COL_VALUE_BIT2 = 8;
	private static final int COL_VALUE_BIT1 = 9;
	private static final int COL_VALUE_BIT0 = 10;

	private static final int COLUMNS_COUNT = 11;

	// ========================================================================
	// ===
	// ========================================================================

	private final Core core;
	private final CoreDescriptor cdesc;
	private final CoreRegistersFile file;

	private final int baseAddress;
	private final int fileSize;

	// ========================================================================
	// ===
	// ========================================================================

	public AbstractCoreRegistersViewModel( Core core, CoreRegistersFile file )
	{
		this.core = core;
		this.cdesc = core.getDescriptor();
		this.file = file;

		switch( file )
		{
			case GENERAL:
				baseAddress = cdesc.getRegistersBase();
				fileSize = cdesc.getRegistersCount();
				break;

			case IO:
				baseAddress = cdesc.getIoRegistersBase();
				fileSize = cdesc.getIoRegistersCount();
				break;

			case EXTENDED_IO:
				baseAddress = cdesc.getExtendedIoRegistersBase();
				fileSize = cdesc.getExtendedIoRegistersCount();
				break;

			default:
				throw new RuntimeException();
		}
	}

	// ========================================================================
	// ===
	// ========================================================================

	public Core getCore()
	{
		return core;
	}

	public CoreRegistersFile getFile()
	{
		return file;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getRowCount()
	{
		return fileSize;
	}

	@Override
	public int getColumnCount()
	{
		return COLUMNS_COUNT;
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		int address = baseAddress + rowIndex;

		CoreRegister reg = core.getIORegisterByPhysicalAddress( address );
		if( reg == null )
		{
			if( columnIndex == COL_ADDRESS )
			{
				return String.format( "0x%04X", address );
			}

			return null;
		}

		// --------------------------------------------------------------------

		switch( columnIndex )
		{
			case COL_ADDRESS:
				return String.format( "0x%04X", address );

			case COL_NAME:
				return reg.getName();

			case COL_VALUE:
				return reg.internGetData();

			case COL_VALUE_BIT7:
			case COL_VALUE_BIT6:
			case COL_VALUE_BIT5:
			case COL_VALUE_BIT4:
			case COL_VALUE_BIT3:
			case COL_VALUE_BIT2:
			case COL_VALUE_BIT1:
			case COL_VALUE_BIT0:
				return reg.getBitName( 7 - (columnIndex - COL_VALUE_BIT7) );

			default:
				throw new RuntimeException( "columnIndex = " + columnIndex );
		}
	}

	// ========================================================================
	// ===
	// ========================================================================

	public int getColumnSize()
	{
		return 0;
	}
}
