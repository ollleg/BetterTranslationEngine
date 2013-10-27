import java.util.List;


import com.betr.engine.TranslationInterface;
import com.betr.engine.TranslationLanguage;
import com.betr.engine.fusion2layer.FusionTranslation;
import com.betr.engine.gogl.GoogleTranslation;
import com.betr.engine.gogl.Translation.Sentences;
import com.betr.evaluation.BleuEvaluator;
import com.betr.evaluation.SimpleBleuEvaluator;
import com.betr.util.Util;


public class Main {
	public static void main(String[] args) {
		String text = "Ainsi, hormis le fragile souvenir que lui consacre ici l’auteur de ce livre, il ne reste plus rien aujourd’hui du mot mystérieux gravé dans la sombre tour de Notre-Dame, rien de la destinée inconnue qu’il résumait si mélancoliquement. L’homme qui a écrit ce mot sur ce mur s’est effacé, il y a plusieurs siècles, du milieu des générations, le mot s’est à son tour effacé du mur de l’église, l’église elle-même s’effacera bientôt peut-être de la terre.";
		TranslationLanguage sourceLang = TranslationLanguage.FRENCH;
		TranslationLanguage targetLang = TranslationLanguage.RUSSIAN;
		TranslationInterface simpleTranslator = new GoogleTranslation();
		TranslationInterface upgradedTranslator = new FusionTranslation(simpleTranslator, new BleuEvaluator());
		
		List<Sentences> translationUpgrade = upgradedTranslator.translate(sourceLang, targetLang, text);
		List<Sentences> translationSimple = simpleTranslator.translate(sourceLang, targetLang, text);
		
		System.out.println();
		System.out.println("Input text: "+text);
		System.out.println("Upgraded translation: "+Util.convertSentencesToMarkedString(translationUpgrade));
		System.out.println("               Score: "+BleuEvaluator.calcScoreBleu(simpleTranslator.translate(targetLang, sourceLang, translationUpgrade)));
		System.out.println("Simple   translation: "+Util.convertSentencesToMarkedString(translationSimple));
		System.out.println("               Score: "+BleuEvaluator.calcScoreBleu(simpleTranslator.translate(targetLang, sourceLang, translationSimple)));
	}
}
