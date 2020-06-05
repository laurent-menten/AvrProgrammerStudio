package be.lmenten.avr.core.instructions;

public enum OperandType
{
	r,	// source
	d,	// destination
	K,	// constant data
	k,	// constant address
	q,	// displacement
	A,	// IO address
	s,	// bit in SREG
	b,	// bit in register or IO register
	;

	public static OperandType lookup( char c )
	{
		return OperandType.valueOf( "" + c );
	}
}
