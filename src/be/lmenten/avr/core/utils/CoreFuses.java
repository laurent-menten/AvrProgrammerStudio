package be.lmenten.avr.core.utils;

public enum CoreFuses
{
	// --- FUSE LOW BYTE ------------------------------------------------------

	CKSEL0		( Fuses.LOW, 1, 0 ),			// 328	2560
	CKSEL1		( Fuses.LOW, 1, 1 ),			// "
	CKSEL2		( Fuses.LOW, 1, 2 ),			// "
	CKSEL3		( Fuses.LOW, 1, 3 ),			// "

	SUT0		( Fuses.LOW, 2, 4 ),			// 328	2560
	SUT1		( Fuses.LOW, 2, 5 ),			// "

	CKOUT		( Fuses.LOW,    6 ),			// 328	2560

	CKDIV8		( Fuses.LOW,    7 ),			// 328	2560

	// --- FUSE HIGH BYTE -----------------------------------------------------

	BOOTRST		( Fuses.HIGH,    0 ),		// 328	2560

	BOOTSZ0		( Fuses.HIGH, 1, 1 ),		// 328	2560
	BOOTSZ1		( Fuses.HIGH, 1, 2 ),		// "

	EESAVE		( Fuses.HIGH,    3 ),		// 328	2560

	WDTON		( Fuses.HIGH,    4 ),		// 328	2560

	SPIEN		( Fuses.HIGH,    5 ),		// 328	2560

	DWEN		( Fuses.HIGH,    6 ),		// 328
	JTAGEN		( Fuses.HIGH,    6 ), 		//    	2560

	RSTDISBL	( Fuses.HIGH,    7 ),		// 328
	OCDEN		( Fuses.HIGH,    7 ),		//    	2560

	// --- EXTENDED FUSE BYTE -------------------------------------------------

	BODLEVEL0	( Fuses.EXTENDED, 1, 0 ),	// 328	2560
	BODLEVEL1	( Fuses.EXTENDED, 1, 1 ),	// "
	BODLEVEL2	( Fuses.EXTENDED, 1, 2 ),	// "
	;

	// ------------------------------------------------------------------------

	public static final boolean PROGRAMMED = false;
	public static final boolean UNPROGRAMMED = true;

	// ------------------------------------------------------------------------

	private final Fuses fuse;
	private final int group;
	private final int bit;

	// ========================================================================
	// === CONSTRUCTOR(S) =====================================================
	// ========================================================================

	private CoreFuses( Fuses fuse, int bit )
	{
		this.fuse = fuse;
		this.group = 0;
		this.bit = bit;
	}

	private CoreFuses( Fuses fuse, int group, int bit )
	{
		this.fuse = fuse;
		this.group = group;
		this.bit = bit;
	}

	// ========================================================================
	// ===
	// ========================================================================

	public Fuses getFuse()
	{
		return fuse;
	}

	public int getGroup()
	{
		return group;
	}

	public int getBit()
	{
		return bit;
	}
}
