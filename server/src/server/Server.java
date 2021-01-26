package server;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import common.TaskList;
import common.CompletedTask;
import common.ConnectionType;
import common.News;
import common.SearchRequest;
import common.Task;

public class Server {

	public final int PORTO = 8080;
	private ServerSocket serversocket;

	private String path;
	private Map<String, News> newsmap = new HashMap<String, News>();

	private TaskList<Task> taskList = new TaskList<Task>();
	private HashMap<Integer, ClientHandler> chList = new HashMap<>();

	private int newsAmmount;
	private int id;
	private int workers;

	public Server(String path) {
		this.path = path;
		lerFicheiros();
	}

	public void start() {
		try {
			serversocket = new ServerSocket(PORTO);
			System.out.println("Servidor Ligado!");
			while (true) {
				try {
					Socket socket = serversocket.accept();
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					ConnectionType aux = (ConnectionType) in.readObject();
					if (aux == ConnectionType.CLIENT) {
						System.out.println("Conexão aceite: Client" + socket);
						ClientHandler ch = new ClientHandler(this, socket, in, out, id, newsAmmount);
						chList.put(id, ch);
						ch.start();
						id++;
					} else {
						System.out.println("Conexão aceite: Worker" + socket);
						WorkerHandler wh = new WorkerHandler(this, socket, in, out);
						wh.start();
					}
				} catch (IOException e) {
					System.err.println("Erro ao estabelecer a ligação com o cliente");
				} catch (ClassNotFoundException e) {
				}
			}
		} catch (IOException e) {
			System.err.println("Erro ao iniciar o servidor");
		} finally {
			try {
				serversocket.close();
			} catch (IOException e) {
			}
		}
	}

	private void lerFicheiros() {
		File[] files = new File(path).listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(".txt");
			}
		});

		for (File f : files) {
			try {
				Scanner scan = new Scanner(f, "UTF-8");
				String title = "";
				String corpus = "";

				title = scan.nextLine();
				corpus = scan.nextLine();

				newsmap.put(f.getName(), new News(title, corpus, f.getName()));
				scan.close();
				newsAmmount++;
			} catch (FileNotFoundException e) {
				System.out.println("Erro ao carregar o ficheiro " + f.getName());
			}
		}

	}

	public void addTasks(SearchRequest sr, int id) {
		ArrayList<Task> aux = new ArrayList<Task>();
		for (News news : newsmap.values()) {
			aux.add(new Task(sr.getKeyword(), news, id));
		}
		taskList.add(aux);
	}

	public void returnTask(Task t) {
		taskList.add(t);
	}

	public Task getTask() throws InterruptedException {
		return taskList.take();
	}

	public News getNews(String name) {
		return newsmap.get(name);
	}

	public void removeCH(int id) {
		chList.remove(id);
	}

	public void addResult(CompletedTask completedTask) {
		ClientHandler aux = chList.get(completedTask.getId());
		aux.addResult(completedTask.getSr());
	}

	public void cancelTasks(int id) {
		taskList.cancelTasks(id);
	}

	public void removeWH() {
		workers--;
	}

}
