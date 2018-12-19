import com.sun.istack.internal.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// Attention: comparable supported but comparator is not
public class Treap<K extends Comparable<K>> implements SortedSet<K> {

    private static final Random rand = new Random();
    private Node<K> root = null;
    private int size;
    int subSize = 0;
    private K fromElement;
    private K toElement;

    static class Node<K> {
        Node<K> right, left;
        final double priority;
        private K value;

        Node(K value) {
            this.value = value;
            this.priority = rand.nextDouble();
        }

    }

    class SubTree extends Treap<K> {

        SubTree() {
            Treap.this.subSize = 0;
            countSubSize();
        }

        @Override
        public boolean add(K data) {
            Node<K> temp = find(data);
            if (temp != null) {
                if (temp.value.compareTo(data) == 0) return false;
                if (fromElement != null && data.compareTo(fromElement) < 0) return false;
                if (toElement != null && data.compareTo(toElement) >= 0) return false;
            }
            root = add(root, data);
            size++;
            Treap.this.subSize++;
            return true;
        }

        @Override
        public boolean remove(Object o) {
            if (o == null) return false;
            @SuppressWarnings("unchecked")
            K data = (K) o;
            Node<K> node = find(data);
            if (node == null) return false;
            if (!this.contains(o)) return false;
            root = remove(root, data);
            size--;
            Treap.this.subSize--;
            return true;
        }

        @Override
        public int size() {
            return Treap.this.subSize;
        }

        @Override
        public boolean contains(Object o) {
            @SuppressWarnings("unchecked")
            K t = (K) o;
            Node<K> closest = find(t);
            return closest != null && t.compareTo(closest.value) == 0 &&
                    (fromElement == null || t.compareTo(fromElement) >= 0) &&
                    (toElement == null || t.compareTo(toElement) < 0);
        }

    }

    private void countSubSize() {
        if (fromElement == null && toElement != null) {
            for (K k : Treap.this) {
                if (k.compareTo(toElement) < 0) subSize++;
            }
        }
        if (fromElement != null && toElement == null) {
            for (K k : Treap.this) {
                if (k.compareTo(fromElement) >= 0) subSize++;
            }
        }
        if (fromElement != null && toElement != null) {
            for (K k : Treap.this) {
                if (k.compareTo(toElement) < 0 && k.compareTo(fromElement) >= 0) subSize++;
            }
        }
    }


