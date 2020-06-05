package be.lmenten.avr.ui.rsyntax;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import be.lmenten.avr.core.instructions.InstructionSet;

public class AvrAssemblerTokenMaker
	extends AbstractTokenMaker
{
	public static final String MIME_TYPE
		= "text/avrasm";

	public static final String CLASS_NAME
		= AvrAssemblerTokenMaker.class.getName();

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public AvrAssemblerTokenMaker()
	{
	}

	// ========================================================================
	// === Install ============================================================
	// ========================================================================

	public static void register()
	{
		AbstractTokenMakerFactory atmf
			= (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();

		atmf.putMapping( MIME_TYPE, CLASS_NAME );
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public TokenMap getWordsToHighlight()
	{
	   TokenMap tokenMap = new TokenMap( true );

	   // ---------------------------------------------------------------------
	   // --- Directives ------------------------------------------------------
	   // ---------------------------------------------------------------------

	   tokenMap.put( ".equ", Token.RESERVED_WORD );
	   tokenMap.put( ".set", Token.RESERVED_WORD );
	   tokenMap.put( ".unset", Token.RESERVED_WORD );

	   tokenMap.put( ".def", Token.RESERVED_WORD );
	   tokenMap.put( ".undef", Token.RESERVED_WORD );

	   tokenMap.put( ".cseg", Token.RESERVED_WORD );
	   tokenMap.put( ".csegsize", Token.RESERVED_WORD );
	   tokenMap.put( ".dseg", Token.RESERVED_WORD );
	   tokenMap.put( ".eseg", Token.RESERVED_WORD );

	   tokenMap.put( ".org", Token.RESERVED_WORD );
	   tokenMap.put( ".overlap", Token.RESERVED_WORD );
	   tokenMap.put( ".nooverlap", Token.RESERVED_WORD );

	   tokenMap.put( ".byte", Token.RESERVED_WORD );
	   tokenMap.put( ".db", Token.RESERVED_WORD );
	   tokenMap.put( ".dw", Token.RESERVED_WORD );
	   tokenMap.put( ".dd", Token.RESERVED_WORD );
	   tokenMap.put( ".dq", Token.RESERVED_WORD );

	   tokenMap.put( ".if", Token.RESERVED_WORD );
	   tokenMap.put( ".ifef", Token.RESERVED_WORD );
	   tokenMap.put( ".ifndef", Token.RESERVED_WORD );
	   tokenMap.put( ".elif", Token.RESERVED_WORD );
	   tokenMap.put( ".else", Token.RESERVED_WORD );
	   tokenMap.put( ".endif", Token.RESERVED_WORD );
	   tokenMap.put( ".message", Token.RESERVED_WORD );
	   tokenMap.put( ".warning", Token.RESERVED_WORD );
	   tokenMap.put( ".error", Token.RESERVED_WORD );
	   tokenMap.put( ".exit", Token.RESERVED_WORD );

	   tokenMap.put( ".macro", Token.RESERVED_WORD );
	   tokenMap.put( ".endmacro", Token.RESERVED_WORD );
	   tokenMap.put( ".endm", Token.RESERVED_WORD );

	   tokenMap.put( ".list", Token.RESERVED_WORD );
	   tokenMap.put( ".listmac", Token.RESERVED_WORD );
	   tokenMap.put( ".nolist", Token.RESERVED_WORD );

	   // ---------------------------------------------------------------------
	   // --- Menmonics -------------------------------------------------------
	   // ---------------------------------------------------------------------

	   for( InstructionSet i : InstructionSet.values() )
	   {
		   tokenMap.put( i.getMnemonic(), Token.RESERVED_WORD_2 );
	   }

	   // ---------------------------------------------------------------------
	   // --- Functions -------------------------------------------------------
	   // ---------------------------------------------------------------------

	   tokenMap.put( "high", Token.FUNCTION );
	   tokenMap.put( "low", Token.FUNCTION );
	   tokenMap.put( "page", Token.FUNCTION );
	   tokenMap.put( "byte2", Token.FUNCTION );
	   tokenMap.put( "byte3", Token.FUNCTION );
	   tokenMap.put( "lwrd", Token.FUNCTION );
	   tokenMap.put( "hwrd", Token.FUNCTION );

	   tokenMap.put( "q7", Token.FUNCTION );
	   tokenMap.put( "q15", Token.FUNCTION );
	   tokenMap.put( "int", Token.FUNCTION );
	   tokenMap.put( "frac", Token.FUNCTION );

	   tokenMap.put( "abs", Token.FUNCTION );
	   tokenMap.put( "exp2", Token.FUNCTION );
	   tokenMap.put( "log2", Token.FUNCTION );
	   
	   tokenMap.put( "address", Token.FUNCTION );
	   tokenMap.put( "ioaddress", Token.FUNCTION );
	   tokenMap.put( "index", Token.FUNCTION );
	   tokenMap.put( "mask", Token.FUNCTION );
	   tokenMap.put( "offset", Token.FUNCTION );
	   tokenMap.put( "relative", Token.FUNCTION );
	   tokenMap.put( "to", Token.FUNCTION );
	   
	   return tokenMap;
	}

	private int currentTokenStart;
	private int currentTokenType;
	private int startTokenType;
	
	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public Token getTokenList( Segment text, int initialTokenType, int startOffset )
	{
		resetTokenList();

		char[] array = text.array;
		int offset = text.offset;
		int count = text.count;
		int end = offset + count;

		// Token starting offsets are always of the form:
		//
		// 		'startOffset + (currentTokenStart-offset)',
		//
		// but since startOffset and offset are constant, tokens' starting
		// positions become:
		//
		// 		'newStartOffset+currentTokenStart'.

		int newStartOffset = startOffset - offset;

		currentTokenStart = offset;
		currentTokenType = startTokenType;

		for( int i = offset; i < end ; i++ )
		{
			char c = array[i];

			switch( currentTokenType )
			{
				// ------------------------------------------------------------
				// ---
				// ------------------------------------------------------------

				case Token.NULL:

					currentTokenStart = i;   // Starting a new token here.

					switch( c )
					{
						case ' ':
						case '\t':
							currentTokenType = Token.WHITESPACE;
							break;

						case '"':
							currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
							break;

						case '#':
							currentTokenType = Token.COMMENT_EOL;
							break;

						default:
							if( RSyntaxUtilities.isDigit( c ) )
							{
								currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
								break;
							}
							else if( RSyntaxUtilities.isLetter(c) || c == '/' || c == '_' )
							{
								currentTokenType = Token.IDENTIFIER;
								break;
							}

							// Anything not currently handled - mark as an identifier

							currentTokenType = Token.IDENTIFIER;
							break;
					}

					break;

				// ------------------------------------------------------------
				// ---
				// ------------------------------------------------------------

				case Token.WHITESPACE:

					switch( c )
					{
						case ' ':
						case '\t':
							break;   // Still whitespace.

						case '"':
							addToken( text, currentTokenStart , i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart );
							currentTokenStart = i;
							currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
							break;

						case '#':
							addToken( text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart );
							currentTokenStart = i;
							currentTokenType = Token.COMMENT_EOL;
							break;

						default:   // Add the whitespace token and start anew.
							addToken( text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart );
							currentTokenStart = i;

							if( RSyntaxUtilities.isDigit( c ) )
							{
								currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
								break;
							}
							else if( RSyntaxUtilities.isLetter(c) || c == '/' || c == '_' )
							{
								currentTokenType = Token.IDENTIFIER;
								break;
							}

							// Anything not currently handled - mark as identifier

							currentTokenType = Token.IDENTIFIER;

					} // End of switch (c).

		            break;

				// ------------------------------------------------------------
				// ---
				// ------------------------------------------------------------

				default: // Should never happen

				// ------------------------------------------------------------
				// ---
				// ------------------------------------------------------------

				case Token.IDENTIFIER:

					switch( c )
					{
						case ' ':
						case '\t':
							addToken( text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart );
							currentTokenStart = i;
							currentTokenType = Token.WHITESPACE;
							break;

						case '"':
							addToken( text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart );
							currentTokenStart = i;
							currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
							break;

						default:
							if( RSyntaxUtilities.isLetterOrDigit(c) || c == '/' || c == '_' )
							{
								break;   // Still an identifier of some type.
							}

							// Otherwise, we're still an identifier (?).

					} // End of switch (c).

					break;

				// ------------------------------------------------------------
				// ---
				// ------------------------------------------------------------

				case Token.LITERAL_NUMBER_DECIMAL_INT:

					switch( c )
					{
						case ' ':
						case '\t':
							addToken( text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentTokenStart );
							currentTokenStart = i;
							currentTokenType = Token.WHITESPACE;
							break;

						case '"':
							addToken( text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
							break;

						default:
							if( RSyntaxUtilities.isDigit( c ) )
							{
								break;   // Still a literal number.
							}

							// Otherwise, remember this was a number and start over.

							addToken( text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset + currentTokenStart );
							i--;

							currentTokenType = Token.NULL;

					} // End of switch (c).

					break;

				// ------------------------------------------------------------
				// ---
				// ------------------------------------------------------------

				case Token.COMMENT_EOL:
					i = end - 1;

					addToken( text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart );

					// We need to set token type to null so at the bottom we don't add one more token.

					currentTokenType = Token.NULL;
					break;

				// ------------------------------------------------------------
				// ---
				// ------------------------------------------------------------

				case Token.LITERAL_STRING_DOUBLE_QUOTE:
					if( c == '"' )
					{
						addToken( text, currentTokenStart, i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset + currentTokenStart );

						currentTokenType = Token.NULL;
					}

					break;

			}	// End of switch (currentTokenType).

		} // End of for (int i=offset; i<end; i++).

		switch( currentTokenType )
		{
			// Remember what token type to begin the next line with.

			case Token.LITERAL_STRING_DOUBLE_QUOTE:
				addToken( text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart );
				break;

			// Do nothing if everything was okay.

			case Token.NULL:
				addNullToken();
				break;

			// All other token types don't continue to the next line...
				
			default:
				addToken( text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart );
				addNullToken();
		}


		// Return the first token in our linked list.

		return firstToken;
	}

	// ========================================================================
	// ===
	// ========================================================================

	@Override
	public void addToken( Segment segment,
							int start, int end,
							int tokenType, int startOffset )
	{
		// This assumes all keywords, etc. were parsed as "identifiers."

		if( tokenType==Token.IDENTIFIER )
		{
			int value = wordsToHighlight.get( segment, start, end );
			if( value != -1 )
			{
				tokenType = value;
			}
		}

		super.addToken( segment, start, end, tokenType, startOffset );
	}
}
