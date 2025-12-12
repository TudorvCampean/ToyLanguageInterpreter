package model.expression;

import exception.MyException;
import model.adts.IHeap;
import model.adts.IMyDictionary;
import model.value.IValue;

public record ConstantExpression(IValue value) implements IExpression {
    @Override
    public IExpression deepCopy() {
        return new ConstantExpression(value.deepCopy());
    }
    @Override
    public IValue evaluate(IMyDictionary<String,IValue> symTable, IHeap heap) throws MyException {
        return value;
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
