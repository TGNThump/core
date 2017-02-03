/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.commands.sources;

import java.util.List;

import uk.me.pilgrim.dev.core.util.text.Text;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public interface CommandSource {

	/**
	 * @param perm
	 * @return
	 */
	public boolean hasPermission(String perm);

	/**
	 * @param of
	 */
	public void sendMessage(String of);
	
	public void info(String content);
	
	public void error(String content);

	/**
	 * @param usages
	 */
	public default void sendMessages(List<String> strings) {
		sendMessage(Text.implode(strings, "\n"));
	}
	
}
