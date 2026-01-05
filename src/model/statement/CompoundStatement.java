package model.statement;

import exception.MyException;
import model.adts.IExecutionStack;
import model.adts.IMyDictionary;
import model.type.IType;

public record CompoundStatement(IStatement first, IStatement second) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IExecutionStack<IStatement> stack = state.getExeStack();
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IMyDictionary<String, IType> typeEnv1 = first.typecheck(typeEnv);
        IMyDictionary<String, IType> typeEnv2 = second.typecheck(typeEnv1);
        return typeEnv2;
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(first.deepCopy(), second.deepCopy());
    }

    @Override
    public String toString(){
        return "(" + first.toString() + "; " + second.toString() + ")";
    }
}
