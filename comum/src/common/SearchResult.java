package common;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int occurences;
	private String name;
	private String title;
	private ArrayList<Integer> index;

	public SearchResult(String name, int occurences, String title, ArrayList<Integer> index) {
		this.name = name;
		this.occurences = occurences;
		this.title = title;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public int getOccurences() {
		return occurences;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<Integer> getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return occurences + " - " + title;
	}
}
