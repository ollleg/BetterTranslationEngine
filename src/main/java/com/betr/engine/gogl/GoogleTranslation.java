package com.betr.engine.gogl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.betr.engine.CoreTranslationInterface;
import com.betr.engine.TranslateException;
import com.betr.engine.Translation;
import com.betr.engine.TranslationLanguage;
import com.betr.engine.Translation.Sentences;
import com.betr.perl.splitter.ISentenceSplitter;
import com.betr.perl.splitter.LinguaSentenceSplitter;

public class GoogleTranslation extends CoreTranslationInterface {
	
	private final String USER_AGENT = "Mozilla/5.0";

	public List<Sentences> translate(TranslationLanguage from,
			TranslationLanguage to, String text) throws TranslateException {
		List<Sentences> translation = super.translate(from, to, text);
		if(translation==null) {
			ISentenceSplitter splitter = new LinguaSentenceSplitter();
			List<Sentences> splitted =  splitter.split(from, text);
			
			translation = new ArrayList<Sentences>();
			for(Sentences sent : splitted) {
				List<Sentences> sentTranslation = translateGoogle(from, to, sent.getInitialOrig());
				if(sentTranslation!=null) {
					translation.addAll(sentTranslation);
				} else {
					System.err.println("Too long sentence."+sent.getInitialOrig());
				}
			}
			
			addToCache(text, to, translation);
		} else {
			System.out.print("*");
		}
		
		return translation;
	}

	private List<Sentences> translateGoogle(TranslationLanguage from,
			TranslationLanguage to, String text) throws TranslateException {
		List<Sentences> translation = null;
		String translatedJSON = getTranslatedData(from,to,text);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			Translation tr = mapper.readValue(translatedJSON, Translation.class);
			
			if(tr.getSentences() != null) {
				translation = tr.getSentences();
				
				/* Add initial translation and target language */
				for(Sentences sent : translation) {
					if(sent.getInitialOrig() == null) {
						sent.setInitialOrig(sent.getOrig());
					}
					sent.setOrigLanguage(from);
					sent.setTargetLanguage(to);
				}
			}
		} catch (Exception e) {
			throw new TranslateException();
		}
		return translation;
	}

	private String getTranslatedData(TranslationLanguage from, TranslationLanguage to,
			String text) {
		String translation = "";
		
		try{
			StringBuilder url = new StringBuilder();
			url.append("http://translate.google.com/translate_a/t?client=p&sl=");
			url.append(from.getName());
			url.append("&tl=");
			url.append(to.getName());
			url.append("&q=");
			url.append(URLEncoder.encode(text, "UTF-8"));
			
			URL obj = new URL(url.toString());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			translation = response.toString();
		} catch (IOException ex) {
			
		}
		
		return translation;
	}

}
