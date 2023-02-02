import java.util.Arrays;
import java.util.LinkedList;

final class CmdParser {
    private final String[] args;
    private TypeOfSort typeOfSort;
    private TypeOfData typeOfData;
    private String outputFile;
    private final LinkedList<String> inputFiles = new LinkedList<>();


    public enum TypeOfSort {
        A,
        D
    }


    public enum TypeOfData {
        I,
        S
    }

    public CmdParser(String[] args) throws Exception {
        this.args = args;
        parseCmdLine();
    }

    private void parseCmdLine() throws Exception {
        if (args.length < 3) {
            throw new Exception("Введены не все аргументы");
        }
        parseTypeOfData(args);
        parseTypeOfSort(args);

        boolean sortOption;

        if (typeOfSort == null) {
            typeOfSort = TypeOfSort.A;
            sortOption = false;
        } else {
            sortOption = true;
        }

        int outIndex;
        if (sortOption) {
            outIndex = 2;
        } else {
            outIndex = 1;
        }
        outputFile = args[outIndex];
        inputFiles.addAll(Arrays.asList(args).subList(outIndex + 1, args.length));
    }

    private void parseTypeOfData(String[] args) throws Exception {
        if (args[0].equals("-s")||args[1].equals("-s")) {
            typeOfData =TypeOfData.S;
        } else if (args[0].equals("-i")||args[1].equals("-i")) {
            typeOfData = TypeOfData.I;
        } else throw new Exception("Введены неверные аргументы");
    }

    private void parseTypeOfSort(String[] args) throws Exception {
        if (args[0].equals("-a")) {
            typeOfSort = TypeOfSort.A;
        } else if (args[0].equals("-d")) {
            typeOfSort = TypeOfSort.D;
        }
    }

    public String getOutputFileName() {
        return outputFile;
    }

    public LinkedList<String> getInputFileNames() {
        return inputFiles;
    }

    public TypeOfData getDataType() {
        return typeOfData;
    }

    public TypeOfSort getSortType() {
        return typeOfSort;
    }
}
