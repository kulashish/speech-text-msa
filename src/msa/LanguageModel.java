package msa;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 */

/**
 * The class is designed to incorporate different language models in to the MSA
 * system. For the time being, I am writing simple class. I would like to modify
 * it using class inheritance and abstract class architecture for multiple
 * Language Models (Good-Turing, KN, Neural Net, etc.)
 * 
 * @author inaim
 *
 */

public class LanguageModel {
	// need some hashtable to store all the necessary LM probabilities
	protected HashMap<String, Double> logprob_table;
	protected HashMap<String, Double> backoff_table;
	public double LOGZERO = -99.0;
	public int Ngram = 3;

	/*
	 * The Good-Turing LM class constructor
	 */
	public LanguageModel(String filePath) {
		// load the LM using the filePath
		this.logprob_table = new HashMap<String, Double>();
		this.backoff_table = new HashMap<String, Double>();

		// start reading the file
		System.out.println("Loading the language model ...");
		readLanguageModelFromFile(filePath);
		System.out.println("Loaded");
	}

	public double getGoodTuringLMProbability(String p) {
		p = p.toLowerCase().trim();
		String[] parts = p.split(" ");
		if (parts.length == 1)
			return getGoodTuringLMProbabilityUnigram(p);
		else if (parts.length == 2)
			return getGoodTuringLMProbabilityBigram(p);
		else
			return getGoodTuringLMProbabilityTrigram(p);
	}

	/*
	 * get the trigram probability using Good-Turing model
	 */
	public double getGoodTuringLMProbabilityTrigram(String trigram) {
		Double logprob, backoffprob;
		String[] trigramParts;

		logprob = this.logprob_table.get(trigram);

		if (logprob != null) {
			return logprob;
		} else {
			trigramParts = trigram.split(" ");
			// logprob = this.logprob_table.get(trigramParts[0] + " "
			// + trigramParts[1]);
			logprob = getGoodTuringLMProbabilityBigram(trigramParts[1] + " "
					+ trigramParts[2]);
			if (logprob == null)
				logprob = this.LOGZERO;

			if (trigramParts.length >= 3) {
				// backoffprob = this.backoff_table.get(trigramParts[1] + " "
				// + trigramParts[2]);
				backoffprob = this.backoff_table.get(trigramParts[0] + " "
						+ trigramParts[1]);
				if (backoffprob == null)
					backoffprob = Double.valueOf(0);
			} else {
				backoffprob = null;
			}

			// if (logprob == null || backoffprob == null) {
			// return Math.exp(this.LOGZERO);
			// } else {
			// return Math.exp(logprob.doubleValue()
			// + backoffprob.doubleValue());
			// }
		}

		return (logprob == null || backoffprob == null) ? this.LOGZERO
				: logprob + backoffprob;
	}

	/*
	 * get the trigram probability using Good-Turing model
	 */
	public double getGoodTuringLMProbabilityBigram(String bigram) {
		Double logprob, backoffprob;
		String[] bigramParts;

		logprob = this.logprob_table.get(bigram);

		if (logprob != null) {
			return logprob;
		} else {
			bigramParts = bigram.split(" ");
			// logprob = this.logprob_table.get(bigramParts[1]);
			logprob = getGoodTuringLMProbabilityUnigram(bigramParts[1]);
			if (logprob == null)
				logprob = this.LOGZERO;

			if (bigramParts.length >= 2) {
				backoffprob = this.backoff_table.get(bigramParts[0]);
				if (backoffprob == null)
					backoffprob = Double.valueOf(0);
			} else {
				backoffprob = null;
			}

			// if (logprob == null || backoffprob == null) {
			// return Math.exp(this.LOGZERO);
			// } else {
			// return Math.exp(logprob.doubleValue()
			// + backoffprob.doubleValue());
			// }
		}
		return (logprob == null || backoffprob == null) ? this.LOGZERO
				: logprob + backoffprob;

	}

	/*
	 * get the trigram probability using Good-Turing model
	 */
	public double getGoodTuringLMProbabilityUnigram(String unigram) {
		Double logprob;

		logprob = this.logprob_table.get(unigram);

		// if (logprob != null) {
		// return Math.exp(logprob.doubleValue());
		// } else {
		// return Math.exp(this.LOGZERO);
		// }
		return logprob != null ? logprob : this.LOGZERO;

	}

	/*
	 * 
	 */
	protected void readLanguageModelFromFile(String filepath) {
		BufferedReader br = null;
		String sequenceString = "";

		int linecount = 0;
		// open and read the file
		try {
			br = new BufferedReader(new FileReader(filepath));

			StringBuilder sb = new StringBuilder();
			String line = null;

			// Added the matches condition
			while ((line = br.readLine()) != null) {
				if (!line.matches("-[0-9\\.]+ .+"))
					continue;
				linecount++;

				// Split by space instead of tab
				String[] parts = line.split(" ");
				System.out
						.println(parts[0] + ", " + parts[1] + ", " + parts[2]);

				if (parts.length < 2) {
					// line = br.readLine();
					continue;
				} else {
					// insert entries to the logprob and backoff table
					// line = br.readLine();
					this.logprob_table.put(parts[1],
							Double.parseDouble(parts[0]));

					if (parts.length > 2) {
						this.backoff_table.put(parts[1],
								Double.parseDouble(parts[2]));
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// close the file
		try {
			if (br != null) {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Lines read :" + linecount);
		System.out.println("Logprob table size : " + this.logprob_table.size());
		System.out.println("Backoff table size: " + this.backoff_table.size());
	}

	public static void main(String[] args) {
		LanguageModel lm = new ARPALanguageModel(
				"D:\\Personal\\PhD\\Speech to text\\language model\\cmusphinx-5.0-en-us.lm\\en-us.lm");

		// String temp = "-0.9758 zyuganov who came ";
		// System.out.println(temp.matches("-[0-9\\.]+ .+"));
		Console console = System.console();
		while (true) {
			String text = console.readLine("Please enter : ");
			if (text.equalsIgnoreCase("exit"))
				break;
			System.out.println(lm.getGoodTuringLMProbability(text));
		}
		// System.out.println(lm
		// .getGoodTuringLMProbability("some its interpretation"));

	}
}
