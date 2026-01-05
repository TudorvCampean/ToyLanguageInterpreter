package model.expression;

import exception.MyException;
import exception.NotDefinedException;
import model.adts.IHeap;
import model.type.IType;
import model.value.IValue;
import model.adts.IMyDictionary;

public record VariableExpression(String name) implements IExpression {
    @Override
    public IExpression deepCopy() {
        return new VariableExpression(name);
    }
    @Override
    public IValue evaluate(IMyDictionary<String,IValue> symTable, IHeap heap) throws MyException {
        if (!symTable.isDefined(name)) {
            throw new NotDefinedException("Variable '" + name + "' is not defined.");
        }
        try {
            return symTable.getValue(name);
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IType typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv.getValue(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
