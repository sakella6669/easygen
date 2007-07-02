package org.easygen.ui.wizards.validator;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.SWTUtils;
import org.easygen.ui.wizards.pages.ICommonPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Mektoub
 * Created on 20 mars 07
 */
public class Validator {
	public static final short FREE = 0;
	public static final short ALPHA = 1;
	public static final short ALPHA_NUMERIC = 2;
	public static final short NUMERIC = 3;
	public static final short PATH = 4;
	public static final short PACKAGE = 5;

	private Log logger = LogFactory.getLog(getClass());
	private ICommonPage wizardPage;
	private ModifyListener listener;
	private List<ValidateField> fields = new LinkedList<ValidateField>();

	/**
	 *
	 */
	public Validator(ICommonPage pPage) {
		wizardPage = pPage;
		listener = new ModifyListener(pPage);
	}

	public void addField(String keyName, Control field, short fieldType) {
		logger.debug("Adding field to validate: " + keyName);
		fields.add(new ValidateField(keyName, field, fieldType));
		addListenerToControl(field);
	}

	/**
	 * @param c
	 * @param listener
	 */
	protected void addListenerToControl(Control c) {
		if (c instanceof Text)
			c.addListener(SWT.Modify, listener);
		else if (c instanceof Button)
			c.addListener(SWT.Selection, listener);
		else if (c instanceof Label)
			c.addListener(SWT.Modify, listener);
	}

	/**
	 * @return
	 */
	public boolean validateFields() {
		resetError();
		for (ValidateField field : fields) {
			if (SWTUtils.isEmpty(field.getControl())) {
				showRequiredError(field.getFieldName());
				return false;
			}
			if (validateField(field) == false) {
				showValueError(field.getFieldName());
				return false;
			}
		}
		return true;
	}

	/**
	 * @param pField
	 * @return
	 */
	protected boolean validateField(ValidateField pField) {
		boolean result = false;
		switch (pField.getFieldType()) {
		case FREE:
			result = true;
			break;
		case ALPHA:
			result = validateAlpha(SWTUtils.getText(pField.getControl()));
			break;
		case ALPHA_NUMERIC:
			result = validateAlphaNumeric(SWTUtils.getText(pField.getControl()));
			break;
		case NUMERIC:
			result = validateNumeric(SWTUtils.getText(pField.getControl()).toString());
			break;
		case PATH:
			result = validatePath(SWTUtils.getText(pField.getControl()));
			break;
		case PACKAGE:
			result = validatePackage(SWTUtils.getText(pField.getControl()));
			break;
		}
		if (result == false)
			logger.warn("Invalid Field: " + pField.getFieldName());
		return result;
	}

	/**
	 * @param pText
	 * @return
	 */
	protected boolean validateAlpha(String pText) {
		return StringUtils.isAlpha(pText);
	}

	/**
	 * @param pText
	 * @return
	 */
	protected boolean validateAlphaNumeric(String pText) {
		return StringUtils.isAlphanumeric(pText);
	}

	/**
	 * @param pString
	 * @return
	 */
	protected boolean validateNumeric(String pText) {
		return StringUtils.isNumeric(pText);
	}

	/**
	 * @param pText
	 * @return
	 */
	protected boolean validatePackage(String pText) {
		for (int i = 0; i < pText.length(); i++) {
			char currentChar = pText.charAt(i);
			if (currentChar != '.' && Character.isJavaIdentifierPart(currentChar) == false)
				return false;
		}
		return true;
	}

	/**
	 * @param pText
	 * @return
	 */
	protected boolean validatePath(String pText) {
		for (int i = 0; i < pText.length(); i++) {
			char currentChar = pText.charAt(i);
			if (CharUtils.isAsciiAlphanumeric(currentChar) == false && currentChar != '.' && currentChar != ':'
					&& currentChar != '-' && currentChar != '_' && currentChar != '/' && currentChar != '\\')
				return false;
		}
		return true;
	}

	/**
	 *
	 */
	protected void resetError() {
		((WizardPage) wizardPage).setErrorMessage(null);
	}

	/**
	 * @param fieldName
	 */
	protected void showRequiredError(String fieldName) {
		String msg = Localization.get("easygen.message.error.field.required", new String[] { fieldName });
		((WizardPage) wizardPage).setErrorMessage(msg);
	}

	/**
	 * @param fieldName
	 */
	protected void showValueError(String fieldName) {
		String msg = Localization.get("easygen.message.error.field.wrongformat", new String[] { fieldName });
		((WizardPage) wizardPage).setErrorMessage(msg);
	}
}
