/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class ALslInstruction extends PInstruction
{
    private TRegister _dest_;

    public ALslInstruction()
    {
        // Constructor
    }

    public ALslInstruction(
        @SuppressWarnings("hiding") TRegister _dest_)
    {
        // Constructor
        setDest(_dest_);

    }

    @Override
    public Object clone()
    {
        return new ALslInstruction(
            cloneNode(this._dest_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALslInstruction(this);
    }

    public TRegister getDest()
    {
        return this._dest_;
    }

    public void setDest(TRegister node)
    {
        if(this._dest_ != null)
        {
            this._dest_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dest_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._dest_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._dest_ == child)
        {
            this._dest_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._dest_ == oldChild)
        {
            setDest((TRegister) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
