package org.easygen.ui.wizards.pages;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

public class CheckSelectedListener implements SelectionListener
{
	private Control[] controls;
	private boolean sameStatus = false;

	/**
	 */
	public CheckSelectedListener(Control pControl)
	{
		this(new Control[] { pControl });
	}
	/**
	 */
	public CheckSelectedListener(Control pControl, boolean pSameStatus)
	{
		this(new Control[] { pControl }, pSameStatus);
	}
	/**
	 * @param pControls
	 */
	public CheckSelectedListener(Control[] pControls)
	{
		this(pControls, false);
	}
	/**
	 * @param pControls
	 */
	public CheckSelectedListener(Control[] pControls, boolean pSameStatus)
	{
		this.controls = pControls;
		this.sameStatus  = pSameStatus;
	}
	/**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent pEvent)
    {
    	handleButtonEvent((Button) pEvent.getSource());	    
    }
	/**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent pEvent)
    {
    	handleButtonEvent((Button) pEvent.getSource());	    
    }
	/**
	 * @param pButton
	 */
	public void handleButtonEvent(Button pButton)
	{
		for (Control control : controls) {
			if (sameStatus)
				control.setEnabled(pButton.getSelection());
			else
				control.setEnabled( ! pButton.getSelection() );
		}
	}
}
