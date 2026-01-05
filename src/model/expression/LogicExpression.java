package model.expression;

import exception.MyException;
import exception.TypeMissMatchException;
import exception.UnknowOperatorException;
import model.adts.IHeap;
import model.adts.IMyDictionary;
import model.type.BooleanType;
import model.type.IType;
import model.value.IValue;
import model.value.BooleanValue;

public record LogicExpression(String operator, IExpression leftExpression, IExpression rightExpression) implements IExpression {
    @Override
    public IExpression deepCopy() {
        return new LogicExpression(operator, leftExpression.deepCopy(), rightExpression.deepCopy());
    }
    @Override
    public String toString() {
        return leftExpression.toString() + " " + operator + " " + rightExpression.toString();
    }

    @Override
    public IValue evaluate(IMyDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        IValue leftValue = leftExpression.evaluate(symTable,heap);
        IValue rightValue = rightExpression.evaluate(symTable,heap);

        if (!leftValue.getType().equals(new BooleanType())) {
            throw new TypeMissMatchException("First operand is not a boolean.");
        }

        if (!rightValue.getType().equals(new BooleanType())) {
            throw new TypeMissMatchException("Second operand is not a boolean.");
        }

        boolean leftBool = ((BooleanValue) leftValue).value();
        boolean rightBool = ((BooleanValue) rightValue).value();

        return switch (operator) {
            case "&&" -> new BooleanValue(leftBool && rightBool);
            case "||" -> new BooleanValue(leftBool || rightBool);
            default -> throw new UnknowOperatorException("Unknown logic operator: " + operator);
        };
    }

    @Override
    public IType typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typ1 = leftExpression.typecheck(typeEnv);
        IType typ2 = rightExpression.typecheck(typeEnv);

        if(!typ1.equals(new BooleanType())) {
            throw new TypeMissMatchException("First operand is not a boolean.");
        }
        if(!typ2.equals(new BooleanType())) {
            throw new TypeMissMatchException("Second operand is not a boolean.");
        }
        return new BooleanType();
    }
}
