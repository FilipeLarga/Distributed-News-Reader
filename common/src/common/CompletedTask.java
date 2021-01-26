package common;

import java.io.Serializable;

public class CompletedTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SearchResult sr;
	private int id;

	public CompletedTask(int id, SearchResult sr) {
		this.id = id;
		this.sr = sr;
	}

	public int getId() {
		return id;
	}

	public SearchResult getSr() {
		return sr;
	}

}
