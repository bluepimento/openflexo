/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.antar.expr;

import java.util.Vector;

import org.openflexo.antar.expr.Constant.BooleanConstant;

public class BinaryOperatorExpression extends Expression {

	private BinaryOperator operator;
	private Expression leftArgument;
	private Expression rightArgument;

	public BinaryOperatorExpression(BinaryOperator operator, Expression leftArgument, Expression rightArgument) {
		super();
		this.operator = operator;
		this.leftArgument = leftArgument;
		this.rightArgument = rightArgument;
	}

	@Override
	public int getDepth() {
		if (leftArgument == this) {
			(new Exception("C'est quoi ce bazard ???")).printStackTrace();
			System.exit(-1);
		}
		if (rightArgument == this) {
			(new Exception("C'est quoi ce bazard ???")).printStackTrace();
			System.exit(-1);
		}
		return Math.max(leftArgument.getDepth(), rightArgument.getDepth()) + 1;
	}

	public int getPriority() {
		if (operator != null) {
			return operator.getPriority();
		}
		return -1;
	}

	public Expression getLeftArgument() {
		return leftArgument;
	}

	public void setLeftArgument(Expression leftArgument) {
		this.leftArgument = leftArgument;
		if (leftArgument == this) {
			(new Exception("C'est quoi ce bazard ???")).printStackTrace();
			System.exit(-1);
		}
	}

	public BinaryOperator getOperator() {
		return operator;
	}

	public void setOperator(BinaryOperator operator) {
		this.operator = operator;
	}

	public Expression getRightArgument() {
		return rightArgument;
	}

	public void setRightArgument(Expression rightArgument) {
		this.rightArgument = rightArgument;
		if (rightArgument == this) {
			(new Exception("C'est quoi ce bazard ???")).printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public Expression evaluate(EvaluationContext context) throws TypeMismatchException {
		_checkSemanticallyAcceptable();
		// System.out.println("left="+leftArgument+" of "+leftArgument.getClass().getSimpleName()+" as "+leftArgument.evaluate(context)+" of "+leftArgument.evaluate(context).getClass().getSimpleName());
		// System.out.println("right="+rightArgument+" of "+rightArgument.getClass().getSimpleName()+" as "+rightArgument.evaluate(context)+" of "+rightArgument.evaluate(context).getClass().getSimpleName());

		Expression evaluatedLeftArgument = leftArgument.evaluate(context);
		;

		// special case for AND operator, lazy evaluation
		if (operator == BooleanBinaryOperator.AND && evaluatedLeftArgument == BooleanConstant.FALSE) {
			return BooleanConstant.FALSE; // No need to analyze further
		}

		Expression evaluatedRightArgument = rightArgument.evaluate(context);

		if (evaluatedLeftArgument instanceof Constant && evaluatedRightArgument instanceof Constant) {
			Constant returned = operator.evaluate((Constant) evaluatedLeftArgument, (Constant) evaluatedRightArgument);
			return returned;
		}
		if (evaluatedLeftArgument instanceof Constant) {
			return operator.evaluate((Constant) evaluatedLeftArgument, evaluatedRightArgument);
		}
		if (evaluatedRightArgument instanceof Constant) {
			return operator.evaluate(evaluatedLeftArgument, (Constant) evaluatedRightArgument);
		}
		return new BinaryOperatorExpression(operator, evaluatedLeftArgument, evaluatedRightArgument);
	}

	@Override
	public EvaluationType getEvaluationType() throws TypeMismatchException {
		return getOperator().getEvaluationType(getLeftArgument().getEvaluationType(), getRightArgument().getEvaluationType());
	}

	@Override
	protected Vector<Expression> getChilds() {
		Vector<Expression> returned = new Vector<Expression>();
		returned.add(getLeftArgument());
		returned.add(getRightArgument());
		return returned;
	}

}
