package model.expression;

import exception.DivisionByZeroException;
import exception.MyException;
import exception.TypeMissMatchException;
import exception.UnknowOperatorException;
import model.adts.IHeap;
import model.adts.IMyDictionary;
import model.type.IType;
import model.type.IntegerType;
import model.value.IValue;
import model.value.IntegerValue;

public record ArithmeticExpression(String operator, IExpression leftExpression, IExpression rightExpression) implements IExpression {

    @Override
    public IExpression deepCopy() {
        return new ArithmeticExpression(operator, leftExpression.deepCopy(), rightExpression.deepCopy());
    }
    @Override
    public IValue evaluate(IMyDictionary<String, IValue> symTable,IHeap heap) throws MyException {
        IValue leftValue = leftExpression.evaluate(symTable,heap);
        IValue rightValue = rightExpression.evaluate(symTable,heap);

        if (!leftValue.getType().equals(new IntegerType())) {
            throw new TypeMissMatchException("First operand is not an integer.");
        }
        if (!rightValue.getType().equals(new IntegerType())) {
            throw new TypeMissMatchException("Second operand is not an integer.");
        }

        int leftInt = ((IntegerValue) leftValue).value();
        int rightInt = ((IntegerValue) rightValue).value();

        return switch (operator) {
            case "+" -> new IntegerValue(leftInt + rightInt);
            case "-" -> new IntegerValue(leftInt - rightInt);
            case "*" -> new IntegerValue(leftInt * rightInt);
            case "/" -> {
                if (rightInt == 0) {
                    throw new DivisionByZeroException("Division by zero");
                }
                yield new IntegerValue(leftInt / rightInt);
            }
            default -> throw new UnknowOperatorException("Unknown arithmetic operator: " + operator);
        };
    }

    @Override
    public IType typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typ1 = leftExpression.typecheck(typeEnv);
        IType typ2 = rightExpression.typecheck(typeEnv);

        if(!typ1.equals(new IntegerType())) {
            throw new TypeMissMatchException("First operand is not an integer.");
        }
        if(!typ2.equals(new IntegerType())) {
            throw new TypeMissMatchException("Second operand is not an integer.");
        }
        return new IntegerType();
    }


    @Override
    public String toString() {
        return leftExpression.toString() + " " + operator + " " + rightExpression.toString();
    }

}
