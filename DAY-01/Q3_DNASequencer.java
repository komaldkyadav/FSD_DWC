public class Q3_DNASequencer {
    StringBuilder dna = new StringBuilder(100000);
    void addSequence(char[] data) {
        dna.append(data);
    }
    void mutate(String oldPart, String newPart) {
        int pos = dna.indexOf(oldPart);
        if (pos != -1) {
            dna.replace(pos, pos + oldPart.length(), newPart);
            System.out.println("Mutation done at index " + pos);
        } else {
            System.out.println("Sequence not found");
        }
    }
    String getDNA() {
        return dna.toString();
    }
    public static void main(String[] args) {
        Q3_DNASequencer d = new Q3_DNASequencer();
        char[] batch1 = "AACTGAACGTTAGCTA".toCharArray();
        char[] batch2 = "CGTACGATCGATCGTA".toCharArray();
        d.addSequence(batch1);
        d.addSequence(batch2);
        System.out.println("DNA Sequence:");
        System.out.println(d.getDNA());
        d.mutate("CGTA", "TTTT");
        System.out.println("After Mutation:");
        System.out.println(d.getDNA());
        d.mutate("XXXX", "YYYY");
    }
}