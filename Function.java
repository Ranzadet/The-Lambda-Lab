
public class Function implements Expression {
    
    private Expression exp;
    private Variable param;

    public Function(Variable v, Expression e){
        exp = e;
        param = v;
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

}
