package repository;

import exception.MyException;
import model.statement.ProgramState;

import java.util.List;

public interface IRepo {
    List<ProgramState> getPrgList();
    void setPrgList(List<ProgramState> programStates);
    void addProgram(ProgramState program);
    void logPrgStateExec(ProgramState programState) throws MyException;
}
