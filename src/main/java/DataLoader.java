import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataLoader {
    public static String getDataFileName() {
        System.out.println("Please type file name to be processed");
        Scanner systemScanner = new Scanner(System.in);

        return systemScanner.nextLine();
    }

    public static List<String> getOligonucleotidesFromFile() throws FileNotFoundException {
        String fileName = getDataFileName();
        List<String> oligonucleotides = new ArrayList<>();

        File file = new File("src/assets/" + fileName + ".txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            oligonucleotides.add(scanner.nextLine());
        }

        return oligonucleotides;
    }

    public static Integer getInstanceLength(List<String> oligonucleotides) {
        return oligonucleotides.size() + oligonucleotides.get(0).length() - 1;
    }
}
