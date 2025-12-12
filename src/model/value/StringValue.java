package model.value;

import model.type.IType;
import model.type.StringType;

public record StringValue(String value) implements IValue {
    @Override
    public IValue deepCopy() {
        return new StringValue(value);
    }
    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return value;
    }
}
