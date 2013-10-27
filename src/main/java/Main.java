import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.betr.engine.TranslationInterface;
import com.betr.engine.TranslationLanguage;
import com.betr.engine.gogl.GoogleTranslation;


public class Main {
	public static void main(String[] args) {
		/* Input */
		String text = "hello my friend";
		TranslationLanguage sourceLang = TranslationLanguage.ENGLISH;
		TranslationLanguage targetLang = TranslationLanguage.RUSSIAN;
		TranslationInterface translator = new GoogleTranslation();
		Map<TranslationLanguage, String> translations = new TreeMap<TranslationLanguage, String>();
		
		/* First layer translation */
		for(TranslationLanguage middleLang : TranslationLanguage.values()) {
			if(middleLang!=sourceLang) {
				String translation = translator.translate(
						sourceLang, 
						middleLang, 
						text);
				translations.put(middleLang, translation);
			}
		}
		
		/* Second layer translation */
		for(TranslationLanguage middleLang : TranslationLanguage.values()) {
			if(middleLang!=sourceLang && middleLang!=targetLang) {
				String translation = translator.translate(
						middleLang, 
						targetLang, 
						translations.get(middleLang));
				translations.put(middleLang, translation);
			}
		}
		
		/* Output */
		System.out.println();
		System.out.println("Input text: "+text);
		System.out.println();
		for(String translation : translations.values()){
			System.out.println(translation);
		}
	}
}
