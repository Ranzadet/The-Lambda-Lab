import java.util.ArrayList;
public class Variable implements Expression {
	private String name;
	private Variable parent = null; //= 0;  //make null parent 0, setParent() assigns variable ID of parameter var
	private int localID;
	private int lastParentID = 0;
	private int parentID = 0;
	private static int globalID = 1;
	
	public Variable(String name) {
		this.name = name;
		localID = globalID++;
	}
	
	public Variable deepCopy() {
		Variable newVar = new Variable(name);
		newVar.setParentID(getParentID());
    	return newVar;//this;
    }
	
	public Variable paramCopy() {
		Variable newVar = new Variable(name);
		newVar.setID(localID);
		return newVar;
	}

	public Variable getParent(){
		return parent;
	}

	public void setParent(Variable v){
		if(parent == null) {
			if(parentID == 0 || parentID == v.getID()) {
				parent = v;
				parentID = v.getID();
			}
		}
			
	}

	public String getName(){
		return name;
	}
	
	public int getID() {
		return localID;
	}
	
	public void setID(int id) {
		localID = id;
	}
	
	public int getParentID() {
		return parentID;
	}
	
	private void setParentID(int id) {
		parentID = id;
	}

	public void setName(String s){
		name = s;
	}
	
	public String toString() {
		return name;
	}

}
