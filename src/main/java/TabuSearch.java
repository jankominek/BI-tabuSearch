import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TabuSearch {

    static List<Sequence> actionAddNewInRandomPlaceDeleteWorst(Sequence parent, List<Integer> worstIndexes, boolean deleteWorst) {
        List<Sequence> sequenceList = new ArrayList<>();

        for (int i = 0; i < worstIndexes.size() - 1; i++) {
            for (int j = 0; j < parent.getNotUsedOliList().size(); j++) {
                Sequence child = addNewInRandomPlaceDeleteWorst(parent, i, deleteWorst, j);
                if (child != null)
                    sequenceList.add(child);
            }
        }

        return sequenceList;
    }

    static Sequence addNewInRandomPlaceDeleteWorst(Sequence parent, Integer worstIndexes, boolean deleteWorst, int notUsedOliIndex) {
        List<Oligonucleotide> oligonucleotidesList = new ArrayList<>();

        for (Oligonucleotide o : parent.getOligonucleotidesList()) {
            oligonucleotidesList.add(new Oligonucleotide(o));
        }

        int length = parent.getLength();
        if (deleteWorst) {
            length = length - oligonucleotidesList.get(worstIndexes).getOffset();
            int temp_index = worstIndexes;
            oligonucleotidesList.remove(temp_index);
            length = checkNewOffset(worstIndexes, oligonucleotidesList, length);
        }

        int randomPlace = new Random().nextInt(oligonucleotidesList.size());

        oligonucleotidesList.add(randomPlace, new Oligonucleotide(parent.getNotUsedOliList().get(notUsedOliIndex), 0));
        length = checkNewOffset(randomPlace, oligonucleotidesList, length);

        if (length <= Main.savedInstanceLength) {
            return new Sequence(oligonucleotidesList, goalFunction(oligonucleotidesList, length), length);
        }

        return null;
    }

    static List<Sequence> actionNewInWorstPlace(Sequence parent, List<Integer> worstIndexes) {
        List<Sequence> sequences = new ArrayList<>();

        for (int i = 0; i < worstIndexes.size(); i++) {
            for (int j = 0; j < parent.getNotUsedOliList().size(); j++) {
                Sequence child = newInWorstPlace(parent, i, j);
                if (child != null)
                    sequences.add(child);
            }
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

        return new Sequence(oligonucleotidesList, goalFunction(oligonucleotidesList, length), length);
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

    static List<Sequence> actionSwapAll(Sequence parent, List<Integer> worstIndexes) {
        List<Sequence> sequenceList = new ArrayList<>();

        for (int i = 0; i < worstIndexes.size() - 1; i++) {
            for (int j = i + 1; j < worstIndexes.size(); j++) {
                Sequence child = actionSwap(parent, worstIndexes.get(i), worstIndexes.get(j));
                if (child != null) {
                    sequenceList.add(child);
                }
            }
        }
        return sequenceList;
    }

    static Sequence actionSwap(Sequence parent, int firstOli, int secondOli) {

        List<Oligonucleotide> oligonucleotidesList = new ArrayList<>();

        for (Oligonucleotide o : parent.getOligonucleotidesList()) {
            oligonucleotidesList.add(new Oligonucleotide(o));
        }

        Oligonucleotide o2 = oligonucleotidesList.get(secondOli);
        oligonucleotidesList.set(secondOli, oligonucleotidesList.get(firstOli));
        oligonucleotidesList.set(firstOli, o2);

        int length = parent.getLength();
        length = checkNewOffset(firstOli, oligonucleotidesList, length);
        length = checkNewOffset(secondOli, oligonucleotidesList, length);

        if (length > Main.savedInstanceLength)
            return null;

        return new Sequence(oligonucleotidesList, goalFunction(oligonucleotidesList, length), length);
    }

    public static List<Integer> findWorstOffset(Sequence parent) {
        int wantedNumber = 50;
        if (parent.getOligonucleotidesList().size() <= wantedNumber) {
            wantedNumber = parent.getOligonucleotidesList().size() / 2;
        }

        List<Integer> worstIndexes = new ArrayList<>();
        List<Integer> worstOffsets = new ArrayList<>();

        int firstOffset = parent.getOligonucleotidesList().get(0).getOffset() + parent.getOligonucleotidesList().get(1).getOffset();

        for (int i = 0; i < wantedNumber; i++) {
            worstOffsets.add(firstOffset);
            worstIndexes.add(0);
        }

        for (int i = 2; i < parent.getOligonucleotidesList().size(); i++) {
            int oliOffset = parent.getOligonucleotidesList().get(i - 1).getOffset() + parent.getOligonucleotidesList().get(i).getOffset();

            for (int j = 0; j < wantedNumber; j++) {
                if (oliOffset > worstOffsets.get(j)) {
                    worstOffsets.set(j, oliOffset);
                    worstIndexes.set(j, i - 1);
                    break;
                }
            }
        }

        return worstIndexes;
    }

    static int checkNewOffset(int id, List<Oligonucleotide> oliList, int length) {
        if (id != 0) {
            length -= oliList.get(id).getOffset();
            int newOffset = MatrixGenerator.checkOffset(oliList.get(id - 1).getSequence(), oliList.get(id).getSequence());
            length += newOffset;
            oliList.get(id).setOffset(newOffset);
        } else {
            length -= oliList.get(id).getOffset();
            oliList.get(id).setOffset(0);
        }

        if (id < oliList.size() - 1) {
            length -= oliList.get(id + 1).getOffset();
            int newOffset = MatrixGenerator.checkOffset(oliList.get(id).getSequence(), oliList.get(id + 1).getSequence());
            length += newOffset;
            oliList.get(id + 1).setOffset(newOffset);
        }

        return length;
    }

    static List<Sequence> actionDelete(Sequence parent, List<Integer> worstIndexes) {
        List<Sequence> sequenceList = new ArrayList<>();

        for (int i = 0; i < worstIndexes.size(); i++) {
            Sequence child = deleteOli(parent, worstIndexes.get(i));
            if (child != null)
                sequenceList.add(child);
        }

        return sequenceList;
    }

    static Sequence deleteOli(Sequence parent, Integer worstIndexes) {
        List<Oligonucleotide> oligonucleotidesList = new ArrayList<>();

        for (Oligonucleotide o : parent.getOligonucleotidesList()) {
            oligonucleotidesList.add(new Oligonucleotide(o));
        }

        int length = parent.getLength() - oligonucleotidesList.get(worstIndexes).getOffset();
        oligonucleotidesList.remove(worstIndexes.intValue());
        length = checkNewOffset(worstIndexes, oligonucleotidesList, length);

        if (length <= Main.savedInstanceLength) {
            return new Sequence(oligonucleotidesList, goalFunction(oligonucleotidesList, length), length);
        }

        return null;
    }

    static Double goalFunction(List<Oligonucleotide> oligonucleotideList, int length) {
        Double goalValue = oligonucleotideList.size() * 1.0 / Main.oligonucleotidesList.size() + (oligonucleotideList.size() * 1.0 / length);
        return goalValue;
    }
}