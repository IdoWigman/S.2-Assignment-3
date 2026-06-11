public class IndexableSkipList extends AbstractSkipList {
    final protected double p;	// p is the probability for "success" in the geometric process generating the height of each node.
    public IndexableSkipList(double probability) {
        super();
        this.p = probability;
    }
	
	@Override
    public void decreaseHeight() {
        this.head.removeLevel();
        this.tail.removeLevel();
    }

    @Override
    public SkipListNode find(int key) {
        SkipListNode p = head;
        for (int i = this.head.height(); i > 0; i--) {
            while (p.getNext(i) != null && p.getNext(i).key() <= key) {
                p = p.getNext(i);
            }
        }
        return p;
    }

    @Override
    public int generateHeight() {
        double head = Math.random();
        int count = 1;
        while (head > this.p) {
            count++;
            head = Math.random();
        }
        return count;
    }

    public int rank(int key) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

    public int select(int index) {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }

}
