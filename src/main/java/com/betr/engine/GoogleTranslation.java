package com.betr.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleTranslation implements TranslationInterface {
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	public GoogleTranslation() {
		
	}
	
	public String translate(TranslationLanguage from, TranslationLanguage to,
			String text) {
		String translated = getTranslatedData(from,to,text);
		return translated;
	}
	
	private String getTranslatedData(TranslationLanguage from, TranslationLanguage to,
			String text) {
		String translation = "";
		
		try{
			String url = "http://translate.google.com/translate_a/t?client=t&sl="+from.getName()+"&tl="+to.getName()+"&sc=2&ie=UTF-8&oe=UTF-8&prev=btn&ssel=0&tsel=0&q="+text;
			 
			URL obj = new URL(url);
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
