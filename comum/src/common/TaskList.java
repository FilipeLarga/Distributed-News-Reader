package common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskList<T> {

	private List<T> list = new ArrayList<T>();

	public synchronized void add(List<T> elements) {
		list.addAll(elements);
		notifyAll();
	}
	
	public synchronized void add(T element) {
		list.add(element);
		notifyAll();
	}

	public synchronized T take() throws InterruptedException {
		while (list.isEmpty()) {
			wait();
		}
		notifyAll();
		return list.remove(0);
	}

	public synchronized void cancelTasks(int id) {
		Iterator<T> it = list.iterator();
		while (it.hasNext()) {

			T element = it.next();
			if (((Task) element).getId() == id)
				it.remove();
		}
	}

	public int getSize() {
		return list.size();
	}

}
