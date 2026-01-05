package controller;

import exception.MyException;
import model.adts.IExecutionStack;
import model.statement.ProgramState;
import model.statement.IStatement;
import repository.IRepo;

import model.value.RefValue;
import model.value.IValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

//Controller class that manages the execution of program states (modificare pentru proba git)

public class Controller {
    private  IRepo repository;
    private ExecutorService executor;

    public Controller(IRepo repository) {
        this.repository = repository;
        this.executor = Executors.newFixedThreadPool(2);
    }
    public IRepo getRepository() {
        return repository;
    }
    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException{
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });

        runGarabageCollector(prgList);

        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>)(() -> { return p.oneStep(); }))
                .collect(Collectors.toList());


        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        System.out.println("Execution Error: " + e.getMessage());
                        return null;
                    }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);

        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });
        repository.setPrgList(prgList);
    }

    public void allSteps() throws MyException {

        List<ProgramState> prgList = removeCompletedPrg(repository.getPrgList());

        while (prgList.size() > 0) {
            try{
                oneStepForAllPrg(prgList);
            } catch (InterruptedException e) {
                throw new MyException("Execution interrupted: " + e.getMessage());
            }
            prgList = removeCompletedPrg(repository.getPrgList());
        }

    }

    //Save the addresses from the symbol table that are RefValues
    private List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)//Keep only RefValues
                .map(v -> { RefValue v1 = (RefValue) v; return v1.address(); })//Get their addresses
                .collect(Collectors.toList());//Collect as a list
    }
    //From a list of addresses, get all the addresses that are referenced from the heap
    private List<Integer> getAddrFromHeap(List<Integer> symTableAddr, Map<Integer, IValue> heap) {

        boolean change = true;
        //Traverse the addresses from the symbol table
        List<Integer> newAddr = symTableAddr;
        //Keep adding new addresses until no new address is found
        while (change) {
            List<Integer> nextAddr = new ArrayList<>(newAddr);
            List<Integer> currentAddr = newAddr;

            heap.entrySet().stream()
                    //Keep only the entries whose key (address) is in the current address list(symTable + previously found addresses)
                    .filter(e -> currentAddr.contains(e.getKey()))
                    .forEach(e -> {
                        //If the value is a RefValue, get its address and add it to the next address list
                        if (e.getValue() instanceof RefValue) {
                            RefValue refValue = (RefValue) e.getValue();
                            int refAddr = refValue.address();
                            if (!nextAddr.contains(refAddr)) {
                                nextAddr.add(refAddr);
                            }
                        }
                    });

            if (nextAddr.size() > newAddr.size()) {
                newAddr = nextAddr;
            } else {
                change = false;
            }
        }
        return newAddr;
    }

    private Map<Integer, IValue> safeGarbageCollector(List<Integer> activeAddresses, Map<Integer, IValue> heap) {
        //Keep only the heap entries whose addresses are in the active addresses list
        return heap.entrySet().stream()
                .filter(e -> activeAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void runGarabageCollector(List<ProgramState> prgList) {
        ProgramState firstPrg = prgList.get(0);
        Map<Integer, IValue> currentHeap = firstPrg.getHeap().getContent();

        List<Integer> symTableAddresses = prgList.stream()
                .flatMap(prg -> getAddrFromSymTable(prg.getSymTable().getContent().values()).stream())
                .collect(Collectors.toList());

        List<Integer> activeAddresses = getAddrFromHeap(symTableAddresses, currentHeap);

        Map<Integer, IValue> newHeapContent = safeGarbageCollector(activeAddresses, currentHeap);

        firstPrg.getHeap().setContent(newHeapContent);
    }

}

