# Binomial Queue
is a generic heap data structure with fast theoretical runtime.

Constructor:
+ BinomialQueue<K, V>(int initialCapacity)
  - O(1)
+ BinomialQueue<K, V>()
  - O(1)
  - initial capacity = 63
Class methods:
+ boolean add(K key, V value)
  - adds a key value pair to the heap
  - keys are not unique
  - returns true on success
  - O(1)

+ K getMinKey()
  - O(1)
+ V getMinValue()
  - returns the value mapped to the min key
  - O(1)
+ V removeMin()
  - removes the min key from the heap
  - returns the attached value
  - O(log n)
+ void merge(BinomialHeap<K, V> heap)
  - does not change the other heap
  - O(log n)
+ boolean isEmpty()
  - O(1)
+ int size()
  - O(1)
