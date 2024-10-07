import java.util.ArrayList;

public class Queue<T> {
    private ArrayList<T> queue;

    public Queue() {
        queue = new ArrayList<>();
    }

    public void enqueue(T value) {
        queue.add(value);
    }

    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Fila Vazia!");
            return null;
        }
        return queue.remove(0);
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return queue.get(0);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}
