package be.lmenten.avr.core;

import be.lmenten.avr.core.descriptor.CoreRegisterDescriptor;

public class CoreStatusRegister
	extends CoreRegister
{
	// ========================================================================
	// ===
	// ========================================================================

	public CoreStatusRegister( CoreRegisterDescriptor rdesc )
	{
		super( rdesc );
	}

	// ========================================================================
	// ===
	// ========================================================================

	public boolean c() { return bit( 0 ); }
	public boolean z() { return bit( 1 ); }
	public boolean n() { return bit( 2 ); }
	public boolean v() { return bit( 3 ); }
	public boolean s() { return bit( 4 ); }
	public boolean h() { return bit( 5 ); }
	public boolean t() { return bit( 6 ); }
	public boolean i() { return bit( 7 ); }

	public void c( boolean state ) { bit( 0, state ); }
	public void z( boolean state ) { bit( 1, state ); }
	public void n( boolean state ) { bit( 2, state ); }
	public void v( boolean state ) { bit( 3, state ); }
	public void s( boolean state ) { bit( 4, state ); }
	public void h( boolean state ) { bit( 5, state ); }
	public void t( boolean state ) { bit( 6, state ); }
	public void i( boolean state ) { bit( 7, state ); }
}
