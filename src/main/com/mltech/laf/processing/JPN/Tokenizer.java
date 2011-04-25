package com.mltech.laf.processing.JPN;


import org.apache.commons.lang.StringUtils;

import com.mltech.laf.annotations.Annotation;
import com.mltech.laf.annotations.FeatureSet;
import com.mltech.laf.document.Document;
import com.mltech.utils.Command;
import com.mltech.utils.CommandException;

public class Tokenizer extends com.mltech.laf.processing.Tokenizer {
	private static String WIDE_ASCII = "！＂＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝";

	private int addTokenAnnotation(Document document, int start, int length,
			int offset, String token, String kind, int kindCode) {
		FeatureSet fs = new FeatureSet();
		fs.put("string", token);
		fs.put("kind", kind);
		fs.put("kindcode", String.valueOf(kindCode)); // debug purposes
		fs.put("length", String.valueOf(length));
		if (offset > -1)
			document.annotations("token")
					.add(
							new Annotation(start + offset, start + length
									+ offset, fs));
		start += length;
		return start;
	}

	@Override
	protected void parse(Document document) {
		String remainingText = document.text();

		if (!document.text().equals("")) {
			Command command;
			String result = "";
			try {
				command = new Command("chasen", "-F", "%m;%M;%h\n");
				result = command.exec(document.text().replace("\n", ""), "EOS");
			} catch (CommandException e1) {
				e1.printStackTrace();
			}

			String token;
			String kind;
			String tmp[];
			int start = 0;
			int offset = 0;
			String wideASCIIToken = "";
			boolean wideASCIITokenFinished = true;

			for (String line : result.split("\n")) {
				tmp = line.split(";");
				if (!line.equals("EOS")) {
					token = tmp[0];
					kind = tmp[2];
					int kindCode = Integer.parseInt(kind);

					if (kindCode == 19)
						kind = "number";
					else if (kindCode >= 27 && kindCode <= 36 || kindCode == 49
							|| kindCode == 53)
						kind = "suffix";
					else if (kindCode >= 41 && kindCode <= 45)
						kind = "prefix";
					else if (kindCode == 76 || kindCode == 77)
						kind = "sign";
					else if (kindCode == 78 || kindCode == 79 || kindCode == 82
							|| kindCode == 83 || kindCode == 85)
						kind = "punctuation";
					// else if (kindCode == 80) kind = "space"; // not used
					else if (kindCode == 81)
						kind = "romaji";
					else if (kindCode == 84 || kindCode >= 86 && kindCode <= 88)
						kind = "other";
					else
						kind = "word";

					int length = token.length();

					offset = remainingText.indexOf(token);

					remainingText = remainingText.substring(offset + length);

					// token.matches("[！-｝]")
					boolean containsWideASCII = StringUtils.containsAny(token,
							WIDE_ASCII);
					wideASCIITokenFinished = wideASCIIToken.length() > 0
							&& offset > 0 || !containsWideASCII ? true : false;

					if (wideASCIITokenFinished && wideASCIIToken.length() > 0) {
						start = addTokenAnnotation(document, start,
								wideASCIIToken.length(), 0, wideASCIIToken,
								"romaji", 81);
						wideASCIIToken = "";
					}

					if (wideASCIITokenFinished && !containsWideASCII) {
						start = addTokenAnnotation(document, start, length,
								offset, token, kind, kindCode);
					}

					if (containsWideASCII)
						wideASCIIToken += token;

					start += offset;
				}
			}
		}
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}
}
