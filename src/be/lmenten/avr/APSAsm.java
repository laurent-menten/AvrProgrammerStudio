package be.lmenten.avr;

import be.lmenten.avr.assembler.parser.lexer.LexerException;
import be.lmenten.avr.assembler.parser.node.Token;
import be.lmenten.avr.assembler.parser.parser.ParserException;
import be.lmenten.avr.core.descriptor.CoreDescriptor;
import be.lmenten.avr.core.descriptor.SupportedCore;
import be.lmenten.avr.project.AvrExternalSource;
import be.lmenten.avr.project.AvrSource;

public class APSAsm {

	public static void main( String[] args )
	{
		if( args.length != 1 )
		{
			System.out.println( "java " + APSAsm.class.getName() + " <file.asm>" );
			System.exit( 0 );;
		}

		// --------------------------------------------------------------------
		try
		{
			CoreDescriptor cdesc = SupportedCore.ATMEGA2560.getDescriptor();

			AvrSource src = new AvrExternalSource( "test.asm", cdesc );

			try
			{
				src.parse();
			}
			catch( LexerException e )
			{
				Token t = e.getToken();				
				int i = e.getMessage().indexOf( ' ' );
	
				System.out.printf( "LEXER ERROR [%s] (%d,%d) : found '%s', %s.",
					t.getClass().getSimpleName(),
					t.getLine() - src.getPrologLineCount(),
					t.getPos(),
					t.getText(),
					e.getMessage().substring( i+1 ) );
			}
			catch( ParserException e )
			{
				Token t = e.getToken();				
				int i = e.getMessage().indexOf( ' ' );
	
				System.out.printf( "PARSER ERROR [%s] (%d,%d) : found '%s', %s.",
					t.getClass().getSimpleName(),
					t.getLine() - src.getPrologLineCount(),
					t.getPos(),
					t.getText(),
					e.getMessage().substring( i+1 ) );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}		
	}
}
