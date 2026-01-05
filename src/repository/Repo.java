    package repository;

    import exception.MyException;
    import exception.RepoException;
    import model.statement.ProgramState;

    import java.io.BufferedWriter;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.util.ArrayList;
    import java.util.List;

    public class Repo implements IRepo {
        private final String logFilePath;
        private List<ProgramState> programStates;
        public Repo(String logFilePath, ProgramState initialProgram) {
            this.logFilePath = logFilePath;
            this.programStates = new ArrayList<>();
            this.addProgram(initialProgram);
            try{
                new PrintWriter(logFilePath).close();
            } catch (IOException ignored){}
        }
        @Override
        public List<ProgramState> getPrgList() {
            return programStates;
        }

        @Override
        public void setPrgList(List<ProgramState> programStates) {
            this.programStates = programStates;
        }

        @Override
        public void addProgram(ProgramState program) {
            programStates.add(program);
        }

       @Override
        public void logPrgStateExec(ProgramState programState) throws MyException {
            try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
                logFile.println(programState.toString());
            } catch (IOException e) {
                throw new RepoException("Could not write to log file: " + e.getMessage());
            }
        }
    }

