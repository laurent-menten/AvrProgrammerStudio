package be.lmenten.avr.core.tmp;

import java.util.Properties;
import java.util.function.BiConsumer;

import be.lmenten.avr.core.descriptor.CoreFeatures;
import be.lmenten.avr.core.utils.CoreFamily;
import be.lmenten.avr.core.utils.CoreIORegisters;
import be.lmenten.avr.core.utils.CoreInterrupts;
import be.lmenten.avr.core.utils.CoreFuses;

public interface CoreDescriptor
{

	// ========================================================================
	// === 
	// ========================================================================

	public String getId();

	public String getName();
	public String getFullName();
	public String getDescription();

	public CoreFamily getCoreFamily();

	// ------------------------------------------------------------------------

	public CoreFeatures [] getFeaturesList();
	public boolean hasFeature( CoreFeatures feature );

	public void importFusesValues( BiConsumer<CoreFuses,Boolean> consumer );

	public CoreInterrupts [] getInterruptsTable();
	public boolean hasInterrupt( CoreInterrupts interrupt );

	/**
	 * Returns the vector number of the interrupt.
	 * 
	 * @param interrupt
	 * @return
	 */
	public int getInterruptVector( CoreInterrupts interrupt );

	public CoreIORegisters [] getIORegistersMap();
	public boolean hasIORegister( CoreIORegisters register );

	/**
	 * Returns the physical address of the IO register.
	 * 
	 * @param register
	 * @return
	 */
	public int getIORegisterOffset( CoreIORegisters register );

	// ------------------------------------------------------------------------

	public int getProgramCounterWidth();
	public int getInterruptVectorSize();

	public int getProgramMemorySize();

	public int getGeneralResistersCount();
	public int getIOResistersCount();
	public int getExtendedIOResistersCount();
	public int getInternalDataMemorySize();

	public int getMaxExternaldDataMemorySize();

	public int getEepromSize();

	// ========================================================================
	// === MEMORY UTILITIES ===================================================
	// ========================================================================

	default public int getOnChipDataMemorySize()
	{
		return		getGeneralResistersCount()
				+	getIOResistersCount()
				+	getExtendedIOResistersCount()
				+	getInternalDataMemorySize()
				;
	}

	// ------------------------------------------------------------------------

	default public int getGeneralRegistersBaseOffset()
	{
		return 0x0000;
	}

	default public int getIORegistersBaseOffset()
	{
		return		getGeneralRegistersBaseOffset()
				+	getGeneralResistersCount()
				;
	}

	default public int getExtendedIORegistersBaseOffset()
	{
		return		getGeneralRegistersBaseOffset()
				+	getGeneralResistersCount()
				+	getIOResistersCount()
				;
	}

	default public int getInternalDataMemoryBaseOffset()
	{
		return		getGeneralRegistersBaseOffset()
				+	getGeneralResistersCount()
				+	getIOResistersCount()
				+	getExtendedIOResistersCount()
				;
	}

	default public int getInternalDataMemoryTopOffset()
	{
		return		getGeneralRegistersBaseOffset()
				+	getGeneralResistersCount()
				+	getIOResistersCount()
				+	getExtendedIOResistersCount()
				+	getInternalDataMemorySize()
				-	1
				;
	}

	// ------------------------------------------------------------------------

	default public int getExternalDataMemoryBaseOffset()
	{
		return		getGeneralRegistersBaseOffset()
				+	getGeneralResistersCount()
				+	getIOResistersCount()
				+	getExtendedIOResistersCount()
				+	getInternalDataMemorySize()
				;
	}

	// ========================================================================
	// === CORE INSTANCE ====================================================== 
	// ========================================================================

	default public Core getCoreInstance()
	{
		return getCoreInstance( null );
	}

	public Core getCoreInstance( Properties config );
}
