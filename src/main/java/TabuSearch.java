import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TabuSearch {

    static Sequence addNewInRandomPlaceDeleteWorst(Sequence parent, List<Integer> worstIndexes, boolean deleteWorst) {
        List<Oligonucleotide> oligonucleotidesList = new ArrayList<>();

        for (Oligonucleotide o : parent.getOligonucleotidesList()) {
            oligonucleotidesList.add(new Oligonucleotide(o));
        }

        int length = parent.getLength();
        if (deleteWorst) {
            length = parent.getLength() - parent.getOligonucleotidesList().get(worstIndexes.get(0)).getOffset();
            int temp_index = worstIndexes.get(0);
            oligonucleotidesList.remove(temp_index);
        }

        int randomPlace = new Random().nextInt(oligonucleotidesList.size());

        for (int i = 0; i < parent.getNotUsedOliList().size(); i++) {
            oligonucleotidesList.add(randomPlace, new Oligonucleotide(parent.getNotUsedOliList().get(i), 0));
            int temp_length = checkNewOffset(worstIndexes.get(0), oligonucleotidesList, length);
            if (temp_length <= Main.savedInstanceLength) {
                return new Sequence(oligonucleotidesList, goalFunction(oligonucleotidesList), temp_length);
            }
            oligonucleotidesList.remove(randomPlace);
        }
        return null;
    }


    static List<Sequence> actionNewInWorstPlace(Sequence parent, Integer worstIndexes) {
        List<Sequence> sequences = new ArrayList<>();
        for (int i = 0; i < parent.getNotUsedOliList().size(); i++) {
            Sequence child = newInWorstPlace(parent, worstIndexes, i);
            if (child != null)
                sequences.add(child);
        }
        return sequences;
    }


    static Sequence newInWorstPlace(Sequence parent, Integer worstIndexes, int indexSeq) {
        List<Oligonucleotide> oligonucleotidesList = new ArrayList<>();

        for (Oligonucleotide o : parent.getOligonucleotidesList()) {
            oligonucleotidesList.add(new Oligonucleotide(o));
        }

        oligonucleotidesList.get(worstIndexes).setSequence(parent.getNotUsedOliList().get(indexSeq));

        int length = parent.getLength();
        length = checkNewOffset(worstIndexes, oligonucleotidesList, length);

        if (length > Main.savedInstanceLength)
            return null;

        return new Sequence(oligonucleotidesList, goalFunction(oligonucleotidesList), length);
    }

    static List<String> listOfNotUsedOli(Sequence sequence) {
        List<String> notUsedOliList = new ArrayList<>();

        for (String oli : Main.oligonucleotidesList) {
            boolean notUsed = true;
            for (Oligonucleotide o : sequence.getOligonucleotidesList()) {
                if (oli.equals(o.getSequence())) {
                    notUsed = false;
                }
            }
            if (notUsed)
                notUsedOliList.add(oli);
        }

        return notUsedOliList;
    }


    static Sequence actionSwap(Sequence parent, List<Integer> worstIndexes) {

        List<Oligonucleotide> oligonucleotidesList = new ArrayList<>();

        for (Oligonucleotide o : parent.getOligonucleotidesList()) {
            oligonucleotidesList.add(new Oligonucleotide(o));
        }

        Oligonucleotide o2 = oligonucleotidesList.get(worstIndexes.get(1));
        oligonucleotidesList.set(worstIndexes.get(1), oligonucleotidesList.get(worstIndexes.get(0)));
        oligonucleotidesList.set(worstIndexes.get(0), o2);

        int length = parent.getLength();
        length = checkNewOffset(worstIndexes.get(0), oligonucleotidesList, length);
        length = checkNewOffset(worstIndexes.get(1), oligonucleotidesList, length);

        if (length > Main.savedInstanceLength)
            return null;

        Sequence child = new Sequence(oligonucleotidesList, goalFunction(oligonucleotidesList), length);

//        System.out.println("Parent   " + parent.getOligonucleotidesList());
//        System.out.println("Children " + oligonucleotidesList);
//        System.out.println("Children " + child.toString());

        return child;
    }

    public static List<Integer> findTwoWorstOffset(Sequence parent) {
        List<Integer> worstIndexes = Arrays.asList(0, 0);
        List<Integer> worstOffsets = Arrays.asList(0, 0);

        worstOffsets.set(1, parent.getOligonucleotidesList().get(0).getOffset() + parent.getOligonucleotidesList().get(1).getOffset());

        for (int i = 1; i < parent.getOligonucleotidesList().size(); i++) {
            int oliOffset = parent.getOligonucleotidesList().get(i - 1).getOffset() + parent.getOligonucleotidesList().get(i).getOffset();
            if (oliOffset > worstOffsets.get(0)) {
                worstOffsets.set(0, oliOffset);
                worstIndexes.set(0, i - 1);
            } else if (oliOffset > worstOffsets.get(1)) {
                worstOffsets.set(1, oliOffset);
                worstIndexes.set(1, i - 1);
            }
        }

        return worstIndexes;
    }

    private static int checkNewOffset(int id, List<Oligonucleotide> oliList, int length) {
        if (id != 0) {
            length -= oliList.get(id).getOffset();
            int newOffset = MatrixGenerator.checkOffset(oliList.get(id - 1).getSequence(), oliList.get(id).getSequence());
            length += newOffset;
            oliList.get(id).setOffset(newOffset);
        } else {
            length -= oliList.get(id).getOffset();
            oliList.get(id).setOffset(0);
        }

        if (id < oliList.size()) {
            length -= oliList.get(id + 1).getOffset();
            int newOffset = MatrixGenerator.checkOffset(oliList.get(id).getSequence(), oliList.get(id + 1).getSequence());
            length += newOffset;
            oliList.get(id + 1).setOffset(newOffset);
        }

        return length;
    }


    static Double goalFunction(List<Oligonucleotide> oligonucleotideList) {
        Double goalValue = oligonucleotideList.size() * 1.0 + (oligonucleotideList.size() * 1.0 / Main.savedInstanceLength);
        return goalValue;
    }
}