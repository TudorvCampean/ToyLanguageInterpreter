package model.type;

public class StringType implements IType {
    @Override
    public IType deepCopy() {
        return new StringType();
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public model.value.IValue defaultValue() {
        return new model.value.StringValue("");
    }
}
