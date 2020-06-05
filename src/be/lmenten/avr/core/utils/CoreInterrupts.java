package be.lmenten.avr.core.utils;

public enum CoreInterrupts
{
									//	328	2560
	RESET,							//	0	0
	INT0,							//	1	1
	INT1,							//	2	2
	INT2,							//		3
	INT3,							//		4
	INT4,							//		5
	INT5,							//		6
	INT6,							//		7
	INT7,							//		8
	PCINT0,							//	3	9
	PCINT1,							//	4	10
	PCINT2,							//	5	11
	WDT,							//	6	12
	TIMER2_COMPA,					//	7	13
	TIMER2_COMPB,					//	8	14
	TIMER2_OVF,						//	9	15
	TIMER1_CAPT,					//	10	16
	TIMER1_COMPA,					//	11	17
	TIMER1_COMPB,					//	12	18
	TIMER1_COMPC,					//		19
	TIMER1_OVF,						//	13	20
	TIMER0_COMPA,					//	14	21
	TIMER0_COMPB,					//	15	22
	TIMER0_OVF,						//	16	23
	SPI,							//	17	24			 STC ?
	USART0_RX,						//	18	25
	USART0_UDRE,					//	19	26
	USART0_TX,						//	20	27
	ANALOG_COMP,					//	23!	28
	ADC,							//	21	29
	EE_READY,						//	22	30
	TIMER3_CAPT,					//		31
	TIMER3_COMPA,					//		32
	TIMER3_COMPB,					//		33
	TIMER3_COMPC,					//		34
	TIMER3_OVF,						//		35
	USART1_RX,						//		36
	USART1_UDRE,					//		37
	USART1_TX,						//		38
	TWI,							//	24	39
	SPM_READY,						//	25	40
	TIMER4_CAPT,					//		41
	TIMER4_COMPA,					//		42
	TIMER4_COMPB,					//		43
	TIMER4_COMPC,					//		44
	TIMER4_OVF,						//		45
	TIMER5_CAPT,					//		46
	TIMER5_COMPA,					//		47
	TIMER5_COMPB,					//		48
	TIMER5_COMPC,					//		49
	TIMER5_OVF,						//		50
	USART2_RX,						//		51
	USART2_UDRE,					//		52
	USART2_TX,						//		53
	USART3_RX,						//		54
	USART3_UDRE,					//		55
	USART3_TX,						//		56
	;
}
