package org.easygen.core.generators.struts2;

import org.easygen.core.config.ViewModuleConfig;
import org.easygen.core.generators.Generator;

public class Struts2ModuleConfig extends ViewModuleConfig {

	public static final String TILES2 = "Tiles2";
	public static final String SITE_MESH = "SiteMesh";

	public static final String NATURE = "Struts2";

	public static final String[] TEMPLATE_ENGINES = { SITE_MESH, TILES2 };

	private String templateEngine;
	private NavigationConfig navigationConfig;

	public Struts2ModuleConfig() {
		super();
		setNature(NATURE);
	}

	public String getTemplateEngine() {
		return this.templateEngine;
	}

	public void setTemplateEngine(String templateEngine) {
		this.templateEngine = templateEngine;
	}

	public boolean isSiteMeshEngine() {
		return SITE_MESH.equals(templateEngine);
	}

	public boolean isTiles2Engine() {
		return TILES2.equals(templateEngine);
	}

	public NavigationConfig getNavigationConfig() {
		return this.navigationConfig;
	}

	public void setNavigationConfig(NavigationConfig navigationConfig) {
		this.navigationConfig = navigationConfig;
	}

	/**
	 * @see org.easygen.core.config.ModuleConfig#getGenerator()
	 */
	@Override
	public Generator getGenerator() {
		return new Struts2Generator();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append("\tTemplateEngine: ").append(getTemplateEngine()).append("\n");
		return buffer.toString();
	}
}
