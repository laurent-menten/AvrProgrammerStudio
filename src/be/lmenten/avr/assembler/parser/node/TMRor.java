/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class TMRor extends Token
{
    public TMRor(String text)
    {
        setText(text);
    }

    public TMRor(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TMRor(getText(), getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTMRor(this);
    }
}
