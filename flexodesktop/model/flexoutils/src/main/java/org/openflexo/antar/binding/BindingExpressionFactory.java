/**
 * 
 */
package org.openflexo.antar.binding;

import java.util.Vector;
import java.util.logging.Level;

import org.openflexo.antar.binding.BindingExpression.BindingValueConstant;
import org.openflexo.antar.binding.BindingExpression.BindingValueFunction;
import org.openflexo.antar.binding.BindingExpression.BindingValueVariable;
import org.openflexo.antar.expr.DefaultExpressionParser;
import org.openflexo.antar.expr.Expression;
import org.openflexo.antar.expr.parser.ExpressionParser;
import org.openflexo.antar.expr.parser.ExpressionParser.ConstantFactory;
import org.openflexo.antar.expr.parser.ExpressionParser.DefaultConstantFactory;
import org.openflexo.antar.expr.parser.ExpressionParser.DefaultFunctionFactory;
import org.openflexo.antar.expr.parser.ExpressionParser.DefaultVariableFactory;
import org.openflexo.antar.expr.parser.ExpressionParser.FunctionFactory;
import org.openflexo.antar.expr.parser.ExpressionParser.VariableFactory;
import org.openflexo.antar.expr.parser.ParseException;
import org.openflexo.antar.expr.parser.Value;
import org.openflexo.antar.expr.parser.Word;
import org.openflexo.antar.pp.ExpressionPrettyPrinter;
import org.openflexo.xmlcode.StringEncoder;

public class BindingExpressionFactory extends StringEncoder.Converter<BindingExpression> {
	private final ExpressionParser parser;
	Bindable _bindable;
	boolean warnOnFailure = true;

	public BindingExpressionFactory() {
		super(BindingExpression.class);
		parser = new DefaultExpressionParser();
		parser.setConstantFactory(new BindingExpressionConstantFactory());
		parser.setVariableFactory(new BindingExpressionVariableFactory());
		parser.setFunctionFactory(new BindingExpressionFunctionFactory());
	}

	public void setWarnOnFailure(boolean aFlag) {
		warnOnFailure = aFlag;
	}

	public ConstantFactory getConstantFactory() {
		return parser.getConstantFactory();
	}

	public VariableFactory getVariableFactory() {
		return parser.getVariableFactory();
	}

	public FunctionFactory getFunctionFactory() {
		return parser.getFunctionFactory();
	}

	@Override
	public BindingExpression convertFromString(String aValue) {
		BindingExpression returned = new BindingExpression();
		try {
			Expression expression = parseExpressionFromString(aValue);
			returned.expression = expression;
		} catch (ParseException e) {
			returned.unparsableValue = aValue;
		}
		returned.setOwner(_bindable);
		return returned;
	}

	public Expression parseExpressionFromString(String aValue) throws ParseException {
		return parser.parse(aValue);
	}

	@Override
	public String convertToString(BindingExpression value) {
		return value.getStringRepresentation();
	}

	public Bindable getBindable() {
		return _bindable;
	}

	public void setBindable(Bindable bindable) {
		_bindable = bindable;
	}

	public class BindingExpressionConstantFactory implements ConstantFactory {
		private final DefaultConstantFactory constantFactory = new DefaultConstantFactory();

		@Override
		public Expression makeConstant(Value value) {
			if (BindingExpression.logger.isLoggable(Level.FINE)) {
				BindingExpression.logger.fine("Make constant from " + value + " of " + value.getClass().getSimpleName());
			}
			return new BindingValueConstant(constantFactory.makeConstant(value), _bindable);
		}
	}

	public class BindingExpressionVariableFactory implements VariableFactory {
		private final DefaultVariableFactory variableFactory = new DefaultVariableFactory();

		@Override
		public Expression makeVariable(Word value) {
			return new BindingValueVariable(variableFactory.makeVariable(value), _bindable);
		}
	}

	public class BindingExpressionFunctionFactory implements FunctionFactory {
		private final DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();

		@Override
		public Expression makeFunction(String functionName, Vector<Expression> args) {
			return new BindingValueFunction(functionFactory.makeFunction(functionName, args), _bindable);
		}
	}

	public ExpressionParser getParser() {
		return parser;
	}

	public ExpressionPrettyPrinter getPrettyPrinter() {
		return BindingExpression.prettyPrinter;
	}

}