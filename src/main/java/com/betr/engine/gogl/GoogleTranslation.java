package com.betr.engine.gogl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.betr.engine.AbstractTranslationInterface;
import com.betr.engine.TranslationInterface;
import com.betr.engine.TranslationLanguage;
import com.betr.engine.gogl.Translation.Sentences;

public class GoogleTranslation extends AbstractTranslationInterface {
	
	private final String USER_AGENT = "Mozilla/5.0";

	public List<Sentences> translate(TranslationLanguage from,
			TranslationLanguage to, String text) {
		String translatedJSON = getTranslatedData(from,to,text);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		List<Sentences> translation = null;
		double score = 0;
		try {
			Translation tr = mapper.readValue(translatedJSON, Translation.class);
			
			if(tr.getSentences() != null) {
				translation = tr.getSentences();
				
				/* Add initial translation and target language */
				for(Sentences sent : translation) {
					sent.setInitialOrig(sent.getOrig());
					sent.setTargetLanguage(to);
				}
			}
			
			if(tr.getDict() != null && tr.getDict().size()>0 &&
					tr.getDict().get(0).getEntry().size()>0) {
				score = tr.getDict().get(0).getEntry().get(0).getScore();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(score!=0) {
			System.out.println("Score: "+score);
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
