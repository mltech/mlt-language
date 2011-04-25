package com.mltech.laf.processing.CHI;

import com.mltech.laf.annotations.Annotation;
import com.mltech.laf.document.Document;
import com.mltech.laf.processing.CHI.Tokenizer;


public class TokenizerTest {
	public static void main(String[] args) {
		Document d = new Document();
		d.setText("京华时报2008年1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。");
		Tokenizer tok = new Tokenizer();
		tok.parse(d);
		for (Annotation t : d.annotations("token")) {
			System.out.print(t.feature("string") + " | ");
		}
	}
}
