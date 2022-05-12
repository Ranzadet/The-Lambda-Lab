
import java.text.ParseException;
import java.util.ArrayList;

public class Parser {
	private ArrayList<String> tokens;
	
	/*
	 * TODO: \a.a  \b.b
	 */
	public Expression parse(ArrayList<String> tokenList) throws ParseException {
		tokens = tokenList;
		//make some sort of stored expressions list that holds previously created variables, functions, etc.

		if(tokens.size() == 0){
			return new Variable("");
		}
		try{
			return _parse(null);
		}
		catch(Exception e){
			System.out.println("Incorrectly Formatted Expression. "+e);
		}
		return null;
		
	}

	private Expression _parse(Expression exp){
		if(tokens.size() == 0){
			return exp;
		}
		String s = tokens.get(0);
		tokens = new ArrayList<String>(tokens.subList(1, tokens.size()));
		
		if(s.equals("\\")){
			Variable v = new Variable(tokens.get(0));
			tokens = new ArrayList<String>(tokens.subList(2, tokens.size())); //skip past the .
			return _parse(new Function(v, _parse(null))); 
		}
		if(s.equals("(")){
			//handle '(' by branching off until a closing paren is found, and then returning to normal recursion
			if(exp == null){
				Expression paren = _parse(exp);
				return _parse(paren);
			}
			Expression e = _parse(null);
			return _parse(new Application(exp, e)); 
			//if exp is not null before a ( is encountered, this means that the parser is looking at an application separated by parens
		}
		if(s.equals(")")){
			//if you run into a ')', recurse back to where '(' was found 
			return exp;
		}
		else{
			//otherwise, the token is a variable
			if(exp == null)
				return _parse(new Variable(s));
			return _parse(new Application(exp, new Variable(s))); //later, check for s in stored expressions list
		}
	}


	// public Expression treeTraverse(String s, Expression exp, int index){
	// 	String s = tokens.get(index);
	// 	while(!s.equals(")") && index < tokens.size()){


	// 		index++;
	// 	}

	// 	return null;
	// }

}
