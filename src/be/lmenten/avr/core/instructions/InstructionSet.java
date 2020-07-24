// = ======================================================================== =
// = === AVR Programmer Studio ======= Copyright (c) 2020+ Laurent Menten === =
// = ======================================================================== =
// = = This program is free software: you can redistribute it and/or modify = =
// = = it under the terms of the GNU General Public License as published by = =
// = = the Free Software Foundation, either version 3 of the License, or    = =
// = = (at your option) any later version.                                  = =
// = =                                                                      = =
// = = This program is distributed in the hope that it will be useful, but  = =
// = = WITHOUT ANY WARRANTY; without even the implied warranty of           = =
// = = MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU    = =
// = = General Public License for more details.                             = =
// = =                                                                      = =
// = = You should have received a copy of the GNU General Public License    = =
// = = along with this program. If not, see                                 = =
// = = <https://www.gnu.org/licenses/>.                                     = =
// = ======================================================================== =

package be.lmenten.avr.core.instructions;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import be.lmenten.avr.core.Core;
import be.lmenten.avr.core.instructions.bit.*;
import be.lmenten.avr.core.instructions.branch.*;
import be.lmenten.avr.core.instructions.compare.*;
import be.lmenten.avr.core.instructions.logic.*;
import be.lmenten.avr.core.instructions.math.*;
import be.lmenten.avr.core.instructions.register.*;
import be.lmenten.avr.core.instructions.system.*;
import be.lmenten.avr.core.instructions.transfer.*;

import be.lmenten.utils.StringUtils;

/**
 * 
 * Informations from document "Atmel AVR Instruction Set Manual"
 * Reference: 0856 rev L, 11/2016.
 */
