package common;

import java.util.Comparator;

public class NewsComparator implements Comparator<SearchResult>{

	@Override
	public int compare(SearchResult o1, SearchResult o2) {
		return o2.getOccurences() - o1.getOccurences();
	}

	
}
