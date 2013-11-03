package com.betr.perl.splitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.betr.engine.Translation.Sentences;
import com.betr.engine.TranslationLanguage;

public class LinguaSentenceSplitter implements ISentenceSplitter {

	public List<Sentences> split(TranslationLanguage language, String text) {
		text = text.replaceAll("\\. ", ".");
		text = text.replaceAll("\\.", ". ");
		text = text.replaceAll("! ", "!");
		text = text.replaceAll("!", "! ");
		text = text.replaceAll("\\? ", "?");
		text = text.replaceAll("\\?", "? ");
		
		List<Sentences> ret = new ArrayList<Sentences>();
		OutputStreamWriter writer =null;
	    BufferedReader stdoutBR = null;
	    try {
	        File tmp = new File("temp_dir");
	        tmp.mkdirs();
	        File cmdFile = File.createTempFile("foo", ".sh", tmp);
	        File tmpFile = File.createTempFile("tmp", ".txt", tmp);
	        File stdout = File.createTempFile("foo_stdout", ".txt", tmp);
	        File stderr = File.createTempFile("foo_stderr", ".txt", tmp);
	        
	        FileUtils.writeStringToFile(tmpFile, text);
	        
	        String script = new StringBuilder()
	        .append("/usr/bin/perl script/split.pl -q -l ")
	        .append(language.getName())
	        .append(" < ")
	        .append(tmpFile.getAbsolutePath())
	        .append(" >")
	        .append(stderr.getAbsolutePath())
	        .append(" >")
	        .append(stdout.getAbsolutePath())
	        .append(" \n")
	        .toString();
	        
	        cmdFile.setExecutable(true);
	        FileUtils.writeStringToFile(cmdFile, script);           

	        ProcessBuilder processBuilder = new ProcessBuilder(cmdFile.getAbsolutePath());
	        processBuilder.redirectErrorStream(true);
	        Process process = processBuilder.start();
	        InputStream numbStream = process.getInputStream();
	        stdoutBR = new BufferedReader(new InputStreamReader(numbStream));

	        String line = null;
	        StringBuilder unexpectedOutput = new StringBuilder();
	        while ((line = stdoutBR.readLine()) !=null) {
	            unexpectedOutput.append(line);
	            unexpectedOutput.append("\n");

	        }
	        process.waitFor();
	        stdoutBR.close();
	        
	        if (process.exitValue() != 0) {
	            String stdoutString = FileUtils.readFileToString(stdout);
	            String stderrString = FileUtils.readFileToString(stderr);
	            throw new RuntimeException("Problem executing script. \nOutput:"+unexpectedOutput.toString()+"\nStdout:"+stdoutString+"\nStderr:"+stderrString);
	        }

	        String output = FileUtils.readFileToString(stdout);
	                    FileUtils.deleteQuietly(cmdFile); 
	                    
	                    
	        for(String str:output.split("\n")) {
	        	Sentences sent = new Sentences();
	        	sent.setInitialOrig(str);
	        	sent.setOrig(str);
	        	sent.setOrigLanguage(language);
	        	ret.add(sent);
	        }
	        
	        FileUtils.deleteQuietly(stdout);
	        FileUtils.deleteQuietly(stderr);
	        FileUtils.deleteQuietly(tmpFile);

	    } catch(Exception e){
	    	e.printStackTrace();
	    } finally {
	        try {
	            if (writer != null) {
	                writer.close();
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		return ret;
	}
	
}
