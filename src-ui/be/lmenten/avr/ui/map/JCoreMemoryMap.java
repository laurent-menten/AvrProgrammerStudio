package be.lmenten.avr.ui.map;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class JCoreMemoryMap
	extends JComponent
{
	private static final long serialVersionUID = 1L;
	public static final String uiClassID = "CoreMemoryMapUI";

	// ------------------------------------------------------------------------

	private CoreMemoryMapModel model;

	// ========================================================================
	// === CONSTRUCTOR(s) =====================================================
	// ========================================================================

	public JCoreMemoryMap()
	{
		setModel( new DefaultCoreMemoryMapModel() );

		updateUI();
	}

	// ========================================================================
	// === MODEL ==============================================================
	// ========================================================================

	public void setModel( CoreMemoryMapModel newModel )
	{
		if( model != newModel )
		{
			CoreMemoryMapModel oldModel = model;
			this.model = newModel;

			firePropertyChange( "model", oldModel, newModel );
		}
	}

	public CoreMemoryMapModel getModel()
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

	public void setUI( CoreMemoryMapUI ui )
	{
		super.setUI( ui );
	}

	@Override
	public void updateUI()
	{
		if( UIManager.get( getUIClassID() ) != null )
		{
			CoreMemoryMapUI ui = (CoreMemoryMapUI) UIManager.getUI( this );
			setUI( ui );
		}
		else
		{
			setUI( new BasicCoreMemoryMapUI() );
        }
	}

	public BasicCoreMemoryMapUI getUI()
	{
        return (BasicCoreMemoryMapUI) ui;
    }
}
