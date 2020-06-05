package be.lmenten.avr.ui.test;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class CoreRegisterView
	extends JComponent
{
	private static final long serialVersionUID = 1L;

	public static final String uiClassID = "CoreRegisterViewUI";
	public static final String PROPERTY_MODEL = "model";

	private CoreRegisterModel model;

	// ========================================================================
	// === CONSTRUTOR(s) ======================================================
	// ========================================================================

	public CoreRegisterView()
	{
		updateUI();
	}

	// ========================================================================
	// === MODEL ==============================================================
	// ========================================================================

	public void setModel( CoreRegisterModel model )
	{
		if( this.model != model )
		{
			CoreRegisterModel oldModel = this.model;
			this.model = model;
			firePropertyChange( PROPERTY_MODEL, oldModel, model );
		}
	}

	public CoreRegisterModel getModel()
	{
		return model;
	}

	// ========================================================================
	// === UI DELEGATE ========================================================
	// ========================================================================

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

	// ------------------------------------------------------------------------

	public void setUI( BasicCoreRegisterViewUI ui )
	{
		super.setUI( ui );
	}

	@Override
    public void updateUI()
	{
		if( UIManager.get( getUIClassID() ) != null )
		{
			CoreRegisterViewUI ui = (CoreRegisterViewUI) UIManager.getUI( this );
			setUI( ui );
		}
		else
		{
			setUI( new BasicCoreRegisterViewUI() );
        }
    }

	public BasicCoreRegisterViewUI getUI()
	{
		return (BasicCoreRegisterViewUI) ui;
	}
}
