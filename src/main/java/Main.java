package main.java;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        CmdParser cmdParseData = new CmdParser(args);
        mergeFiles(cmdParseData);
        for (Scanner scanner : inputFilesScanners)
            scanner.close();
        outputWriter.close();
    }

    private static CmdParser.TypeOfSort typeOfSort;
    private static CmdParser.TypeOfData typeOfData;
    private static FileWriter outputWriter;
    private static Scanner[] inputFilesScanners;
    private static String[] lastScannedValues;
    private static HashSet<Integer> readFiles = new HashSet<>();
    private static int scannerIndexWithMinValue;
    private static int valueToWrite;
    private static String stringToWrite;
    private static boolean wasAtLeastOneWrite;
    private static int lastWrittenValue;





    private static void mergeFiles(CmdParser cmdParseData) throws Exception {
        ArrayList<File> inputFiles = cmdParseData.getInputFiles();
        File outputFile = cmdParseData.getOutputFile();

        typeOfSort = cmdParseData.getSortType();
        typeOfData = cmdParseData.getDataType();

        outputWriter = new FileWriter(outputFile);
        inputFilesScanners = new Scanner[inputFiles.size()];
        for (int i = 0; i < inputFiles.size(); i++) {
            inputFilesScanners[i] = new Scanner(inputFiles.get(i));
        }
        lastScannedValues = new String[inputFilesScanners.length];
        resetFinalValueToWrite();
        while (readFiles.size() != inputFilesScanners.length) {
            for (int i = 0; i < inputFilesScanners.length; i++) {
                if (lastScannedValues[i] == null) {
                    if (!inputFilesScanners[i].hasNext()) {
                        readFiles.add(i);
                        continue;
                    }
                    lastScannedValues[i] = inputFilesScanners[i].nextLine();
                    if (lastScannedValues[i].contains(" ") || lastScannedValues[i].contains("\n")) {
                        throw new Exception("¯\\(0_o)/¯\nПохоже, что в файле пустая строка");
                    }
                }
                int currentValue = getValueDependsOnDataType(lastScannedValues[i]);
                if (compareDependsOnSortType(currentValue, valueToWrite)) {
                    valueToWrite = currentValue;
                    scannerIndexWithMinValue = i;
                    if (typeOfData == CmdParser.TypeOfData.STRING) {
                        stringToWrite = lastScannedValues[i];
                    }
                }
            }
            writeToOutput();
        }
    }

    private static int getValueDependsOnDataType(String line) throws Exception {
        if (typeOfData == CmdParser.TypeOfData.INT) {
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                throw new Exception("¯\\(0_o)/¯\nНе могу конвертировать строку" + line + "в число");
            }
        } else {
            return line.charAt(0);
        }
    }

    private static boolean compareDependsOnSortType(int currentValue, int writeToOutput) {
        if (typeOfSort == CmdParser.TypeOfSort.ASC) {
            return currentValue < writeToOutput;
        } else {
            return currentValue > writeToOutput;
        }
    }

    private static void resetFinalValueToWrite() {
        if (typeOfSort == CmdParser.TypeOfSort.ASC) {
            valueToWrite = Integer.MAX_VALUE;
        } else {
            valueToWrite = Integer.MIN_VALUE;
        }
    }

    private static void writeToOutput() throws IOException {
        if (readFiles.size() != inputFilesScanners.length) {
            if (checkLastWrittenValue(valueToWrite)) {
                if (typeOfData == CmdParser.TypeOfData.STRING && stringToWrite != null) {
                    outputWriter.write(stringToWrite + "\n");
                } else {
                    outputWriter.write(valueToWrite + "\n");
                }
                lastWrittenValue = valueToWrite;
                wasAtLeastOneWrite = true;
            }
            lastScannedValues[scannerIndexWithMinValue] = null;
            resetFinalValueToWrite();
        }
    }
    private static boolean checkLastWrittenValue(int currentWriteValue) {
        if (!wasAtLeastOneWrite) {
            return true;
        }

        if ((typeOfSort == CmdParser.TypeOfSort.ASC && currentWriteValue < lastWrittenValue)
                || typeOfSort == CmdParser.TypeOfSort.DESC && currentWriteValue > lastWrittenValue) {
            return false;
        } else {
            return true;
        }
    }
}


