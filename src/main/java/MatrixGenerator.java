import java.util.ArrayList;
import java.util.List;

public class MatrixGenerator {
    public static List<List<Integer>> generateMatrix(List<String> oligonucleotides) {
        List<List<Integer>> tempMatrix = new ArrayList<>();

        for (int i = 0; i < oligonucleotides.size(); i++) {

            List<Integer> tempRowMatrix = new ArrayList<>();
            for (int j = 0; j < oligonucleotides.size(); j++) {

                if (i == j) tempRowMatrix.add(100);
                else {
                    Integer offset = checkOffset(oligonucleotides.get(i), oligonucleotides.get(j), oligonucleotides.get(i).length());
                    tempRowMatrix.add(offset);
                }
            }
            tempMatrix.add(tempRowMatrix);
        }

        return tempMatrix;
    }

    public static Integer checkOffset(String current, String next, int length) {
        int offset = 1;
        for (int i = 1; i < length; i++) {
            if (current.substring(i, length).equals(next.substring(0, length - i))) {
                break;
            }
            offset += 1;
        }
        return offset;
    }

    static void printMatrix(List<List<Integer>> matrixList, List<String> oligonucleotides) {
        for (int l = 0; l < oligonucleotides.get(0).length(); l++) {
            for (int i = -1; i < matrixList.get(0).size(); i++) {
                if (i == -1) {
                    System.out.printf("%" + (oligonucleotides.get(0).length() - 2) + "s", " ");
                } else {
                    System.out.printf("%7s", oligonucleotides.get(i).charAt(l));
                }
            }
            System.out.println("");
        }
        for (int i = 0; i < matrixList.size(); i++) {
            System.out.print(String.format("%2s", oligonucleotides.get(i)) + " |");

            for (int j = 0; j < matrixList.size(); j++) {
                System.out.print(String.format("%5s", matrixList.get(i).get(j)) + " |");
            }
            System.out.println("");
        }
    }
}
