package be.lmenten.avr.project;

import be.lmenten.avr.core.descriptor.CoreDescriptor;

public class AvrInternalSource
	extends AvrSource
{
    // ========================================================================
	// ===
	// ========================================================================

	private static final String [] program = 
	{
			"	.org	SRAM_START + (2* 0x10)",			// 1

			"	.cseg",
			"start:",
			"	ldi		r16, __LINE__",						// 4
			"	cli",										// 5
			"x:",
			"	ldi		r17, high( RAMEND )",
			"y:",
			"	ldi		r16, low( RAMEND )",
			"z:",											// 10
			"	out		address SPL - 0x20, r16",
			"	out		SPH, r17",

			"	in		r16, SREG",
			"	andi	r16, ~( mask SREG[ I, T ] )",
			"	sbr		r16, mask SREG.C",					// 15
			"	out		SREG, r16",

			"	ldi		r16, __LINE__",						// 17

			".exit:",
			"	rjmp	$",									// 19


			"	.dseg",
			"data1:",
/*			"	.db 1",
			"	.dw 1,1",
			"	.dd 1,1,1",
			"	.dq 1,1,1,1",*/
			"data2:",
			"	.rs 1",
			"data3:",
			""
	};

    // ========================================================================
	// === CONSTRUTOR(s) ======================================================
	// ========================================================================

	public AvrInternalSource( String fileName, CoreDescriptor cdesc )
	{
		super( fileName, cdesc );
	}

    // ========================================================================
	// ===
    // ========================================================================

	public int getSourceLineCount()
	{
		return program.length;
	}

	public String getSourceLine( int line )
	{
		if( line < getSourceLineCount() )
		{
			return program[ line ];
		}

		return null;
	}
}
