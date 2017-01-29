/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.core.foundation;

import com.google.inject.AbstractModule;



/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public abstract class GuiceModule extends AbstractModule{

	@Override
	protected void configure() {
		
	}
		
	public <T> T inject(T object){
		this.requestInjection(object);
		return object;
	}
	
}