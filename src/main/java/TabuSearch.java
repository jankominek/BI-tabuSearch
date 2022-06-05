import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabuSearch {

    static List<String> oligonucleotidesList;
    static List<List<Integer>> matrix;

    public static void main(String[] args) throws IOException {
        String fileName = DataLoader.getDataFileName();
        oligonucleotidesList = DataLoader.getOligonucleotidesFromFile(fileName);
        //System.out.println(oligonucleotidesList);

        matrix = MatrixGenerator.generateMatrix(oligonucleotidesList);
        printMatrix(matrix);

        generateAllGreedyInstances();

    }






    static void generateAllGreedyInstances() {
        Map<String, List<Oligonucleotide>> greedyMapInstaces = new HashMap<>();
        oligonucleotidesList.stream().forEach((oligonucleotide) -> {
            List<Oligonucleotide> resultOfGreedyFunction = generateGreedyAlgorithmForSelectedInstance(oligonucleotidesList.indexOf(oligonucleotide), oligonucleotide.length(), 8);
            Double goal = goalFunction(8, resultOfGreedyFunction);
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

    }

    static List<Oligonucleotide> generateGreedyAlgorithmForSelectedInstance(Integer processedOli, Integer lengthInstance, Integer generalDNALengthSequence) {
        List<Oligonucleotide> selectedOlList = new ArrayList<>();
        Integer currentLength = 0;
        Oligonucleotide oligonucleotide = Oligonucleotide.builder()
                .sequence(oligonucleotidesList.get(processedOli))
                .offset(0)
                .starting(true)
                .build();
        currentLength += lengthInstance;
        selectedOlList.add(oligonucleotide);

        Integer index = processedOli;

        // to jest pętla po wszystkich oligonukleotydach indeksy i nie mają tutaj znaczenia ( nie chciałem rekurencji robić )
        for (int i = 0; i < oligonucleotidesList.size(); i++) {
            Integer bestSelectedIndex = index;
            Integer selectedOffset = 100;
            //szukanie najlepszego oligonukleotydu z offsetem
            for (int j = 0; j < oligonucleotidesList.size(); j++) {
                if (matrix.get(index).get(j) < selectedOffset) {
                    bestSelectedIndex = j;
                    selectedOffset = matrix.get(index).get(j);
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

        return selectedOlList;

    }

    static void printMatrix(List<List<Integer>> matrixList) {
        for (int i = 0; i < matrixList.get(0).size() + 1; i++) {
            if (i == 0) {
                System.out.print(String.format("%5s", " "));
            } else {
                System.out.print(String.format("%7s", oligonucleotidesList.get(i - 1)));
            }
        }
        System.out.println("");
        for (int i = 0; i < matrixList.size(); i++) {
            System.out.print(String.format("%2s", oligonucleotidesList.get(i)) + " |");
            for (int j = 0; j < matrixList.size(); j++) {

                System.out.print(String.format("%5s", matrixList.get(i).get(j)) + " |");
            }
            System.out.println("");
        }
    }

    static Double goalFunction(Integer generalLengthInstance, List<Oligonucleotide> oligonucleotideList) {
        Double goalValue = oligonucleotideList.size() * 1.0 + (oligonucleotideList.size() * 1.0 / generalLengthInstance);
        return goalValue;
    }


}
