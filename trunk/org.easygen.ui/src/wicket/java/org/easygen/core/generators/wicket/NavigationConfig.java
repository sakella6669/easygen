package org.easygen.core.generators.wicket;

public class NavigationConfig {

	private boolean showPage;
	private boolean addPage;
	private boolean viewPage;
	private boolean editPage;
	private boolean enableRemoveObject;

	public boolean isShowPage() {
		return this.showPage;
	}
	public void setShowPage(boolean showPage) {
		this.showPage = showPage;
	}
	public boolean isAddPage() {
		return this.addPage;
	}
	public void setAddPage(boolean addPage) {
		this.addPage = addPage;
	}
	public boolean isViewPage() {
		return this.viewPage;
	}
	public void setViewPage(boolean viewPage) {
		this.viewPage = viewPage;
	}
	public boolean isEditPage() {
		return this.editPage;
	}
	public void setEditPage(boolean editPage) {
		this.editPage = editPage;
	}
	public boolean isEnableRemoveObject() {
		return this.enableRemoveObject;
	}
	public void setEnableRemoveObject(boolean enableRemoveObject) {
		this.enableRemoveObject = enableRemoveObject;
	}
}
