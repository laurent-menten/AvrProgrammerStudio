/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.avr.assembler.parser.node;

import java.util.*;

import be.lmenten.avr.assembler.parser.analysis.*;

@SuppressWarnings("nls")
public final class AMaskExpr extends PExpr
{
    private TIdentifier _register_;
    private final LinkedList<TIdentifier> _bits_ = new LinkedList<TIdentifier>();

    public AMaskExpr()
    {
        // Constructor
    }

    public AMaskExpr(
        @SuppressWarnings("hiding") TIdentifier _register_,
        @SuppressWarnings("hiding") List<?> _bits_)
    {
        // Constructor
        setRegister(_register_);

        setBits(_bits_);

    }

    @Override
    public Object clone()
    {
        return new AMaskExpr(
            cloneNode(this._register_),
            cloneList(this._bits_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMaskExpr(this);
    }

    public TIdentifier getRegister()
    {
        return this._register_;
    }

    public void setRegister(TIdentifier node)
    {
        if(this._register_ != null)
        {
            this._register_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._register_ = node;
    }

    public LinkedList<TIdentifier> getBits()
    {
        return this._bits_;
    }

    public void setBits(List<?> list)
    {
        for(TIdentifier e : this._bits_)
        {
            e.parent(null);
        }
        this._bits_.clear();

        for(Object obj_e : list)
        {
            TIdentifier e = (TIdentifier) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._bits_.add(e);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._register_)
            + toString(this._bits_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._register_ == child)
        {
            this._register_ = null;
            return;
        }

        if(this._bits_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._register_ == oldChild)
        {
            setRegister((TIdentifier) newChild);
            return;
        }

        for(ListIterator<TIdentifier> i = this._bits_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((TIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}