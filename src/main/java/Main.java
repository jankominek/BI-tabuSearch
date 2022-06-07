import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    static List<String> ansiList = Arrays.asList("\u001B[30m", "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m","\u001B[35m" ,"\u001B[36m", "\u001B[37m");
    static List<String> oligonucleotidesList;
    static Integer savedInstanceLength;
    static List<List<Integer>> matrix;
    static Map<String, List<Oligonucleotide>> generalGreedyMapInstances;
    static List<Sequence> sortedGreedyInstances;
    static int useGreedySequenceIndex = 0;

    static int iterationWithoutImprovement = 0;
    static int itInTabuList = 10;

    public static void main(String[] args) throws IOException {
        List<List<String>> datas = DataLoader.getAllData();
        System.out.println(datas.get(0).size()-1);
        AtomicReference<Integer> interate = new AtomicReference<>(0);
        datas.stream().forEach( (data) -> {
            String folderName = data.get(0);
            data.remove(0);
            System.out.println(ansiList.get(interate.get()) + "FOLDER NAME ---> " + folderName);
            data.stream().forEach( (file) -> {
                try {
                    oligonucleotidesList = DataLoader.getOligonucleotidesFromFileWithName(folderName, file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                Integer il = Integer.parseInt(file.substring(file.length()-3, file.length()));
                savedInstanceLength = Integer.parseInt(file.substring(file.length()-3, file.length()));
                matrix = MatrixGenerator.generateMatrix(oligonucleotidesList);
//                MatrixGenerator.printMatrix(matrix, oligonucleotidesList);

                generalGreedyMapInstances = GreedyAlgorithm.generateAllGreedyInstances(oligonucleotidesList, savedInstanceLength);

                Collections.sort(sortedGreedyInstances);
                Collections.reverse(sortedGreedyInstances);
                for (Sequence s : sortedGreedyInstances) {
                    int oldLength = s.getLength();
//            System.out.println("OldLength: " + oldLength);

                    for (int i = 1; i < s.getOligonucleotidesList().size(); i++) {
                        int newOffset = MatrixGenerator.checkOffset(s.getOligonucleotidesList().get(i-1).getSequence(),
                                s.getOligonucleotidesList().get(i).getSequence());
                        if (newOffset !=  s.getOligonucleotidesList().get(i).getOffset() || newOffset == 0 || s.getOligonucleotidesList().get(i).getOffset() == 0){
                            System.out.println("old: "+ s.getOligonucleotidesList().get(i) + " new:" +  newOffset);
                        }
                        s.getOligonucleotidesList().get(i).setOffset(newOffset);
                    }

                    if (s.getOligonucleotidesList().get(0).getOffset() != 0) {
                        System.out.println("Klopoty najmana 1");
                    }

                    int newLength = s.getOligonucleotidesList().get(0).getSequence().length();
                    for (int i = 0; i < s.getOligonucleotidesList().size(); i++) {
                        newLength += s.getOligonucleotidesList().get(i).getOffset();
//                System.out.println("NewLength: " + newLength);
                    }

                    if (oldLength != newLength) {
                        System.out.println("Klopoty najmana - zła długość");
                    }
                }
                System.out.print(" file ---> " + file);
                System.out.print("   olis size --->  " + sortedGreedyInstances.get(0).getOligonucleotidesList().size());
                System.out.println();
            });
            interate.updateAndGet(v -> v + 1);
            System.out.println(ANSI_RESET);

        });

//        List<Sequence> tabuList = new ArrayList<>();
//        sortedGreedyInstances.get(0).setNotUsedOliList(TabuSearch.listOfNotUsedOli(sortedGreedyInstances.get(0)));
//        tabuList.add(sortedGreedyInstances.get(0));
//
//        Sequence bestSequence = new Sequence(sortedGreedyInstances.get(0));
//        Double bestRating = bestSequence.getRating();
//        Sequence parenSequence = new Sequence(sortedGreedyInstances.get(0));

//        int iteratonCounter = 0;
//        while (true) {
//            iteratonCounter++;
//            List<Sequence> neighbors = new ArrayList<>();
//            List<Integer> worstIndexes = TabuSearch.findWorstOffset(parenSequence);
//
//            List<Sequence> child = TabuSearch.actionSwapAll(parenSequence, worstIndexes);
//            if (child.size() > 0) neighbors.addAll(child);
//
//            child = TabuSearch.actionNewInWorstPlace(parenSequence, worstIndexes);
//            if (child.size() > 0) neighbors.addAll(child);
//
//            child = TabuSearch.actionAddNewInRandomPlaceDeleteWorst(parenSequence, worstIndexes, true);
//            if (child.size() > 0) neighbors.addAll(child);
//
//            child = TabuSearch.actionAddNewInRandomPlaceDeleteWorst(parenSequence, worstIndexes, false);
//            if (child.size() > 0) neighbors.addAll(child);
//
//            child = TabuSearch.actionDelete(parenSequence, worstIndexes);
//            if (child.size() > 0) neighbors.addAll(child);
//
//            child = TabuSearch.actionAddNew(parenSequence);
//            if (child.size() > 0) {
//                neighbors.addAll(child);
//            }
//
////            for (Sequence n : neighbors) {
////                if (iterationWithoutImprovement % 2 < 1)
////                    n.setRating(TabuSearch.goalFunction(n.getOligonucleotidesList(), n.getLength()));
////                else
////                    n.setRating(TabuSearch.goalFunction1(n.getOligonucleotidesList(), n.getLength()));
////            }
//
//            Collections.sort(neighbors);
//            Collections.reverse(neighbors);
//
//            int newParentIndex = findNewParendIndex(neighbors, tabuList);
//            if (newParentIndex == neighbors.size()) {
//                break;
//            }
//
//            if (neighbors.get(newParentIndex).getRating() > bestRating) {
//                bestSequence = new Sequence(neighbors.get(newParentIndex));
//                bestRating = bestSequence.getRating();
//                iterationWithoutImprovement = 1;
//            } else iterationWithoutImprovement++;
//
//            if (iterationWithoutImprovement % 100 == 0 || neighbors.get(newParentIndex).getOligonucleotidesList().size() == 1) {
//                useGreedySequenceIndex += 1;
//
//                if (useGreedySequenceIndex >= sortedGreedyInstances.size()) {
//                    break;
//                }
//
//                sortedGreedyInstances.get(useGreedySequenceIndex).setNotUsedOliList(TabuSearch.listOfNotUsedOli(sortedGreedyInstances.get(useGreedySequenceIndex)));
//                tabuList.add(sortedGreedyInstances.get(useGreedySequenceIndex));
//                parenSequence = new Sequence(sortedGreedyInstances.get(useGreedySequenceIndex));
//
//                tabuList.clear();
//
//                continue;
//            }
//
//            if (iterationWithoutImprovement >= 1000) {
//                break;
//            }
//
//            if (tabuList.size() == itInTabuList) tabuList.remove(0);
//
//            neighbors.get(newParentIndex).setNotUsedOliList(TabuSearch.listOfNotUsedOli(neighbors.get(newParentIndex)));
//            tabuList.add(neighbors.get(newParentIndex));
//            parenSequence = new Sequence(neighbors.get(newParentIndex));
//
//            System.out.println(neighbors.get(0).getRating());
//        }
//
//        System.out.println(iteratonCounter);
//        System.out.println(bestSequence);
//        System.out.println(bestRating);
//        System.out.println(bestSequence.getOligonucleotidesList().size());
//        System.out.println(bestSequence.getLength());
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
