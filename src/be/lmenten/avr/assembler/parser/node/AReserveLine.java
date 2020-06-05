/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class AReserveLine extends PLine
{
    private PExpr _size_;

    public AReserveLine()
    {
        // Constructor
    }

    public AReserveLine(
        @SuppressWarnings("hiding") PExpr _size_)
    {
        // Constructor
        setSize(_size_);

    }

    @Override
    public Object clone()
    {
        return new AReserveLine(
            cloneNode(this._size_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAReserveLine(this);
    }

    public PExpr getSize()
    {
        return this._size_;
    }

    public void setSize(PExpr node)
    {
        if(this._size_ != null)
        {
            this._size_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._size_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._size_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._size_ == child)
        {
            this._size_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._size_ == oldChild)
        {
            setSize((PExpr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
