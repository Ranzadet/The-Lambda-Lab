//Benen Sullivan
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lexer {
	
	/*
	 * A lexer (or "tokenizer") converts an input into tokens that
	 * eventually need to be interpreted.
	 * 
	 * Given the input 
	 *    (\bat  .bat flies)cat  λg.joy! )
	 * you should output the ArrayList of strings
	 *    [(, \, bat, ., bat, flies, ), cat, \, g, ., joy!, )]
	 *
	 */
	private ArrayList<Character> allowed = new ArrayList<>(Arrays.asList('+', '-', '/', '*', '^', '&', '$', '#', '@', '!', ':', '<', '>', '%'));
	
	public ArrayList<String> tokenize(String input) {
		ArrayList<String> tokens = new ArrayList<String>();
		ArrayList<String> important = new ArrayList<>();
		important.add(".");
		important.add("=");
		important.add("(");
		important.add(")");
		
				
		

		String currString = "";
		int i = 0;
		while(i<input.length()){
			String c = input.substring(i,i+1);
			try{
				if(c.equals("λ")){
					if(!currString.strip().equals(""))
						tokens.add(currString.strip());
					tokens.add("\\");
					//i++;
					currString = "";
				}
				//System.out.println(c);
			}
			catch(Exception e){	
			}
			

			if(c.equals(" ")){
				if(!currString.strip().equals(""))
				    tokens.add(currString.strip());
				currString = "";
			}
			else if(important.contains(c)){
				if(!currString.strip().equals(""))
					tokens.add(currString.strip());
				currString = "";
				currString += c;
				tokens.add(currString);
				currString = "";
			}
			else if (isLetter(c.charAt(0))){
				currString += c;
			}
			
			i++;
		}
		if(!currString.equals(""))
			tokens.add(currString);

		String s = "" + (char)206;
		tokens.removeAll(Arrays.asList(s));
		
		//System.out.println(tokens);
		return tokens;
	}
	
	private boolean isLetter(Character c) {
		char start = '!';
		char end = '~';

		
		if ((c >= start && c <= end))
			return true;
		return false;
	}



}
