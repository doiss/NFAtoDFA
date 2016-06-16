package converterPackage;

import java.util.ArrayList;

public class Node {
	String name;
	
	ArrayList<String> transitionList = new ArrayList<String>();
	public Node(String name){
		this.name = name;
		this.transitionList = new ArrayList<String>();
	}
	public void setTransition(ArrayList<String> transitionList){
		this.transitionList = transitionList;
		
	}
	public String getName(){
		return this.name;
	}


	public ArrayList<String> getTransitionList(){
		return this.transitionList;
	}
}
