package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.SwingUtilities;

import common.ConnectionType;
import common.NewsRequest;
import common.SearchRequest;

public class Client {

	ServerHandler sh;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private Gui gui;
	private String ip;

	public Client(String ip) {
		this.ip = ip;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					createAndShowGui();
				}
			});
		} catch (InvocationTargetException | InterruptedException e1) {
		}

		try {
			doConnections();
			sh = new ServerHandler(gui, in, this);
			sh.start();
		} catch (IOException e) {
			System.err.println("Erro ao estabelecer ligação ao servidor, a desligar...");
			gui.close();
		}
	}

	public void shutdownBySH() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	public void shutdown() {
		sh.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	private void doConnections() throws IOException {
		InetAddress adress = InetAddress.getByName(null);
		socket = new Socket(adress, 8080);

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		out.writeObject(ConnectionType.CLIENT);
	}

	private void createAndShowGui() {
		gui = new Gui(this);
		gui.addComponents();
	}

	public void sendSearchRequest(SearchRequest sr) {
		try {
			out.writeObject(sr);
		} catch (IOException e) {
			System.out.println("Erro ao enviar o pedido de procura, tente de novo.");
		}
	}

	public void sendNewsRequest(NewsRequest nr) {
		try {
			out.writeObject(nr);
		} catch (IOException e) {
			System.out.println("Erro ao enviar o pedido de noticia, tente de novo.");
		}
	}

}
