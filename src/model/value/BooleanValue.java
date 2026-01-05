package model.value;

import model.type.BooleanType;
import model.type.IType;

public record BooleanValue(boolean value) implements IValue {
    @Override
    public IValue deepCopy() {
        return new BooleanValue(value);
    }

    @Override
    public IType getType() {
        return new BooleanType();
    }
}