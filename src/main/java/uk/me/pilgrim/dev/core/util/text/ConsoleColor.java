/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.util.text;

import org.fusesource.jansi.Ansi;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class ConsoleColor {
	public static final String RESET = Ansi.ansi().fg(Ansi.Color.DEFAULT).toString() + Ansi.ansi().reset();
	
	public static final String BLACK = Ansi.ansi().fg(Ansi.Color.BLACK).toString();
	public static final String BLUE = Ansi.ansi().fg(Ansi.Color.BLUE).toString();
	public static final String CYAN = Ansi.ansi().fg(Ansi.Color.CYAN).toString();
	public static final String GREEN = Ansi.ansi().fg(Ansi.Color.GREEN).toString();
	public static final String MAGENTA = Ansi.ansi().fg(Ansi.Color.MAGENTA).toString();
	public static final String RED = Ansi.ansi().fg(Ansi.Color.RED).toString();
	public static final String WHITE = Ansi.ansi().fg(Ansi.Color.WHITE).toString();
	public static final String YELLOW = Ansi.ansi().fg(Ansi.Color.YELLOW).toString();
}
