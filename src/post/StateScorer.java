package post;

import msa.ARPALanguageModel;
import msa.ConfigurableParameters;
import msa.LanguageModel;

public class StateScorer {

	private static StateScorer stateScorer;
	private LanguageModel langModel;

	private StateScorer() {
		if (ConfigurableParameters.isLM)
			langModel = new ARPALanguageModel(ConfigurableParameters.lmFilePath);
	}

	public static StateScorer getInstance() {
		if (stateScorer == null)
			stateScorer = new StateScorer();
		return stateScorer;
	}

	public StateScore scoreState(TokenState state) {
		StateScore score = new StateScore();
		String toScore = state.getToken();
		TokenState thisState = state;
		if (toScore == null)
			score.setScore(thisState.getPrevState().getStateScore().getScore()
					+ TokenState.EMPTYSTATE_PENALTY);
		else {

			if ((state = state.getPrevState()) != null)
				if (state.getToken() != null && !state.getToken().equals(""))
					toScore = state.getToken() + " " + toScore;
			if ((state = state.getPrevState()) != null)
				if (state.getToken() != null && !state.getToken().equals(""))
					toScore = state.getToken() + " " + toScore;

			double localScore = langModel.getGoodTuringLMProbability(toScore);
			double aggScore = localScore
					+ thisState.getPrevState().getStateScore().getScore();
			score.setScore(aggScore);
		}
		thisState.setStateScore(score);
		return score;
	}
}
