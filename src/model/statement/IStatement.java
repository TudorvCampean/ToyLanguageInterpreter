package model.statement;

import exception.MyException;

public interface IStatement {
    ProgramState execute(ProgramState state) throws MyException;
    IStatement deepCopy();
    String toString();
}
