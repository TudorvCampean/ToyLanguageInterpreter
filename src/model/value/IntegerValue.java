package model.value;

import model.type.IType;
import model.type.IntegerType;

public record IntegerValue(int value) implements IValue {
    @Override
    public IValue deepCopy() {
        return new IntegerValue(value);
    }
    @Override
    public IType getType() {
        return new IntegerType();
    }
}