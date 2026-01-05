package model.statement;

import exception.HeapException;
import exception.MyException;
import exception.NotDefinedException;
import exception.TypeMissMatchException;
import model.adts.IHeap;
import model.adts.IMyDictionary;
import model.expression.IExpression;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

public record WriteHeapStatement(String varName, IExpression expression) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IHeap heap = state.getHeap();
        IMyDictionary<String, IValue> symTable = state.getSymTable();

        if (!symTable.isDefined(varName)) {
            throw new NotDefinedException("Variable '" + varName + "' is not defined.");
        }
        IValue varValue = symTable.getValue(varName);
        if (!(varValue instanceof RefValue)) {
            throw new TypeMissMatchException("Variable '" + varName + "' is not of RefType.");
        }
        RefValue refValue = (RefValue) varValue;
        if (!heap.isDefined(refValue.address())) {
            throw new HeapException("Address " + refValue.address() + " is not defined in the heap.");
        }

        IValue evaluated = expression.evaluate(symTable, heap);

        if (!evaluated.getType().equals(refValue.locationType())) {
            throw new TypeMissMatchException("Type mismatch: variable '" + varName + "' points to " + refValue.locationType() + " but expression evaluates to " + evaluated.getType());
        }
        heap.update(refValue.address(), evaluated);
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.getValue(varName);
        IType typeExpr = expression.typecheck(typeEnv);
        if (typeVar instanceof RefType) {
            RefType refType = (RefType) typeVar;
            if (typeExpr.equals(refType.getInner())) {
                return typeEnv;
            } else {
                throw new TypeMissMatchException("WRITE HEAP statement: right hand side and left hand side have different types ");
            }
        } else {
            throw new TypeMissMatchException("WRITE HEAP statement: variable " + varName + " is not of RefType");
        }
    }


    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression.toString() + ")";
    }
}
