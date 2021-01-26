package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import common.News;
import common.SearchResult;

public class ServerHandler extends Thread {

	private Gui gui;
	private ObjectInputStream in;
	private Client client;

	public ServerHandler(Gui gui, ObjectInputStream in, Client client) {
		this.gui = gui;
		this.in = in;
		this.client = client;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			while (!isInterrupted()) {
				Object aux = in.readObject();
				if (aux instanceof News)
					gui.updateText((News) aux);
				else
					gui.updateList((List<SearchResult>) aux);
			}
		} catch (ClassNotFoundException | IOException | NullPointerException e) {
			client.shutdownBySH();
		}
	}

}
