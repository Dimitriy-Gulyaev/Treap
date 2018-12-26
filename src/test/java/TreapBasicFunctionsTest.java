import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class TreapBasicFunctionsTest {

    // Добавлям и удаляем элементы, проверяем факт добавки/удаления, сохранения упорядоченности дерева, приоритетности
    // узлов и т.п.
    @Test
    void testAddAndRemove() {
        Treap<Integer> treap = new Treap<>();

        // Тестируем добавление
        treap.add(10);
        treap.add(5);
        treap.add(7);
        treap.add(10);
        assertEquals(3, treap.size());
        assertTrue(treap.contains(5));
        treap.add(3);
        treap.add(1);
        treap.add(3);
        treap.add(4);
        assertEquals(6, treap.size());
        assertFalse(treap.contains(8));
        treap.add(8);
        treap.add(15);
        treap.add(15);
        treap.add(20);
        assertEquals(9, treap.size());
        assertTrue(treap.contains(8));
        assertFalse(treap.add(null));
        assertTrue(treap.checkInvariant());
        assertTrue(treap.checkPriorityOrder());
        assertEquals((Integer) 1, treap.first());
        assertEquals((Integer) 20, treap.last());

        // Тестируем удаление
        assertFalse(treap.remove(37));
        assertFalse(treap.remove(null));
        assertTrue(treap.remove(15));
        assertFalse(treap.contains(15));
        assertTrue(treap.checkInvariant());
        assertTrue(treap.checkPriorityOrder());
    }

    // Проверяем, работает ли наше дерево со сторонним компаратором. Создаём класс Dog, реализующий Comparable,
    // создаём для него компаратор, передаём его в конструктор нашего дерева, проводим всяческие проверки
    @Test
    void testCustomComparator() {
        class Dog implements Comparable<Dog> {
            int size;
            int age;
            String name;

            Dog(int size, int age, String name) {
                this.size = size;
                this.age = age;
                this.name = name;
            }

            @Override
            public int compareTo(@NotNull Dog o) {
                if (this.size == o.size) return 0;
                else return this.size > o.size ? 1 : -1;
            }
        }
        Comparator<Dog> comparator = Dog::compareTo;
        Treap<Dog> dogTreap = new Treap<>(comparator);
        assertNotNull(dogTreap.comparator());
        assertSame(dogTreap.comparator(), comparator);
        Dog bigDog = new Dog(3, 5, "Abraham");
        Dog averageDog = new Dog(2, 10, "Bob");
        Dog smallDog = new Dog(1, 15, "Cindy");
        dogTreap.add(bigDog);
        dogTreap.add(averageDog);
        dogTreap.add(smallDog);
        assertEquals(smallDog, dogTreap.first());
        assertEquals(bigDog, dogTreap.last());
        assertArrayEquals(new Dog[]{smallDog, averageDog, bigDog}, dogTreap.toArray());
    }

    // Более обильный тест операции удаления
    @Test
    void testRemove() {

        // 100 раз генерируем список 20-и случайных элементов
        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 1; j <= 20; j++) {
                list.add(random.nextInt(100));
            }

            // Создаём по объекту классов Treap и TreeSet, заполняем их содержимым текущего листа
            SortedSet<Integer> treeSet = new TreeSet<>();
            Treap<Integer> treap = new Treap<>();
            for (Integer element : list) {
                treeSet.add(element);
                treap.add(element);
            }

            // Удаляем элемент листа со случайным индексом и из объекта TreeSet, и из объяекта Treap.
            Integer toRemove = list.get(random.nextInt(list.size()));
            treeSet.remove(toRemove);
            treap.remove(toRemove);

            // Должны получить одинаковый результат, проверяем это.
            System.out.println("Removing " + toRemove.toString() + " from " + list.toString());
            assertEquals(treeSet.size(), treap.size());
            for (Integer element : list) {
                boolean inn = !element.equals(toRemove);
                assertEquals(inn, treap.contains(element),
                        inn ? element.toString() + "should be in treap" :
                                element.toString() + " shouldn't be in treap");
            }
            assertTrue(treap.checkInvariant());
            assertTrue(treap.checkPriorityOrder());
        }
    }

    // Тестируем итератор
    @Test
    void testIterator() {

        // 100 раз генерируем список 20-и случайных элементов
        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 1; j <= 20; j++) {
                list.add(random.nextInt(100));
            }

            // Создаём по объекту классов Treap и TreeSet, заполняем их содержимым текущего листа
            SortedSet<Integer> treeSet = new TreeSet<>();
            Treap<Integer> treap = new Treap<>();
            for (Integer element : list) {
                treeSet.add(element);
                treap.add(element);
            }

            // Создаём по итератору от каждого класса, проходимся ими обоими по соотв. деревьям и сравниваем результат
            Iterator<Integer> treeIt = treeSet.iterator();
            Iterator<Integer> treapIt = treap.iterator();
            System.out.println("Traversing" + list.toString());
            while (treeIt.hasNext()) {
                assertEquals(treeIt.next(), treapIt.next());
            }
        }
    }
}
