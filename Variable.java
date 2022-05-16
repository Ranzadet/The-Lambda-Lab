
public class Variable implements Expression {
	private String name;
	private Variable parent = null;
	
	public Variable(String name) {
		this.name = name;
	}

	public Variable getParent(){
		return parent;
	}

	public void setParent(Variable v){
		if(parent == null)
			parent = v;
	}

	public String getName(){
		return name;
	}

	public void setName(String s){
		name = s;
	}
	
	public String toString() {
		return name;
	}

}
