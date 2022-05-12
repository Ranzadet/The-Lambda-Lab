
public class Variable implements Expression {
	private String name;
	public String type = "FREE";
	
	public Variable(String name) {
		this.name = name;
	}

	public Variable(String name, String type){
		this.type = type;
	}

	public String getName(){
		return name;
	}
	
	public String toString() {
		return name;
	}

}
