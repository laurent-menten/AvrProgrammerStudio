package be.lmenten.avr.assembler.ast;

import be.lmenten.avr.assembler.def.AvrRegisters;
import be.lmenten.avr.assembler.def.AvrUpperRegisters;
import be.lmenten.avr.assembler.parser.ExpressionValue;
import be.lmenten.avr.assembler.parser.node.AAdcInstruction;
import be.lmenten.avr.assembler.parser.node.AAddInstruction;
import be.lmenten.avr.assembler.parser.node.AAndiInstruction;
import be.lmenten.avr.assembler.parser.node.AInInstruction;
import be.lmenten.avr.assembler.parser.node.ALdiInstruction;
import be.lmenten.avr.assembler.parser.node.AOutInstruction;
import be.lmenten.avr.core.instructions.logic.ANDI;
import be.lmenten.avr.core.instructions.math.ADC;
import be.lmenten.avr.core.instructions.math.ADD;
import be.lmenten.avr.core.instructions.transfer.IN;
import be.lmenten.avr.core.instructions.transfer.LDI;
import be.lmenten.avr.core.instructions.transfer.OUT;
import be.lmenten.avr.project.AvrSource;

/**
 * in: values from source
 * out: assembled code
 * 
 * @author Laurent Menten
 */
public class AvrSourceAssembler
	extends AvrStagedDepthFirstAdapter
{
	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AvrSourceAssembler( AvrSource source )
	{
		super( source );
	}

	// ========================================================================
	// === 
	// ========================================================================

	@Override
	public void outAAdcInstruction( AAdcInstruction node )
	{
		String dest = node.getDest().getText();
		AvrRegisters rd = AvrRegisters.valueOf( dest.toUpperCase() );

		String src = node.getSrc().getText();
		AvrRegisters rs = AvrRegisters.valueOf( src.toUpperCase() );

		ADC adc = new ADC( rd, rs );

		setOut( node, adc );
	}

	@Override
	public void outAAddInstruction( AAddInstruction node )
	{
		String dest = node.getDest().getText();
		AvrRegisters rd = AvrRegisters.valueOf( dest.toUpperCase() );

		String src = node.getSrc().getText();
		AvrRegisters rs = AvrRegisters.valueOf( src.toUpperCase() );

		ADD add = new ADD( rd, rs );

		setOut( node, add );
	}

	@Override
	public void outAAndiInstruction( AAndiInstruction node )
	{
		String dest = node.getDest().getText();
		AvrUpperRegisters rd = AvrUpperRegisters.valueOf( dest.toUpperCase() );

		ExpressionValue k = (ExpressionValue) getIn( node.getK() );

		ANDI andi = new ANDI( rd, k.getByte() );

		setOut( node, andi );
	}

	@Override
	public void outAInInstruction( AInInstruction node )
	{
		String dest = node.getDest().getText();
		AvrRegisters rd = AvrRegisters.valueOf( dest.toUpperCase() );

		ExpressionValue a = (ExpressionValue) getIn( node.getA() );

		IN in = new IN( rd, a.getWord() );

		setOut( node, in );
	}

	@Override
	public void outAOutInstruction( AOutInstruction node )
	{
		String src = node.getSrc().getText();
		AvrRegisters rs = AvrRegisters.valueOf( src.toUpperCase() );

		ExpressionValue a = (ExpressionValue) getIn( node.getA() );

		OUT out = new OUT( a.getWord(), rs );

		setOut( node, out );
	}

	@Override
	public void outALdiInstruction( ALdiInstruction node )
	{
		String dest = node.getDest().getText();
		AvrUpperRegisters rd = AvrUpperRegisters.valueOf( dest.toUpperCase() );

		ExpressionValue k = (ExpressionValue) getIn( node.getK() );

		LDI ldi = new LDI( rd, k.getByte() );

		setOut( node, ldi );
	}
}
