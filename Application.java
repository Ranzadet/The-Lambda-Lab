//Benen Sullivan

public class Application implements Expression {
    private Expression left;
    private Expression right;

    public Application(Expression l, Expression r){
        left = l;
        right = r;
    }
    
    public Application deepCopy() {
    	return new Application(left.deepCopy(), right.deepCopy());
    }

    public Expression getLeft(){
        return left;
    }

    public Expression getRight(){
        return right;
    }


    public String toString(){
        return String.format("(" + left + " " + right + ")");
    }

}
