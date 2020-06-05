package be.lmenten.avr.ui.test;

import javax.swing.UIManager;

public class Install
{
	public static void install()
	{
		UIManager.getDefaults().put( CoreStateView.uiClassID, BasicCoreStateViewUI.class.getName() );
		UIManager.getDefaults().put( CoreRegisterView.uiClassID, BasicCoreRegisterViewUI.class.getName() );
	}
}
