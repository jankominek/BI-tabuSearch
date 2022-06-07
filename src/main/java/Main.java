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
    static int itInTabuList = 10;

    public static void main(String[] args) throws IOException {
        oligonucleotidesList = DataLoader.getOligonucleotidesFromFile();
        savedInstanceLength = DataLoader.getInstanceLength(oligonucleotidesList);

        matrix = MatrixGenerator.generateMatrix(oligonucleotidesList);
        MatrixGenerator.printMatrix(matrix, oligonucleotidesList);

        generalGreedyMapInstances = GreedyAlgorithm.generateAllGreedyInstances(oligonucleotidesList, savedInstanceLength);

        Collections.sort(sortedGreedyInstances);
        Collections.reverse(sortedGreedyInstances);

        System.out.println(sortedGreedyInstances.get(0));

        List<Sequence> tabuList = new ArrayList<>();
        sortedGreedyInstances.get(0).setNotUsedOliList(TabuSearch.listOfNotUsedOli(sortedGreedyInstances.get(0)));
        tabuList.add(sortedGreedyInstances.get(0));

        Sequence bestSequence = new Sequence(sortedGreedyInstances.get(0));
        Double bestRating = bestSequence.getRating();
        Sequence parenSequence = new Sequence(sortedGreedyInstances.get(0));

        int iteratonCounter = 0;
        while (true) {
            iteratonCounter++;
            List<Sequence> neighbors = new ArrayList<>();
            List<Integer> worstIndexes = TabuSearch.findWorstOffset(parenSequence);

            List<Sequence> child = TabuSearch.actionSwapAll(parenSequence, worstIndexes);
            if (child.size() > 0) neighbors.addAll(child);

            child = TabuSearch.actionNewInWorstPlace(parenSequence, worstIndexes);
            if (child.size() > 0) neighbors.addAll(child);

            child = TabuSearch.actionAddNewInRandomPlaceDeleteWorst(parenSequence, worstIndexes, true);
            if (child.size() > 0) neighbors.addAll(child);

            child = TabuSearch.actionAddNewInRandomPlaceDeleteWorst(parenSequence, worstIndexes, false);
            if (child.size() > 0) neighbors.addAll(child);

            child = TabuSearch.actionDelete(parenSequence, worstIndexes);
            if (child.size() > 0) neighbors.addAll(child);

            child = TabuSearch.actionAddNew(parenSequence);
            if (child.size() > 0) {
                neighbors.addAll(child);
            }

            for (Sequence s : neighbors) {
                int length = s.getLength();
                for (int i = 0; i < s.getOligonucleotidesList().size(); i++) {
                    TabuSearch.checkNewOffset(i, s.getOligonucleotidesList(), length);
                }

                int newLength = s.getOligonucleotidesList().get(0).getSequence().length();
                if (s.getOligonucleotidesList().get(0).getOffset() != 0) {
                    System.out.println("Klopoty najmana");
                }

                for (int i = 1; i < s.getOligonucleotidesList().size(); i++) {
                    newLength += s.getOligonucleotidesList().get(i).getOffset();
                }

                if (length != newLength) {
                    System.out.println("Klopoty najmana znowu");
                }

//            System.out.println(s);
            }
//            for (Sequence n : neighbors) {
////                if (iterationWithoutImprovement % 2 < 1)
//                    n.setRating(TabuSearch.goalFunction(n.getOligonucleotidesList(), n.getLength()));
//                else
//                    n.setRating(TabuSearch.goalFunction1(n.getOligonucleotidesList(), n.getLength()));
//            }

            Collections.sort(neighbors);
            Collections.reverse(neighbors);

            int newParentIndex = findNewParendIndex(neighbors, tabuList);
            if (newParentIndex == neighbors.size()) {
                break;
            }

            if (neighbors.get(newParentIndex).getRating() > bestRating) {
                bestSequence = new Sequence(neighbors.get(newParentIndex));
                bestRating = bestSequence.getRating();
                iterationWithoutImprovement = 1;
            } else iterationWithoutImprovement++;

            if (iterationWithoutImprovement % 100 == 0 || neighbors.get(newParentIndex).getOligonucleotidesList().size() == 1) {
                useGreedySequenceIndex += 1;

                if (useGreedySequenceIndex >= sortedGreedyInstances.size()) {
                    break;
                }

                sortedGreedyInstances.get(useGreedySequenceIndex).setNotUsedOliList(TabuSearch.listOfNotUsedOli(sortedGreedyInstances.get(useGreedySequenceIndex)));
                tabuList.add(sortedGreedyInstances.get(useGreedySequenceIndex));
                parenSequence = new Sequence(sortedGreedyInstances.get(useGreedySequenceIndex));

                tabuList.clear();

                continue;
            }

            if (iterationWithoutImprovement >= 1000) {
                break;
            }

            if (tabuList.size() == itInTabuList) tabuList.remove(0);

            neighbors.get(newParentIndex).setNotUsedOliList(TabuSearch.listOfNotUsedOli(neighbors.get(newParentIndex)));
            tabuList.add(neighbors.get(newParentIndex));
            parenSequence = new Sequence(neighbors.get(newParentIndex));

            System.out.println(neighbors.get(0).getRating());
        }

        System.out.println(iteratonCounter);
        System.out.println(bestSequence);
        System.out.println(bestRating);
        System.out.println(bestSequence.getOligonucleotidesList().size());
        System.out.println(bestSequence.getLength());
    }

    static int findNewParendIndex(List<Sequence> neighbors, List<Sequence> tabuList) {
        int newParentIndex = neighbors.size();
        for (int i = 0; i < neighbors.size(); i++) {
            boolean onTabu = false;
            for (Sequence t : tabuList) {
                if (t.getRating().equals(neighbors.get(i).getRating()) &&
                        t.getOligonucleotidesList().equals(neighbors.get(i).getOligonucleotidesList()) &&
                        t.getLength() == neighbors.get(i).getLength()) {
                    onTabu = true;
                    break;
                }
            }
            if (!onTabu) {
                newParentIndex = i;
                break;
            }
        }
        return newParentIndex;
    }

}
