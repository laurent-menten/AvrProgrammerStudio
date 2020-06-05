package be.lmenten.avr.core;

import be.lmenten.avr.assembler.AvrRegister;
import be.lmenten.avr.core.descriptor.CoreDescriptor;
import be.lmenten.avr.core.descriptor.CoreMemory;
import be.lmenten.avr.core.instructions.Instruction;

public interface CoreModel
{
	// ========================================================================
	// ===
	// ========================================================================

	public boolean ioDebugRegisterDirty();
	public void ioDebugRegisterDirty( boolean ioDebugRegisterDirty );

	// ========================================================================
	// ===
	// ========================================================================

	public CoreDescriptor getDescriptor();

	// ========================================================================
	// ===
	// ========================================================================

	public boolean supportsBootLoaderSection();
	
	public int getApplicationSectionBase();
	public int getApplicationSectionSize();

	public int getBootLoaderSectionBase();
	public int getBootLoaderSectionSize();

	// ------------------------------------------------------------------------

	public boolean supportsExternalMemoryFeature();

	public int getExternalSramSize();

	// ========================================================================
	// ===
	// ========================================================================

	public int getProgramCounter();
	public long getClockCyclesCounter();

	public Instruction getCurrentInstruction();
	public Instruction getFollowingInstruction();

	public RunningMode getCoreMode();
	
	public void step();
	public void interrupt( int vector );
	public void reset( ResetSources resetSource );

	// ========================================================================
	// ===
	// ========================================================================

	public CoreRegister getGeneralRegister( AvrRegister reg );
	public CoreRegister getIORegister( int address );
	public CoreRegister getIORegisterByPhysicalAddress( int address );
	public CoreRegister getRegisterByName( String name );

	public CoreRegister getFuseByte( String name );
	public CoreRegister getLockBits( String name );

	// ========================================================================
	// ===
	// ========================================================================

	public Instruction getFlashCell( int address );
	public CoreData getSramCell( int address );
	public CoreData getEepromCell( int address );

	public String getSymbol( CoreMemory memory, int address );
}
