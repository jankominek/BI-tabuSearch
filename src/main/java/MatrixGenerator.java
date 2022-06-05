import java.util.ArrayList;
import java.util.List;

public class MatrixGenerator {
    static List<List<Integer>> generateMatrix(List<String> ol) {
        List<List<Integer>> tempMatrix = new ArrayList<>();
        for (int i = 0; i < ol.size(); i++) {
            List<Integer> tempRowMatrix = new ArrayList<>();
            for (int j = 0; j < ol.size(); j++) {
                if (i == j) tempRowMatrix.add(100);
                else {
                    Integer offset = checkOffset(ol.get(i), ol.get(j), 3);
                    tempRowMatrix.add(offset);
                }
            }
            tempMatrix.add(tempRowMatrix);
        }

        return tempMatrix;
    }

    static Integer checkOffset(String current, String next, int length) {

        Integer offset = 1;

        for (int i = 1; i < length; i++) {
            if (current.substring(i, length).equals(next.substring(0, length - i))) {
                break;
            }
            offset += 1;
        }
        return offset;
    }
}
