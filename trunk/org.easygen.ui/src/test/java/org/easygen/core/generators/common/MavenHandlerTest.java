package org.easygen.core.generators.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class MavenHandlerTest {

	@Test
	public void testIsMavenFound() {
		MavenHandler handler = new MavenHandler();
		assertTrue(handler.isMavenFound());
	}
}