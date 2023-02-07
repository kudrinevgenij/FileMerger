import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

final class CmdParser {
    private final String[] args;
    private TypeOfSort typeOfSort;
    private TypeOfData typeOfData;
    //private String outputFile;
    private File outputFile;
    private final ArrayList<String> inputFilesNames = new ArrayList<>();
    private ArrayList<File> inputFiles;


    public enum TypeOfSort {
        ASC,
        DESC
    }


    public enum TypeOfData {
        INT,
        STRING
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

        boolean sortOrder;

        if (typeOfSort == null) {
            typeOfSort = TypeOfSort.ASC;
            sortOrder = false;
        } else {
            sortOrder = true;
        }

        int outIndex;
        if (sortOrder) {
            outIndex = 2;
        } else {
            outIndex = 1;
        }
        outputFile = new File(args[outIndex]);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        inputFilesNames.addAll(Arrays.asList(args).subList(outIndex + 1, args.length));
    }

    private void createInputFiles(ArrayList<String> inputFilesNames) throws IOException {
        inputFiles = new ArrayList<>();
        for (String fileName: inputFilesNames){
            File file = new File(fileName);
            if(file.canRead())
                inputFiles.add(file);
        }
        if (inputFiles.size() == 0) {
            throw new IOException("Все входные файли недоступны для чтения");
        }
    }



    private void parseTypeOfData(String[] args) throws Exception {
        if (args[0].equals("-s")||args[1].equals("-s")) {
            typeOfData =TypeOfData.STRING;
        } else if (args[0].equals("-i")||args[1].equals("-i")) {
            typeOfData = TypeOfData.INT;
        } else throw new Exception("Введены неверные аргументы");
    }

    private void parseTypeOfSort(String[] args) {
        if (args[0].equals("-a")) {
            typeOfSort = TypeOfSort.ASC;
        } else if (args[0].equals("-d")) {
            typeOfSort = TypeOfSort.DESC;
        }
    }

    public File getOutputFile() {
        return outputFile;
    }

    public ArrayList<File> getInputFiles() {
        return inputFiles;
    }

    public TypeOfData getDataType() {
        return typeOfData;
    }

    public TypeOfSort getSortType() {
        return typeOfSort;
    }
}
