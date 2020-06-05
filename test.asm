/**
 * Test program for external source assembler
 */

	.org	SRAM_START + (2* 0x10)

   .set     temp2 = 1
   .set     temp2 = temp2 + 1
   .set     temp2 = temp2 + 2
   .set     temp2 = temp2 + 3

	.cseg
start:
	cli
	ldi		r17, high( RAMEND )
	ldi		r16, low( RAMEND )
	out		SPH, r17						// ioaddress( SPH )

	movw	r0:r1, r16:r17
	nop		cli								; do nothin

	in		r16, SREG
	andi	r16, ~( mask SREG[ I, T ] )
	sbr		r16, mask SREG.C
	out		SREG, r16

.exit:
	rjmp	$
