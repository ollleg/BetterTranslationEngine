package com.betr.engine;
public enum TranslationLanguage {
	ENGLISH("en"),
	RUSSIAN("ru"),
	FRENCH("fr"),
	SPANISH("es"),
	UKRAINIAN("ua"),
	ITALIAN("it"),
	GERMAIN("gr");
	
	private String name;
	
	private TranslationLanguage(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
