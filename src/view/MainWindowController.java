package view;

import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.statement.ProgramState;
import model.value.IValue;

import java.util.List;
import java.util.Map;

public class MainWindowController {

    // --- ELEMENTE FXML (Legatura cu interfata) ---
    @FXML
    private TextField numberOfPrgStatesTextField;

    @FXML
    private TableView<Map.Entry<Integer, IValue>> heapTableView;
    @FXML
    private TableColumn<Map.Entry<Integer, IValue>, Integer> heapAddressColumn;
    @FXML
    private TableColumn<Map.Entry<Integer, IValue>, String> heapValueColumn;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> prgStateListIdListView;

    @FXML
    private TableView<Map.Entry<String, IValue>> symbolTableView;
    @FXML
    private TableColumn<Map.Entry<String, IValue>, String> symTableVarNameColumn;
    @FXML
    private TableColumn<Map.Entry<String, IValue>, String> symTableValueColumn;

    @FXML
    private ListView<String> exeStackListView;

    @FXML
    private Button runOneStepButton;

    // --- CODAREA LOGICII ---
    private Controller controller;

    // Aceasta metoda va fi apelata din ProgramSelectorController pentru a seta controller-ul
    public void setController(Controller controller) {
        this.controller = controller;
        populateAll(); // Afisam datele initiale (starea de start)
    }

    @FXML
    public void initialize() {
        // Aici vom configura coloanele tabelelor (Assignment 7)
        // Setam cum sa isi ia valorile din Map.Entry

        heapAddressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        heapValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        symTableVarNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        symTableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        prgStateListIdListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    void handleRunOneStep(ActionEvent event) {
        if (controller == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The program was not selected correctly", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            // Verificăm dacă programul s-a terminat
            List<ProgramState> prgList = controller.getRepository().getPrgList();
            if (!prgList.isEmpty()) {
                // EXECUTĂ UN PAS (folosind logica din Controller)
                // Metoda oneStepForAllPrg ar trebui să ruleze Garbage Collector și să facă pasul
                controller.oneStepForAllPrg(prgList);

                // ACTUALIZĂM INTERFAȚA
                populateAll();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Program finished!", ButtonType.OK);
                alert.showAndWait();

                // Dezactivăm butonul
                runOneStepButton.setDisable(true);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void handleChangePrgState(MouseEvent event) {
        // Când se schimbă selecția în ListView-ul de ID-uri, actualizăm tabelul de simboluri și stiva de execuție
        populateSymTable();
        populateExeStack();
    }

    private void populateAll() {
        populateNumber();
        populateHeap();
        populateOutput();
        populateFileTable();
        populatePrgStateIdentifiers();

        populateSymTable();
        populateExeStack();
    }

    private void populateNumber() {
        List<ProgramState> prgList = controller.getRepository().getPrgList();
        numberOfPrgStatesTextField.setText(String.valueOf(prgList.size()));
    }

    private void populateHeap() {
        if (!controller.getRepository().getPrgList().isEmpty()) {
            // Heap-ul este partajat, deci îl luăm de la primul program disponibil
            Map<Integer, IValue> heapContent = controller.getRepository().getPrgList().get(0).getHeap().getContent();
            heapTableView.setItems(javafx.collections.FXCollections.observableArrayList(heapContent.entrySet()));
            heapTableView.refresh();
        }
    }

    private void populateOutput() {
        if (!controller.getRepository().getPrgList().isEmpty()) {
            // Luăm obiectul Out (care este un IMyList<IValue>)
            var outputContainer = controller.getRepository().getPrgList().get(0).getOut();

            // Apelăm metoda nou creată getList()
            List<IValue> outList = outputContainer.getList();

            // Transformăm IValue/StringValue în String Java pentru ListView<String>
            outputListView.setItems(javafx.collections.FXCollections.observableArrayList(
                    outList.stream()
                            .map(Object::toString) // Aceasta rezolvă eroarea de tip
                            .toList()
            ));
        }
    }

    private void populateFileTable() {
        if (!controller.getRepository().getPrgList().isEmpty()) {
            // Obținem conținutul tabelului de fișiere (Map-ul)
            var fileTable = controller.getRepository().getPrgList().get(0).getFileTable().getContent();

            // Transformăm Set<StringValue> în List<String>
            List<String> filesAsString = fileTable.keySet().stream()
                    .map(Object::toString) // Folosim toString-ul tău care returnează String-ul Java
                    .toList();

            fileTableListView.setItems(javafx.collections.FXCollections.observableArrayList(filesAsString));
        }
    }

    private void populatePrgStateIdentifiers() {
        List<ProgramState> prgList = controller.getRepository().getPrgList();
        List<Integer> idList = prgList.stream().map(ProgramState::getId).toList();
        prgStateListIdListView.setItems(javafx.collections.FXCollections.observableArrayList(idList));
    }

    private ProgramState getSelectedPrgState() {
        if (controller.getRepository().getPrgList().isEmpty()) return null;
        Integer id = prgStateListIdListView.getSelectionModel().getSelectedItem();
        if (id == null) return controller.getRepository().getPrgList().get(0); // Default la primul dacă nu e selectat nimic

        return controller.getRepository().getPrgList().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void populateSymTable() {
        ProgramState selected = getSelectedPrgState();
        if (selected != null) {
            symbolTableView.setItems(javafx.collections.FXCollections.observableArrayList(
                    selected.getSymTable().getContent().entrySet()
            ));
            symbolTableView.refresh();
        } else {
            symbolTableView.getItems().clear();
        }
    }

    private void populateExeStack() {
        ProgramState selected = getSelectedPrgState();
        if (selected != null) {
            // Folosim metoda getReverse pe care am adăugat-o în clasa ta Stack
            exeStackListView.setItems(javafx.collections.FXCollections.observableArrayList(
                    selected.getExeStack().getReverse().stream().map(Object::toString).toList()
            ));
        } else {
            exeStackListView.getItems().clear();
        }
    }
}