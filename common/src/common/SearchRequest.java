package common;

import java.io.Serializable;

public class SearchRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyword;

	public SearchRequest(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}
}
