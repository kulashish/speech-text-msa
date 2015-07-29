package post;

public class TokenState {

	public static final double EMPTYSTATE_PENALTY = -2.0d;
	private String token;
	private StateScore stateScore;
	private TokenState[] nextStates;
	private TokenState prevState;
	private String aggTokens;

	public TokenState(String t) {
		token = t;
		stateScore = new StateScore(0.0d);
		nextStates = null;
		prevState = null;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAggTokens() {
		return aggTokens;
	}

	public StateScore getStateScore() {
		return stateScore;
	}

	public TokenState[] getNextStates() {
		return nextStates;
	}

	public void setNextStates(TokenState[] nextStates) {
		this.nextStates = nextStates;
	}

	public TokenState getPrevState() {
		return prevState;
	}

	public void setPrevState(TokenState prevState) {
		this.prevState = prevState;
	}

	public void setStateScore(StateScore stateScore) {
		this.stateScore = stateScore;
	}

	public void createNextStates(ColumnAlignment tokenList) {
		// System.out.println("Creating next states for " + this.token);
		nextStates = new TokenState[tokenList.getTokensList().size()];
		// nextStates[0] = new TokenState(null);
		// nextStates[0].setPrevState(this);
		// StateScorer.getInstance().scoreState(nextStates[0]);
		// nextStates[0].aggTokens = this.aggTokens;
		// System.out.println("Created next state " + nextStates[0].token +
		// " : "
		// + nextStates[0].aggTokens);
		for (int stateIndex = 0; stateIndex < nextStates.length; stateIndex++) {
			nextStates[stateIndex] = new TokenState(tokenList.get(stateIndex));
			nextStates[stateIndex].aggTokens = this.aggTokens != null ? this.aggTokens
					: "";
			if (nextStates[stateIndex].token != null
					&& !nextStates[stateIndex].token.equals(""))
				nextStates[stateIndex].aggTokens += " "
						+ nextStates[stateIndex].token;
			nextStates[stateIndex].aggTokens.trim();
			nextStates[stateIndex].setPrevState(this);
			StateScorer.getInstance().scoreState(nextStates[stateIndex]);
			// System.out.println("Created next state "
			// + nextStates[stateIndex].token + " : "
			// + nextStates[stateIndex].aggTokens);
		}
	}

}
