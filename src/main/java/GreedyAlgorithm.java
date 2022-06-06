import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreedyAlgorithm {
    static private int seqLength = 0;

    static Map<String, List<Oligonucleotide>> generateAllGreedyInstances(List<String> oligonucleotidesList, Integer savedInstanceLength) {
        Map<String, List<Oligonucleotide>> greedyMapInstaces = new HashMap<>();
        List<Sequence> greedySequenceList = new ArrayList<>();

        oligonucleotidesList.forEach((oligonucleotide) -> {
            List<Oligonucleotide> resultOfGreedyFunction = generateGreedyAlgorithmForSelectedInstance(oligonucleotidesList.indexOf(oligonucleotide), oligonucleotide.length(), savedInstanceLength, oligonucleotidesList);
            Double goal = TabuSearch.goalFunction(resultOfGreedyFunction);
            String key = "k" + greedyMapInstaces.size() + "/" + goal;
            greedyMapInstaces.put(key, resultOfGreedyFunction);

            Sequence sequence = new Sequence(resultOfGreedyFunction, goal, seqLength);
            greedySequenceList.add(sequence);
        });

        Main.sortedGreedyInstances = greedySequenceList;

        for (Map.Entry<String, List<Oligonucleotide>> entry : greedyMapInstaces.entrySet()) {
            System.out.print(entry.getKey() + "   ");
            for (Oligonucleotide oli : entry.getValue()) {
                System.out.print(oli.getSequence() + "   ");
            }
            System.out.println();
        }

        return greedyMapInstaces;

    }

    static List<Oligonucleotide> generateGreedyAlgorithmForSelectedInstance(Integer processedOli, Integer lengthInstance, Integer generalDNALengthSequence, List<String> oligonucleotidesList) {
        List<Oligonucleotide> selectedOlList = new ArrayList<>();
        List<Integer> blockedIndexes = new ArrayList<>();
        Integer currentLength = 0;

        Oligonucleotide oligonucleotide = Oligonucleotide.builder()
                .sequence(oligonucleotidesList.get(processedOli))
                .offset(0)
                .build();

        currentLength += lengthInstance;
        selectedOlList.add(oligonucleotide);
        blockedIndexes.add(processedOli);

        int index = processedOli;

        // to jest pętla po wszystkich oligonukleotydach indeksy i nie mają tutaj znaczenia ( nie chciałem rekurencji robić )
        for (int i = 0; i < oligonucleotidesList.size(); i++) {
            int bestSelectedIndex = index;
            Integer selectedOffset = 100;

            //szukanie najlepszego oligonukleotydu z offsetem
            for (int j = 0; j < oligonucleotidesList.size(); j++) {
                if (Main.matrix.get(index).get(j) < selectedOffset) {
                    if (!blockedIndexes.contains(j)) {
                        bestSelectedIndex = j;
                        selectedOffset = Main.matrix.get(index).get(j);
                    }
                }
            }
            //dodanie znalezionego offsetu do ogólnej długości
            currentLength += selectedOffset;

            //sprawdzenie czy możemy taki oligonukleotyd dodać
            if (currentLength <= generalDNALengthSequence) {
                Oligonucleotide selectedOligonucleotide = Oligonucleotide.builder()
                        .sequence(oligonucleotidesList.get(bestSelectedIndex))
                        .offset(selectedOffset)
                        .build();

                selectedOlList.add(selectedOligonucleotide);
                index = bestSelectedIndex;
            } else {
                seqLength = currentLength - selectedOffset;
                break;
            }
        }
//        System.out.println(blockedIndexes);
        return selectedOlList;
    }
}
