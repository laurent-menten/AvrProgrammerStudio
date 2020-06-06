package be.lmenten.avr.core.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

//	Name 00 00000000

public class ByteDisplayPanel
	extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static final int DISPLAY_WIDTH = 8;

	// ------------------------------------------------------------------------

	private byte lastValue;
	private boolean [] lastBitValues = new boolean [DISPLAY_WIDTH];

	private String [] bitNames = null;

	private Color bitSet = Color.BLACK;
	private Color bitClear = Color.GRAY;
	private Color changedColor = Color.PINK;
	private Color unchangedColor = Color.WHITE;

	// ------------------------------------------------------------------------

	private JTextField hexValue;
	private JTextField [] bitValues = new JTextField [DISPLAY_WIDTH];

	private Font normalFont;
	private Font boldFont;

	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public ByteDisplayPanel()
	{
		this( (byte) 0, null );
	}

	public ByteDisplayPanel( byte initialValue )
	{
		this( initialValue, null );
	}

	public ByteDisplayPanel( byte initialValue, String [] bitNames )
	{
		lastValue = initialValue;
		for( int i = 0 ; i < DISPLAY_WIDTH ; i++ )
		{
			lastBitValues[i] = ((initialValue & (1 << i)) == (1 << i));
		}

		this.bitNames = bitNames;

		// --------------------------------------------------------------------

		int maxBitNameSize = 1;
		for( int i = 0 ; (bitNames != null) && (i < DISPLAY_WIDTH) ; i++ )
		{
			if( (bitNames[i] != null) && (bitNames[i].length() > maxBitNameSize) )
			{
				maxBitNameSize = bitNames[i].length();
			}				
		}

		// --------------------------------------------------------------------

		setLayout( new GridBagLayout() );

		// --------------------------------------------------------------------

		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 1;
		c.gridheight = 9;
		
		hexValue = new JTextField( 2 );
		hexValue.setEditable( false );
		hexValue.setHorizontalAlignment( JTextField.CENTER );

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets( 0, 0, 0, 5 );
		add( hexValue, c );

		normalFont = hexValue.getFont();
		boldFont = normalFont.deriveFont( Font.BOLD );

		c.insets = new Insets( 0, 0, 0, 0 );

		for( int i = 0 ; i < DISPLAY_WIDTH ; i++ )
		{
			bitValues[i] = new JTextField( maxBitNameSize );
			bitValues[i].setEditable( false );
			bitValues[i].setHorizontalAlignment( JTextField.CENTER );

			c.gridx = 9 - i;
			c.gridy = 0;
			add( bitValues[i], c );
		}

		update( initialValue );
	}

	// ========================================================================
	// === 
	// ========================================================================

	public void setChangedColor( Color changedColor )
	{
		this.changedColor = changedColor;
	}

	public Color getChangedColor()
	{
		return changedColor;
	}

	public void setUnchangedColor( Color unchangedColor )
	{
		this.unchangedColor = unchangedColor;
	}

	public Color getUnchangedColor()
	{
		return unchangedColor;
	}

	// ========================================================================
	// === 
	// ========================================================================

	public void update( byte newValue )
	{
		if( newValue != lastValue )
		{
			lastValue = newValue;

			hexValue.setText( String.format( "%02X", newValue ) );
			hexValue.setBackground( changedColor );

			for( int i = 0 ; i < DISPLAY_WIDTH ; i++ )
			{
				if( lastBitValues[i] != ((newValue & (1 << i)) == (1 << i)) )
				{
					lastBitValues[i] = ((newValue & (1 << i)) == (1 << i));

					if( bitNames != null ) 
					{
						if( bitNames[7-i] != null )
							bitValues[i].setText( bitNames[7-i] );
						else
							bitValues[i].setText( "-" );

						if( lastBitValues[i] )
						{
							bitValues[i].setFont( boldFont );;
							bitValues[i].setForeground( bitSet );
						}
						else
						{
							bitValues[i].setFont( normalFont );;
							bitValues[i].setForeground( bitClear );
						}
					}
					else
					{
						bitValues[i].setText( lastBitValues[i] ? "1" : "0" );						

						if( lastBitValues[i] )
						{
							bitValues[i].setFont( boldFont );;
							bitValues[i].setForeground( bitSet );
						}
						else
						{
							bitValues[i].setFont( normalFont );;
							bitValues[i].setForeground( bitClear );
						}
					}

					bitValues[i].setBackground( changedColor );
				}
				else
				{
					bitValues[i].setBackground( unchangedColor );
				}
			}
		}

		else
		{
			hexValue.setText( String.format( "%02X", newValue ) );
			hexValue.setBackground( unchangedColor );

			for( int i = 0 ; i < DISPLAY_WIDTH ; i++ )
			{
				if( bitNames != null ) 
				{
					if( bitNames[7-i] != null )
						bitValues[i].setText( bitNames[7-i] );
					else
						bitValues[i].setText( "-" );

					if( lastBitValues[i] )
					{
						bitValues[i].setFont( boldFont );;
						bitValues[i].setForeground( bitSet );
					}
					else
					{
						bitValues[i].setFont( normalFont );;
						bitValues[i].setForeground( bitClear );
					}
				}
				else
				{
					bitValues[i].setText( lastBitValues[i] ? "1" : "0" );						

					if( lastBitValues[i] )
					{
						bitValues[i].setFont( boldFont );;
						bitValues[i].setForeground( bitSet );
					}
					else
					{
						bitValues[i].setFont( normalFont );;
						bitValues[i].setForeground( bitClear );
					}
				}

				bitValues[i].setBackground( unchangedColor );
			}
		}
	}
}
