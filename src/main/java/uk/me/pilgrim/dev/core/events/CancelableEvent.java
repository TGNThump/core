/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.events;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public abstract class CancelableEvent extends Event {
	
	private boolean isCanceled = false;
	
	public void cancel(){
		setCanceled(true);
	}
	
	public void setCanceled(boolean value){
		this.isCanceled = value;
	}
	
	public boolean isCanceled(){
		return this.isCanceled;
	}
	
}
