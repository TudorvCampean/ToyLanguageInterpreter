package model.expression;

import exception.HeapException;
import exception.MyException;
import model.adts.IHeap;
import model.adts.IMyDictionary;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

public record ReadHeapExpression(IExpression expression) implements IExpression {

    public IValue evaluate(IMyDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        IValue value = expression.evaluate(symTable, heap);
        if (!(value instanceof RefValue)) {
            throw new HeapException("ReadHeapExpression: Expected a ReferenceValue.");
        }
        RefValue refValue = (RefValue) value;
        int address = refValue.address();

        if (!heap.isDefined(address)) {
            throw new HeapException("ReadHeapExpression: Address " + address + " is not defined in the heap.");

        }
        return heap.get(address);
    }

    @Override
    public IType typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType type = expression.typecheck(typeEnv);
        if (type instanceof RefType) {
            RefType reft = (RefType) type;
            return reft.getInner();
        } else {
            throw new MyException("ReadHeapExpression: The expression is not of RefType.");
        }
    }

    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(expression.deepCopy());
    }


    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}
