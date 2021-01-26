package common;

import java.io.Serializable;
import java.util.ArrayList;

public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String corpus;
	private String name;

	public String getTitle() {
		return title;
	}

	public String getCorpus() {
		return corpus;
	}

	public News(String title, String corpus, String name) {
		this.title = title;
		this.corpus = corpus;
		this.name = name;
	}

	public SearchResult search(String s) {

		ArrayList<Integer> index = new ArrayList<Integer>();
		int indexAux = 0;
		int matches = 0;

		String[] vtitle = title.split(" |,|;|\\.|\\?|!|-|:");
		for (String aux : vtitle) {
			indexAux++;
			if (aux.matches(s)) {
				matches++;
				index.add(indexAux);
			}
		}

		String[] vbody = corpus.split(" |,|;|\\.|\\?|!|:");
		for (String aux : vbody) {
			indexAux++;
			if (aux.matches(s)) {
				matches++;
				index.add(indexAux);
			}
		}
		return new SearchResult(name, matches, title, index);
	}
}
