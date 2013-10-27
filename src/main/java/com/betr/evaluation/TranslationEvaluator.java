package com.betr.evaluation;

public interface TranslationEvaluator {
	public void reset();
	public double calculateScore();
	public void addSentence(String[] refTokens, String[] candTokens);
}