    private Node<K> find(K value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<K> find(Node<K> start, K value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    // check if the tree is naturally ordered (left < node < right)
    boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<K> node) {
        Node<K> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<K> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    // maximum heap implemented
    boolean checkPriorityOrder() {
        return root == null || checkPriorityOrder(root);
    }

    private boolean checkPriorityOrder(Node<K> node) {
        Node<K> left = node.left;
        if (left != null && (left.priority > node.priority || !checkPriorityOrder(left))) return false;
        Node<K> right = node.right;
        return right == null || right.priority < node.priority && checkPriorityOrder(right);
    }

    @Nullable
    @Override
    public Comparator<? super K> comparator() {
        return null;
    }

    @NotNull
    @Override
    public SortedSet<K> subSet(K fromElement, K toElement) {
        this.fromElement = fromElement;
        this.toElement = toElement;
        return new SubTree();
    }

    @NotNull
    @Override
    public SortedSet<K> headSet(K toElement) {
        this.fromElement = null;
        this.toElement = toElement;
        return new SubTree();
    }

    @NotNull
    @Override
    public SortedSet<K> tailSet(K fromElement) {
        this.fromElement = fromElement;
        this.toElement = null;
        return new SubTree();
    }

    @Override
    public K first() {
        if (root == null) throw new NoSuchElementException();
        Node<K> current = this.root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public K last() {
        if (root == null) throw new NoSuchElementException();
        Node<K> current = this.root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        K t = (K) o;
        Node<K> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    @NotNull
    @Override
    public Iterator<K> iterator() {
        return new TreapIterator();
    }


    @NotNull
    @Override
    public Object[] toArray() {
        Object[] result = new Object[this.size];
        Iterator<K> iterator = this.iterator();
        for (int i = 0; i < this.size; i++) result[i] = iterator.next();
        return result;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public Object[] toArray(@NotNull Object[] a) {
        Object[] array = this.toArray();
        if (this.size >= 0) System.arraycopy(array, 0, a, 0, this.size);
        return a;
    }

    @Override
    public boolean add(K data) {
        if (data == null) return false;
        Node<K> temp = find(data);
        if (temp != null) {
            if (temp.value.compareTo(data) == 0) return false;
        }
        root = add(root, data);
        size++;
        return true;
    }

    Node<K> add(Node<K> node, K data) {
        if (node == null) {
            Node<K> newNode = new Node<>(data);
            if (toElement != null && fromElement != null) {
                if (newNode.value.compareTo(toElement) < 0 &&
                        newNode.value.compareTo(fromElement) >= 0) {
                    subSize++;
                }
            } else if (toElement != null && newNode.value.compareTo(toElement) < 0
                    || fromElement != null && newNode.value.compareTo(fromElement) >= 0) {
                subSize++;
            }
            return newNode;
        }


        int compare = data.compareTo(node.value);
        if (compare < 0) {
            node.left = add(node.left, data);
            if (node.priority < node.left.priority)
                return rotateRight(node);
        } else if (compare > 0) {
            node.right = add(node.right, data);
            if (node.priority < node.right.priority)
                return rotateLeft(node);
        }
        return node;
    }

    private Node<K> rotateRight(Node<K> node) {
        Node<K> leftNode = node.left;
        node.left = leftNode.right;
        leftNode.right = node;
        return leftNode;
    }

    private Node<K> rotateLeft(Node<K> node) {
        Node<K> rightNode = node.right;
        node.right = rightNode.left;
        rightNode.left = node;
        return rightNode;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        @SuppressWarnings("unchecked")
        K data = (K) o;
        Node<K> node = find(data);
        if (node == null) return false;
        if (node.value.compareTo(data) != 0) return false;
        if (toElement != null && fromElement != null) {
            if (node.value.compareTo(toElement) < 0 &&
                    node.value.compareTo(fromElement) >= 0) {
                subSize--;
            }
        } else if (toElement != null && node.value.compareTo(toElement) < 0 ||
                fromElement != null && node.value.compareTo(fromElement) >= 0) {
            subSize--;
        }
        root = remove(root, data);
        size--;
        return true;
    }

    Node<K> remove(Node<K> node, K data) {
        if (node != null) {
            int compare = data.compareTo(node.value);
            if (compare < 0) {
                node.left = remove(node.left, data);
            } else if (compare > 0) {
                node.right = remove(node.right, data);
            } else {
                if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    node.value = this.leftmost(node.right);
                    node.right = remove(node.right, node.value);
                }
            }
        }
        return node;
    }

    private K leftmost(Node<K> searchNode) {
        Node<K> node = searchNode;
        while (node.left != null) node = node.left;
        return node.value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(@NotNull Collection c) {
        for (Object element : c) {
            if (!add((K) element)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection c) {
        if (this.containsAll(c)) {
            for (Object element : c) {
                remove(element);
            }
            return true;
        } else return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection c) {
        if (this.containsAll(c)) {
            for (Object element : this) {
                if (!c.contains(element)) remove(element);
            }
            return true;
        } else return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection c) {
        for (Object element : c) {
            if (!contains(element)) return false;
        }
        return true;
    }

    @Override
    public void clear() {
        this.root = null;
    }

    public class TreapIterator implements Iterator<K> {

        private Node<K> current;
        private LinkedList<Node<K>> elements;

        private TreapIterator() {
            current = null;
            elements = new LinkedList<>();
            Node<K> node = root;
            while (node != null) {
                elements.add(0, node);
                node = node.left;
            }
        }

        private Node<K> findNext(boolean fromNext) {
            if (current == null) {
                if (fromNext) {
                    return elements.pollFirst();
                }
                if (elements.size() == 0) return null;
                else return elements.getFirst();
            }

            Node<K> result = current;

            if (result.right == null && elements.size() != 0) {
                result = elements.getFirst();

                if (fromNext) {
                    elements.remove(0);
                }
            } else {
                result = result.right;
                if (result != null) {
                    while (result.left != null) {
                        if (fromNext) {
                            elements.add(0, result);
                        }
                        result = result.left;
                    }
                }
            }
            return result;
        }

        @Override
        public boolean hasNext() {
            return findNext(false) != null;
        }

        @Override
        public K next() {
            current = findNext(true);
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }
    }
}
