package model.statement;

import exception.MyException;
import model.adts.IMyDictionary;
import model.type.IType;

public class NoOperationStatement implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        // This statement does nothing and simply returns the current state
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        // No operation does not affect the type environment
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NoOperationStatement();
    }
}
