package com.betr.perl.splitter;

import java.util.List;
import org.junit.Test;

import com.betr.engine.TranslationLanguage;
import com.betr.engine.Translation.Sentences;

import static org.junit.Assert.*;

public class LinguaSentenceSplitterTest {

	private LinguaSentenceSplitter splitter = new LinguaSentenceSplitter();
	
	@Test
	public void splitTestEn() {
		List<Sentences> sentences = splitter.split(TranslationLanguage.ENGLISH, "Hello.Hello: boy.");
		assertEquals(sentences.size(), 2);
		assertEquals(sentences.get(0).getInitialOrig(), "Hello.");
		assertEquals(sentences.get(0).getOrig(), "Hello.");
		assertEquals(sentences.get(0).getOrigLanguage(), TranslationLanguage.ENGLISH);
		assertEquals(sentences.get(1).getInitialOrig(), "Hello: boy.");
		assertEquals(sentences.get(1).getOrig(), "Hello: boy.");
		assertEquals(sentences.get(1).getOrigLanguage(), TranslationLanguage.ENGLISH);
	}
	
	@Test
	public void splitTestDe() {
		List<Sentences> sentences = splitter.split(TranslationLanguage.GERMAN, "Hallo?Hello: boy.");
		assertEquals(sentences.size(), 2);
		assertEquals(sentences.get(0).getInitialOrig(), "Hallo?");
		assertEquals(sentences.get(0).getOrig(), "Hallo?");
		assertEquals(sentences.get(0).getOrigLanguage(), TranslationLanguage.GERMAN);
		assertEquals(sentences.get(1).getInitialOrig(), "Hello: boy.");
		assertEquals(sentences.get(1).getOrig(), "Hello: boy.");
		assertEquals(sentences.get(1).getOrigLanguage(), TranslationLanguage.GERMAN);
	}
	
	@Test
	public void splitTestUk() {
		List<Sentences> sentences = splitter.split(TranslationLanguage.UKRAINIAN, "Привіт!Привіт: привіт.");
		assertEquals(sentences.size(), 2);
		assertEquals(sentences.get(0).getInitialOrig(), "Привіт!");
		assertEquals(sentences.get(0).getOrig(), "Привіт!");
		assertEquals(sentences.get(0).getOrigLanguage(), TranslationLanguage.UKRAINIAN);
		assertEquals(sentences.get(1).getInitialOrig(), "Привіт: привіт.");
		assertEquals(sentences.get(1).getOrig(), "Привіт: привіт.");
		assertEquals(sentences.get(1).getOrigLanguage(), TranslationLanguage.UKRAINIAN);
	}
	
}
