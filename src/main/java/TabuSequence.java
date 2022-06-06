import lombok.Data;

import java.util.List;

@Data
public class TabuSequence {
    private Sequence sequence;
    private int leftInTabuListIteration;

    public TabuSequence(Sequence s, int leftInTabuListIteration) {
        this.sequence = new Sequence(s);
        this.leftInTabuListIteration = leftInTabuListIteration;
    }
}
