import java.util.List;

import lingutil.bleu.BleuMeasurer;

import com.betr.engine.TranslationInterface;
import com.betr.engine.TranslationLanguage;
import com.betr.engine.fusion2layer.FusionTranslation;
import com.betr.engine.gogl.GoogleTranslation;
import com.betr.engine.gogl.Translation.Sentences;
import com.betr.util.Util;


public class Main {
	public static void main(String[] args) {
		String text = "Следуя суточному ритму, наиболее низкая температура тела отмечается утром, около 6 часов, а максимальное значение достигается вечером. Как и многие другие биоритмы, температура следует суточному циклу Солнца, а не уровню нашей активности. Люди, работающие ночью и спящие днем, демонстрируют тот же цикл изменения температуры, что и остальные.";
		TranslationLanguage sourceLang = TranslationLanguage.RUSSIAN;
		TranslationLanguage targetLang = TranslationLanguage.ENGLISH;
		TranslationInterface simpleTranslator = new GoogleTranslation();
		TranslationInterface upgradedTranslator = new FusionTranslation(simpleTranslator);
		
		List<Sentences> translationUpgrade = upgradedTranslator.translate(sourceLang, targetLang, text);
		List<Sentences> translationSimple = simpleTranslator.translate(sourceLang, targetLang, text);
		
		System.out.println();
		System.out.println("Input text: "+text);
		System.out.println("Upgraded translation: "+Util.convertSentencesToMarkedString(translationUpgrade));
		System.out.println("               Score: "+BleuMeasurer.calcScoreBleu(Util.convertStringToSentences(text), simpleTranslator.translate(targetLang, sourceLang, Util.convertSentencesToString(translationUpgrade))));
		System.out.println("Simple   translation: "+Util.convertSentencesToString(translationSimple));
		System.out.println("               Score: "+BleuMeasurer.calcScoreBleu(Util.convertStringToSentences(text), simpleTranslator.translate(targetLang, sourceLang, Util.convertSentencesToString(translationSimple))));
	}
}
