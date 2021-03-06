import com.sun.istack.internal.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Treap<K extends Comparable<K>> implements SortedSet<K> {

    private final Comparator<K> comparator;

    private static final Random rand = new Random();
    private Node<K> root = null;
    private int size;
    int subSize = 0; // размер подмножества

    // Границы подмножества
    private K fromElement;
    private K toElement;

    public Treap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public Treap() {
        this.comparator = Comparable::compareTo;
    }

    static class Node<K> {
        Node<K> right, left;

        // Приоритет узла, в данном случае узлы располагаются по принципу максимальной двоичной кучи.
        // У корня самый высокий приоритет
        final double priority;
        private K value;

        Node(K value) {
            this.value = value;
            this.priority = rand.nextDouble();
        }

    }

    // Класс подмножества дерева
    class SubTree extends Treap<K> {

        // При создании нового множества обнуляем его размер и пересчитываем заново методом countSubSize()
        SubTree() {
            Treap.this.subSize = 0;
            countSubSize();
        }

        // Переопределяем методы класса Treap, переиспользуем их с учетом границ множества
        @Override
        public boolean add(K data) {
            Node<K> temp = find(data);
            if (temp != null) {
                Comparator<K> comparator = this.comparator();
                assert comparator != null;
                if (comparator.compare(temp.value, data) == 0) return false;
                if (fromElement != null && comparator.compare(data, fromElement) < 0) return false;
                if (toElement != null && comparator.compare(data, toElement) >= 0) return false;
            }
            root = add(root, data, Treap.this.comparator());
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
            root = remove(root, data, Treap.this.comparator());
            size--;
            Treap.this.subSize--;
            return true;
        }

        @Override
        public int size() {
            return Treap.this.subSize;
        }

        // Если элемент присутствует в дереве и входит в границы подмножества, возвращаем true, иначе false
        @Override
        public boolean contains(Object o) {
            @SuppressWarnings("unchecked")
            K t = (K) o;
            Node<K> closest = find(t);
            Comparator<K> comparator = this.comparator();
            assert comparator != null;
            return closest != null && comparator.compare(t, closest.value) == 0 &&
                    (fromElement == null || comparator.compare(t, fromElement) >= 0) &&
                    (toElement == null || comparator.compare(t, toElement) < 0);
        }

    }

    // Подсчет размера подмножества. Алгоритм прост: выбираем нужный случай существования границ и проходимся по всем
    // элементам дерева, сверяя их с границами и увеличивая размер подмножества, если элемент в них находится.
    private void countSubSize() {
        Comparator<K> comparator = this.comparator();
        assert comparator != null;
        if (fromElement == null && toElement != null) {
            for (K k : Treap.this) {
                if (comparator.compare(k, toElement) < 0) subSize++;
            }
        }
        if (fromElement != null && toElement == null) {
            for (K k : Treap.this) {
                if (comparator.compare(k, fromElement) >= 0) subSize++;
            }
        }
        if (fromElement != null && toElement != null) {
            for (K k : Treap.this) {
                if (comparator.compare(k, toElement) < 0 && comparator.compare(k, fromElement) >= 0) subSize++;
            }
        }
    }

    // Обычный  рекурсивный алгоритм поиска (как в обычном двоичном дереве)
    private Node<K> find(K value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<K> find(Node<K> start, K value) {
        int comparison = this.comparator() == null ? value.compareTo(start.value) :
                this.comparator().compare(value, start.value);
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

    // Проверка на естественную упорядоченность дерева. Алгоритм - по рекурсии спускаемся в низ левой ветки, оттуда
    // переходим к следующему по возрастанию элементу, сравниваем.
    boolean checkInvariant() {
        Comparator<K> comparator = this.comparator();
        assert comparator != null;
        return root == null || checkInvariant(root, comparator);
    }

    private boolean checkInvariant(Node<K> node, Comparator<K> comparator) {
        Node<K> left = node.left;
        if (left != null && (comparator.compare(left.value, node.value) >= 0 || !checkInvariant(left, comparator)))
            return false;
        Node<K> right = node.right;
        return right == null || comparator.compare(right.value, node.value) > 0 && checkInvariant(right, comparator);
    }

    // Проверка соответствия дерева свойствам максимальной двоичной кучи. Алгоритм практически идентичный предыдущему,
    // но с другой проверкой.
    boolean checkPriorityOrder() {
        return root == null || checkPriorityOrder(root);
    }

    private boolean checkPriorityOrder(Node<K> node) {
        Node<K> left = node.left;
        if (left != null && (left.priority > node.priority || !checkPriorityOrder(left))) return false;
        Node<K> right = node.right;
        return right == null || right.priority < node.priority && checkPriorityOrder(right);
    }

    // Возвращаем компаратор этого дерева, если таковой был передан ему в конструктор при создании. В противном случае
    // возвращаем компаратор, осуществляющий естественный порядок элементов.
    @Nullable
    @Override
    public Comparator<K> comparator() {
        return this.comparator != null ? this.comparator : Comparable::compareTo;
    }


    // Создание подмножеств. Данные методы устанвливают границы множеств и возвращают новый объект класса SubSet
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
        Comparator<K> comparator = this.comparator();
        assert comparator != null;
        return closest != null && comparator.compare(t, closest.value) == 0;
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

    // Если пользователь пытается добавить null или уже находящийся в девере элемент, возвращаемся false и
    // ничего не делаем. В противном случае добавляем элемент, увлечиваем размер дерева, возвращаем true.
    @Override
    public boolean add(K data) {
        Comparator<K> comparator = this.comparator();
        assert comparator != null;
        if (data == null) return false;
        Node<K> temp = find(data);
        if (temp != null) {
            if (comparator.compare(temp.value, data) == 0) return false;
        }
        root = add(root, data, comparator);
        size++;
        return true;
    }

    /**
     * Рекусрвиный алгоритм добавления. Доходим рекурсией до места, куда нужно добавить элемент, добавляем,
     * далее поднимаемся вверх по сетку вызовов, на каждом шаге при необходимости вращая дерево
     * (если нарушены приоритеты узлов) и переприсваивая результат вращения соответствующему потомку предыдущего узла,
     * с которого вызывался метод, таким образом доходя до корня. В худшем случае перестаивается всё дерево.
     */
    Node<K> add(Node<K> node, K data, Comparator<K> comparator) {
        if (node == null) {
            Node<K> newNode = new Node<>(data);

            // Проверяем, добавлять ли элемент в подмножетво
            // (увеличиваем размер множества, если элемент находится в границах)
            if (toElement != null && fromElement != null) {
                if (comparator.compare(newNode.value, toElement) < 0 &&
                        comparator.compare(newNode.value, fromElement) >= 0) {
                    subSize++;
                }
            } else if (toElement != null && comparator.compare(newNode.value, toElement) < 0
                    || fromElement != null && comparator.compare(newNode.value, fromElement) >= 0) {
                subSize++;
            }

            return newNode;
        }

        int compare = comparator.compare(data, node.value);
        if (compare < 0) {
            node.left = add(node.left, data, comparator);
            if (node.priority < node.left.priority)
                return rotateRight(node);
        } else if (compare > 0) {
            node.right = add(node.right, data, comparator);
            if (node.priority < node.right.priority)
                return rotateLeft(node);
        }
        return node;
    }

    // Методы правого и левого поворотов соответственно (как и в АВЛ-дереве). Ось вращения - узел, подаваемый на
    // вход метода, он же node
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

    // Если пользователь пытается удалить null или несуществующий в дереве объект, или если дерево пустое - возвращаем
    // false и ничего не делаем. В противном случае безопасно удаляем нужный элемент, уменьшаем границы дерева
    // (и множества, если элемент туда входил) и возвращаем true.
    @Override
    public boolean remove(Object o) {
        Comparator<K> comparator = this.comparator();
        assert comparator != null;
        if (o == null) return false;
        @SuppressWarnings("unchecked")
        K data = (K) o;
        Node<K> node = find(data);
        if (node == null) return false;
        if (comparator.compare(node.value, data) != 0) return false;

        // Проверка на вхождение элемента в границы множества
        if (toElement != null && fromElement != null) {
            if (comparator.compare(node.value, toElement) < 0 &&
                    comparator.compare(node.value, fromElement) >= 0) {
                subSize--;
            }
        } else if (toElement != null && comparator.compare(node.value, toElement) < 0 ||
                fromElement != null && comparator.compare(node.value, fromElement) >= 0) {
            subSize--;
        }

        root = remove(root, data, comparator);
        size--;
        return true;
    }

    // Рекурсивный алгоритм удаления. Опускаемся вниз до удаляемого элемента, удаляем, сохраняя связи в дереве,
    // поднимаемся обратно наверх.
    Node<K> remove(Node<K> node, K data, Comparator<K> comparator) {
        if (node != null) {
            int compare = comparator.compare(data, node.value);
            if (compare < 0) {
                node.left = remove(node.left, data, comparator);
            } else if (compare > 0) {
                node.right = remove(node.right, data, comparator);
            } else {
                if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    node.value = this.leftmost(node.right);
                    node.right = remove(node.right, node.value, comparator);
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
            while (node != null) { // Заполняем очередь левой веткой дерева. Первый элемент наименьший
                elements.add(0, node);
                node = node.left;
            }
        }

        // fromNext - индикатор, сигнализирующий о том, опрашиваем мы итератор (false) или
        // двигаемся по нему вперёд (true)
        private Node<K> findNext(boolean fromNext) {
            if (current == null) {
                if (fromNext) {
                    return elements.pollFirst();
                }
                if (elements.size() == 0) return null;
                else return elements.getFirst();
            }

            Node<K> result = current;

            // Если у текущего узла нет правого потомка и при этом наша очередь не пуста, записываем в результат
            // первый её элемент (двигаемся вверх по левой ветке)
            if (result.right == null && elements.size() != 0) {
                result = elements.getFirst();

                if (fromNext) {
                    elements.remove(0); // удаляем элемент, если вызыван next() и мы двигаемся по дереву дальше
                }

            } else {

                // Если у текущего узла есть правый потомок, спускаемся вниз по его левой ветви (если она есть)
                // и в зависимости от флага fromNext либо возвращаем самый крайний (false),
                // либо как в конструкторе заполняем нашу очередь (true)
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
