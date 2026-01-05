package model.type;

import model.value.BooleanValue;
import model.value.IValue;

public class BooleanType implements IType {
    @Override
    public IType deepCopy() {
        return new BooleanType();
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof BooleanType;
    }

    @Override
    public IValue defaultValue() {
        return new BooleanValue(false);
    }

    @Override
    public String toString() {
        return "boolean";
    }
}
