import java.util.HashMap;
import java.text.ParseException;
import java.util.ArrayList;

public class Parser {
	private ArrayList<String> tokens;
	private ArrayList<Byte> parens = new ArrayList<>();
	private HashMap<String, Expression> symbols = new HashMap<>();
	private String firstToken = "";
	
	/*
	 * TODO: prevent reuse of variable names
	 */
	public Expression parse(ArrayList<String> tokenList) throws ParseException {
		tokens = tokenList;
		//make some sort of stored expressions list that holds previously created variables, functions, etc.

		if(tokens.size() == 0){
			return new Variable("");
		}
		firstToken = tokens.get(0);
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
		//System.out.println(s);
		tokens = new ArrayList<String>(tokens.subList(1, tokens.size()));
		
		if(s.equals("\\")){
			Variable v = new Variable(tokens.get(0));
			tokens = new ArrayList<String>(tokens.subList(2, tokens.size())); //skip past the .
			if (parens.isEmpty()) {
				if(exp == null)
					return _parse(new Function(v, _parse(null))); 
				return _parse(new Application(exp, new Function(v, _parse(null))));
			}
			else {
				if(exp == null)
					return new Function(v, _parse(null)); 
				return new Application(exp, new Function(v, _parse(null)));
			}
		}
		if(s.equals("(")){
			//handle '(' by branching off until a closing paren is found, and then returning to normal recursion
			parens.add((byte)1);
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
			parens.remove(0);
			return exp;
		}
		if(s.equals("=")){
			/*
			 * 
			 */
//			ArrayList<Expression> valsEx = new ArrayList<>(symbols.values());
//			ArrayList<String> valsStr = new ArrayList<>();
//			valsEx.forEach(val -> valsStr.add(val.toString()));
			ArrayList<String> keys = new ArrayList(symbols.keySet());
			Expression n = symbols.putIfAbsent(exp.toString(), _parse(null));
			if(n == null && !keys.contains(firstToken)){
				return new Variable("Added " + exp + " as " + symbols.get(exp.toString()));
			}
			else{
				return new Variable(firstToken + " is already defined");
			}
		}
		else{
			//otherwise, the token is a variable
			Expression mapped = symbols.get(s);

			if(mapped == null){
				if(exp == null)
					return _parse(new Variable(s));
				return _parse(new Application(exp, new Variable(s)));
			}
			if(exp == null)
				return _parse(mapped);
			return _parse(new Application(exp, mapped)); //later, check for s in stored expressions list
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
