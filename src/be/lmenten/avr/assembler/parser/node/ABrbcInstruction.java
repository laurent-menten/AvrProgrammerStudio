/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class ABrbcInstruction extends PInstruction
{
    private PExpr _k_;
    private PExpr _s_;

    public ABrbcInstruction()
    {
        // Constructor
    }

    public ABrbcInstruction(
        @SuppressWarnings("hiding") PExpr _k_,
        @SuppressWarnings("hiding") PExpr _s_)
    {
        // Constructor
        setK(_k_);

        setS(_s_);

    }

    @Override
    public Object clone()
    {
        return new ABrbcInstruction(
            cloneNode(this._k_),
            cloneNode(this._s_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABrbcInstruction(this);
    }

    public PExpr getK()
    {
        return this._k_;
    }

    public void setK(PExpr node)
    {
        if(this._k_ != null)
        {
            this._k_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._k_ = node;
    }

    public PExpr getS()
    {
        return this._s_;
    }

    public void setS(PExpr node)
    {
        if(this._s_ != null)
        {
            this._s_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._s_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._k_)
            + toString(this._s_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._k_ == child)
        {
            this._k_ = null;
            return;
        }

        if(this._s_ == child)
        {
            this._s_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._k_ == oldChild)
        {
            setK((PExpr) newChild);
            return;
        }

        if(this._s_ == oldChild)
        {
            setS((PExpr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}