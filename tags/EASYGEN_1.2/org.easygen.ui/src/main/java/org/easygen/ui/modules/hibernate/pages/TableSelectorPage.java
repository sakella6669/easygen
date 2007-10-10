package org.easygen.ui.modules.hibernate.pages;

import java.util.List;

import org.easygen.core.config.DataObject;
import org.easygen.core.config.ProjectConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.pages.ModulePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;

/**
 * @author Mektoub Created on 23 mars 07
 *
 */
public class TableSelectorPage extends ModulePage
{
	public static final String NAME = "tableSelectorPage";

	private Table tableSelectionTable = null;

	private TableViewer tableViewer;

	private Button selectDeselectButton;

	private List<DataObject> objects;

	private static final String[] columnNames = new String[] {
	        Localization.get("hibernate.label.tableSelector.selected"),
	        Localization.get("hibernate.label.tableSelector.tableName"),
	        Localization.get("hibernate.label.tableSelector.className"),
	        Localization.get("hibernate.label.tableSelector.useCache") };

	/**
	 * @param pName
	 * @param pTitle
	 */
	public TableSelectorPage()
	{
		super(NAME, Localization.get("hibernate.title.tableSelector"));
	}

	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#initControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void initControl(Composite pMainComposite)
	{
		createTableSelectionGroup(pMainComposite);
	}

	/**
	 * @see org.easygen.ui.wizards.pages.ICommonPage#init(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void init(ProjectConfig pProjectConfig)
	{
		objects = pProjectConfig.getObjects();
		tableViewer.setInput(objects);
		setPageComplete(validatePage());
	}

	/**
	 * This method initializes tableSelectionGroup
	 */
	protected void createTableSelectionGroup(Composite pParent)
	{
		Group tableSelectionGroup = WidgetUtils.createGroup(pParent, Localization
		        .get("hibernate.title.tableSelector.tableSelection"), 1);

		selectDeselectButton = WidgetUtils.createSimpleButton(tableSelectionGroup, Localization.get("hibernate.label.tableSelector.invertSelection"));

		tableSelectionTable = WidgetUtils.createTable(tableSelectionGroup, 200);
		tableSelectionTable.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				setPageComplete(validatePage());
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		int index = 0;
		WidgetUtils.createTableColumn(tableSelectionTable, columnNames[index++], 20);
		WidgetUtils.createTableColumn(tableSelectionTable, columnNames[index++], 150);
		WidgetUtils.createTableColumn(tableSelectionTable, columnNames[index++], 150);
		WidgetUtils.createTableColumn(tableSelectionTable, columnNames[index++], 100);

		tableViewer = WidgetUtils.createTableViewer(tableSelectionTable, columnNames);

		index = 0;
		// Create the cell editors
		CellEditor[] editors = new CellEditor[tableSelectionTable.getColumnCount()];
		// Column : Selected (CheckBox)
		editors[index++] = new CheckboxCellEditor(tableSelectionTable);
		// Column : Table Name (Text)
		editors[index++] = new TextCellEditor(tableSelectionTable);
		// Column : Class Name (Text)
		editors[index++] = new TextCellEditor(tableSelectionTable);
		// Column : Use cache (Combo Box)
		editors[index++] = new CheckboxCellEditor(tableSelectionTable);
		// Assign the cell editors to the viewer
		tableViewer.setCellEditors(editors);

		// Create the cell modifier and label provider for the viewer
		TableCellModifier tableCellModifier = new TableCellModifier(tableViewer, columnNames);
		tableViewer.setCellModifier(tableCellModifier);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(tableCellModifier);

		selectDeselectButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				for (DataObject data : objects) {
					data.setSelected(!data.isSelected());
				}
				tableViewer.refresh();
				setPageComplete(validatePage());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#validatePage()
	 */
	@Override
	public boolean validatePage() {
		if (!super.validatePage())
			return false;
		if (objects == null) {
			String msg = Localization.get("easygen.message.error.field.required", new String[] { "tables" });
			setErrorMessage(msg);
			return false;
		}
		for (DataObject object : objects) {
			if (object.isSelected())
				return true;
		}
		return false;		
	}
}
