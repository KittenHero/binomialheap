import java.lang.reflect.Array;

import javax.naming.SizeLimitExceededException;
public class BinomialQueue<K extends Comparable<K>, V> {
	
	protected class BinomialHeap {
		K minKey;
		V value;
		BinomialHeap top;
		BinomialHeap bottom;
		int level;
		
		protected BinomialHeap(K priority, V val) {
			
			this.minKey = priority;
			this.value = val;
			this.top = this.bottom = null;
			this.level = 0;
		}
		
		protected void merge(BinomialHeap other) {
			if (other.level != this.level) throw new IllegalArgumentException("unmergable heaps");
			
			BinomialHeap copy = new BinomialHeap(this.minKey, this.value);
			copy.top = this.top;
			copy.bottom = this.bottom;
			copy.level = this.level++;
			
			if ((other.minKey).compareTo(this.minKey) < 0) {
				this.minKey = other.minKey;
				this.value = other.value;
				this.top = other;
				this.bottom = copy;
			} else {
				this.top = copy;
				this.bottom = other;
			}
		}
		
		protected K getMinKey() { return this.minKey; }
		protected V getMinValue() { return this.value; }
		
		protected BinomialHeap[] getSubtrees() {
			BinomialHeap[] subtrees = makeGenArray(level);
			
			BinomialHeap cur = this;
			int index = subtrees.length - 1;
			while (cur.top != null) {
				subtrees[index--] = cur.bottom;
				cur = cur.top;
			}
			
			return subtrees;
		}
	}
	
	BinomialHeap[] queue;
	int minIndex = -1;
	int size;
	int capacity;

	public BinomialQueue() { this(63); }
	
	public BinomialQueue(int capacity) {
		size = 0;
		queue = makeGenArray((int) (Math.log(capacity)/Math.log(2)));
		this.capacity = (int) Math.pow(2, queue.length) - 1;
	}
	
	@SuppressWarnings("unchecked")
	private BinomialHeap[] makeGenArray(int size) {
		return (BinomialHeap[]) Array.newInstance(BinomialHeap.class, size);
	}
	
	public boolean isEmpty() { return size == 0; }
	public int size() { return size; }
	public V getMinValue() { return size == 0 ? null : queue[minIndex].getMinValue(); }
	public K getMinKey() { return size == 0 ? null : queue[minIndex].getMinKey(); }
	public boolean add(K priority, V value) throws SizeLimitExceededException {
		
		if (++size > capacity) growQueue();
		
		BinomialHeap[] bin0 = makeGenArray(1);
		bin0[0] = new BinomialHeap(priority, value);
		this.meld(bin0);
		return true;
	}
	
	public V removeMin() {
		if (size == 0) return null;
		
		size--;
		V min = queue[minIndex].getMinValue();
		
		BinomialHeap[] subtrees = queue[minIndex].getSubtrees();
		queue[minIndex] = null;
		this.meld(subtrees);
		
		return min;
	}
	
	public void merge(BinomialQueue<K,V> other) throws SizeLimitExceededException {
		this.size += other.size;
		while (size > capacity) {
			growQueue();
		}
		this.meld(other.queue);
		other.queue = makeGenArray(other.queue.length);
		other.size = 0;
		other.minIndex = -1;
	}

	private void meld(BinomialHeap[] other) {
		
		int index = 0;
		BinomialHeap carrier = null;
		while (index < other.length) {
			
			// carrier digit                                        //this, other, carrier
			if (carrier != null && other[index] != null)            //0/1 + 1 + 1 
				carrier.merge(other[index]);
			else if (carrier != null && queue[index] != null) {     //1 + 0 + 1
				carrier.merge(queue[index]);
				queue[index] = null;
			} else if (queue[index] != null && other[index] != null) {//1 + 1 + 0
				carrier = queue[index];
				queue[index] = null;
				carrier.merge(other[index]);
			}
			
			//output digit
			else if (queue[index] == null && other[index] == null) {//0 + 0 + 0/1
				queue[index] = carrier;
				carrier = null;
			} else if (queue[index] == null && carrier == null) {   //0 + 1 + 0
				queue[index] = other[index];
			}                                                       //omitted 1 + 0 + 0
			
			index++;
		}
		
		if (carrier != null) {
			while (index < queue.length && queue[index] != null) {
				carrier.merge(queue[index]);
				queue[index++] = null;
			}
			queue[index] = carrier;
		}
		
		findMin();
	}
	
	private void findMin() {
		minIndex = -1;
		for (int i = 0; i < queue.length; i++) {
			if (queue[i] != null)
 				if (minIndex == -1 || queue[i].getMinKey().compareTo(queue[minIndex].getMinKey()) < 0)
					minIndex = i;
		}
	}
	
	private void growQueue() throws SizeLimitExceededException {
		if (capacity == Integer.MAX_VALUE) throw new SizeLimitExceededException();
		BinomialHeap[] newQ = makeGenArray(queue.length + 4);
		capacity = (int) Math.pow(2, newQ.length) - 1;
		
		for (int i = 0; i < queue.length; i++) {
			newQ[i] = queue[i];
		}
		queue = newQ;
	}
}
