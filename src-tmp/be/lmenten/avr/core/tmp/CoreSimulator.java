package be.lmenten.avr.core.tmp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import be.lmenten.avr.core.instructions.InstructionSet;

/**
 * <pre>
 * 	CoreSimulator sim = new CoreSimulator();
 *	CoreDescriptor desc = sim.getCoreDescriptor( ATmega328.ID );
 *	Core core = desc.getInstance();
 * </pre>
 * 
 * @since 1.0
 * @author <A HREF="mailto:laurent.menten@gmail.com">Laurent Menten</A>
 */
public class CoreSimulator
{
	private final ServiceLoader<CoreDescriptor> coreDescriptorsLoader;

	// WARNING : The descriptorsList is directly referenced by the AbstractCore
	// class constructor to retrieve its own descriptor.
	
	/*package*/ static final Map<String,CoreDescriptor> descriptorsList
		= new HashMap<>();

	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	public CoreSimulator()
	{
//		InstructionSet.init();

		coreDescriptorsLoader = ServiceLoader.load( CoreDescriptor.class );
		for( CoreDescriptor coreDescriptor : coreDescriptorsLoader )
		{
			registerCoreDescriptor( coreDescriptor );
		}
	}
	
	// ========================================================================
	// === CORE DESCRIPTORS MANAGEMENT ========================================
	// ========================================================================

	/**
	 * Register a {@link CoreDescriptor} instance to the core simulator.
	 * 
	 * @param coreDescriptor
	 */
	public void registerCoreDescriptor( CoreDescriptor coreDescriptor )
	{
		if( ! descriptorsList.containsKey(  coreDescriptor.getId() ) )
		{
			descriptorsList.put( coreDescriptor.getId(), coreDescriptor );
		}
	}

	/**
	 * Get the {@link CoreDescriptor} by its identifier.
	 * 
	 * @param coreId
	 * @return
	 */
	public CoreDescriptor getCoreDescriptor( String coreId )
	{		
		return descriptorsList.get( coreId );
	}

	/**
	 * Get a list of all {@link CoreDescriptor} registered to the core
	 * simulator.
	 * 
	 * @return
	 */
	public Collection<CoreDescriptor> getCoreDescriptorsList()
	{
		return descriptorsList.values();
	}
}
