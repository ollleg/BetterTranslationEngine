package com.betr.engine;
public enum TranslationLanguage {
	ENGLISH("en"),
	RUSSIAN("ru"),
	FRENCH("fr"),
	SPANISH("es"),
	UKRAINIAN("uk"),
	ITALIAN("it"),
	GERMAN("de");
	
	private String name;
	
	private TranslationLanguage(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
