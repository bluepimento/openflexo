package org.openflexo.foundation.viewpoint.binding;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.openflexo.antar.binding.AbstractBinding.BindingEvaluationContext;
import org.openflexo.antar.binding.BindingPathElement;
import org.openflexo.antar.binding.BindingVariable;
import org.openflexo.antar.binding.SimplePathElement;
import org.openflexo.antar.binding.TypeUtils;
import org.openflexo.foundation.viewpoint.EditionScheme;
import org.openflexo.foundation.viewpoint.EditionSchemeParameter;

public class EditionSchemeParameterPathElement<T> implements SimplePathElement<T>, BindingVariable<T> {
	private static final Logger logger = Logger.getLogger(EditionSchemeParameterPathElement.class.getPackage().getName());

	private BindingPathElement parent;
	private EditionSchemeParameter parameter;

	public EditionSchemeParameterPathElement(BindingPathElement aParent, EditionSchemeParameter parameter) {
		super();
		parent = aParent;
		this.parameter = parameter;
	}

	@Override
	public Class getDeclaringClass() {
		return TypeUtils.getBaseClass(parent.getType());
	}

	@Override
	public String getSerializationRepresentation() {
		return getLabel();
	}

	@Override
	public boolean isBindingValid() {
		return true;
	}

	@Override
	public Type getType() {
		return parameter.getWidget().getType();
	}

	@Override
	public String getLabel() {
		return parameter.getName();
	}

	@Override
	public String getTooltipText(Type resultingType) {
		return parameter.getDescription();
	}

	@Override
	public boolean isSettable() {
		return false;
	}

	@Override
	public T getBindingValue(Object target, BindingEvaluationContext context) {
		if (target instanceof Hashtable) {
			return (T) ((Hashtable) target).get(parameter.getName());
		} else {
			logger.warning("Unexpected: " + target);
			return null;
		}
	}

	@Override
	public void setBindingValue(T value, Object target, BindingEvaluationContext context) {
		// Not relevant because not settable
	}

	@Override
	public EditionScheme getContainer() {
		return parameter.getScheme();
	}

	@Override
	public String getVariableName() {
		return parameter.getName();
	}

	public EditionSchemeParameter getParameter() {
		return parameter;
	}

}