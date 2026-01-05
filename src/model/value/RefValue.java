package model.value;
import model.type.IType;
import model.type.RefType;

public record RefValue(int address, IType locationType) implements IValue {
    @Override
    public IValue deepCopy() {
        return new RefValue(address, locationType.deepCopy());
    }

    @Override
    public IType getType() {
        return new RefType(locationType);
    }
}
