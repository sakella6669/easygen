package org.easygen.core.generators;

import org.easygen.core.config.ProjectConfig;

public interface Generator
{
	public abstract void init(ProjectConfig projectConfig);

	public abstract void generate(ProjectConfig projectConfig) throws GenerationException;

	public abstract String[] copyLibraries(ProjectConfig projectConfig) throws GenerationException;

	public abstract void postProcess(ProjectConfig projectConfig) throws GenerationException;
}
