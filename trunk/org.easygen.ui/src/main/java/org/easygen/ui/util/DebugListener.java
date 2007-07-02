package org.easygen.ui.util;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

/**
 * @author eveno
 * Created on 9 mars 07
 *
 */
public class DebugListener implements ModifyListener
{
	/**
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText(ModifyEvent pEvent)
	{
		if (pEvent.getSource() instanceof Combo)
			System.out.println(WidgetUtils.getComboSelection((Combo) pEvent.getSource()));
		else if (pEvent.getSource() instanceof Text)
			System.out.println( ((Text) pEvent.getSource()).getText() );
		else
			System.out.println("Unknown ...");
	}
}
