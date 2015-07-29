package msa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ARPALanguageModel extends LanguageModel {

	private static final String LB_REGEX = "(-[0-9\\.]+) (.*) (-[0-9\\.]+)";
	private static final String L_REGEX = "(-[0-9\\.]+) (.*)";

	private static Pattern lbPattern = Pattern.compile(LB_REGEX);
	private static Pattern lPattern = Pattern.compile(L_REGEX);

	public ARPALanguageModel(String filePath) {
		super(filePath);
	}

	protected void readLanguageModelFromFile(String filepath) {
		BufferedReader br = null;
		int linecount = 0;
		Matcher lbMatcher = null;
		Matcher lMatcher = null;
		// open and read the file
		try {
			br = new BufferedReader(new FileReader(filepath));
			StringBuilder sb = new StringBuilder();
			String line = null;

			// Added the matches condition
			while ((line = br.readLine()) != null) {
				linecount++;
				lbMatcher = lbPattern.matcher(line);
				lMatcher = lPattern.matcher(line);
				if (lbMatcher.matches()) {
					this.logprob_table.put(lbMatcher.group(2).trim(),
							Double.parseDouble(lbMatcher.group(1)));
					this.backoff_table.put(lbMatcher.group(2).trim(),
							Double.parseDouble(lbMatcher.group(3)));
				} else if (lMatcher.matches()) {
					this.logprob_table.put(lMatcher.group(2).trim(),
							Double.parseDouble(lMatcher.group(1)));
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

}
