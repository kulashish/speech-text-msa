package post;

import java.util.ArrayList;
import java.util.List;

public class ColumnAlignment {

	private List<String> tokensList;

	public ColumnAlignment() {
		tokensList = new ArrayList<String>();
	}

	public List<String> getTokensList() {
		return tokensList;
	}

	public void setTokensList(List<String> tokensList) {
		this.tokensList = tokensList;
	}

	public String get(int stateIndex) {
		return tokensList.get(stateIndex);
	}

	public void addToken(String token) {
		String toAdd = token.equals("_") ? "" : token;
		toAdd = toAdd.replaceAll("\\.", "");
		if (!getTokensList().contains(toAdd)) {
			System.out.println("Adding token : " + token);
			getTokensList().add(toAdd);
		}
	}

}
