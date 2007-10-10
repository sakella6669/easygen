package org.easygen.ui.modules.hibernate.pages;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.ProjectConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.pages.ModulePage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;

/**
 * @author admin
 */
public class FieldConfigurationPage extends ModulePage {
	public static final String NAME = "fieldConfigurationPage";

	private static final String[] columnNames = new String[] {
			Localization.get("hibernate.label.fieldSelector.selected"),
			Localization.get("hibernate.label.fieldSelector.columnName"),
			Localization.get("hibernate.label.fieldSelector.columnType"),
			Localization.get("hibernate.label.fieldSelector.fieldName"),
			Localization.get("hibernate.label.fieldSelector.fieldType")
			// TODO Afficher les primary keys
			// TODO Afficher les foreign keys avec possibilité de les supprimer (d'un seul coté ou des deux)
		};

	private static final String[] javaTypes = { "java.lang.String", "java.lang.Character", "java.lang.Short",
			"java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.BigDecimal",
			"java.lang.BigInteger", "java.lang.Boolean", };

	private Table fieldConfigurationTable;

	private TableViewer tableViewer;

	private List<DataObject> dataObjects;

	private Combo tableChooser;

	/**
	 * @param pName
	 * @param pTitle
	 */
	public FieldConfigurationPage() {
		super(NAME, Localization.get("hibernate.title.fieldSelector"));
	}

	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#initControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void initControl(Composite mainComposite) {
		Group tableSelectionGroup = WidgetUtils.createGroup(mainComposite, Localization
				.get("hibernate.title.fieldSelector.fieldConfiguration"), 1);

		createTableCruiser(tableSelectionGroup);
		createFieldConfigurationTable(tableSelectionGroup);
	}

	protected void createTableCruiser(Composite pParent) {
		Composite cruiserComposite = new Composite(pParent, SWT.NONE);
		cruiserComposite.setLayout(new GridLayout(3, false));

		Button previousButton = WidgetUtils.createSimpleButton(cruiserComposite, "<");
		previousButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				previousItemInCombo();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		tableChooser = WidgetUtils.createCombo(cruiserComposite, javaTypes);
		Button nextButton = WidgetUtils.createSimpleButton(cruiserComposite, ">");
		nextButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				nextItemInCombo();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	protected void createFieldConfigurationTable(Composite pParent) {
		fieldConfigurationTable = WidgetUtils.createTable(pParent, 150);

		int index = 0;
		WidgetUtils.createTableColumn(fieldConfigurationTable, columnNames[index++], 20);
		WidgetUtils.createTableColumn(fieldConfigurationTable, columnNames[index++], 100);
		WidgetUtils.createTableColumn(fieldConfigurationTable, columnNames[index++], 150);
		WidgetUtils.createTableColumn(fieldConfigurationTable, columnNames[index++], 100);
		WidgetUtils.createTableColumn(fieldConfigurationTable, columnNames[index++], 150);

		tableViewer = WidgetUtils.createTableViewer(fieldConfigurationTable, columnNames);

		index = 0;
		// Create the cell editors
		CellEditor[] editors = new CellEditor[fieldConfigurationTable.getColumnCount()];
		// Column : Selected (CheckBox)
		editors[index++] = new CheckboxCellEditor(fieldConfigurationTable);
		// Column : Column Name (Text)
		editors[index++] = new TextCellEditor(fieldConfigurationTable);
		// Column : Column Type (Text)
		editors[index++] = new TextCellEditor(fieldConfigurationTable);
		// Column : Field Name (Text)
		editors[index++] = new TextCellEditor(fieldConfigurationTable);
		// Column : Field Type (Text)
		editors[index++] = new ComboBoxCellEditor(fieldConfigurationTable, javaTypes);
		// Assign the cell editors to the viewer
		tableViewer.setCellEditors(editors);

		// Set the content provider that extracts the data (DataFields[]) from an input DataObject
		tableViewer.setContentProvider(new FieldsContentProvider());
		// Create the Cell Modifier and the Label Provider for the viewer
		FieldsCellModifier fieldsCellModifier = new FieldsCellModifier(tableViewer, columnNames);
		tableViewer.setCellModifier(fieldsCellModifier);
		tableViewer.setLabelProvider(fieldsCellModifier);
	}

	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#init(org.easygen.core.config.ProjectConfig)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(ProjectConfig pProjectConfig) {
		super.init(pProjectConfig);
		dataObjects = (List<DataObject>) CollectionUtils.select(pProjectConfig.getObjects(), new Predicate() {
			public boolean evaluate(Object object) {
				return ((DataObject)object).isSelected();
			}
		});//SelectedObjectArray();
		addObjectNamesIntoCombo();
	}

	protected void addObjectNamesIntoCombo() {
		if (dataObjects.size() == 0)
			return ;

		int objectSize = dataObjects.size();
		String[] items = new String[objectSize];
		for (int i = 0; i < objectSize; i++) {
			items[i] = dataObjects.get(i).getTableName();
		}

		tableChooser.setItems(items);
		selectInCombo(0);
	}

	protected void nextItemInCombo() {
		int index = tableChooser.getSelectionIndex();
		if (index >= (tableChooser.getItemCount()-1))
			return;
		selectInCombo(index + 1);
	}

	protected void previousItemInCombo() {
		int index = tableChooser.getSelectionIndex();
		if (index == 0)
			return;
		selectInCombo(index - 1);
	}

	protected void selectInCombo(int i) {
		tableChooser.select(i);
		tableViewer.setInput(dataObjects.get(i));
	}
}
