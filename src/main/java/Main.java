import java.io.IOException;
import java.util.*;

public class Main {
    static List<String> oligonucleotidesList;
    static Integer savedInstanceLength;
    static List<List<Integer>> matrix;
    static Map<String, List<Oligonucleotide>> generalGreedyMapInstances;
    static List<Sequence> sortedGreedyInstances;
    static int useGreedySequenceIndex = 0;
    static List<Integer> usedIndexes = new ArrayList<>();

    static int iterationWithoutImprovement = 0;
    static int itInTabuList = 10;

    public static void main(String[] args) throws IOException {
        oligonucleotidesList = DataLoader.getOligonucleotidesFromFile();
        savedInstanceLength = DataLoader.getInstanceLength(oligonucleotidesList);

        matrix = MatrixGenerator.generateMatrix(oligonucleotidesList);
//        MatrixGenerator.printMatrix(matrix, oligonucleotidesList);

        generalGreedyMapInstances = GreedyAlgorithm.generateAllGreedyInstances(oligonucleotidesList, savedInstanceLength);

//        Collections.sort(sortedGreedyInstances);
//        Collections.reverse(sortedGreedyInstances);

        useGreedySequenceIndex = new Random().nextInt(sortedGreedyInstances.size());
//        System.out.println(useGreedySequenceIndex);
        usedIndexes.add(useGreedySequenceIndex);

        System.out.println(sortedGreedyInstances.get(useGreedySequenceIndex));

        List<Sequence> tabuList = new ArrayList<>();
        sortedGreedyInstances.get(useGreedySequenceIndex).setNotUsedOliList(TabuSearch.listOfNotUsedOli(sortedGreedyInstances.get(useGreedySequenceIndex)));
        tabuList.add(sortedGreedyInstances.get(useGreedySequenceIndex));

        Sequence bestSequence = new Sequence(sortedGreedyInstances.get(useGreedySequenceIndex));
        Double bestRating = bestSequence.getRating();
        Sequence parenSequence = new Sequence(sortedGreedyInstances.get(useGreedySequenceIndex));

        int iteratonCounter = 0;

        long time = 0;
        long startTime = System.currentTimeMillis();

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

//            for (Sequence n : neighbors) {
//                if (iterationWithoutImprovement % 2 < 1)
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
                System.out.println("poprawa\told: " + bestRating + "\tnew: " + neighbors.get(newParentIndex).getRating());
                bestSequence = new Sequence(neighbors.get(newParentIndex));
                bestRating = bestSequence.getRating();
                iterationWithoutImprovement = 1;
            } else iterationWithoutImprovement++;

            if (iterationWithoutImprovement % 100 == 0 || neighbors.get(newParentIndex).getOligonucleotidesList().size() == 1) {

                if (usedIndexes.size() >= sortedGreedyInstances.size()) {
                    break;
                }

                while (true) {
                    useGreedySequenceIndex = new Random().nextInt(sortedGreedyInstances.size());

                    boolean notInSeq = true;
                    for (int ui : usedIndexes) {
                        if (ui == useGreedySequenceIndex) {
                            notInSeq = false;
                            break;
                        }
                    }

                    if (notInSeq) {
                        usedIndexes.add(useGreedySequenceIndex);
                        break;
                    }
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

            if ((System.currentTimeMillis() - startTime) > 60000) {
                break;
            }
        }

        System.out.println("Iteration num:    " + iteratonCounter);
        System.out.println("Num of reversion: " + usedIndexes.size());
        System.out.println("Processing time:  " + (System.currentTimeMillis() - startTime) * 1.0 / 1000  + "s");
        System.out.println("Best Sequence:    " + bestSequence);
        System.out.println("Best Rating:      " + bestRating);
        System.out.println("Num od used Oli:  " + bestSequence.getOligonucleotidesList().size());
        System.out.println("Best seq length:  " + bestSequence.getLength());
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
