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

package be.lmenten.avr.assembler.parser;

import java.io.IOException;
import java.io.PushbackReader;

import be.lmenten.avr.assembler.parser.lexer.Lexer;
import be.lmenten.avr.assembler.parser.lexer.LexerException;

public class AvrSourceLexer
	extends Lexer
{
	public AvrSourceLexer( PushbackReader in )
	{
		super( in );
	}

	@Override
	protected void filter()
		throws LexerException, IOException
	{
		super.filter();
	}
}
