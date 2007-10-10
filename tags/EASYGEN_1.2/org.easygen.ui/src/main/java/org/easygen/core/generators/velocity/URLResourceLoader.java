package org.easygen.core.generators.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * 
 */
public class URLResourceLoader extends ResourceLoader {
	/**
	 * 
	 */
	public URLResourceLoader() {
	}

	/**
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#init(org.apache.commons.collections.ExtendedProperties)
	 */
	@Override
	public void init(ExtendedProperties properties) {

	}
	/**
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
	 */
	@Override
	public long getLastModified(Resource resource) {
		return System.currentTimeMillis();
	}
	/**
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getResourceStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceStream(String urlString) throws ResourceNotFoundException {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(urlString);
			if (url == null) {
				throw new ResourceNotFoundException("Resource not found : "+urlString);
			}
			return url.openStream();
		} catch (IOException e) {
			ResourceNotFoundException exception = new ResourceNotFoundException("Can't read resource : "+urlString);
			exception.initCause(e);
			throw exception;
		}
	}
	/**
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
	 */
	@Override
	public boolean isSourceModified(Resource resource) {
		return true;
	}
}
