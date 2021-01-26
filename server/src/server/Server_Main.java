package server;

import java.util.ArrayList;

public class Server_Main {

	public static void main(String[] args) {

		Server server = new Server(args[0]);
		server.start();
	}

}
