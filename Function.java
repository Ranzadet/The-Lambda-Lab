//Benen Sullivan
import java.util.Set;
import java.util.HashSet;
public class Function implements Expression {

    private Expression exp;
    private Variable param;
    private static Set<String> taken = new HashSet<>();

    public Function(Variable v, Expression e){
        param = v;//.deepCopy();
        exp = e;//.deepCopy();
        assignParent(exp);
    }
    
    public Function deepCopy() {
    	return new Function(param.paramCopy(), exp.deepCopy());
    }

    public Expression getExp(){
        return exp;
    }

    public Variable getVar(){
        return param;
    }

    public String toString(){
        return "(λ" + param + "." + exp + ")";
    }

    private void assignParent(Expression e){
        if (e instanceof Variable){
            if(e.toString().equals(param.toString())) {
                ((Variable)e).setParent(param);
            }
        }
        if (e instanceof Application){
            assignParent(((Application)e).getLeft());
            assignParent(((Application)e).getRight());
        }
        if(e instanceof Function){
            assignParent(((Function)e).getExp());
        }
    }

    //Add current variable name to list of taken names, then generate a new name and replace
    public void alphaReduce(){
        taken.add(param.toString());
        char c = 'A';
        while(taken.contains(""+c)){
            c = (char)(c + 1);
            if(c > 'Z' && c < 'a'){
                c = 'a';
            }
        }

        _alphaReduce(exp, ""+c);
        param.setName(""+c);
    }

    private void _alphaReduce(Expression e, String replacement){
        if (e instanceof Variable){
            if(((Variable)e).getParent() == param)
                ((Variable)e).setName(replacement);
        }
        if (e instanceof Application){
            _alphaReduce(((Application)e).getLeft(), replacement);
            _alphaReduce(((Application)e).getRight(), replacement);
        }
        if(e instanceof Function){
            _alphaReduce(((Function)e).getExp(), replacement);
        }
    }
    
    public boolean needsReduction(){
    	return needsReduction(exp);
    }
    
    private boolean needsReduction(Expression e) {
    	if(e instanceof Variable) {
    		if(((Variable)e).getName().equals(param.getName()) && ((Variable)e).isFree())
    			return true;
    		return false;
    	}
    	if(e instanceof Function) {
    		return needsReduction(((Function)e).getExp());
    	}
    	
    	return (needsReduction(((Application)e).getLeft()) || needsReduction(((Application)e).getRight()));
    }

    

}