public enum InstructionSet
	implements Comparable<InstructionSet>
{
	//                                                     S      32
	ADC( ADC.class,					"0001 11rd dddd rrrr", false, false, null ),
	ADD( ADD.class,					"0000 11rd dddd rrrr", false, false, null ),
	ADIW( ADIW.class,					"1001 0110 KKdd KKKK", false, false, null ),
	AND( AND.class,					"0010 00rd dddd rrrr", false, false, null ),
	ANDI( ANDI.class,					"0111 KKKK dddd KKKK", false, false, null ),
	ASR( ASR.class,					"1001 010d dddd 0101", false, false, null ),
	BCLR( BCLR.class,					"1001 0100 1sss 1000", false, false, null ),
	BLD( BLD.class, 					"1111 100d dddd 0bbb", false, false, null ),
	BRBC( BRBC.class, 				"1111 01kk kkkk ksss", false, false, null ),
	BRBS( BRBS.class,					"1111 00kk kkkk ksss", false, false, null ),

	BRCC( BRCC.class, 				"1111 01kk kkkk k000", false, false, null )		// BRBC 0 (carry)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRCS( BRCS.class, 				"1111 00kk kkkk k000", false, false, null  )	// BRBS 0 (carry)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BREAK( BREAK.class, 				"1001 0101 1001 1000", false, false, null ),
	BREQ( BREQ.class, 				"1111 00kk kkkk k001", false, false, null )		// BRBS 1 (zero)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRGE( BRGE.class, 				"1111 01kk kkkk k100", false, false, null )		// BRBC 4 (sign)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRHC( BRHC.class, 				"1111 01kk kkkk k101", false, false, null )		// BRBC 5 (half carry)	
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRHS( BRHS.class,					"1111 00kk kkkk k101", false, false, null )		// BRBS 5 (half carry)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRID( BRID.class, 				"1111 01kk kkkk k111", false, false, null )		// BRBC 7 (interrupt)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRIE( BRIE.class,					"1111 00kk kkkk k111", false, false, null )		// BRBS 7 (interrupt)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRLO( BRLO.class,					"1111 00kk kkkk k000", false, false, null )		// BRBS 0 (carry)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRLT( BRLT.class,					"1111 00kk kkkk k100", false, false, null )		// BRBS 4 (sign)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRMI( BRMI.class,					"1111 00kk kkkk k010", false, false, null )		// BRBS 2 (negative)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRNE( BRNE.class,					"1111 01kk kkkk k001", false, false, null )		// BRBC 1 (zero)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRPL( BRPL.class,					"1111 01kk kkkk k010", false, false, null )		// BRBC 2 (negative)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRSH( BRSH.class,					"1111 01kk kkkk k000", false, false, null )		// BRBC 0 (carry)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRTC( BRTC.class,					"1111 01kk kkkk k110", false, false, null )		// BRBC 6 (t)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRTS( BRTS.class,					"1111 00kk kkkk k110", false, false, null )		// BRBS 6 (t)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRVC( BRVC.class,					"1111 01kk kkkk k011", false, false, null )		// BRBC 3 (overflow)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BRVS( BRVS.class,					"1111 00kk kkkk k011", false, false, null )		// BRBS 3 (overflow)
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.s, 0b0000_0000_0000_0111 );
		}
	},
	BSET( BSET.class,					"1001 0100 0sss 1000", false, false, null ),
	BST( BST.class,					"1111 101d dddd 0bbb", false, false, null ),
	CALL( CALL.class,					"1001 010k kkkk 111k", false, true, null ),
	CBI( CBI.class,					"1001 1000 AAAA Abbb", false, false, null ),
	CBR( CBR.class,					"0111 KKKK dddd KKKK", false, false, null ),	// ANDI Rd, $FF-K
	CLC( CLC.class,					"1001 0100 1000 1000", false, false, null ),	// BCLR 0
	CLH( CLH.class,					"1001 0100 1101 1000", false, false, null ),	// BCLR 5
	CLI( CLI.class,					"1001 0100 1111 1000", false, false, null ),	// BCLR 7
	CLN( CLN.class,					"1001 0100 1010 1000", false, false, null ),	// BCLR 2
	CLR( CLR.class,					"0010 01rd dddd rrrr", false, false, null ),	// EOR Rd, Rd
	CLS( CLS.class,					"1001 0100 1100 1000", false, false, null ),	// BCLR 4
	CLT( CLT.class,					"1001 0100 1110 1000", false, false, null ),	// BCLR 6
	CLV( CLV.class,					"1001 0100 1011 1000", false, false, null ),	// BCLR 3
	CLZ( CLZ.class,					"1001 0100 1001 1000", false, false, null ),	// BCLR 1
	COM( COM.class,					"1001 010d dddd 0000", false, false, null ),
	CP( CP.class,						"0001 01rd dddd rrrr", false, false, null ),
	CPC( CPC.class,					"0000 01rd dddd rrrr", false, false, null ),
	CPI( CPI.class,					"0011 KKKK dddd KKKK", false, false, null ),
	CPSE( CPSE.class,					"0001 00rd dddd rrrr", false, false, null ),
	DEC( DEC.class,					"1001 010d dddd 1010", false, false, null ),
	DES( DES.class,					"1001 0100 KKKK 1011", false, false, null ),
	EICALL( EICALL.class,			"1001 0101 0001 1001", false, false, null ),
	EIJMP( EIJMP.class,				"1001 0100 0001 1001", false, false, null ),
	ELPM_R0_Z( ELPM_R0.class,		"1001 0101 1101 1000", false, false, "ELPM" ),
	ELPM_Z( ELPM.class,				"1001 000d dddd 0110", false, false, "ELPM" ),
	ELPM_ZP( ELPM.class,				"1001 000d dddd 0111", false, false, "ELPM" ),
	EOR( EOR.class,					"0010 01rd dddd rrrr", false, false, null ),
	FMUL( FMUL.class,					"0000 0011 0ddd 1rrr", false, false, null ),
	FMULS( FMULS.class,				"0000 0011 1ddd 0rrr", false, false, null ),
	FMULSU( FMULSU.class,			"0000 0011 1ddd 1rrr", false, false, null ),
	ICALL( ICALL.class,				"1001 0101 0000 1001", false, false, null ),
	IJMP( IJMP.class,					"1001 0100 0000 1001", false, false, null ),
	IN( IN.class,						"1011 0AAd dddd AAAA", false, false, null ),
	INC( INC.class,					"1001 010d dddd 0011", false, false, null ),
	JMP( JMP.class,					"1001 010k kkkk 110k", false, true, null ),
	LAC( LAC.class,					"1001 001r rrrr 0110", false, false, null ),
	LAS( LAS.class,					"1001 001r rrrr 0101", false, false, null ),
	LAT( LAT.class,					"1001 001r rrrr 0111", false, false, null ),
	LD_X( LD.class,					"1001 000d dddd 1100", false, false, "LD" ),
	LD_XP( LD.class,					"1001 000d dddd 1101", false, false, "LD" ),
	LD_DX( LD.class,					"1001 000d dddd 1110", false, false, "LD" ),
	LD_Y( LDD.class,					"1000 000d dddd 1000", false, false, "LD" )		// LDD Rd, Y+0
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.q, 0b0010_1100_0000_0111 );
		}
	},
	LD_YP( LD.class,					"1001 000d dddd 1001", false, false, "LD" ),
	LD_DY( LD.class,					"1001 000d dddd 1010", false, false, "LD" ),
	LDD_YQ( LDD.class,				"10q0 qq0d dddd 1qqq", false, false, "LDD" ),
	LD_Z( LDD.class,					"1000 000d dddd 0000", false, false, "LD" )		// LDD Rd, Z+0
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.q, 0b0010_1100_0000_0111 );
		}
	},
	LD_ZP( LD.class,					"1001 000d dddd 0001", false, false, "LD" ),
	LD_DZ( LD.class,					"1001 000d dddd 0010", false, false, "LD" ),
	LDD_ZQ( LDD.class,				"10q0 qq0d dddd 0qqq", false, false, "LDD" ),
	LDI( LDI.class,					"1110 KKKK dddd KKKK", false, false, null ),
	LDS( LDS.class,					"1001 000d dddd 0000", false, true, null ),
	LDS_16( LDS16.class,				"1010 0kkk dddd kkkk", false, false, "LDS" ),
	LPM_R0_Z( LPM_R0.class,			"1001 0101 1100 1000", false, false, "LPM" ),
	LPM_Z( LPM.class,					"1001 000d dddd 0100", false, false, "LPM" ),
	LPM_ZP( LPM.class,				"1001 000d dddd 0101", false, false, "LPM" ),
	LSL( LSL.class,					"0000 11rd dddd rrrr", false, false, null ),
	LSR( LSR.class,					"1001 010d dddd 0110", false, false, null ),
	MOV( MOV.class,					"0010 11rd dddd rrrr", false, false, null ),
	MOVW( MOVW.class,					"0000 0001 dddd rrrr", false, false, null ),
	MUL( MUL.class,					"1001 11rd dddd rrrr", false, false, null ),
	MULS( MULS.class,					"0000 0010 dddd rrrr", false, false, null ),
	MULSU( MULSU.class,				"0000 0011 0ddd 0rrr", false, false, null ),
	NEG( NEG.class,					"1001 010d dddd 0001", false, false, null ),
	NOP( NOP.class,					"0000 0000 0000 0000", false, false, null ),
	OR( OR.class,						"0010 10rd dddd rrrr", false, false, null ),
	ORI( ORI.class,					"0110 KKKK dddd KKKK", false, false, null ),
	OUT( OUT.class,					"1011 1AAr rrrr AAAA", true, false, null ),
	POP( POP.class,					"1001 000d dddd 1111", false, false, null ),
	PUSH( PUSH.class,					"1001 001d dddd 1111", false, false, null ),
	RCALL( RCALL.class,				"1101 kkkk kkkk kkkk", false, false, null ),
	RET( RET.class,					"1001 0101 0000 1000", false, false, null ),
	RETI( RETI.class,					"1001 0101 0001 1000", false, false, null ),
	RJMP( RJMP.class,					"1100 kkkk kkkk kkkk", false, false, null ),
	ROL( ROL.class,					"0001 11rd dddd rrrr", false, false, null ),	// ADC Rd, Rd
	ROR( ROR.class,					"1001 010d dddd 0111", false, false, null ),
	SBC( SBC.class,					"0000 10rd dddd rrrr", false, false, null ),
	SBCI( SBCI.class,					"0100 KKKK dddd KKKK", false, false, null ),
	SBI( SBI.class,					"1001 1001 AAAA Abbb", false, false, null ),
	SBIC( SBIC.class,					"1001 1001 AAAA Abbb", false, false, null ),
	SBIS( SBIS.class,					"1001 1011 AAAA Abbb", false, false, null ),
	SBIW( SBIW.class,					"1001 0111 KKdd KKKK", false, false, null ),
	SBR( SBR.class,					"0110 KKKK dddd KKKK", false, false, null ),	// ORI Rd K
	SBRC( SBRC.class,					"1111 110r rrrr 0bbb", false, false, null ),
	SBRS( SBRS.class,					"1111 111r rrrr 0bbb", false, false, null ),
	SEC( SEC.class,					"1001 0100 0000 1000", false, false, null ),	// BSET 0
	SEH( SEH.class,					"1001 0100 0101 1000", false, false, null ),	// BSET 5
	SEI( SEI.class,					"1001 0100 0111 1000", false, false, null ),	// BSET 7
	SEN( SEN.class,					"1001 0100 0010 1000", false, false, null ),	// BSET 2
	SER( SER.class,					"1110 1111 dddd 1111", false, false, null )		// LDI Rd, 0xFF
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.K, 0b0000_1111_0000_1111 );
		}
	},
	SES( SES.class,					"1001 0100 0100 1000", false, false, null ),	// BSET 4
	SET( SET.class,					"1001 0100 0110 1000", false, false, null ),	// BSET 6
	SEV( SEV.class,					"1001 0100 0011 1000", false, false, null ),	// BSET 3
	SEZ( SEV.class,					"1001 0100 0001 1000", false, false, null ),	// BSET 1
	SLEEP( SLEEP.class,				"1001 0101 1000 1000", false, false, null ),
	SPM( SPM.class,					"1001 0101 1110 1000", true, false, null ),
	SPM_ZP( SPM.class,				"1001 0101 1111 1000", true, false, "SPM" ),
	ST_X( ST.class,					"1001 001r rrrr 1100", true, false, "ST" ),
	ST_XP( ST.class,					"1001 001r rrrr 1101", true, false, "ST" ),
	ST_DX( ST.class,					"1001 001r rrrr 1110", true, false, "ST" ),
	ST_Y( STD.class,					"1000 001r rrrr 1000", true, false, "ST" )		// STD Rd, Y+0
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.q, 0b0010_1100_0000_0111 );
		}
	},
	ST_YP( ST.class,					"1001 001r rrrr 1001", true, false, "ST" ),
	ST_DY( ST.class,					"1001 001r rrrr 1010", true, false, "ST" ),
	STD_YQ( STD.class,				"10q0 qq1r rrrr 1qqq", true, false, "STD" ),
	ST_Z( STD.class,					"1000 001r rrrr 0000", true, false, "ST" )		// STD Rd, Z+0
	{
		@Override
		protected void postProcessOpcode()
		{
			operands.put( OperandType.q, 0b0010_1100_0000_0111 );
		}
	},
	ST_ZP( ST.class,					"1001 001r rrrr 0001", true, false, "ST" ),
	ST_DZ( ST.class,					"1001 001r rrrr 0010", true, false, "ST" ),
	STD_ZQ( STD.class,				"10q0 qq1r rrrr 0qqq", true, false, "STD" ),
	STS( STS.class,					"1001 001r rrrr 0000", true, true, null ),
	STS_16( STS16.class,				"1010 1kkk rrrr kkkk", true, false, "STS" ),
	SUB( SUB.class,					"0001 10rd dddd rrrr", false, false, null ),
	SUBI( SUBI.class,					"0101 KKKK dddd KKKK", false, false, null ),
	SWAP( SWAP.class,					"1001 010d dddd 0010", false, false, null ),
	TST( TST.class,					"0010 00rd dddd rrrr", false, false, null ),	// AND Rd, Rd
	WDR( WDR.class,					"1001 0101 1010 1000", false, false, null ),
	XCH( XCH.class,					"1001 001d dddd 0100", false, false, null ),

	DATA( InstructionData.class,	"kkkk kkkk kkkk kkkk", false, false, null )
	{
		@Override
		protected void processOpcode()
		{
			// Make sure DATA is not processed as an AVR opcode
		}
	}

	;

	// -------------------------------------------------------------------------

	private static final EnumSet<InstructionSet> disabled =
		EnumSet.of( LDS_16, STS_16 );

	// =========================================================================
	// === Data ================================================================
	// =========================================================================

	protected static ResourceBundle res;

	protected static List<InstructionSet> decoderList;
	protected static Comparator<InstructionSet> instructionsComparator;

	// -------------------------------------------------------------------------

	protected final Class<? extends Instruction> clazz;
	protected final String opcode;
	protected final boolean isStore;
	protected final boolean is32bits;
	protected final String mnemonic;
	protected final String shortDescription;

	protected final Constructor<? extends Instruction> constructor;

	protected int opcodeValue;
	protected int opcodeMask;
	protected int opcodeMaskWeight;

	protected final Map<OperandType,Integer> operands
		= new HashMap<>();

	// =========================================================================
	// === CONSTRUCTOR(s) ======================================================
	// =========================================================================

	private InstructionSet( Class<? extends Instruction> clazz,
			String opcode, boolean isWrite, boolean is32bits, String mnemonic )
	{
		this.clazz = clazz;
		this.opcode = opcode;
		this.isStore = isWrite;
		this.is32bits = is32bits;
		this.mnemonic = mnemonic;

		// ----------------------------------------------------------------------
		// - Get description from "InstructionSet.properties" file --------------
		// ----------------------------------------------------------------------

		this.shortDescription = lookupDescription();

		// ----------------------------------------------------------------------
		// - Look for Instruction( InstructionSet entry, int opcode ) -----------
		// ----------------------------------------------------------------------

		Constructor<? extends Instruction> constructor = null;
		try
		{
			if( clazz != null )
			{
				final Class<?> [] parameters  = 
				{
					InstructionSet.class,
					Integer.TYPE
				};

				constructor = clazz.getConstructor( parameters );
			}
		}
		catch( Exception e )
		{
			throw new RuntimeException(
				"INTERNAL ERROR: "
				+ "Could not get constructor with parameters (InstructionSet,int)"
				+ " for instruction " + this.name(), e );
		}
		finally
		{
			this.constructor = constructor;
		}

		// --------------------------------------------------------------------
		// - Process opcode string for disassembler and operand masks ---------
		// --------------------------------------------------------------------

		processOpcode();

		// --------------------------------------------------------------------
		// - Entry specific tweaking ------------------------------------------
		// --------------------------------------------------------------------

		postProcessOpcode();
	}

	/**
	 * 
	 */
	protected void postProcessOpcode()
	{
	}

	// ========================================================================
	// ===
	// ========================================================================

	/**
	 * 
	 */
	protected void processOpcode()
	{
		// --------------------------------------------------------------------
		// - Initialise decoder list ------------------------------------------
		// --------------------------------------------------------------------

		if( decoderList == null )
		{
			decoderList = new ArrayList<>();

			instructionsComparator = new Comparator<InstructionSet>()
			{
				/**
				 *	Descending order of opcode mask weights
				 * 		Descending order of masks 
				 */
				@Override
				public int compare( InstructionSet o1, InstructionSet o2 )
				{
					int rc = o2.opcodeMaskWeight - o1.opcodeMaskWeight;
					if( rc == 0)
					{
						rc = o2.opcodeMask - o1.opcodeMask;
					}

					return rc;
				}
			};
		}
		
		// --------------------------------------------------------------------
		// - Process opcode string (see document ATMEL 0856.L 11/2016) --------
		// --------------------------------------------------------------------

		opcodeValue = 0x0000;
		opcodeMask = 0x0000;
		opcodeMaskWeight = 0;
		
		for( int i = 0 ; i < opcode.length() ; i++ )
		{
			char c = opcode.charAt( i );
			switch( c )
			{
				// ------------------------------------------------------------
				// - Opcode bit with value 0 ----------------------------------
				// ------------------------------------------------------------

				case '0':
				{
					opcodeValue <<= 1;
					opcodeValue &= ~ 0x0001;

					opcodeMask <<= 1;
					opcodeMask |= 0x0001;

					opcodeMaskWeight++;

					for( Map.Entry<OperandType,Integer> entry : operands.entrySet() )
					{
						int operandMask = entry.getValue();
						operandMask <<= 1;
					
						entry.setValue( operandMask );
					}

					break;
				}

				// ------------------------------------------------------------
				// - Opcode bit with value 1 ----------------------------------
				// ------------------------------------------------------------

				case '1':
				{
					opcodeValue <<= 1;
					opcodeValue |= 0x0001;

					opcodeMask <<= 1;
					opcodeMask |= 0x0001;

					opcodeMaskWeight++;
					
					for( Map.Entry<OperandType,Integer> entry : operands.entrySet() )
					{
						int operandMask = entry.getValue();
						operandMask <<= 1;
					
						entry.setValue( operandMask );
					}

					break;
				}

				// ------------------------------------------------------------
				// - Operand bit ----------------------------------------------
				// ------------------------------------------------------------

				case 'r':		// source
				case 'd':		// destination (or source)
				case 'K':		// constant data
				case 'k':		// constant address
				case 'q':		// displacement
				case 'A':		// IO address
				case 's':		// bit in SREG
				case 'b':		// bit in register or IO register
				{
					OperandType type = OperandType.lookup( c );
					if( type == null )
					{
						throw new RuntimeException(
							"INTERNAL ERROR: "
							+ "Could not find OperandType '" + c + "'"
							+ " found at position" + i
							+ " in \"" + opcode + "\" "
							+ "for instruction " + this.name() );
					}

					if( ! operands.containsKey( type ) )
					{
						operands.put( type, 0b0000_0000_0000_0000 );
					}

					for( Map.Entry<OperandType,Integer> entry : operands.entrySet() )
					{
						int operandMask = entry.getValue();
						operandMask <<= 1;
						operandMask |= (entry.getKey() == type) ? 1 : 0;

						entry.setValue( operandMask );
					}

					opcodeValue <<= 1;
					opcodeValue &= ~ 0x0001;

					opcodeMask <<= 1;
					opcodeMask &= ~ 0x0001;
					break;
				}

				// ------------------------------------------------------------
				// - For readability ------------------------------------------
				// ------------------------------------------------------------

				case ' ':		// spacer
				{
					break;
				}

				// ------------------------------------------------------------
				// - Ill formated opcode string - SHOULD NEVER EVER HAPPEN ----
				// ------------------------------------------------------------

				default:
				{
					throw new RuntimeException(
						"INTERNAL ERROR: "
						+ "Unexpected opcode character '" + c + "'"
						+ " found at position" + i
						+ " in \"" + opcode + "\" "
						+ "for instruction " + this.name() );
				}
			}
		}

		if( is32bits )
		{
			if( ! operands.containsKey( OperandType.k ) )
			{
				operands.put( OperandType.k, 0xFFFF );
			}
			else
			{
				int mask = operands.get( OperandType.k );
				mask = (mask << 16) | 0xFFFF;

				operands.put( OperandType.k, mask );
			}
		}

		// --------------------------------------------------------------------
		// - Add entry to decoder list ----------------------------------------
		// --------------------------------------------------------------------

		decoderList.add( this );
		decoderList.sort( instructionsComparator );
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	protected String lookupDescription()
	{
		if( res == null )
		{
			res	= ResourceBundle.getBundle( InstructionSet.class.getName() );
		}

		try
		{
			return res.getString( getMnemonic() );
		}
		catch( Exception e )
		{
			throw new RuntimeException(
				"INTERNAL ERROR: "
				+ "Could not find description "
				+ "for instruction " + this.name(), e );
		}
	}

	// ========================================================================
	// === DISABLING INSTRUCTIONS =============================================
	// ========================================================================

	// While analysing the instruction set, it appeared that the 16-bits
	// LDS/STS opcode collided with the LDD/STD instructions. In the future,
	// the descriptor will be used to enable/disable some instructions
	//
	//	LDS16		1010 0kkk dddd kkkk
	//	STS16		1010 1kkk dddd kkkk
	//
	//	STD Y+q		10q0 qq1d dddd 1qqq
	//	STD Z+q 	10q0 qq1d dddd 0qqq
	//	LDD Y+q		10q0 qq0d dddd 1qqq
	//	LDD Z+q		10q0 qq0d dddd 0qqq
	//

	/**
	 * 
	 * @return
	 */
	public boolean isDisabled()
	{
		return disabled.contains( this );
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEnabled()
	{
		return ! isDisabled();
	}

	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public void enable()
	{
		disabled.add( this ); 
	}

	/**
	 * 
	 */
	public void disable()
	{
		disabled.remove( this );
	}

	// ========================================================================
	// === GETTER(s) ==========================================================
	// ========================================================================

	/**
	 * Get the textual representation of the opcode as found int Atmel
	 * AVR Instruction Set Manual (0856.L 11/2016) document.
	 * 
	 * @return
	 */
	public String getOpcodeString()
	{
		return opcode;
	}

	/**
	 * Get the mnemonic for this instruction.
	 * 
	 * @return
	 */
	public String getMnemonic()
	{
		if( mnemonic == null )
		{
			return name();
		}

		return mnemonic;
	}

	public String getShortDescription()
	{
		return shortDescription;
	}

	// ------------------------------------------------------------------------

	public boolean isStore()
	{
		return isStore;
	}

	public boolean isLoad()
	{
		return ! isStore;
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns this instruction size in bits.
	 * 
	 * @return
	 */
	public int getOpcodeWidth()
	{
		return is32bits ? 32 : 16;
	}

	public int getOpcodeSize()
	{
		return is32bits ? 2 : 1;
	}

	/**
	 * Get the significant numeric value of the the opcode. For 32 bits
	 * instructions only the first word is returned as the second word is
	 * only data.
	 * 
	 * @return
	 */
	public int getOpcode()
	{
		return opcodeValue;
	}

	/**
	 * Get the mask to extract opcode bits from the assembled intruction.
	 * 
	 * @return
	 */
	public int getOpcodeMask()
	{
		return opcodeMask;
	}

	/**
	 * Get the count of bits for the opcode mask.
	 * 
	 * @return
	 */
	public int getOpcodeMaskWeight()
	{
		return opcodeMaskWeight;
	}

	// ------------------------------------------------------------------------

	/**
	 * Check if this InstructionSet entry has an operand of the given type.
	 * 
	 * @param type
	 * @return
	 * @See {@link OperandType}
	 */
	public boolean hasOperand( OperandType type )
	{
		return operands.containsKey( type );
	}

	/**
	 * Get the mask of a specific operand type. See {@link OperandType} for
	 * details.
	 * 
	 * @param type
	 * @return
	 * @See {@link OperandType}
	 */
	public int getOperandMask( OperandType type )
	{
		return operands.get( type );
	}

	public int setOperand( int opcode, OperandType type, int value )
	{
		if( ! operands.containsKey( type ) )
		{
			throw new RuntimeException(
				"INTERNAL ERROR: "
				+ "Could not find operand '" + type + "' "
				+ "for instruction " + this.name() );
		}

		int mask = operands.get( type );
		int rc = opcode;

		for( int from=0, to=0 ; to < (is32bits ? 32 : 16) ; to++ )
		{
			if( (mask & (1 << to)) == (1 << to) )
			{
				if( (opcode & (1 << from)) == (1 << from) )
				{
					rc |= (1 << to);
				}
				else
				{
					rc &= ~(1 << to);
				}

				from++;
			}
		}

		return rc;
	}

	/**
	 * Extract the value for the given operand type. For operands split at
	 * several locations in the opcode, the value is packed and can be directly
	 * used.
	 * 
	 * @param type
	 * @param data
	 * @return
	 */
	public int getOperand( OperandType type, int opcode )
	{
		if( ! operands.containsKey( type ) )
		{
			throw new RuntimeException(
				"INTERNAL ERROR: "
				+ "Could not find operand '" + type + "' "
				+ "for instruction " + this.name() );
		}

		int mask = operands.get( type );
		int rc = 0;

		for( int from=0, to=0 ; from < (is32bits ? 32 : 16) ; from++ )
		{
			if( (mask & (1 << from)) == (1 << from) )
			{
				if( (opcode & (1 << from)) == (1 << from) )
				{
					rc |= (1 << to);
				}
				else
				{
					rc &= ~(1 << to);
				}

				to++;
			}
		}

		return rc;
	}

	// ------------------------------------------------------------------------

	/**
	 * The the class implementing the execute/assemble/disassemble operations
	 * for this instruction.
	 * 
	 * @return
	 */
	public Class<? extends Instruction> getImplementingClass()
	{
		return clazz;
	}

	// ========================================================================
	// === Factory ============================================================
	// ========================================================================

	public Instruction getInstance( int data )
	{
		try
		{
			return constructor.newInstance( this, data );
		}
		catch( Exception e )
		{
			throw new RuntimeException(
				"INTERNAL ERROR: instantiation for instruction "
				+ this.name(), e );
		}
	}

	public Instruction getDataInstance( int data )
	{
		return DATA.getInstance( data );
	}

	// ========================================================================
	// === Entry lookup =======================================================
	// ========================================================================

	public boolean check( int data )
	{
		return ((data & getOpcodeMask()) == getOpcode());
	}

	// ------------------------------------------------------------------------

	public static InstructionSet getInstructionSetEntry( int data )
	{
		for( InstructionSet entry : decoderList )
		{
			if( entry.isDisabled() )
			{
				continue;
			}

			if( entry.check( data ) )
			{
				return entry;
			}
		}

		return null;
	}

	// ========================================================================
	// === SELF-TEST ==========================================================
	// ========================================================================

	/**
	 * <p>
	 * Load a core with all possible instructions.
	 * 
	 * <p>
	 * <b>!!! WARNING !!!</b> no boundary checks are made.
	 * 
	 * @param core
	 */
	@SuppressWarnings( "deprecation" )
	public static void selfTest( Core core )
	{
		core.loadProgram( flash ->
		{
			for( int address = 0, i = 0 ; i < 65536 ; i++ )
			{
				int opcode = i;

				InstructionSet entry = InstructionSet.getInstructionSetEntry( opcode );
				if( entry != null )
				{
					if( entry.getOpcodeWidth() == 32 )
					{
						opcode = ((opcode & 0xFFFF) << 16) + 0x0000;
					}

					Instruction instruction = entry.getInstance( opcode );
					instruction.setAddress( address*2 );

					flash[address] = instruction;

					if( entry.getOpcodeWidth() == 32 )
					{
						Instruction instruction2 = instruction.getSecondWord();
						instruction2.setAddress( (address*2) + 1 );

						flash[address+ 1] = instruction2;

						address++;
					}
				}
				else
				{
					Instruction instruction = new InstructionData( opcode );
					instruction.setAddress( address*2 );

					flash[address].setAddress( address );
				}

				address++;
			}
		} );
	}

	// ========================================================================
	// === Object =============================================================
	// ========================================================================

	/**
	 *	mnemonic
	 *	[
	 *		decoderListIndex
	 *		o=opcode m=opcodeMask : opcodeMaskWeight
	 *		//
	 *		operandKey=operandMask*
	 *	]
	 */
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( String.format( "%9s", this.name() ) )

		 .append( " [ " )

		 .append( String.format( "%3d", decoderList.indexOf( this ) ) )
		 .append( " o=" )
		 .append( StringUtils.toBinaryString( opcodeValue, 16 ) )
		 .append( " m=" )
		 .append( StringUtils.toBinaryString( opcodeMask, 16 ) )
		 .append( " : " )
		 .append( String.format( "%-2d", opcodeMaskWeight ) )
		 ;

		if( operands.size() > 0 )
		{
			s.append( " //" );
	
			for( Map.Entry<OperandType,Integer> entry : operands.entrySet() )
			{
				s.append( ' ' )
				 .append( entry.getKey() )
				 .append( '=' )
				 .append( StringUtils.toBinaryString( entry.getValue(), 16 ) )
				 ;
			}
		}

		s.append( " ]" );

		return s.toString();
	}
}
