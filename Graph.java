package converterPackage;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Graph {
	ArrayList<Node> Graph;
	ArrayList<Character> alphabetList;
	int size;
	public Graph(){
		Graph = new ArrayList<Node>();
		alphabetList = new ArrayList<Character>();
		size = 0;
	}
	public void setAlphabetList(ArrayList<Character> inputs){
		this.alphabetList = inputs;
	}
	public void addNode(Node node){
		Graph.add(node);
		
	}
	public Node getNode(int index){
		return Graph.get(index);
	}
	public int getSize(){
		return Graph.size();
	}
	public void printGraph(){
		PrintWriter writer;
		try {
			writer = new PrintWriter("out.DFA");
			String printString = "";
			for(Node n: Graph){
				printString = "State ";
				printString+= n.getName();
				ArrayList<String> transitions = n.getTransitionList();
				for(int i = 0; i < transitions.size(); ++i){
					if(transitions.get(i) != "n"){
						printString+= " is connected to State ";
						printString+=n.transitionList.get(i);
						printString+= " by ";
						printString+= alphabetList.get(i);	
					}
					else{
						printString+= " has no input for ";
						printString+=alphabetList.get(i);
					}
				}
				writer.println(printString);
				System.out.println(printString);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		

		
		}
	}
	public void setAlphabet(ArrayList<Character> inputs){
			this.alphabetList = inputs;
	}
	public boolean contains(String name){
		for(Node n: Graph){
			if(n.getName().equals(name)){
				return true;
			}
		}
		
		return false;
	}
}
