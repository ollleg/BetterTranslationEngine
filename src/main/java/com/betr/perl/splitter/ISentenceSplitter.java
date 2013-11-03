package com.betr.perl.splitter;

import java.util.List;

import com.betr.engine.TranslationLanguage;
import com.betr.engine.Translation.Sentences;

public interface ISentenceSplitter {
	public List<Sentences> split(TranslationLanguage language, String text);
}
