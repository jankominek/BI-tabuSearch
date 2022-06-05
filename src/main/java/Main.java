import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class Main {
    static List<String> oligonucleotidesList;
    static Integer savedInstanceLength;
    static List<List<Integer>> matrix;
    static Map<String, List<Oligonucleotide>> generalGreedyMapInstances;

    public static void main(String[] args) throws URISyntaxException, IOException {
        oligonucleotidesList = DataLoader.getOligonucleotidesFromFile();
        savedInstanceLength = DataLoader.getInstanceLength(oligonucleotidesList);

        System.out.println(oligonucleotidesList);
        System.out.println(savedInstanceLength);

        matrix = MatrixGenerator.generateMatrix(oligonucleotidesList);
        MatrixGenerator.printMatrix(matrix, oligonucleotidesList);

        generalGreedyMapInstances = GreedyAlgorithm.generateAllGreedyInstances(oligonucleotidesList, savedInstanceLength);
    }
}
