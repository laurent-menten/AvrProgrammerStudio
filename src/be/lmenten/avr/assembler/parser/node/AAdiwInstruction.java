/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class AAdiwInstruction extends PInstruction
{
    private TRegisterPair _dest_;
    private PExpr _k_;

    public AAdiwInstruction()
    {
        // Constructor
    }

    public AAdiwInstruction(
        @SuppressWarnings("hiding") TRegisterPair _dest_,
        @SuppressWarnings("hiding") PExpr _k_)
    {
        // Constructor
        setDest(_dest_);

        setK(_k_);

    }

    @Override
    public Object clone()
    {
        return new AAdiwInstruction(
            cloneNode(this._dest_),
            cloneNode(this._k_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAdiwInstruction(this);
    }

    public TRegisterPair getDest()
    {
        return this._dest_;
    }

    public void setDest(TRegisterPair node)
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

    @Override
    public String toString()
    {
        return ""
            + toString(this._dest_)
            + toString(this._k_);
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

        if(this._k_ == child)
        {
            this._k_ = null;
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
            setDest((TRegisterPair) newChild);
            return;
        }

        if(this._k_ == oldChild)
        {
            setK((PExpr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
