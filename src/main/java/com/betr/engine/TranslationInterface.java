package com.betr.engine;

import java.util.List;

import com.betr.engine.gogl.Translation.Sentences;

public interface TranslationInterface {
	public List<Sentences> translate(TranslationLanguage from, TranslationLanguage to, String text);
}
