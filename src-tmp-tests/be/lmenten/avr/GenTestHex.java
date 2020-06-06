package be.lmenten.avr;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class GenTestHex {

	public static void main( String[] args )
		throws FileNotFoundException
	{
		PrintStream fileOut = new PrintStream( "./test.hex" );
		System.setOut( fileOut );

		System.out.printf( ":%02X%04X00", 32, 0x0000 );
		int checksum = 32;

		for( int i = 0 ; i < 65536 ; i++ )
		{
			checksum += ((i >> 8) & 0xFF) + (i & 0xFF);

			if( ((i-1) % 32) == 31 )
			{
				System.out.printf( "%02X\n", (0 - checksum) & 0xFF );

				System.out.printf( ":%02X%04X00", 32, i );
				checksum += ((i >> 8) & 0xFF) + (i & 0xFF);
			}

			System.out.printf( "%02X%02X", (i & 0xFF), ((i >> 8) & 0xFF) );
		}
			
		System.out.printf( "%02X\n", (0 - checksum) & 0xFF );
		System.out.printf( ":00000001FF\n" );
	}
}
