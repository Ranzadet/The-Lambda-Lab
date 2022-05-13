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
			ArrayList<String> keys = new ArrayList(symbols.keySet());
			Expression n = symbols.putIfAbsent(exp.toString(), _parse(null));
			if(n == null && !keys.contains(firstToken)){
				return new Variable("Added " + symbols.get(exp.toString()) + " as " + exp);
			}
			else{
				return new Variable(firstToken + " is already defined");
			}
		}
		if(s.equals("run")){
			Expression runnable = _parse(null);
			Expression ran = run(runnable);
			return ran;
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
	
	
//	public Expression reduceExp (Expression runnable) {
//		if (runnable instanceof Variable)
//			return runnable;
//		if (runnable instanceof Function) {
//			if (!(((Function)runnable).getExp() instanceof Variable)) {
//				return reduceExp(((Function)runnable).getExp());
//			}
//			return runnable;
//		}
//		if (!(((Application)runnable).getLeft() instanceof Function)){
//			return runnable;
//		}
//		
//		Expression ran = run(runnable);
//		return reduceExp(ran);
//	}


	private Expression run(Expression exp){
		if (exp instanceof Variable)
			return exp;

		if (exp instanceof Function){
			if (((Function)exp).getExp() instanceof Application){
				return new Function(((Function)exp).getVar(), run((((Function)exp).getExp())));
			}
			return exp;
		}
		
			

		Expression left = run(((Application)exp).getLeft());
		Expression right = run(((Application)exp).getRight());
		Expression newApp = new Application(left, right);

		if(left instanceof Function){
			Variable var = ((Function)left).getVar();
			Expression funcExp = ((Function)left).getExp();
			newApp = varReplace(var, funcExp, right);
			if(newApp instanceof Application) {
				return run(newApp);
			}
		}
		
//		if(left instanceof Application) {
//			exp = new Application(run(left))
//			return run(exp);
//		}
		return newApp;
	}

	private Expression varReplace(Variable v, Expression e, Expression replace){
		if (e.toString().equals(v.toString())){
			return replace;
		}
		if (e instanceof Application){
			return new Application(varReplace(v, ((Application)e).getLeft(), replace), varReplace(v, ((Application)e).getRight(), replace));
		}
		if (e instanceof Function) {
			return new Function(((Function)e).getVar(), varReplace(v, ((Function)e).getExp(), replace));
		}

		
		return e;
	} 

}
