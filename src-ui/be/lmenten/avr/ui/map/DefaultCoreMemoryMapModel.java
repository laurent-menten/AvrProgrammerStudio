package be.lmenten.avr.ui.map;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.descriptor.CoreDescriptor;
import be.lmenten.avr.core.descriptor.CoreMemory;
import be.lmenten.avr.core.descriptor.CoreMemoryRange;

public class DefaultCoreMemoryMapModel
	extends AbstractCoreMemoryMapModel
{
	private static final long serialVersionUID = 1L;

	// ========================================================================
	// ===
	// ========================================================================

	public DefaultCoreMemoryMapModel() 
	{
		super( "empty" );
	}

	public DefaultCoreMemoryMapModel( CoreMemory memory, CoreDescriptor coreDescriptor )
	{
		super( memory.getName() );

		switch( memory )
		{
			// ----------------------------------------------------------------
			// ---
			// ----------------------------------------------------------------

			case FLASH:
			{
				CoreMemoryRange range;

				range = coreDescriptor.getMemoryRange( memory, "flash" );
				add( range );
				break;
			}

			// ----------------------------------------------------------------
			// ---
			// ----------------------------------------------------------------

			case SRAM:
			{
				CoreMemoryRange range;

				range = coreDescriptor.getMemoryRange( memory, "registers" );
				add( range );

				range = coreDescriptor.getMemoryRange( memory, "io" );
				add( range );

				range = coreDescriptor.getMemoryRange( memory, "extended_io" );
				add( range );

				range = coreDescriptor.getMemoryRange( memory, "sram" );
				add( range );

				break;
			}

			// ----------------------------------------------------------------
			// ---
			// ----------------------------------------------------------------

			case EEPROM:
			{
				CoreMemoryRange range;

				range = coreDescriptor.getMemoryRange( memory, "eeprom" );
				add( range );
				break;
			}
		}
	}

	public DefaultCoreMemoryMapModel( CoreMemory memory, Core core )
	{
		this( memory, core.getDescriptor() );

		switch( memory )
		{

			// ----------------------------------------------------------------
			// ---
			// ----------------------------------------------------------------

			case FLASH:
			{
				CoreMemoryRange range;

				if( core.supportsBootLoaderSection() )
				{
					remove( getElementAt( 0 ) );

					range = new CoreMemoryRange( memory, "application",
							core.getApplicationSectionBase() / 2,
							core.getApplicationSectionSize() / 2 );

					range = new CoreMemoryRange( memory, "bls",
							core.getBootLoaderSectionBase() / 2,
							core.getBootLoaderSectionSize() / 2 );
					add( range );
				}

				break;
			}

			// ----------------------------------------------------------------
			// ---
			// ----------------------------------------------------------------

			case SRAM:
			{
				if( core.supportsExternalMemoryFeature() && (core.getExternalSramSize() > 0) )
				{
					CoreDescriptor coreDescriptor = core.getDescriptor();
					CoreMemoryRange range;

					range = new CoreMemoryRange( memory, "ext.sram",
							coreDescriptor.getExternalSramBase(),
							core.getExternalSramSize() );
					add( range );

					break;
				}
			}

			// ----------------------------------------------------------------
			// ---
			// ----------------------------------------------------------------

			case EEPROM:
			{
				break;
			}
		}
	}
}
