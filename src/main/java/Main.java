import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {
    static List<String> oligonucleotidesList;
    static Integer savedInstanceLength;
    static List<List<Integer>> matrix;
    static Map<String, List<Oligonucleotide>> generalGreedyMapInstances;
    static List<Sequence> sortedGreedyInstances;
    static int useGreedySequenceIndex = 0;

    static int iterationWithoutImprovement = 0;
    static int itInTabuList = 30;

    public static void main(String[] args) throws IOException {
        oligonucleotidesList = DataLoader.getOligonucleotidesFromFile();
        savedInstanceLength = DataLoader.getInstanceLength(oligonucleotidesList);

        System.out.println(oligonucleotidesList);
        System.out.println(savedInstanceLength);

        matrix = MatrixGenerator.generateMatrix(oligonucleotidesList);
        MatrixGenerator.printMatrix(matrix, oligonucleotidesList);

        generalGreedyMapInstances = GreedyAlgorithm.generateAllGreedyInstances(oligonucleotidesList, savedInstanceLength);

        Collections.sort(sortedGreedyInstances);
        Collections.reverse(sortedGreedyInstances);

        for (Sequence sequence : sortedGreedyInstances) {
            System.out.println(sequence.getRating());
            for (Oligonucleotide o : sequence.getOligonucleotidesList()) {
                System.out.print(o.getSequence() + " ");
            }
            System.out.println();
        }

        List<TabuSequence> tabuList = new ArrayList<>();
        sortedGreedyInstances.get(0).setNotUsedOliList(TabuSearch.listOfNotUsedOli(sortedGreedyInstances.get(0)));

        tabuList.add(new TabuSequence(sortedGreedyInstances.get(0), itInTabuList));
        Sequence bestSequence = new Sequence(sortedGreedyInstances.get(0));
        Sequence parenSequence = new Sequence(sortedGreedyInstances.get(0));

//        while (1) {
        List<Sequence> neighbors = new ArrayList<>();

        List<Integer> worstIndexes = TabuSearch.findTwoWorstOffset(parenSequence);

        Sequence swapChild = TabuSearch.actionSwap(parenSequence, worstIndexes);
        if (swapChild != null)
            neighbors.add(swapChild);

        neighbors.addAll(TabuSearch.actionNewInWorstPlace(parenSequence, worstIndexes.get(0)));
        neighbors.addAll(TabuSearch.actionNewInWorstPlace(parenSequence, worstIndexes.get(1)));

//        neighbors.addAll();

        for (Sequence s : neighbors)
            System.out.println(s);


//            if (iterationWithoutImprovement >= 10000) {
//                break;
//            }
//    }
    }
}
