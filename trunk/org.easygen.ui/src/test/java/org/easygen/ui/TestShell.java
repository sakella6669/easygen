package org.easygen.ui;

import org.easygen.ui.wizards.NewProjectWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author eveno
 * Created on 2 mars 07
 *
 */
public class TestShell
{
	/**
	 *
	 */
	public TestShell()
	{
		createShell();
	}

	/**
	 * This method initializes sShell
	 */
	private void createShell()
	{
	    Display display = new Display();
	    Shell shell = new Shell(display);

		NewProjectWizard project = new NewProjectWizard();
    	WizardDialog dialog = new WizardDialog(shell, project);
    	project.setContainer(dialog);
		dialog.setPageSize(500, 350);
		project.init(null, null);
    	dialog.open();

//		sShell.setSize(new Point(400, 400));
//		sShell.setText("TestShell");
//		sShell.setLayout(new GridLayout(1, true));
//		sShell.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

//		new ChooseModulePage(sShell);
//		new NewProjectPage(sShell);
//		new GenericModulePage(sShell);

//		sShell.pack();
//		sShell.open();

//	    while (!sShell.isDisposed())
//	      if (!display.readAndDispatch())
//	        display.sleep();
//
//	    project.close();
//	    display.dispose();
	}

	public static void main(String[] args)
    {
		new TestShell();
    }
}
