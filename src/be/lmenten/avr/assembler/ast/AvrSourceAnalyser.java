package be.lmenten.avr.assembler.ast;

import java.util.LinkedList;

import be.lmenten.avr.assembler.parser.ExpressionValue;
import be.lmenten.avr.assembler.parser.node.*;
import be.lmenten.avr.core.descriptor.CoreMemory;
import be.lmenten.avr.core.descriptor.CoreRegisterDescriptor;
import be.lmenten.avr.core.instructions.InstructionSet;
import be.lmenten.avr.project.AvrSource;
import be.lmenten.utils.StringUtils;

/**
 * Stage 1: prepare all constants and labels
 * 
 * @author Laurent Menten
 */
public class AvrSourceAnalyser
	extends AvrStagedDepthFirstAdapter
{
	private String lastGlobalLabel = null;
	private int lineDelta;

	// ------------------------------------------------------------------------

	private CoreMemory memory;
	private int cLocation = 0;
	private int dLocation = 0;
	private int eLocation = 0;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AvrSourceAnalyser( AvrSource source )
	{
		super( source );

		this.memory = CoreMemory.FLASH;
	}

	// ========================================================================
	// === PROLOG / ENDPROLOG =================================================
	// ========================================================================

	@Override
	public void inAPrologLine( APrologLine node )
	{
	}

	@Override
	public void inAEndprologLine( AEndprologLine node )
	{
		lineDelta = node.getKeyword().getLine();
	}

	// ========================================================================
	// === CONSTANTS / VARIABLES ==============================================
	// ========================================================================

	/**
	 * Create a constant
	 */
	@Override
	public void outAEquLine( AEquLine node )
	{
		String name = node.getName().getText();
		Node valueNode = node.getValue();

		ExpressionValue value = (ExpressionValue)getIn( valueNode );
		if( value != null )
		{
			value.setProtected();

			setSymbol( name, value );
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Create a variable
	 */
	@Override
	public void outASetLine( ASetLine node )
	{
		String name = node.getName().getText();
		Node valueNode = node.getValue();

		ExpressionValue value = (ExpressionValue)getIn( valueNode );
		if( value != null )
		{
			setSymbol( name, value );
		}
	}

	/**
	 * Destroy a variable
	 */
	@Override
	public void outAUnsetLine( AUnsetLine node )
	{
		String name = node.getName().getText();

		setSymbol( name, null );
	}

	// ========================================================================
	// === NUMBERS ============================================================
	// ========================================================================

	@Override
	public void inAIntegerExpr( AIntegerExpr node )
	{
		String integerString = node.getInteger().getText();

		long value = StringUtils.parseNumber( integerString );

		ExpressionValue exprValue = new ExpressionValue( value );

		setIn( node, exprValue );
	}

	@Override
	public void inADecimalExpr( ADecimalExpr node )
	{
		String decimalString = node.getDecimal().getText();

		double value = Double.parseDouble( decimalString );

		ExpressionValue exprValue = new ExpressionValue( value );

		setIn( node, exprValue );
	}

	@Override
	public void inAConstantExpr( AConstantExpr node )
	{
		String constantName = node.getName().getText();

		// --------------------------------------------------------------------
		// - Handle __LINE__ as a special case --------------------------------
		// --------------------------------------------------------------------

		if( constantName.equalsIgnoreCase( AvrSource.SYM_LINE ) )
		{
			ExpressionValue exprValue = new ExpressionValue( node.getName().getLine() - lineDelta );

			setIn( node, exprValue );

			return;
		}

		// --------------------------------------------------------------------
		// - If parent is I/O instruction -> try register name ----------------
		// --------------------------------------------------------------------

		Node parent = node.parent();
		if( (parent instanceof AInInstruction)				// in rd, A
				||	(parent instanceof AOutInstruction)		// out A, rd
				||	(parent instanceof ACbiInstruction)		// cbi A, b
				||	(parent instanceof ASbciInstruction)	// sbi A, b
				||	(parent instanceof ASbicInstruction)	// sbic A, b
				||	(parent instanceof ASbisInstruction) )	// sbis A, b
		{
			CoreRegisterDescriptor rdesc = getCoreDescriptor().getRegisterDescriptor( constantName );
			if( rdesc != null )
			{
				int rAddress = rdesc.getAddress() - getCoreDescriptor().getIoRegistersBase();
				if( rAddress > getCoreDescriptor().getIoRegistersCount() )
				{
					// FIXME handle unknown register
					System.out.println( "Register " + constantName + " is out of I/O range" );
					return;
				}
	
				ExpressionValue exprValue = new ExpressionValue( rAddress );

				setIn( node, exprValue );

				return;
			}

			// --- Not a register ---------------------------------------------
		}

		// --------------------------------------------------------------------
		// - General case -----------------------------------------------------
		// --------------------------------------------------------------------

		ExpressionValue exprValue = getSymbol( constantName );
		if( exprValue == null )
		{
			// FIXME handle unknown symbol
			System.out.println( "Unknown symbol " + constantName );
			return;
		}

		setIn( node, exprValue );
	}

	// ========================================================================
	// === SPECIAL $ SYMBOL 'HERE' ============================================
	// ========================================================================

	@Override
	public void inAHereExpr( AHereExpr node )
	{
		setIn( node, new ExpressionValue( getCurrentLocation() ) );
	}

	// ========================================================================
	// === (IO)ADDRESS / INDEX / MASK =========================================
	// ========================================================================

	@Override
	public void inAAddressExpr( AAddressExpr node )
	{
		String rName = node.getRegister().getText();
		CoreRegisterDescriptor rdesc = getCoreDescriptor().getRegisterDescriptor( rName );
		if( rdesc == null )
		{
			// FIXME handle unknown register
			System.out.println( "Unknown register " + rName );
			return;
		}

		int rAddress = rdesc.getAddress();

		ExpressionValue exprValue = new ExpressionValue( rAddress );

		setIn( node, exprValue );
	}

	@Override
	public void inAIoaddressExpr( AIoaddressExpr node )
	{
		String rName = node.getRegister().getText();
		CoreRegisterDescriptor rdesc = getCoreDescriptor().getRegisterDescriptor( rName );
		if( rdesc == null )
		{
			// FIXME handle unknown register
			System.out.println( "Unknown register " + rName );
			return;
		}

		int rAddress = rdesc.getAddress() - getCoreDescriptor().getIoRegistersBase();
		if( rAddress > getCoreDescriptor().getIoRegistersCount() )
		{
			// FIXME handle extended io register
			System.out.println( "Register " + rName + " is out of I/O range" );
			return;
		}

		ExpressionValue exprValue = new ExpressionValue( rAddress );

		setIn( node, exprValue );
	}

	@Override
	public void inAIndexExpr( AIndexExpr node )
	{
		String rName = node.getRegister().getText();
		CoreRegisterDescriptor rdesc = getCoreDescriptor().getRegisterDescriptor( rName );
		if( rdesc == null )
		{
			// FIXME handle unknown register
			System.out.println( "Unknown register " + rName );
			return;
		}

		String bName = node.getBit().getText();
		int bIndex = rdesc.getBitByName( bName );
		if( bIndex == -1 )
		{
			// FIXME handle unknown register
			System.out.println( "Unknown bit " + rName + "." + bName );
			return;
		}

		ExpressionValue exprValue = new ExpressionValue( bIndex );

		setIn( node, exprValue );
	}

	@Override
	public void inAMaskExpr( AMaskExpr node )
	{
		String rName = node.getRegister().getText();
		CoreRegisterDescriptor rdesc = getCoreDescriptor().getRegisterDescriptor( rName );
		if( rdesc == null )
		{
			// FIXME handle unknown register
			System.out.println( "Unknown register " + rName );
			return;
		}

		int bMask = 0;

		LinkedList<TIdentifier> bits = node.getBits();
		for( TIdentifier bit : bits )
		{
			String bName = bit.getText();
			int bIndex = rdesc.getBitByName( bName );
			if( bIndex == -1 )
			{
				// FIXME handle unknown register
				System.out.println( "Unknown bit " + rName + "." + bName );
				return;
			}

			bMask |= 1 << bIndex;
		}

		ExpressionValue exprValue = new ExpressionValue( bMask );

		setIn( node, exprValue );
	}

	// ========================================================================
	// === STRUCTURES =========================================================
	// ========================================================================

	@Override
	public void inAOffsetExpr( AOffsetExpr node )
	{
		@SuppressWarnings("unused")
		String sName = node.getStruct().getText();
		@SuppressWarnings("unused")
		String sMember = node.getMember().getText();

		// FIXME handle structure
		
		ExpressionValue exprValue = new ExpressionValue( 0 );

		setIn( node, exprValue );
	}

	// ========================================================================
	// === SEGMENT ============================================================
	// ========================================================================

	@Override
	public void inACsegLine( ACsegLine node )
	{
		memory = CoreMemory.FLASH;
	}

	@Override
	public void inADsegLine( ADsegLine node )
	{
		memory = CoreMemory.SRAM;
	}

	@Override
	public void inAEsegLine( AEsegLine node )
	{
		memory = CoreMemory.EEPROM;
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param address
	 */
	private void setCurrentLocation( int address )
	{
		switch( memory )
		{
			case FLASH:
				cLocation = address;
				break;

			case SRAM:
				dLocation = address;
				break;

			case EEPROM:
				eLocation = address;
				break;
		}
	}
	
	/**
	 * 
	 * @param offset
	 */
	private void updateCurrentLocation( int offset )
	{
		setCurrentLocation( getCurrentLocation() + offset );
	}

	/**
	 * 
	 * @return
	 */
	private int getCurrentLocation()
	{
		int rc = 0;
		switch( memory )
		{
			case FLASH:
				rc = cLocation;
				break;

			case SRAM:
				rc = dLocation;
				break;

			case EEPROM:
				rc = eLocation;
				break;
		}

		return rc;
	}

	// ========================================================================
	// === LABELS =============================================================
	// ========================================================================

	@Override
	public void inALabelLine( ALabelLine node )
	{
		String label = node.getName().getText();

		// remove trailing ':'

		label = label.substring( 0 , label.length()-1 );

		if( label.charAt( 0 ) == '.' )	// ------------------------ local label
		{
			label = lastGlobalLabel + label;
		}

		else	// ----------------------------------------------- global label
		{
			lastGlobalLabel = label;
		}

		// set trimmed & fully qualified name in AST

		node.getName().setText( label );

		ExpressionValue value = new ExpressionValue( getCurrentLocation()*2 );
		value.setProtected();

		setSymbol( label, value );
		setIn( node, value );
	}

	// ========================================================================
	// === UPDATE location counter ============================================
	// ========================================================================

	//FIXME add data generation

	// ------------------------------------------------------------------------

	@Override
	public void inAAdcInstruction( AAdcInstruction node )
	{
		updateCurrentLocation( InstructionSet.ADC.getOpcodeSize() );
	}

	@Override
	public void inAAddInstruction( AAddInstruction node )
	{
		updateCurrentLocation( InstructionSet.ADD.getOpcodeSize() );
	}

	@Override
	public void inAAdiwInstruction( AAdiwInstruction node )
	{
		updateCurrentLocation( InstructionSet.ADIW.getOpcodeSize() );
	}

	@Override
	public void inAAndInstruction( AAndInstruction node )
	{
		updateCurrentLocation( InstructionSet.AND.getOpcodeSize() );
	}

	@Override
	public void inAAndiInstruction( AAndiInstruction node )
	{
		updateCurrentLocation( InstructionSet.ANDI.getOpcodeSize() );
	}

	@Override
	public void inAAsrInstruction( AAsrInstruction node )
	{
		updateCurrentLocation( InstructionSet.ASR.getOpcodeSize() );
	}

	@Override
	public void inABclrInstruction( ABclrInstruction node )
	{
		updateCurrentLocation( InstructionSet.BCLR.getOpcodeSize() );
	}

	@Override
	public void inABldInstruction( ABldInstruction node )
	{
		updateCurrentLocation( InstructionSet.BLD.getOpcodeSize() );
	}

	@Override
	public void inABrbcInstruction( ABrbcInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRBC.getOpcodeSize() );
	}

	@Override
	public void inABrbsInstruction( ABrbsInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRBS.getOpcodeSize() );
	}

	@Override
	public void inABreakInstruction( ABreakInstruction node )
	{
		updateCurrentLocation( InstructionSet.BREAK.getOpcodeSize() );
	}

	@Override
	public void inABreqInstruction( ABreqInstruction node )
	{
		updateCurrentLocation( InstructionSet.BREQ.getOpcodeSize() );
	}

	@Override
	public void inABrgeInstruction( ABrgeInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRGE.getOpcodeSize() );
	}

	@Override
	public void inABrhcInstruction( ABrhcInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRHC.getOpcodeSize() );
	}

	@Override
	public void inABrhsInstruction( ABrhsInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRHS.getOpcodeSize() );
	}

	@Override
	public void inABridInstruction( ABridInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRID.getOpcodeSize() );
	}

	@Override
	public void inABrieInstruction( ABrieInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRIE.getOpcodeSize() );
	}

	@Override
	public void inABrloInstruction( ABrloInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRLO.getOpcodeSize() );
	}

	@Override
	public void inABrltInstruction( ABrltInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRLT.getOpcodeSize() );
	}

	@Override
	public void inABrmiInstruction( ABrmiInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRMI.getOpcodeSize() );
	}

	@Override
	public void inABrneInstruction( ABrneInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRNE.getOpcodeSize() );
	}

	@Override
	public void inABrplInstruction( ABrplInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRPL.getOpcodeSize() );
	}

	@Override
	public void inABrshInstruction( ABrshInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRSH.getOpcodeSize() );
	}

	@Override
	public void inABrtcInstruction( ABrtcInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRTC.getOpcodeSize() );
	}

	@Override
	public void inABrtsInstruction( ABrtsInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRTS.getOpcodeSize() );
	}

	@Override
	public void inABrvcInstruction( ABrvcInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRVC.getOpcodeSize() );
	}

	@Override
	public void inABrvsInstruction( ABrvsInstruction node )
	{
		updateCurrentLocation( InstructionSet.BRVS.getOpcodeSize() );
	}

	@Override
	public void inABsetInstruction( ABsetInstruction node )
	{
		updateCurrentLocation( InstructionSet.BSET.getOpcodeSize() );
	}

	@Override
	public void inABstInstruction( ABstInstruction node )
	{
		updateCurrentLocation( InstructionSet.BST.getOpcodeSize() );
	}

	@Override
	public void inACallInstruction( ACallInstruction node )
	{
		updateCurrentLocation( InstructionSet.CALL.getOpcodeSize() );
	}

	@Override
	public void inACbiInstruction( ACbiInstruction node )
	{
		updateCurrentLocation( InstructionSet.CBI.getOpcodeSize() );
	}

	@Override
	public void inACbrInstruction( ACbrInstruction node )
	{
		updateCurrentLocation( InstructionSet.CBR.getOpcodeSize() );
	}

	@Override
	public void inAClcInstruction( AClcInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLC.getOpcodeSize() );
	}
	
	@Override
	public void inAClhInstruction( AClhInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLH.getOpcodeSize() );
	}

	@Override
	public void inACliInstruction( ACliInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLI.getOpcodeSize() );
	}

	@Override
	public void inAClnInstruction( AClnInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLN.getOpcodeSize() );
	}

	@Override
	public void inAClrInstruction( AClrInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLR.getOpcodeSize() );
	}

	@Override
	public void inAClsInstruction( AClsInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLS.getOpcodeSize() );
	}

	@Override
	public void inACltInstruction( ACltInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLT.getOpcodeSize() );
	}

	@Override
	public void inAClvInstruction( AClvInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLV.getOpcodeSize() );
	}

	@Override
	public void inAClzInstruction( AClzInstruction node )
	{
		updateCurrentLocation( InstructionSet.CLZ.getOpcodeSize() );
	}

	@Override
	public void inAComInstruction( AComInstruction node )
	{
		updateCurrentLocation( InstructionSet.COM.getOpcodeSize() );
	}

	@Override
	public void inACpInstruction( ACpInstruction node )
	{
		updateCurrentLocation( InstructionSet.CP.getOpcodeSize() );
	}

	@Override
	public void inACpcInstruction( ACpcInstruction node )
	{
		updateCurrentLocation( InstructionSet.CPC.getOpcodeSize() );
	}

	@Override
	public void inACpiInstruction( ACpiInstruction node )
	{
		updateCurrentLocation( InstructionSet.CPI.getOpcodeSize() );
	}

	@Override
	public void inACpseInstruction( ACpseInstruction node )
	{
		updateCurrentLocation( InstructionSet.CPSE.getOpcodeSize() );
	}

	@Override
	public void inADecInstruction( ADecInstruction node )
	{
		updateCurrentLocation( InstructionSet.DEC.getOpcodeSize() );
	}

	@Override
	public void inADesInstruction( ADesInstruction node )
	{
		updateCurrentLocation( InstructionSet.DES.getOpcodeSize() );
	}

	@Override
	public void inAEicallInstruction( AEicallInstruction node )
	{
		updateCurrentLocation( InstructionSet.EICALL.getOpcodeSize() );
	}

	@Override
	public void inAEijmpInstruction( AEijmpInstruction node )
	{
		updateCurrentLocation( InstructionSet.EIJMP.getOpcodeSize() );
	}

	// FIXME ELPM

	@Override
	public void inAEorInstruction( AEorInstruction node )
	{
		updateCurrentLocation( InstructionSet.EOR.getOpcodeSize() );
	}

	@Override
	public void inAFmulInstruction( AFmulInstruction node )
	{
		updateCurrentLocation( InstructionSet.FMUL.getOpcodeSize() );
	}

	@Override
	public void inAFmulsInstruction( AFmulsInstruction node )
	{
		updateCurrentLocation( InstructionSet.FMULS.getOpcodeSize() );
	}

	@Override
	public void inAFmulsuInstruction( AFmulsuInstruction node )
	{
		updateCurrentLocation( InstructionSet.FMULSU.getOpcodeSize() );
	}

	@Override
	public void inAIcallInstruction( AIcallInstruction node )
	{
		updateCurrentLocation( InstructionSet.ICALL.getOpcodeSize() );
	}

	@Override
	public void inAIjmpInstruction( AIjmpInstruction node )
	{
		updateCurrentLocation( InstructionSet.IJMP.getOpcodeSize() );
	}

	@Override
	public void inAInInstruction( AInInstruction node )
	{
		updateCurrentLocation( InstructionSet.IN.getOpcodeSize() );
	}

	@Override
	public void inAIncInstruction( AIncInstruction node )
	{
		updateCurrentLocation( InstructionSet.INC.getOpcodeSize() );
	}

	@Override
	public void inAJmpInstruction( AJmpInstruction node )
	{
		updateCurrentLocation( InstructionSet.JMP.getOpcodeSize() );
	}

	@Override
	public void inALacInstruction( ALacInstruction node )
	{
		updateCurrentLocation( InstructionSet.LAC.getOpcodeSize() );
	}

	@Override
	public void inALasInstruction( ALasInstruction node )
	{
		updateCurrentLocation( InstructionSet.LAS.getOpcodeSize() );
	}

	@Override
	public void inALatInstruction( ALatInstruction node )
	{
		updateCurrentLocation( InstructionSet.ADD.getOpcodeSize() );
	}

	// FIXME LD
	// FIXME LDD

	@Override
	public void inALdiInstruction( ALdiInstruction node )
	{
		updateCurrentLocation( InstructionSet.LDI.getOpcodeSize() );
	}

	// FIXME LDS
	// FIXME LPM

	@Override
	public void inALslInstruction( ALslInstruction node )
	{
		updateCurrentLocation( InstructionSet.LSL.getOpcodeSize() );
	}

	@Override
	public void inALsrInstruction( ALsrInstruction node )
	{
		updateCurrentLocation( InstructionSet.LSR.getOpcodeSize() );
	}

	@Override
	public void inAMovInstruction( AMovInstruction node )
	{
		updateCurrentLocation( InstructionSet.MOV.getOpcodeSize() );
	}

	@Override
	public void inAMovwInstruction( AMovwInstruction node )
	{
		updateCurrentLocation( InstructionSet.MOVW.getOpcodeSize() );
	}

	@Override
	public void inAMulInstruction( AMulInstruction node )
	{
		updateCurrentLocation( InstructionSet.MUL.getOpcodeSize() );
	}

	@Override
	public void inAMulsInstruction( AMulsInstruction node )
	{
		updateCurrentLocation( InstructionSet.MULS.getOpcodeSize() );
	}

	@Override
	public void inAMulsuInstruction( AMulsuInstruction node )
	{
		updateCurrentLocation( InstructionSet.MULSU.getOpcodeSize() );
	}

	@Override
	public void inANegInstruction( ANegInstruction node )
	{
		updateCurrentLocation( InstructionSet.NEG.getOpcodeSize() );
	}

	@Override
	public void inANopInstruction( ANopInstruction node )
	{
		updateCurrentLocation( InstructionSet.NOP.getOpcodeSize() );
	}

	@Override
	public void inAOrInstruction( AOrInstruction node )
	{
		updateCurrentLocation( InstructionSet.OR.getOpcodeSize() );
	}

	@Override
	public void inAOriInstruction( AOriInstruction node )
	{
		updateCurrentLocation( InstructionSet.ORI.getOpcodeSize() );
	}

	@Override
	public void inAOutInstruction( AOutInstruction node )
	{
		updateCurrentLocation( InstructionSet.OUT.getOpcodeSize() );
	}

	@Override
	public void inAPushInstruction( APushInstruction node )
	{
		updateCurrentLocation( InstructionSet.PUSH.getOpcodeSize() );
	}

	@Override
	public void inAPopInstruction( APopInstruction node )
	{
		updateCurrentLocation( InstructionSet.POP.getOpcodeSize() );
	}

	@Override
	public void inARcallInstruction( ARcallInstruction node )
	{
		updateCurrentLocation( InstructionSet.RCALL.getOpcodeSize() );
	}

	@Override
	public void inARetInstruction( ARetInstruction node )
	{
		updateCurrentLocation( InstructionSet.RET.getOpcodeSize() );
	}

	@Override
	public void inARetiInstruction( ARetiInstruction node )
	{
		updateCurrentLocation( InstructionSet.RETI.getOpcodeSize() );
	}

	@Override
	public void inARjmpInstruction( ARjmpInstruction node )
	{
		updateCurrentLocation( InstructionSet.RJMP.getOpcodeSize() );
	}

	@Override
	public void inARolInstruction( ARolInstruction node )
	{
		updateCurrentLocation( InstructionSet.ROL.getOpcodeSize() );
	}

	@Override
	public void inARorInstruction( ARorInstruction node )
	{
		updateCurrentLocation( InstructionSet.ROR.getOpcodeSize() );
	}

	@Override
	public void inASbcInstruction( ASbcInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBC.getOpcodeSize() );
	}

	@Override
	public void inASbciInstruction( ASbciInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBCI.getOpcodeSize() );
	}

	@Override
	public void inASbiInstruction( ASbiInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBI.getOpcodeSize() );
	}

	@Override
	public void inASbicInstruction( ASbicInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBIC.getOpcodeSize() );
	}

	@Override
	public void inASbisInstruction( ASbisInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBIS.getOpcodeSize() );
	}

	@Override
	public void inASbiwInstruction( ASbiwInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBIW.getOpcodeSize() );
	}

	@Override
	public void inASbrInstruction( ASbrInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBR.getOpcodeSize() );
	}

	@Override
	public void inASbrcInstruction( ASbrcInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBR.getOpcodeSize() );
	}

	@Override
	public void inASbrsInstruction( ASbrsInstruction node )
	{
		updateCurrentLocation( InstructionSet.SBRS.getOpcodeSize() );
	}

	@Override
	public void inASecInstruction( ASecInstruction node )
	{
		updateCurrentLocation( InstructionSet.SEC.getOpcodeSize() );
	}

	@Override
	public void inASehInstruction( ASehInstruction node )
	{
		updateCurrentLocation( InstructionSet.SEH.getOpcodeSize() );
	}

	@Override
	public void inASeiInstruction( ASeiInstruction node )
	{
		updateCurrentLocation( InstructionSet.SEI.getOpcodeSize() );
	}

	@Override
	public void inASenInstruction( ASenInstruction node )
	{
		updateCurrentLocation( InstructionSet.SEN.getOpcodeSize() );
	}

	@Override
	public void inASerInstruction( ASerInstruction node )
	{
		updateCurrentLocation( InstructionSet.SER.getOpcodeSize() );
	}

	@Override
	public void inASesInstruction( ASesInstruction node )
	{
		updateCurrentLocation( InstructionSet.SES.getOpcodeSize() );
	}

	@Override
	public void inASetInstruction( ASetInstruction node )
	{
		updateCurrentLocation( InstructionSet.SET.getOpcodeSize() );
	}

	@Override
	public void inASevInstruction( ASevInstruction node )
	{
		updateCurrentLocation( InstructionSet.SEV.getOpcodeSize() );
	}

	@Override
	public void inASezInstruction( ASezInstruction node )
	{
		updateCurrentLocation( InstructionSet.SEZ.getOpcodeSize() );
	}

	@Override
	public void inASleepInstruction( ASleepInstruction node )
	{
		updateCurrentLocation( InstructionSet.SLEEP.getOpcodeSize() );
	}

	// FIXME SPM
	// FIXME ST
	// FIXME STD
	// FIXME STS

	@Override
	public void inASubInstruction( ASubInstruction node )
	{
		updateCurrentLocation( InstructionSet.SUB.getOpcodeSize() );
	}

	@Override
	public void inASubiInstruction( ASubiInstruction node )
	{
		updateCurrentLocation( InstructionSet.SUBI.getOpcodeSize() );
	}

	@Override
	public void inASwapInstruction( ASwapInstruction node )
	{
		updateCurrentLocation( InstructionSet.SWAP.getOpcodeSize() );
	}

	@Override
	public void inATstInstruction( ATstInstruction node )
	{
		updateCurrentLocation( InstructionSet.TST.getOpcodeSize() );
	}

	@Override
	public void inAWdrInstruction( AWdrInstruction node )
	{
		updateCurrentLocation( InstructionSet.WDR.getOpcodeSize() );
	}

	@Override
	public void inAXchInstruction( AXchInstruction node )
	{
		updateCurrentLocation( InstructionSet.XCH.getOpcodeSize() );
	}
}
