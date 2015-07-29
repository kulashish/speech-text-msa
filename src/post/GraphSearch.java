package post;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class GraphSearch {

	private ColumnAlignment[] alignColumnsList;
	private TokenState startState;
	private TokenState endState;

	public GraphSearch(ColumnAlignment[] columns) {
		alignColumnsList = columns;
		startState = new TokenState(null);
		endState = new TokenState(null);
	}

	public void buildGraph() {
		TokenState currentState = startState;
		Queue<TokenState> statesQ = new LinkedList<TokenState>();
		Queue<TokenState> nextStatesQ = null;
		statesQ.add(startState);

		for (ColumnAlignment tokenList : alignColumnsList) {
			nextStatesQ = new LinkedList<TokenState>();
			while ((currentState = statesQ.poll()) != null) {
				currentState.createNextStates(tokenList);
				nextStatesQ.addAll(Arrays.asList(currentState.getNextStates()));
			}
			statesQ = nextStatesQ;
		}

		while ((currentState = statesQ.poll()) != null) {
			System.out.println(currentState.getAggTokens() + " : "
					+ currentState.getStateScore().getScore());
		}
	}

	public static void main(String args[]) {
		String file = "D:\\Personal\\PhD\\Speech to text\\MSA-Scribe-master\\align_out\\msa8.out";
		ColumnAlignment[] cAlignments = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			String[] tokens = line.split("\t\t");
			cAlignments = new ColumnAlignment[tokens.length];
			for (int i = 0; i < cAlignments.length; i++) {
				cAlignments[i] = new ColumnAlignment();
				cAlignments[i].addToken(tokens[i]);
			}
			while ((line = reader.readLine()) != null) {
				tokens = line.split("\t\t");
				for (int i = 0; i < cAlignments.length; i++)
					cAlignments[i].addToken(tokens[i]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GraphSearch g = new GraphSearch(cAlignments);
		g.buildGraph();
	}
}
