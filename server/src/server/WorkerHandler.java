package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.CompletedTask;
import common.SearchResult;
import common.Task;

public class WorkerHandler extends Thread {

	private Server server;
	private Socket socket;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	Task task;

	public WorkerHandler(Server server, Socket socket, ObjectInputStream in, ObjectOutputStream out) {
		this.server = server;
		this.socket = socket;
		this.in = in;
		this.out = out;
	}

	@Override
	public void run() {
		try {
			while (true) {
				task = server.getTask();
				out.writeObject(task);
				CompletedTask ct = (CompletedTask) in.readObject();
				server.addResult(ct);
			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			server.returnTask(task);
			try {
				socket.close();
			} catch (IOException e1) {
			}
		}

	}

}
