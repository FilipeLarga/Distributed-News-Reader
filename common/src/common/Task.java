package common;

import java.io.Serializable;

public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyword;
	private News news;
	private int id;

	public Task(String keyword, News news, int id) {
		this.keyword = keyword;
		this.news = news;
		this.id = id;
	}

	public News getNews() {
		return news;
	}

	public String getKeyword() {
		return keyword;
	}

	public int getId() {
		return id;
	}

}
