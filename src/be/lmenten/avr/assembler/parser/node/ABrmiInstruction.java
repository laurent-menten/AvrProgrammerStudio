/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class ABrmiInstruction extends PInstruction
{
    private PExpr _k_;

    public ABrmiInstruction()
    {
        // Constructor
    }

    public ABrmiInstruction(
        @SuppressWarnings("hiding") PExpr _k_)
    {
        // Constructor
        setK(_k_);

    }

    @Override
    public Object clone()
    {
        return new ABrmiInstruction(
            cloneNode(this._k_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABrmiInstruction(this);
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
            + toString(this._k_);
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

        throw new RuntimeException("Not a child.");
    }
}
