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
        int currentIndex = 0;
        int currentLevel = head.height();
        SkipListNode p = head;
        while ((p.key() < key) && currentLevel >= 0) {
            if (p.getNext(currentLevel).key() > key) {
                currentLevel--;
            }
            else  {
                currentIndex += p.getWidth(currentLevel);
                p = p.getNext(currentLevel);
            }
        }
        return currentIndex - 1;
    }

    public int select(int index) {
        int remainingDistance = index + 1;
        SkipListNode p = head;
        int currentLevel = head.height();
        while (currentLevel >= 0) {
            if (remainingDistance >= p.getWidth(currentLevel)) {
                remainingDistance -= p.getWidth(currentLevel);
                p = p.getNext(currentLevel);
            }
            else {
                currentLevel--;
            }
        }
        return p.key();
    }

}
