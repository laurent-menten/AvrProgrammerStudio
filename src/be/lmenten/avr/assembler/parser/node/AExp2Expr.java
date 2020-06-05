/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class AExp2Expr extends PExpr
{
    private PExpr _arg_;

    public AExp2Expr()
    {
        // Constructor
    }

    public AExp2Expr(
        @SuppressWarnings("hiding") PExpr _arg_)
    {
        // Constructor
        setArg(_arg_);

    }

    @Override
    public Object clone()
    {
        return new AExp2Expr(
            cloneNode(this._arg_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExp2Expr(this);
    }

    public PExpr getArg()
    {
        return this._arg_;
    }

    public void setArg(PExpr node)
    {
        if(this._arg_ != null)
        {
            this._arg_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._arg_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._arg_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._arg_ == child)
        {
            this._arg_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._arg_ == oldChild)
        {
            setArg((PExpr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}