package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import common.CompletedTask;
import common.ConnectionType;
import common.News;
import common.SearchResult;
import common.Task;

public class WorkerThread {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private String ip;
	private boolean connected = false;

	public WorkerThread(String ip) {
		this.ip = ip;
	}

	public void start() {
		while (true) {
			if (connected) {
				try {
					Task task = (Task) in.readObject();
					News n = task.getNews();
					SearchResult sr = n.search(task.getKeyword());
					out.writeObject(new CompletedTask(task.getId(), sr));
				} catch (ClassNotFoundException | IOException e) {
					connected = false;
					try {
						socket.close();
					} catch (IOException e1) {
					}
				}
			} else {
				try {
					doConnections();
					connected = true;
					System.out.println("Ligação efetuada com sucesso!");
				} catch (IOException e) {
					System.err.println("Erro ao estabelecer ligação ao servidor, a tentar de novo...");
				}
			}
		}
	}

	public void doConnections() throws IOException {
		try {
			InetAddress adress = InetAddress.getByName(null);
			socket = new Socket(adress, 8080);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.writeObject(ConnectionType.WORKER);
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException | NullPointerException e1) {
			}
			throw new IOException();
		}
	}

}
