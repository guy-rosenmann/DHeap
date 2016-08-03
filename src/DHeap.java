/**
 * @author Guy Rosenmann
 * 
 * D-Heap
 */



public class DHeap {

	private int size;				// Number of items in the heap
	private int max_size;			// Maximum number of items allowed in the heap
	private int d;					// The parameter of the heap
	private DHeap_Item[] array;		// The array of items which represents the heap

	/**
	 * DHeap(int m_d, int m_size)
	 * 
	 * Constructor
	 * Preconditions: m_d >= 2, m_size > 0
	 */
	DHeap(int m_d, int m_size) {
		max_size = m_size;
		d = m_d;
		array = new DHeap_Item[max_size];
		size = 0;
	}

	/**
	 * public int getSize()
	 * 
	 * Returns the heap's size (number of items currently in the heap)
	 */
	public int getSize() {
		return (this.size);
	}
	
	/**
     * public void arrayToHeap()
     *
     * The function builds a new heap from the given Item array.
     * The heap's previous array is deleted.
     * precondition: array1.length() <= max_size
     * postcondition: isHeap()
     *                size = array.length()
     */
	public int arrayToHeap(DHeap_Item[] array1) {
		int comparisons = 0;
		for (int i = 0; i < array1.length; i++) {	// Insert all items into the heap
			this.array[i] = array1[i];
			this.array[i].setPos(i);
		}
		this.size = array1.length;								// Update the size of the heap
		if (this.size != 0) {
			for (int i = (this.size - 2)/d; i >= 0; i--)		// Heapify-Down all non-leaf keys
				comparisons += heapifyDown(i);					// Update the number of comparisons
		}
		return comparisons;
	}
    
	/**
	 * private int heapifyDown(int i)
	 * 
	 * HeapifyDown the element at index i
	 * Returns the total number of comparisons made
	 */
	private int heapifyDown(int i) {
		int comparisons = 0;
		DHeap_Item smallest = this.array[i];
		int maxPossibleChildIndex = this.d * (i+1);	
		for (int j = 1 + this.d*i;
				j <= maxPossibleChildIndex && j < this.size; j++) {	// Iterates over all of the children of the item in index i
			comparisons++;
			if (smallest.getKey() > this.array[j].getKey())			// If child's key is smaller - update smallest pointer
				smallest = this.array[j];
		}
		if (smallest.getPos() != i) {	// If the smallest item is not the parent, switch parent with smallest child
			DHeap_Item tempItem = this.array[i];
			tempItem.setPos(smallest.getPos());
			smallest.setPos(i);
			this.array[i] = smallest;
			this.array[tempItem.getPos()] = tempItem;
			comparisons += heapifyDown(tempItem.getPos());	// Continue Heapify-Down process after switching with smallest child
		}
		return comparisons;
	}
    
	/**
	 * private int heapifyUp(int i)
	 * 
	 * HeapifyUp the element at index i
	 * Returns total number of comparisons made
	 */
    private int heapifyUp(int i) {
    	int comparisons = 0;
    	DHeap_Item parent;
    	while (i > 0) {										// While not the root
    		parent = this.array[DHeap.parent(i, this.d)];	// Update current parent
    		comparisons++;
    		if (parent.getKey() <= this.array[i].getKey())	// If parent's key is smaller (or equals) - done
    			break;
			DHeap_Item tempItem = this.array[i];			// Else - switch with parent
			tempItem.setPos(parent.getPos());
			parent.setPos(i);
			this.array[i] = parent;
			this.array[tempItem.getPos()] = tempItem;
			i = tempItem.getPos();							// Update current index
    	}
    	return comparisons;
    }

    /**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property.
     *   
     */
    public boolean isHeap() {
    	return isHeapAux(0); // Checks the heap property from the root down
    }
    
    /**
     * private boolean isHeapAux(int i)
     * 
     * A recursive function that returns true if and only if the D-ary tree rooted
     * at array[i] satisfies the heap property.
     */
    private boolean isHeapAux(int i) {
    	int maxPossibleChildIndex = this.d * (i+1);
    	for (int j = 1 + (this.d * i);	// Recursively call & check heap property, for each child
    			j <= maxPossibleChildIndex && j < this.size; j++) {
    		if ((this.array[i].getKey() > this.array[j].getKey()) || (!isHeapAux(j)))
    			return false;	// Heap property doesn't keep
    	}
    	return true;
    }


    /**
     * public static int parent(i), child(i,k)
     * (2 methods)
     *
     * precondition: i >= 0
     *
     * The methods compute the index of the parent and the k-th child of 
     * the item at position i in a complete D-ary tree stored in an array. 1 <= k <= d.
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) {
    	return ((i-1)/d);
    } 
    
    public static int child (int i, int k, int d) {
    	return (d*i + k);
    } 

    /**
    * public int Insert(DHeap_Item item)
    *
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) {        
    	this.array[this.size] = item;		// Insert new item as last in the array
    	item.setPos(this.size);
    	this.size++;						// Update the heap's size
    	return (this.heapifyUp(this.size - 1));	// Heapify-Up to restore the heap property if needed and return the number of comparisons
    }

    /**
    * public int Delete_Min()
    *
    * precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    public int Delete_Min() {
     	return (this.Delete(this.array[0]));	// Delete first item in the heap (minimal item)
    }


    /**
     * public String Get_Min()
     *
     * precondition: heapsize > 0
     *               isHeap()
     *				 size > 0
     * 
     * postcondition: isHeap()
     */
	public DHeap_Item Get_Min() {
		return (this.array[0]);
	}
	
	/**
     * public int Decrease_Key(DHeap_Item item, int delta)
     *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta)
    {
    	item.setKey(item.getKey() - delta);		// Decrease the key
    	return (this.heapifyUp(item.getPos()));	// Heapify-Up to restore the heap property if needed
    }
	
	  /**
     * public void Delete(DHeap_Item item)
     *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
	public int Delete(DHeap_Item item) {
		int comparisons = 0;
		if (this.size == 1)	// Heap has only 1 item
			this.size = 0;
		else {
			int pos = item.getPos();						// Get item position
			this.array[pos] = this.array[this.size - 1];	// Replace item with the last item in the heap
			this.array[pos].setPos(pos);					// Update its position
			this.size--;									// Update heap size
			comparisons = this.heapifyDown(pos);			// Heapify-down to restore the heap property
		}
		return comparisons;
	}
	
	/**
	* Sort the array in-place using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, value of item is irrelevant.
	* Returns the number of comparison performed.
	*/
	public static int DHeapSort(int[] array, int d) {
		int comparisons = 0;
		DHeap dheap = new DHeap(d, array.length);			// Create a new empty heap
		DHeap_Item[] items = new DHeap_Item[array.length];	// Create an empty dheap_items array
		for (int i = 0; i < array.length; i++)				// fills items array with numbers form input array
			items[i] = new DHeap_Item(null, array[i]);
		comparisons += dheap.arrayToHeap(items);			// Fills the heap with items array
		for (int i = 0; i < array.length; i++) {			// For each item in the heap
			array[i] = dheap.Get_Min().getKey();			// Deletes minimum from heap and places it in the right index in input array
			comparisons += dheap.Delete_Min();
		}
		return comparisons;
	}

}
