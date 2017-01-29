/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core;



/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class PomData {
	
	public static final String GROUP_ID = "${project.groupId}";
	public static final String ARTIFACT_ID = "${project.artifactId}";
	public static final String NAME = "${project.name}";
	public static final String VERSION = "${buildNumber}";

}
