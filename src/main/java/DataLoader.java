import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataLoader {
    public static List<String> directories = Arrays.asList("negative", "negative_repeat", "positive", "positive_end");
    public static String getDataFileName() {
        System.out.println("Please type file name to be processed");
        Scanner systemScanner = new Scanner(System.in);

        return systemScanner.nextLine();
    }

    public static List<String> getOligonucleotidesFromFile() throws FileNotFoundException {
        String fileName = getDataFileName();
        List<String> oligonucleotides = new ArrayList<>();

        File file = new File("src/assets/" + fileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            oligonucleotides.add(scanner.nextLine());
        }

        return oligonucleotides;
    }
    public static List<String> getOligonucleotidesFromFileWithName(String folder, String fileName) throws FileNotFoundException {
        List<String> oligonucleotides = new ArrayList<>();

        File file = new File("src/assets/" + folder + "/" + fileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            oligonucleotides.add(scanner.nextLine());
        }

        return oligonucleotides;
    }

    public static Integer getInstanceLength(List<String> oligonucleotides) {
//        return oligonucleotides.size() + oligonucleotides.get(0).length() - 1;
        System.out.println("Please type instance length");
        Scanner systemScanner = new Scanner(System.in);

        return Integer.parseInt(systemScanner.nextLine());
    }

    public static Integer getInstanceLengthWithLength(List<String> oligonucleotides, Integer instanceLength) {
//        return oligonucleotides.size() + oligonucleotides.get(0).length() - 1;
        System.out.println("Please type instance length");
        Scanner systemScanner = new Scanner(System.in);

        return Integer.parseInt(systemScanner.nextLine());
    }

    public static List<List<String>> getAllData(){
        List<List<String>> datas = new ArrayList<>();
        directories.stream().forEach( (dir) -> {
            List<String> currentProcessingData = new ArrayList<>();
            currentProcessingData.add(dir);
            List<String> toReturn = getDataFromFolder(new File("src/assets/"+dir));
            currentProcessingData.addAll(toReturn);
            datas.add(currentProcessingData);
        });

        return datas;
    }

    public static List<String> getDataFromFolder(final File folder){
        List<String> fileNameList = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getDataFromFolder(fileEntry);
            } else {
                fileNameList.add(fileEntry.getName());
            }
        }
        return fileNameList;
    }
}
