package worker;

public class WorkerMain {

	public static void main(String[] args) {
		WorkerThread workerThread = new WorkerThread(args[0]);
		workerThread.start();
	}

}
