package model.statement;

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

public record NewStatement(String varName, IExpression expression) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMyDictionary<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new NotDefinedException("Variable '" + varName + "' is not defined.");
        }
        IValue varValue = symTable.getValue(varName);
        if (!(varValue.getType() instanceof RefType)) {
            throw new TypeMissMatchException("Variable " + varName + " is not of type RefType.");
        }

        IValue evaluated = expression.evaluate(symTable, heap);

        RefValue refValue = (RefValue) varValue;
        RefType refType = (RefType) refValue.getType();
        if (!evaluated.getType().equals(refType.getInner())) {
            throw new TypeMissMatchException("Type mismatch: variable " + varName + " points to " + refType.getInner() + " but expression evaluates to " + evaluated.getType());
        }

        int newAddress = heap.allocate(evaluated);
        symTable.update(varName, new RefValue(newAddress, refType.getInner()));
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.getValue(varName);
        IType typeExpr = expression.typecheck(typeEnv);
        if (typeVar.equals(new RefType(typeExpr))) {
            return typeEnv;
        } else {
            throw new TypeMissMatchException("NEW statement: right hand side and left hand side have different types ");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new NewStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression.toString() + ")";
    }
}
