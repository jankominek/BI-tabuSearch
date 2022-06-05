import java.util.List;

public class TabuSearch {
    static Double goalFunction(Integer generalLengthInstance, List<Oligonucleotide> oligonucleotideList) {
        Double goalValue = oligonucleotideList.size() * 1.0 + (oligonucleotideList.size() * 1.0 / generalLengthInstance);
        return goalValue;
    }
}