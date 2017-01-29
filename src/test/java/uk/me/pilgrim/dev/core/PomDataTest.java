/**
 * 
 */
package uk.me.pilgrim.dev.core;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class PomDataTest extends TestCase {
	
	@Test
	public void testArtifactId() {
		assertNotNull(PomData.ARTIFACT_ID);
		assertNotSame(PomData.ARTIFACT_ID, "");
	}
	
	@Test
	public void testGroupId() {
		assertNotNull(PomData.GROUP_ID);
		assertNotSame(PomData.GROUP_ID, "");
	}
	
	@Test
	public void testName() {
		assertNotNull(PomData.NAME);
		assertNotSame(PomData.NAME, "");
	}
	
	@Test
	public void testVersion() {
		assertNotNull(PomData.VERSION);
		assertNotSame(PomData.VERSION, "");
	}
}
