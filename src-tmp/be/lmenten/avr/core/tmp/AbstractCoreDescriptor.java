package be.lmenten.avr.core.tmp;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import be.lmenten.avr.core.utils.CoreIORegisters;
import be.lmenten.avr.core.utils.CoreInterrupts;
import be.lmenten.avr.core.descriptor.CoreFeatures;
import be.lmenten.avr.core.utils.CoreFuses;

public abstract class AbstractCoreDescriptor
	implements CoreDescriptor
{
	protected final Set<CoreFeatures> coreFeaturesList =
		EnumSet.noneOf( CoreFeatures.class );

//	protected final Set<LockBits> lockBitsList =
//		EnumSet.noneOf( LockBits.class );

	protected final Map<CoreFuses,Boolean> lockBitsList =
			new HashMap<>();

	protected final List<CoreInterrupts> coreInterrupts =
		new ArrayList<>();

	protected final List<CoreIORegisters> coreIOPorts =
		new ArrayList<>();

	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	protected AbstractCoreDescriptor()
	{
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public CoreFeatures [] getFeaturesList()
	{
		return coreFeaturesList.toArray( new CoreFeatures [ 0 ] );
	}

	@Override
	public boolean hasFeature( CoreFeatures feature )
	{
		return coreFeaturesList.contains( feature );
	}

	// ------------------------------------------------------------------------

	@Override
	public void importFusesValues( BiConsumer<CoreFuses,Boolean> consumer )
	{
		for( Map.Entry<CoreFuses,Boolean> lockBit : lockBitsList.entrySet() )
		{
			consumer.accept( lockBit.getKey(), lockBit.getValue() );
		}
	}

	// ------------------------------------------------------------------------

	@Override
	public CoreInterrupts [] getInterruptsTable()
	{
		return coreInterrupts.toArray( new CoreInterrupts [0] );
	}

	@Override
	public boolean hasInterrupt( CoreInterrupts interrupt )
	{
		return coreInterrupts.contains( interrupt );
	}

	@Override
	public int getInterruptVector( CoreInterrupts interrupt )
	{
		return coreInterrupts.indexOf( interrupt );
	}

	// ------------------------------------------------------------------------

	@Override
	public CoreIORegisters[] getIORegistersMap()
	{
		return coreIOPorts.toArray( new CoreIORegisters [0] );
	}

	@Override
	public boolean hasIORegister( CoreIORegisters register )
	{
		return coreIOPorts.contains( register );
	}

	@Override
	public int getIORegisterOffset( CoreIORegisters register )
	{
		return coreIOPorts.indexOf( register ) + getIORegistersBaseOffset();
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( "+ --- " )
		 .append( getId() )
		 .append( " = " )
		 .append( getFullName() )
		 .append( '\n' );

		s.append( "|                   Features : " );
		for( CoreFeatures f : getFeaturesList() )
		{
			s.append( f )
			 .append( ' ' );
		}
		s.append( '\n' );

		s.append( "|                  Lock bits : " );
		for( CoreFuses b : lockBitsList.keySet() )
		{
			s.append( b )
			 .append( ' ' );
		}
		s.append( '\n' );

		s.append( "|          General Registers : " )
		 .append( getGeneralResistersCount() )
		 .append( '\n' );

		s.append( "|               IO registers : " )
		 .append( getIOResistersCount() )
		 .append( '\n' );

		s.append( "|      Extended IO registers : " )
		 .append( getExtendedIOResistersCount() )
		 .append( '\n' );

		s.append( "|       Internal data memory : " )
		 .append( getInternalDataMemorySize() )
		 .append( '\n' );

		s.append( "|        On-chip data memory : " )
		 .append( getOnChipDataMemorySize() )
		 .append( '\n' );

		s.append( "| Top of on-chip data memory : " )
		 .append( String.format( "%04x", getInternalDataMemoryTopOffset() ) )
		 .append( '\n' );

		if( hasFeature( CoreFeatures.EXTERNAL_SRAM ) )
		{
			s.append( "|       Off-chip data memory : " )
			 .append( getMaxExternaldDataMemorySize() )
			 .append( " (max)\n" );
		}
		
		return s.toString();
	}
}
