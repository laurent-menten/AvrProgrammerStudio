package be.lmenten.avr.core.tmp;

import java.util.function.Consumer;

import be.lmenten.avr.core.RunningMode;
import be.lmenten.avr.core.descriptor.CoreFeatures;
import be.lmenten.avr.core.instructions.Instruction;
import be.lmenten.avr.core.ResetSources;
import be.lmenten.avr.core.ui.CoreUI;
import be.lmenten.avr.core.utils.CoreFuses;

public interface Core
	extends CoreComponent
{
	public static final String CONFIG_DEBUG = "core.config.debug";

	public static final String CONFIG_EXTERNAL_MEMORY_SIZE = "core.config.external.memory";

	// ========================================================================
	// ===
	// ========================================================================

	public String getId();
	public CoreDescriptor getCoreDescriptor();
	public CoreUI getCoreUI();

	// ------------------------------------------------------------------------

	public void registerCoreComponent( CoreComponent coreComponent );

	public void registerIORegisterListener( int address, IORegisterListener listener );
	public void registerDebugListener( DebugListener listener );

	// ========================================================================
	// === Features ===========================================================
	// ========================================================================

	public boolean hasFeature( CoreFeatures feature );

	// ========================================================================
	// === Fuses ==============================================================
	// ========================================================================

	/**
	 * 
	 * @param fuse
	 */
	public void programFuse( CoreFuses fuse );

	/**
	 * 
	 * @param fuse
	 */
	public void unprogramFuse( CoreFuses fuse );

	/**
	 * 
	 * @param fuse
	 * @return
	 */
	public boolean getFuseState( CoreFuses fuse );

	// ========================================================================
	// === Boot Loader Section ================================================
	// ========================================================================

	/**
	 * Get the application section offset within flash memory. This address
	 * is usualy equal to 0x0000.
	 * 
	 * @return
	 */
	public int getApplicationSectionOffset();

	/**
	 * Get the size of the application section of the flash memory. If the BLS
	 * is not supported, the full flash memory size is returned.
	 * 
	 * @return
	 */
	public int getApplicationSectionSize();

	// ------------------------------------------------------------------------

	/**
	 * Check if the given address resides in the BLS.
	 * 
	 * @param address
	 * @return
	 */
	public boolean isInBootLoaderSection( int address );	// BOOT_LOADER

	/**
	 * If the BLS is supported by this core, get the BLS offset within flash
	 * memory. This offset is computed from the BOOTSZ fuses.
	 * 
	 * @return
	 */
	public int getBootLoaderSectionOffset();				// BOOT_LOADER

	/**
	 * If the BLS is supported by this core, get the BLS size. This size
	 * returned as a word count and is computed from the BOOTSZ fuses.
	 * 
	 * @return
	 */
	public int getBootLoaderSectionSize();					// BOOT_LOADER

	// ========================================================================
	// === Run mode ===========================================================
	// ========================================================================

	/**
	 * 
	 * @param mode
	 */
	public void setCoreMode( RunningMode mode );

	/**
	 * 
	 * @return
	 */
	public RunningMode getCoreMode();

	// ------------------------------------------------------------------------

	public Instruction getCurrentInstruction();
	public Instruction getFollowingInstruction();

	public void reset( ResetSources source );
	public void step();

	// ========================================================================
	// === MCU CONTROL ========================================================
	// ========================================================================

	public int getProgramCounter();
	public void setProgramCounter( int address );
	public void updateProgramCounter( int offset );

	public void interrupt( int vector );

	public void queueInterrupt( int level );

	public void pushProgramCounter();
	public void popProgramCounter();

	// ------------------------------------------------------------------------

	public void setStackPointer( int address );
	public int getStackPointer();
	public void push( int value );
	public int pop();

	// ------------------------------------------------------------------------

	public long getClockCounter();
	public void setClockCounter( long value );
	public void updateClockCounter( long increment );

	// ------------------------------------------------------------------------
	// --- STATUS REGISTER HELPERS --------------------------------------------
	// ------------------------------------------------------------------------

	public boolean c();			public void c( boolean c );
	public boolean z();			public void z( boolean z );
	public boolean n();			public void n( boolean n );
	public boolean v();			public void v( boolean v );
	public boolean s();			public void s( boolean s );
	public boolean h();			public void h( boolean h );
	public boolean t();			public void t( boolean t );
	public boolean i();			public void i( boolean i );

	// ========================================================================
	// === MEMORY ACCESS ======================================================
	// ========================================================================

	public void initProgramMemory( Consumer<Instruction []> loader );
	public int readProgramMemory( int address );
	public int writeProgramMemory( int address, int value );

	public byte readDataMemory( int address );
	public byte writeDataMemory( int address, byte value );

	public byte readEeprom( int address );
	public byte writeEeprom( int address, byte value );

	// ========================================================================
	// === Utilities ==========================================================
	// ========================================================================

	public default boolean isGeneralRegisterAddress( int registerAddress )
	{
		int tmp = registerAddress - getCoreDescriptor().getGeneralRegistersBaseOffset();

		return (tmp >= 0)
				&& (tmp < getCoreDescriptor().getGeneralResistersCount());
	}
	

	// ------------------------------------------------------------------------

	public default boolean isIOPortAddress( int physicalAddress )
	{
		int tmp = physicalAddress - getCoreDescriptor().getIORegistersBaseOffset();

		return (tmp >= 0)
				&& (tmp < getCoreDescriptor().getIOResistersCount());
	}

	public default boolean isExtendedIOPortAddress( int physicalAddress )
	{
		int tmp = physicalAddress - getCoreDescriptor().getExtendedIORegistersBaseOffset();

		return (tmp >= 0)
				&& (tmp < getCoreDescriptor().getExtendedIOResistersCount());
	}

	public default boolean isAnyIOPortAddress( int physicalAddress )
	{
		return isIOPortAddress( physicalAddress )
				&& isExtendedIOPortAddress( physicalAddress );
	}
}
