package be.lmenten.avr.ui;

import javax.swing.UIManager;

import be.lmenten.avr.ui.map.BasicCoreMemoryMapUI;
import be.lmenten.avr.ui.map.JCoreMemoryMap;

public class CoreUIDefaults
{
	public static void installDefaults()
	{
		UIManager.getDefaults().put( JCoreMemoryMap.uiClassID,
				BasicCoreMemoryMapUI.class.getName() );
	}
}
