package org.easygen.ui.util;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * @author eveno
 * Created on 2 mars 07
 *
 */
public class WidgetUtils {

	private WidgetUtils() {
	}

	public static String getComboSelection(Combo combo) {
		int index = combo.getSelectionIndex();
		return combo.getItem(index);
	}

	public static Group createGroup(Composite pParent, String pTitle, int pNumColumns) {
		Group layerGroup = new Group(pParent, SWT.NONE);
		layerGroup.setText(pTitle);
		layerGroup.setLayout(new GridLayout(pNumColumns, false));

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		layerGroup.setLayoutData(gridData);

		return layerGroup;
	}

	public static Combo createLabelComboPair(Composite pParent, String pLabel, String[] pItems) {
		createLabel(pParent, pLabel);
		return createCombo(pParent, pItems);
	}

	public static Text createLabelTextPair(Composite pParent, String pLabel, String pDefaultText) {
		createLabel(pParent, pLabel);
		return createText(pParent, pDefaultText);
	}

	public static Button createCheckBox(Composite pParent, String pLabel, boolean defaultSelected) {
		Button b = createButton(pParent, pLabel, SWT.CHECK | SWT.RIGHT);
		b.setSelection(defaultSelected);
		return b;
	}

	public static Button createSimpleButton(Composite pParent, String pLabel) {
		return createButton(pParent, pLabel, SWT.NONE);
	}

	private static Button createButton(Composite pParent, String pLabel, int pStyle) {
		Button button = new Button(pParent, pStyle);
		button.setText(pLabel);
		return button;
	}

	private static Text createText(Composite pParent, String pDefaultText) {
		Text textField = new Text(pParent, SWT.BORDER);
		textField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (pDefaultText != null)
			textField.setText(pDefaultText);

		return textField;
	}

	public static Label createLabel(Composite pParent, String pLabel) {
		Label label = new Label(pParent, SWT.LEFT);
		label.setText(pLabel);
		return label;
	}

	public static Combo createCombo(Composite pParent, String[] pItems) {
		Combo chosenLayer = new Combo(pParent, SWT.READ_ONLY);
		chosenLayer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		chosenLayer.setItems(pItems);
		chosenLayer.select(0);
		return chosenLayer;
	}

	public static void createHorizontalSpan(Composite pComposite, int pColumn) {
		GridData buttonData = null;
		if (pComposite.getLayoutData() == null || !(pComposite.getLayoutData() instanceof GridData))
			buttonData = new GridData();
		else
			buttonData = (GridData) pComposite.getLayoutData();
		buttonData.horizontalSpan = pColumn;
		pComposite.setLayoutData(buttonData);
	}

	public static void createHorizontalSpan(Control pControl, int pColumn) {
		GridData buttonData = null;
		if (pControl.getLayoutData() == null || !(pControl.getLayoutData() instanceof GridData))
			buttonData = new GridData();
		else
			buttonData = (GridData) pControl.getLayoutData();
		buttonData.horizontalSpan = pColumn;
		pControl.setLayoutData(buttonData);
	}

	public static Table createTable(Composite pParent, int minimumHeight) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION;
		/*SWT.CHECK | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.HIDE_SELECTION*/

		Table newTable = new Table(pParent, style);
		newTable.setHeaderVisible(true);
		newTable.setLinesVisible(true);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.minimumHeight = minimumHeight;
		gridData.heightHint = minimumHeight;
		newTable.setLayoutData(gridData);

		return newTable;
	}

	public static TableColumn createTableColumn(Table tableParent, String columnName, int width) {
		TableColumn newTableColumn = createTableColumn(tableParent, columnName);
		newTableColumn.setWidth(width);
		return newTableColumn;
	}

	public static TableColumn createTableColumn(Table tableParent, String columnName) {
		TableColumn newTableColumn = new TableColumn(tableParent, SWT.LEFT);
		newTableColumn.setText(columnName);
		return newTableColumn;
	}

	public static Button createRadioButton(Composite pParent, String pLabel) {
		return createButton(pParent, pLabel, SWT.RADIO);
	}

	public static TableViewer createTableViewer(Table table, String[] columnNames) {
		TableViewer tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columnNames);
		return tableViewer;
	}
}
