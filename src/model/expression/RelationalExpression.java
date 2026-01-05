package model.expression;

import exception.MyException;
import exception.TypeMissMatchException;
import exception.UnknowOperatorException;
import model.adts.IHeap;
import model.adts.IMyDictionary;
import model.type.BooleanType;
import model.type.IType;
import model.type.IntegerType;
import model.value.IValue;
import model.value.BooleanValue;
public record RelationalExpression(String operator, IExpression leftExpression, IExpression rightExpression) implements IExpression {
    @Override
    public IExpression deepCopy() {
        return new RelationalExpression(operator, leftExpression.deepCopy(), rightExpression.deepCopy());
    }

    @Override
    public IValue evaluate(IMyDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        IValue leftValue = leftExpression.evaluate(symTable,heap);
        IValue rightValue = rightExpression.evaluate(symTable,heap);

        if(!leftValue.getType().equals(new IntegerType()) || !rightValue.getType().equals(new IntegerType())) {
            throw new TypeMissMatchException("Type mismatch in relational expression: " + this.toString());
        }
        int leftInt = ((model.value.IntegerValue) leftValue).value();
        int rightInt = ((model.value.IntegerValue) rightValue).value();

        return switch (operator) {
            case "<" -> new BooleanValue(leftInt < rightInt);
            case "<=" -> new BooleanValue(leftInt <= rightInt);
            case "==" -> new BooleanValue(leftInt == rightInt);
            case "!=" -> new BooleanValue(leftInt != rightInt);
            case ">" -> new BooleanValue(leftInt > rightInt);
            case ">=" -> new BooleanValue(leftInt >= rightInt);
            default -> throw new UnknowOperatorException("Invalid relational operator: " + operator);
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
        return new BooleanType();
    }

    @Override
    public String toString() {
        return leftExpression.toString() + " " + operator + " " + rightExpression.toString();
    }
}
