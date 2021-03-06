/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.persistence.dataset;

import java.io.Serializable;

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.dataset.util.ParameterUtil;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * A value associated to a particular parameter.
 * 
 * @author Jens Happe, Dennis Westermann
 * 
 * @param <T>
 *            Type of the value.
 */
public class ParameterValue<T> implements Serializable, Comparable<ParameterValue<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 448079727442973952L;

	private static final double TOLERANCE_VALUE = 0.0000000000000001;
	private ParameterDefinition parameterDefinition;
	private T parameterValue;

	protected ParameterValue(ParameterDefinition parameter, T value) {
		super();
		validate(parameter, value);
		this.parameterDefinition = parameter;
		this.parameterValue = value;
	}

	private void validate(ParameterDefinition parameter, Object value) {
		if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.DOUBLE)
				&& !(value instanceof Double)) {
			throw new IllegalArgumentException("Invalid value type for parameter " + parameter);
		} else if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.INTEGER)
				&& !(value instanceof Integer)) {
			throw new IllegalArgumentException("Invalid value type for parameter " + parameter);
		} else if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.STRING)
				&& !(value instanceof String)) {
			throw new IllegalArgumentException("Invalid value type for parameter " + parameter);
		} else if (ParameterUtil.getTypeEnumeration(parameter.getType()).equals(ParameterType.BOOLEAN)
				&& !(value instanceof Boolean)) {
			throw new IllegalArgumentException("Invalid value type for parameter " + parameter);
		}
	}

	public ParameterDefinition getParameter() {
		return parameterDefinition;
	}

	public T getValue() {
		return parameterValue;
	}

	public void setValue(Object value) {
		@SuppressWarnings("unchecked")
		T convertedValue = (T) ParameterValueFactory.convertValue(value,
				ParameterUtil.getTypeEnumeration(parameterDefinition.getType()));
		this.parameterValue = convertedValue;
	}

	@Override
	public int compareTo(ParameterValue<?> o) {
		if (parameterValue instanceof Integer && o.getValue() instanceof Integer) {
			return ((Integer) parameterValue).compareTo((Integer) o.getValue());
		} else if (parameterValue instanceof Double && o.getValue() instanceof Double) {
			if (Math.abs((Double) parameterValue - (Double) o.getValue()) < TOLERANCE_VALUE) {
				return 0;
			} else if ((Double) parameterValue < (Double) o.getValue()) {
				return -1;
			} else {
				return 1;
			}
		} else if (parameterValue instanceof Boolean && o.getValue() instanceof Boolean) {
			return ((Boolean) parameterValue).compareTo((Boolean) o.getValue());

		} else if (parameterValue instanceof String && o.getValue() instanceof String) {
			return ((String) parameterValue).compareTo((String) o.getValue());

		} else {
			throw new IllegalArgumentException(
					"Comparison supported only for Double, Integer, Boolean and String ParameterValues! But value of "
							+ this.getParameter().getFullName() + " is of type " + this.getParameter().getType());
		}
	}

	public String getValueAsString() {

		if (parameterValue instanceof Double) {
			return Double.toString((Double) parameterValue);
		} else if (parameterValue instanceof Integer) {
			return Integer.toString((Integer) parameterValue);
		} else if (parameterValue instanceof Boolean) {
			return Boolean.toString((Boolean) parameterValue);
		} else if (parameterValue instanceof String) {
			return (String) parameterValue;
		}
		return "";
	}

	public double getValueAsDouble() {
		if (parameterValue instanceof Double) {
			return (Double) parameterValue;
		} else if (parameterValue instanceof Integer) {
			return ((Integer) parameterValue).doubleValue();
		} else {
			throw new IllegalStateException(
					"The function getValueAsDouble is supported only by Double and Integer ParameterValues! But value of "
							+ this.getParameter().getFullName() + " is of type " + this.getParameter().getType());
		}
	}

	public boolean getValueAsBoolean() {
		if (parameterValue instanceof Boolean) {
			return (Boolean) parameterValue;
		} else {
			throw new IllegalStateException(
					"The function getValueAsBoolean is supported only by Boolean ParameterValues! But value of "
							+ this.getParameter().getFullName() + " is of type " + this.getParameter().getType());
		}
	}

	public int getValueAsInteger() {
		if (parameterValue instanceof Double) {
			return ((Double) parameterValue).intValue();
		} else if (parameterValue instanceof Integer) {
			return (Integer) parameterValue;
		} else {
			throw new IllegalStateException(
					"The function getValueAsInteger is supported only by Double and Integer ParameterValues! But value of "
							+ this.getParameter().getFullName() + " is of type " + this.getParameter().getType());
		}
	}

	public ParameterValue<?> copy() {

		if (parameterValue instanceof Double) {
			return new ParameterValue<Double>(parameterDefinition, (Double) parameterValue);
		} else if (parameterValue instanceof Integer) {
			return new ParameterValue<Integer>(parameterDefinition, (Integer) parameterValue);
		} else if (parameterValue instanceof Boolean) {
			return new ParameterValue<Boolean>(parameterDefinition, (Boolean) parameterValue);
		} else if (parameterValue instanceof String) {
			return new ParameterValue<String>(parameterDefinition, (String) parameterValue);
		}
		return null;
	}
}
