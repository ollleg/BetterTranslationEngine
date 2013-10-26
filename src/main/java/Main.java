import com.betr.engine.GoogleTranslation;
import com.betr.engine.TranslationInterface;
import com.betr.engine.TranslationLanguage;


public class Main {
	public static void main(String[] args) {
		TranslationInterface translator = new GoogleTranslation();
		String translation = translator.translate(
				TranslationLanguage.ENGLISH, 
				TranslationLanguage.FRENCH, 
				"hello");
		System.out.println(translation);
	}
}
