import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Oligonucleotide {

    private String sequence;
    private Integer offset;
    private Boolean starting;

}
