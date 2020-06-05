/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class ASbrsInstruction extends PInstruction
{
    private TRegister _dest_;
    private PExpr _b_;

    public ASbrsInstruction()
    {
        // Constructor
    }

    public ASbrsInstruction(
        @SuppressWarnings("hiding") TRegister _dest_,
        @SuppressWarnings("hiding") PExpr _b_)
    {
        // Constructor
        setDest(_dest_);

        setB(_b_);

    }

    @Override
    public Object clone()
    {
        return new ASbrsInstruction(
            cloneNode(this._dest_),
            cloneNode(this._b_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASbrsInstruction(this);
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

    public PExpr getB()
    {
        return this._b_;
    }

    public void setB(PExpr node)
    {
        if(this._b_ != null)
        {
            this._b_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._b_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._dest_)
            + toString(this._b_);
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

        if(this._b_ == child)
        {
            this._b_ = null;
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

        if(this._b_ == oldChild)
        {
            setB((PExpr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}