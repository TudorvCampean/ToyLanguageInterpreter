package model.statement;

import exception.FileException;
import exception.MyException;
import exception.TypeMissMatchException;
import model.adts.IHeap;
import model.expression.IExpression;
import model.adts.IMyDictionary;
import model.type.IType;
import model.type.StringType;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record OpenRFileStatement(IExpression expression) implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMyDictionary<String, IValue> symbolTable = state.getSymTable();
        IMyDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IHeap heap = state.getHeap();

        IValue value = expression.evaluate(symbolTable,heap);
        if (!value.getType().equals(new StringType())) {
            throw new TypeMissMatchException("Expression must evaluate to a string.");
        }

        StringValue fileNameValue = (StringValue) value;
        if (fileTable.isDefined(fileNameValue)) {
            throw new FileException("File already opened: " + fileNameValue.value());
        }

        BufferedReader bufferedReader;
        try {
            String fileName = fileNameValue.value();
            bufferedReader = new BufferedReader(new FileReader(fileName));
        }catch (IOException e) {
            throw new FileException("Could not open file: " + fileNameValue.value());
        }

        fileTable.update(fileNameValue, bufferedReader);
        return null;
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typeExpr = expression.typecheck(typeEnv);
        if (!typeExpr.equals(new StringType())){
            throw new TypeMissMatchException("The expression of openRFile does not have the type string");
        }
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenRFileStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "openRFile(" + expression.toString() + ")";
    }
}
