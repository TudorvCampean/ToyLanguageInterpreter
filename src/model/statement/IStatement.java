package model.statement;

import exception.MyException;
import model.adts.IMyDictionary;
import model.type.IType;

public interface IStatement {
    ProgramState execute(ProgramState state) throws MyException;
    IMyDictionary<String,IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException;
    IStatement deepCopy();
    String toString();
}
