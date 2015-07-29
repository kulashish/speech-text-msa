package msa;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class LMPostProcess {

	LanguageModel lm;
	Alignment alignment;

	public LMPostProcess(LanguageModel model, Alignment a) {
		lm = model;
		alignment = a;
	}

	public String getLMVoting() {
		String combinedTranscription = "";
		List<String> combinedWords = new ArrayList<String>();
		ArrayList<Integer> combinedWorkerIDList = new ArrayList<Integer>();

		int y = 0;
		// define the hashmaps for storing votes, timestamps, and worker ids
		HashMap<String, Double> votes;
		// stores the index of the worker for the word and the smallest
		// timestamp and word
		HashMap<String, Integer> workerIDsHash;

		String keyWord;
		int[] bits;
		int[] currentPos = new int[alignment.nrows];

		// initialize the current positions to zero
		for (int i = 0; i < alignment.nrows; i++)
			currentPos[i] = 0;

		System.out.println("");

		for (int j = 0; j < alignment.ncols; j++) {
			// initialize the hashmaps
			votes = new HashMap<String, Double>();
			workerIDsHash = new HashMap<String, Integer>();

			y = alignment.alignList.get(j);
			bits = alignment.getBm().getBitsFromInteger(y, alignment.nrows);

			for (int i = 0; i < alignment.nrows; i++) {

				String[] wordlisti = Alignment.wordsList.get(i);

				// take votes only for non-gap words. Ignore any gap words
				if (bits[i] != 0) {
					keyWord = wordlisti[currentPos[i]];
					if (!votes.containsKey(keyWord))
						votes.put(keyWord, lm
								.getGoodTuringLMProbability(getPhrase(
										combinedWords, keyWord, 3)));
					currentPos[i]++;
				}
			}

			if (votes.isEmpty()) {
				return null;
			}
			// get the maximum voted string
			String majority_voted_word = getMostLikelyWord(votes);
			System.out.print(majority_voted_word + " ");

			if (majority_voted_word != null && !majority_voted_word.isEmpty()) {
				combinedTranscription += majority_voted_word + " ";
				combinedWords.add(majority_voted_word);
			}

			votes.clear();
			votes = null;
		}

		System.out.println("");

		return combinedTranscription;
	}

	private String getMostLikelyWord(HashMap<String, Double> votes) {
		double p = 0.0d;
		String likelyWord = null;
		Iterator<Entry<String, Double>> iter = votes.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Double> entry = iter.next();
			System.out.println("CHECKING " + entry.getKey() + " : "
					+ entry.getValue());
			if (entry.getValue() > p) {
				p = entry.getValue();
				likelyWord = entry.getKey();
			}
		}
		System.out.println("MOST LIKELY: " + likelyWord + " : " + p);
		return likelyWord;
	}

	private String getPhrase(List<String> combinedWords, String keyWord,
			int ngram) {
		String phrase = "";
		int len = combinedWords.size();
		for (int i = len - ngram + 1; i < len; i++)
			if (i >= 0)
				phrase += combinedWords.get(i) + " ";
		phrase += keyWord;
		System.out.println("Looking up " + phrase);
		return phrase;
	}

}
