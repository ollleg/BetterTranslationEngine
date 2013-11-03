import java.util.List;


import com.betr.engine.CoreTranslationInterface;
import com.betr.engine.TranslateException;
import com.betr.engine.TranslationInterface;
import com.betr.engine.TranslationLanguage;
import com.betr.engine.Translation.Sentences;
import com.betr.engine.fusion2layer.FusionMLTranslation;
import com.betr.engine.gogl.GoogleTranslation;
import com.betr.evaluation.BleuEvaluator;
import com.betr.util.Util;


public class Main {
	public static void main(String[] args) {
		String text = "This is also possible and widely used by Moses developers. Instructions are the same as for Linux, assuming that you have a working C++ development environment that includes boost.";
		TranslationLanguage sourceLang = TranslationLanguage.ENGLISH;
		TranslationLanguage targetLang = TranslationLanguage.RUSSIAN;
		TranslationInterface simpleTranslator = new GoogleTranslation();
		TranslationInterface upgradedTranslator = new FusionMLTranslation(simpleTranslator, new BleuEvaluator());
		
		System.out.println();
		System.out.println("Input text: "+text);
		
		List<Sentences> translationUpgrade;
		try {
			translationUpgrade = upgradedTranslator.translate(sourceLang, targetLang, text);
			System.out.println("Upgraded translation: "+Util.convertSentencesToMarkedString(translationUpgrade, true));
			System.out.println("               Score: "+BleuEvaluator.calcScoreBleu(simpleTranslator.translate(targetLang, sourceLang, translationUpgrade)));
		} catch (TranslateException e) {
			System.err.println("Can't translate. Updated");
		}
		List<Sentences> translationSimple;
		try {
			translationSimple = simpleTranslator.translate(sourceLang, targetLang, text);
			System.out.println("Simple   translation: "+Util.convertSentencesToMarkedString(translationSimple, true));
			System.out.println("               Score: "+BleuEvaluator.calcScoreBleu(simpleTranslator.translate(targetLang, sourceLang, translationSimple)));
		} catch (TranslateException e) {
			System.err.println("Can't translate. Simple");
		}
		
		System.out.println();
		System.out.println("Translation core:");
		System.out.println("Total translated: "+CoreTranslationInterface.getTotalTranslations());
		System.out.println("Cache hits: "+CoreTranslationInterface.getCacheHits());
		System.out.println("Cache hits(%): "+String.valueOf(100.0*(double)CoreTranslationInterface.getCacheHits()/(double)CoreTranslationInterface.getTotalTranslations()) + "%");
		System.out.println("Cache size: "+CoreTranslationInterface.getCacheSize());
	}
}
