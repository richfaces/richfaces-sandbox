/**
 * Licensed under the Common Development and Distribution License,
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.sun.com/cddl/
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.ajax4jsf.taglib.html.facelets;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

class VariableMapperWrapper extends VariableMapper {

	private VariableMapper mapper;
	
	private Map<String, ValueExpression> vars = null;

	public VariableMapperWrapper(VariableMapper mapper) {
		this.mapper = mapper;
	}
	
	/* (non-Javadoc)
	 * @see javax.el.VariableMapper#resolveVariable(java.lang.String)
	 */
	@Override
	public ValueExpression resolveVariable(String variable) {
		ValueExpression ve = null;
		try {
			if (this.vars != null) {
				ve = (ValueExpression) this.vars.get(variable);
			}
			if (ve == null) {
				return this.mapper.resolveVariable(variable);
			}
			return ve;
		} catch (StackOverflowError e) {
			throw new ELException("Could not Resolve Variable [Overflow]: " + variable, e);
		}
	}

	/* (non-Javadoc)
	 * @see javax.el.VariableMapper#setVariable(java.lang.String, javax.el.ValueExpression)
	 */
	@Override
	public ValueExpression setVariable(String variable,
			ValueExpression expression) {

		if (this.vars == null) {
			this.vars = new HashMap<String, ValueExpression>();
		}
		return (ValueExpression) this.vars.put(variable, expression);
	}

}
