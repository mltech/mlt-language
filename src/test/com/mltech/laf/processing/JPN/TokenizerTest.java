package com.mltech.laf.processing.JPN;

import com.mltech.laf.annotations.Annotation;
import com.mltech.laf.document.Document;
import com.mltech.laf.processing.JPN.Tokenizer;


public class TokenizerTest {
	public static void main(String[] args) {
		Document d = new Document();
		d.setText("このサイトは盆栽、ミニ盆栽、"
				+ "小品盆栽など盆栽をより多く広める為そして盆栽を愛する全ての人達に役立てていただけるように作られたサイトです。"
				+ "盆栽づくりの楽しみは、" + "自然の美しい樹木をミ＝チュア化して、"
				+ "自分好みの樹形に仕立てることができる点です。" + "盆栽といえばお年寄りの趣味のように思われていましたが、"
				+ "最近では若い人たちの間や海外でも静かなブームになっています。"
				+ "その一方で、実際に盆栽を始めても、自分の思いどおりの木の姿・形に仕立てることができず、"
				+ "落胆している人も少なくないようです。" + "「葉はよく茂るのに、"
				+ "下枝から枯れてくる」「何年たっても花が咲かなvい」「実がついたと思つたら、"
				+ "すぐ落ちてしまった」などという話をよく耳にします。" + "このような現象は、"
				+ "樹木についての基本的な知識や技術が不足しているために生じることです。"
				+ "樹木には、それぞれ順調に生長するために必要な条件があり、" + "枝の伸び方や花の咲き方にも個性があはす。"
				+ "それらの条件や個々の性質を考えながら適切な手入れをしていくことが大切です。");
		Tokenizer tok = new Tokenizer();
		tok.parse(d);
		for (Annotation t : d.annotations("token")) {
			System.out.println(t.feature("string"));
		}
	}
}
