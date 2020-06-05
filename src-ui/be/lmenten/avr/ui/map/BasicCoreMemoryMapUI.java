package be.lmenten.avr.ui.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.ComponentUI;

import be.lmenten.avr.core.descriptor.CoreMemoryRange;

public class BasicCoreMemoryMapUI
	extends CoreMemoryMapUI
{
	private JCoreMemoryMap coreMemoryMap;
	private CoreMemoryMapModel coreMemoryMapModel;

	private final FontRenderContext fontRendererContex
		= new FontRenderContext( null, false, false );

	private Dimension preferedSize = new Dimension( 75, 250 );

	private Font normalFont;
	private Font normalBoldFont;
	private Font smallFont;

	private int mapX = 0;
	private int mapY = 0;
	private int mapW = 75;
	private int mapH = 250;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public BasicCoreMemoryMapUI()
	{
	}

	// ------------------------------------------------------------------------

    public static ComponentUI createUI( JComponent c )
    {
        return new BasicCoreMemoryMapUI();
    }

    // ========================================================================
    // ===
    // ========================================================================

    @Override
    public Dimension getPreferredSize( JComponent c )
    {
		return preferedSize;
    }

    // ========================================================================
	// === UI DELEGATE ========================================================
	// ========================================================================

	@Override
	public void installUI( JComponent component )
	{
		super.installUI( component );

		coreMemoryMap = (JCoreMemoryMap) component;
		coreMemoryMapModel = coreMemoryMap.getModel();

		coreMemoryMap.addPropertyChangeListener( "model", propertyChangeListener );
		coreMemoryMapModel.addListDataListener( listDataListener );

		// --------------------------------------------------------------------

		normalFont = new Font( "sansserif", Font.PLAIN, 12 );
		normalBoldFont = normalFont.deriveFont( Font.BOLD );
		smallFont = normalFont.deriveFont( AffineTransform.getScaleInstance( 0.80, 0.80 ) );

		// --------------------------------------------------------------------

		Rectangle2D r1;
		r1 = normalBoldFont.getStringBounds( "M", fontRendererContex );

		Rectangle2D r2;
		r2 = smallFont.getStringBounds( "0xFFFFFF", fontRendererContex );

		preferedSize.width = mapW + (int)r2.getWidth() + 5;
		preferedSize.height = mapH + (int)r1.getHeight() + 2;
	}

	@Override
	public void uninstallUI( JComponent component )
	{
		super.uninstallUI( component );

		if( coreMemoryMapModel != null )
		{
			coreMemoryMapModel.removeListDataListener( listDataListener );
		}

		coreMemoryMap = null;
		coreMemoryMapModel = null;
	}

	// ========================================================================
	// ===
	// ========================================================================

	private final PropertyChangeListener propertyChangeListener
		= new PropertyChangeListener()
	{
		@Override
		public void propertyChange( PropertyChangeEvent evt )
		{
			CoreMemoryMapModel oldModel = (CoreMemoryMapModel) evt.getOldValue();
			if( oldModel != null )
			{
				oldModel.removeListDataListener( listDataListener );
			}

			CoreMemoryMapModel newModel = (CoreMemoryMapModel) evt.getNewValue();
			if( newModel != null )
			{
				newModel.addListDataListener( listDataListener );
			}

			coreMemoryMapModel = newModel;
			coreMemoryMap.revalidate();
			coreMemoryMap.repaint();
		}
	};

	// ------------------------------------------------------------------------

	private final ListDataListener listDataListener
		= new ListDataListener()
	{
		@Override
		public void intervalRemoved( ListDataEvent e )
		{
			coreMemoryMap.revalidate();
			coreMemoryMap.repaint();
		}
		
		@Override
		public void intervalAdded( ListDataEvent e )
		{
			coreMemoryMap.revalidate();
			coreMemoryMap.repaint();
		}
		
		@Override
		public void contentsChanged( ListDataEvent e )
		{
			coreMemoryMap.revalidate();
			coreMemoryMap.repaint();
		}
	};

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void paint( Graphics g, JComponent c )
	{
		super.paint( g, c );

		renderMemoryMap( g );
	}

	private void renderMemoryMap( Graphics g )
	{
		if( (coreMemoryMap == null) || (coreMemoryMapModel == null) )
		{
			return;
		}

		int mapSize = coreMemoryMapModel.getSize();

		float [] weights = coreMemoryMapModel.getRangeWeights();
		float [] normalizedWeights = normalizeRangeWeights( weights );

		// --------------------------------------------------------------------

		Rectangle2D rectRangeName = normalBoldFont.getStringBounds( coreMemoryMapModel.getMapName(), fontRendererContex );
		mapY = (int) rectRangeName.getHeight();
		
		g.setFont( normalBoldFont );
		g.drawString( coreMemoryMapModel.getMapName(), mapX, mapY - 2 );
		
		g.setColor( Color.BLACK );

		g.setColor( Color.WHITE );
		g.fillRect( mapX,  mapY, mapW, mapH );
		
		g.setColor( Color.BLACK );
		g.drawRect( mapX,  mapY, mapW, mapH );

		String mapLimit = String.format( "0x%05X", coreMemoryMapModel.getMapLimit() );
		g.setFont( smallFont );
		g.setColor( Color.DARK_GRAY );
		g.drawString( mapLimit, mapX + mapW + 5	, mapY + mapH );

		int lastY = mapY;
		int y = mapY;

		for( int i = 0 ; i < mapSize ; i ++ )
		{
			CoreMemoryRange range = coreMemoryMapModel.getElementAt( i );
			
			y += (mapH * normalizedWeights[i]);

			if( (i + 1) < mapSize ) // not for last line
			{
				g.setColor( Color.BLACK );
				g.drawLine( mapX, y, mapX+mapW, y );
			}

			// ----------------------------------------------------------------

			String rangeBase = String.format( "0x%05X", range.getRangeBase() );
			Rectangle2D rectBase = smallFont.getStringBounds( rangeBase, fontRendererContex );
			int baseX = mapX + mapW + 5;
			int baseY = lastY + ((int)rectBase.getHeight() / 2);

			g.setFont( smallFont );
			g.setColor( Color.DARK_GRAY );
			g.drawString( rangeBase, baseX, baseY );

			// ----------------------------------------------------------------

			Rectangle2D rectName = smallFont.getStringBounds( range.getRangeName(), fontRendererContex );
			int nameX = mapX + (((mapW - mapX) - ((int)rectName.getWidth())) / 2);
			int nameY = lastY + ((y - lastY) / 2) + ((int)rectBase.getHeight() / 2);

			g.setFont( normalFont );
			g.setColor( Color.BLACK );
			g.drawString( range.getRangeName(), nameX, nameY );

			lastY = y;
		}

	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 * @param weights
	 * @return
	 */
	private float[] normalizeRangeWeights( float [] weights )
	{
		float [] normalizedWeights = new float [ weights.length ];

		float reduc = 0.0f;
		for( int i = 0 ; i < weights.length ; i++ )
		{
			if( weights[i] < 0.1f )
			{
				normalizedWeights[i] = 0.1f;
				reduc += (0.1f - weights[i]);
			}
		}

		for( int i = 0 ; i < weights.length ; i++ )
		{
			if( normalizedWeights[i] == 0.0f )
			{
				normalizedWeights[i] = weights[i] * (1.0f - reduc);
			}
		}

		return normalizedWeights;
	}
}
