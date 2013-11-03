package com.betr.engine;

import java.util.List;

import com.betr.engine.Translation.Sentences;

public interface TranslationInterface {
	public List<Sentences> translate(TranslationLanguage from, TranslationLanguage to, String text) throws TranslateException;
	public List<Sentences> translate(TranslationLanguage from, TranslationLanguage to, List<Sentences> inputSentences) throws TranslateException;
}
