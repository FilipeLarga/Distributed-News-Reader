package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.News;
import common.NewsComparator;
import common.NewsRequest;
import common.ResultBarrier;
import common.SearchRequest;
import common.SearchResult;

public class ClientHandler extends Thread {

	private Server server;
	private Socket socket;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	private SearchRequest sr;
	private ResultBarrier<SearchResult> barrier;
	private ArrayList<SearchResult> toSend = new ArrayList<SearchResult>();
	private int id;

	public ClientHandler(Server server, Socket socket, ObjectInputStream in, ObjectOutputStream out, int id,
			int limit) {
		this.server = server;
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.id = id;
		barrier = new ResultBarrier<>(limit);
	}

	@Override
	public void run() {
		try {
			while (true) {
				handleRequest();
				resetStream();
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Ligação Quebrada");
			server.removeCH(id);
			server.cancelTasks(id);
			try {
				socket.close();
			} catch (IOException e1) {
			}
			System.out.println("Socket Desligado");
		}
	}

	private void resetStream() {
		try {
			out.reset();
		} catch (IOException e) {
		}
	}

	public void handleRequest() throws ClassNotFoundException, IOException {
		Object aux = in.readObject();

		if (aux instanceof SearchRequest) {
			sr = (SearchRequest) aux;
			server.addTasks(sr, id);
			ArrayList<SearchResult> results = new ArrayList<SearchResult>();
			try {
				results.addAll(barrier.take());
				barrier.clear();
				for (SearchResult result : results) {
					if (result.getOccurences() > 0) {
						toSend.add(result);
					}
				}
				Collections.sort(toSend, new NewsComparator());
				sendResults(toSend);
				toSend.clear();
			} catch (InterruptedException e) {
			}

		} else {
			NewsRequest nr = (NewsRequest) aux;
			News n = server.getNews(nr.getName());
			sendNews(n);
		}
	}

	public void sendNews(News news) throws IOException {
		out.writeObject(news);
	}

	public void sendResults(ArrayList<SearchResult> results) throws IOException {
		out.writeObject(results);
	}

	public void addResult(SearchResult sr) {
		barrier.add(sr);
	}

	public void sendCount(int workers) {
		try {
			out.writeObject(workers);
		} catch (IOException e) {
		}

	}

}
