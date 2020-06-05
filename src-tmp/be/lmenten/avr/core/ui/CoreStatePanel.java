package be.lmenten.avr.core.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import be.lmenten.avr.core.tmp.Core;

/*
PC 0000	      SP 0000
SREG 00 ithsvnzc

r0 00  r1 00	r16 00 r17 00
r2 00  r3 00	r18 00 r19 00
r4 00  r5 00	r20 00 r21 00
r6 00  r7 00	r22 00 r23 00
r8 00  r9 00	r24 00 r25 00
r10 00 r11 00	r26 00 r27 00
r12 00 r13 00	r28 00 r29 00
r14 00 r15 00	r30 00 r31 00

RAMPD 00		     RAMPX 00
EIND 00		     RAPMY 00
			     RAMPZ 00
*/

public class CoreStatePanel
	extends JPanel
{
	private static final long serialVersionUID = 1L;

	// ========================================================================
	// ===
	// ========================================================================

	private final Core core;

	// ========================================================================
	// ===
	// ========================================================================

	public CoreStatePanel( Core core )
	{
		this.core = core;

		setLayout( new GridBagLayout() );

		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 8;
		c.gridheight = 13;
	}

	public void update()
	{
	}
}
