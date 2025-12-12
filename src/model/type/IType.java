package model.type;

import model.value.IValue;

public interface IType {
    IValue defaultValue();
    boolean equals(Object another);
    IType deepCopy();
}
