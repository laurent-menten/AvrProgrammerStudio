package be.lmenten.avr.ui.map;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import be.lmenten.avr.core.descriptor.CoreMemoryRange;

public abstract class AbstractCoreMemoryMapModel
	extends AbstractListModel<CoreMemoryRange>
	implements CoreMemoryMapModel
{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	private String mapName;

	private final List<CoreMemoryRange> map
		= new ArrayList<CoreMemoryRange>();;

	// ========================================================================
	// ===
	// ========================================================================

	public AbstractCoreMemoryMapModel()
	{
		this( null, null );
	}

	public AbstractCoreMemoryMapModel( String mapName )
	{
		this( mapName, null );
	}


	public AbstractCoreMemoryMapModel( String mapName, List<CoreMemoryRange> map )
	{
		this.mapName = mapName;

		if( map != null )
		{
			this.map.addAll( map );
		}
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public int getSize()
	{
		return map.size();
	}

	@Override
	public CoreMemoryRange getElementAt( int index )
	{
		return map.get( index );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public void add( CoreMemoryRange range )
	{
		if( ! map.contains( range ) )
		{
			map.add( range );

			fireContentsChanged( this, 0, map.size() );
		}
	}

	public void remove( CoreMemoryRange range )
	{
		if( map.remove( range ) )
		{
			fireContentsChanged( this, 0, map.size() );
		}
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void setMapName( String mapName )
	{
		this.mapName = mapName;
	}
	
	@Override
	public String getMapName()
	{
		return mapName;
	}

	@Override
	public int getMapBase()
	{
		int minBase = 0;

		for( CoreMemoryRange range : map )
		{
			if( range.getRangeBase() < minBase )
			{
				minBase = range.getRangeBase();
			}
		}

		return minBase;
	}

	@Override
	public int getMapSize()
	{
		return getMapLimit() - getMapBase();
	}

	@Override
	public int getMapLimit()
	{
		int minLimit = 0;

		for( CoreMemoryRange range : map )
		{
			if( range.getRangeLimit() > minLimit )
			{
				minLimit = range.getRangeLimit();
			}
		}

		return minLimit;
	}

	// ------------------------------------------------------------------------

	@Override
	public float [] getRangeWeights()
	{
		float [] weights = new float[ getSize() ];

		float mapSize = getMapSize();
		for( int i = 0  ; i < map.size() ; i++ )
		{
			CoreMemoryRange range = map.get( i );
			float rangeSize = range.getRangeSize();

			weights[i] = rangeSize / mapSize;
		}
		
		return weights;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( getMapName() )
		 .append( '\n' )
		 ;

		float [] weights = getRangeWeights();

		for( int i = 0 ; i < getSize() ; i++ )
		{
			CoreMemoryRange range = getElementAt( i );

			s.append( String.format( "  %d. ", i ) )
			 .append( String.format( "%8s ", range.getRangeName() ) )
			 .append( String.format( "%05x-%05x ", range.getRangeBase(), range.getRangeLimit() ) )
			 .append( String.format( "(%d) ", range.getRangeSize() ) )
			 .append( String.format( "%2.2f%%", weights[i] ) )
			 .append( '\n' )
			 ;
		}

		return s.toString();
	}
}
