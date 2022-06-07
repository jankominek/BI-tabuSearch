import lombok.Data;

import java.util.List;

@Data
public class Sequence implements Comparable<Sequence> {
    private List<Oligonucleotide> oligonucleotidesList;
    private Double rating;
    private int length;
    private List<String> notUsedOliList;

    public Sequence(List<Oligonucleotide> oligonucleotidesList, Double rating, int length) {
        this.oligonucleotidesList = oligonucleotidesList;
        this.rating = rating;
        this.length = length;
    }

    public Sequence(Sequence s) {
        this.oligonucleotidesList = s.getOligonucleotidesList();
        this.rating = s.getRating();
        this.length = s.length;
        this.notUsedOliList = s.notUsedOliList;
    }

    @Override
    public int compareTo(Sequence sequence) {
        return getRating().compareTo(sequence.getRating());
    }


}
