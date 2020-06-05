Abstract Syntax Tree

	expr
		=	{integer}	integer
		|	{float}		float
//		|	{bit}		bit	
//		|	{bits}		bits

		|	{par}		expr

		|	{add}		[left] :expr	[right] :expr
		|	{sub}		[left] :expr	[right] :expr
		|	{and}		[left] :expr	[right] :expr

		|	{mul}		[left] :expr	[right] :expr
		|	{div}		[left] :expr	[right] :expr
		|	{mod}		[left] :expr	[right] :expr
		|	{or}		[left] :expr	[right] :expr
		|	{eor}		[left] :expr	[right] :expr
		|	{lshift}	[left] :expr	[right] :expr
		|	{rshift}	[left] :expr	[right] :expr

		|	{abs}		expr
		|	{byte2}		expr
		|	{byte3}		expr
		|	{byte4}		expr
//		|	{defined}
		|	{exp2}		expr
		|	{frac}		expr
		|	{high}		expr
		|	{hwrd}		expr
		|	{index}		bit
		|	{int}		expr
		|	{log2}		expr
		|	{low}		expr
		|	{lwrd}		expr
		|	{mask}		bits
		|	{not}		expr
//		|	{offset}	expr
		|	{page}		expr
		|	{q7}		expr
		|	{q15}		expr	
//		|	{strlen}
		;

	integer = integer;
	float = float;
	bit	= bit;
	bits = bits;












CoreDesciptor
Core


CoreMemoryCell
	CoreData
		CoreRegister
	Instruction
		...










.
LDS16	1010 0kkk dddd kkkk
STS16	1010 1kkk dddd kkkk
STD Y+q	10q0 qq1d dddd 1qqq
STD Z+q 10q0 qq1d dddd 0qqq
LDD Y+q	10q0 qq0d dddd 1qqq
LDD Z+q 10q0 qq0d dddd 0qqq
              .   
LDS r16->r31, 0b1011111
LDD r0->r15, Y+ 0b111111






AbstractInstructionRdRr
	0000 00rd dddd rrrr								Rd_Rr			R0..31
AbstractInstructionRdRr4
	0000 0000 dddd rrrr								Rd4_Rr4			R16..31
AbstractInstructionRdRr3
	0000 0000 0ddd 0rrr								Rd3_Rr3			R16..23

+AbstractInstructionRx
	0000 000d dddd 0000								Rd, Rr			R0..31
	-> AbstractInstructionRxB
		0000 000d dddd 0bbb							Rd_b, Rr_b		R0..31 0..7
	-> AbstractInstructionRxA
		0000 0AAd dddd AAAA							Rd_A6, A6_Rr, 	R0..31 0..63
	-> AbstractInstructionRxQ
		00q0 qq0d dddd 0qqq							Rd_q, Rr_q 		R0..31 0..63	
	-> AbstractInstructionRxK
		0000 000d dddd 0000 kkkk kkkk kkkk kkkk		Rd_k16, Rr_k16	R0..31, 

+AbstractInstructionRx4
	0000 0000 dddd 0000								Rd, Rr			R16..31
	-> AbstractInstructionRdK
		0000 0kkk dddd kkkk							Rd4_k7			R16..31
		0000 KKKK dddd KKKK							Rd4_K8			R16..31

AbstractInstructionRx2K
		0000 0000 KKdd KKKK							Rd2_K6			R24..30 (W)

0000 00kk kkkk ksss									k7_s		
0000 0000 AAAA Abbb									A5_b		0..31 0..7


AbstractInstrutionK
	0000 0000 KKKK 0000								K4				0..15
	0000 kkkk kkkk kkkk								k12				-2K..2K
	0000 000k kkkk 000k kkkk kkkk kkkk kkkk			k22				0..4M
AbstractInstrutionS
	0000 0000 0sss 0000								s






















## MVC for swing custom components

###Model

	* interface XxxModel
	* abstract class AbstractXxxModel implements XxxModel
	* class DefaultXxxModel extends AbstractXxxModel

###View

	* class XxxComponents extends JComponent
		* private static final String uiClassID = "XxxComponentUI";

###Delegate

	* abstract class XxxUI extends ComponentUI
	* class BasicXxxUI extends XxxUI

###Install

	UIManager.getDefaults().put( uiClassID, BasicZoomUI.class.getName() );


## Core components

New device:
	1. create CoreDescriptor class (extends AbstractCoreDescriptor)
	2. add class to /META-INF/services/be.lmenten.avr.core.CoreDescriptor
	3. create Core class (extends AbstractCore)

New component:
	1. create CoreComponent class (extends AbstactCoreComponent)
