package model.statement;
import exception.FileException;
import exception.MyException;
import exception.TypeMissMatchException;
import model.adts.IHeap;
import model.expression.IExpression;
import model.adts.IMyDictionary;
import model.type.StringType;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFileStatement(IExpression expression) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IMyDictionary<String, IValue> symTable = state.getSymTable();
        IMyDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IHeap heap = state.getHeap();

        IValue value = expression.evaluate(symTable,heap);
        if (!value.getType().equals(new StringType())) {
            throw new TypeMissMatchException("Expression must evaluate to a string.");
        }

        StringValue fileNameValue = (StringValue) value;
        if (!fileTable.isDefined(fileNameValue)) {
            throw new FileException("File not opened: " + fileNameValue.value());
        }
        BufferedReader bufferedReader = fileTable.getValue(fileNameValue);
        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new FileException("Could not close file: " + fileNameValue.value());
        }
        fileTable.remove(fileNameValue);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseRFileStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression.toString() + ")";
    }
}
