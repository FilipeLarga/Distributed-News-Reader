package common;

import java.util.ArrayList;

public class ResultBarrier<T> {

	private int limite;
	private int current;
	private ArrayList<T> list = new ArrayList<T>();

	public ResultBarrier(int limite) {
		this.limite = limite;
	}

	public synchronized void add(T element) {
		current++;
		list.add(element);
		if (current == limite)
			notifyAll();
	}

	public synchronized ArrayList<T> take() throws InterruptedException {
		while (current < limite) {
			wait();
		}
		return list;
	}

	public synchronized void clear() {
		list.clear();
		current = 0;
	}

}
