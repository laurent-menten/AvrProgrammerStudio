package be.lmenten.avr.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import be.lmenten.avr.project.AvrProject;

public class AvrProjectRenderer
	extends JPanel
	implements ListCellRenderer<AvrProject>
{
	private static final long serialVersionUID = 1L;

	private JLabel name;
	private JLabel path;

	public AvrProjectRenderer()
	{
		setLayout( new BoxLayout( this, BoxLayout.PAGE_AXIS ) );

		name = new JLabel();
		name.setBackground( Color.GRAY );
		name.setForeground( Color.BLACK );
		add( name );

		path = new JLabel();
		path.setBackground( Color.GRAY );
		path.setForeground( Color.BLUE );
		add( path );
	}

	@Override
	public Component getListCellRendererComponent( JList<? extends AvrProject> list, AvrProject value, int index, boolean isSelected, boolean cellHasFocus )
	{
		name.setText( value.name );
		path.setText( value.path );

		return this;
	}	
}
