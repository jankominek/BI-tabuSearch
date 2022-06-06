import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Oligonucleotide {
    private String sequence;
    private Integer offset;

    public Oligonucleotide(String sequence, Integer offset) {
        this.sequence = sequence;
        this.offset = offset;
    }

    public Oligonucleotide(Oligonucleotide o) {
        this.sequence = o.sequence;
        this.offset = o.offset;
    }
}
