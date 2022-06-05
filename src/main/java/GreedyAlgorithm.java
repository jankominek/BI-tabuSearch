import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreedyAlgorithm {
    static Map<String, List<Oligonucleotide>> generateAllGreedyInstances(List<String> oligonucleotidesList, Integer savedInstanceLength) {
        Map<String, List<Oligonucleotide>> greedyMapInstaces = new HashMap<>();

        oligonucleotidesList.stream().forEach((oligonucleotide) -> {
            List<Oligonucleotide> resultOfGreedyFunction = generateGreedyAlgorithmForSelectedInstance(oligonucleotidesList.indexOf(oligonucleotide), oligonucleotide.length(), savedInstanceLength, oligonucleotidesList);
            Double goal = TabuSearch.goalFunction(savedInstanceLength, resultOfGreedyFunction);
            String key = "k" + greedyMapInstaces.size() + "/" + goal;
            greedyMapInstaces.put(key, resultOfGreedyFunction);
        });

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
                .starting(true)
                .build();

        currentLength += lengthInstance;
        selectedOlList.add(oligonucleotide);
        blockedIndexes.add(processedOli);

        Integer index = processedOli;

        // to jest pętla po wszystkich oligonukleotydach indeksy i nie mają tutaj znaczenia ( nie chciałem rekurencji robić )
        for (int i = 0; i < oligonucleotidesList.size(); i++) {
            Integer bestSelectedIndex = index;
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
                        .starting(false)
                        .build();

                selectedOlList.add(selectedOligonucleotide);
                index = bestSelectedIndex;
            } else {
                break;
            }
        }
//        System.out.println(blockedIndexes);
        return selectedOlList;
    }
}
