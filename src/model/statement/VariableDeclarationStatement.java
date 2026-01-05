package model.statement;

import exception.AlreadyDefinedException;
import exception.MyException;
import model.type.IType;
import model.value.IValue;
import model.adts.IMyDictionary;


public record VariableDeclarationStatement(IType type, String name) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMyDictionary<String, IValue> symbolTable = state.getSymTable();


        if (symbolTable.isDefined(name)) {
            throw new AlreadyDefinedException("Variable already defined: " + name);
        }

        IValue defaultValue = type.defaultValue();
        symbolTable.update(name, defaultValue);

        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        typeEnv.update(name, type);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new VariableDeclarationStatement(type, name);
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }
}
