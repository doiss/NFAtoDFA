package converterPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Parser {
	static ArrayList<Character> states;
	static ArrayList<Character> alphabet;
	static String startStateString;
	static ArrayList<Character> acceptStates;
	static ArrayList<Character> transitions;
	static ArrayList<String> finalStates;

	public static void parser(String filePath){
		String path = filePath;
		FileReader fr = null;
		String line1 = null;
		String line2 = null;
		String line3 = null;
		String line4 = null;
		String restOfLines = null;

		try {
			fr = new FileReader(path);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		states = new ArrayList<Character>();
		alphabet = new ArrayList<Character>();
		startStateString = "";
		acceptStates = new ArrayList<Character>();
		transitions = new ArrayList<Character>();
		try {
			line1 = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		char[] line1Array = line1.toCharArray();
		for(int i =0; i<line1.length();++i){
			states.add(line1Array[i]);
			if(i < line1.length()-1){
				if(line1Array[i+1] == '\t'){
					++i;
				}
			}
		}

		try {
			line2 = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		char[] line2Array = line2.toCharArray();
		for(int i =0; i<line2.length();++i){
			alphabet.add(line2Array[i]);
			if(i < line2.length()-1){
				if(line2Array[i+1] == '\t'){
					++i;
				}
			}
		}
		try {
			line3 = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startStateString = line3;
		try {
			line4 = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		char[] line4Array = line4.toCharArray();
		for(int i =0; i<line4.length();++i){
			acceptStates.add(line4Array[i]);
			if(i < line4.length()-1){
				if(line4Array[i+1] == '\t'){
					++i;
				}
			}
		}
		try {
			restOfLines = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		char[] restOfLinesArray = restOfLines.toCharArray();
		char emptyState = 'e';
		for(int i =0; i<restOfLines.length();++i){
			transitions.add(restOfLinesArray[i]);
			++i;
			transitions.add(restOfLinesArray[++i]);
			++i;
			if(restOfLinesArray[++i] == 'E'){
				if(restOfLinesArray[i+1] == 'M'){
					transitions.add(emptyState);
				}
			}
			else{
				transitions.add(restOfLinesArray[i]);
			}
			if(i < restOfLines.length()-1){
				if(restOfLinesArray[i+1] == '\t'){
					++i;
				}
			}
		}

	}
	
	
	public static ArrayList<ArrayList> nfaTableMaker(ArrayList<Character> initialStates, ArrayList<Character> alphabetInputs, ArrayList<Character> transitionedStates){
		ArrayList<ArrayList>nfaTable = new ArrayList<ArrayList>();
		char putter = 'n';
		for(int i = 0; i<states.size(); ++i){
			ArrayList<Character> filledTableList = new ArrayList<Character>();
			for(int j = 0; j<alphabet.size(); ++j){
				for(int k = 0; k<initialStates.size(); ++k){
					putter = 'n';
					if(initialStates.get(k).equals(states.get(i)) && alphabetInputs.get(k).equals(alphabet.get(j))){
						putter = transitionedStates.get(k);
						break;
					}

				}
				filledTableList.add(putter);
			}
			nfaTable.add(filledTableList);
		}
		return nfaTable;
	}
	
	
	public static ArrayList<ArrayList> dfaMaker(ArrayList<ArrayList> nfaTable,ArrayList<String> epsilons ){
		ArrayList<ArrayList> DFAtable = new ArrayList<ArrayList>();
		String newState = "";
		Queue<String> newStates = new LinkedList<String>();
		finalStates = new ArrayList<String>();
		newStates.add(startStateString);
		finalStates.add(startStateString);
		while(!newStates.isEmpty()){
			String currentState = newStates.remove();
			char[] splitState = currentState.toCharArray();
			ArrayList<String> oneLine = new ArrayList<String>();
			for(int k = 0; k < splitState.length; ++k){
				for(int j = 0; j<alphabet.size(); ++j){
					int index = states.indexOf(splitState[k]);
					if(!nfaTable.get(index).get(j).equals('n')){
						for(int l = 0; l<states.size(); ++l){
							if(states.get(l).equals(nfaTable.get(index).get(j))){

								newState = epsilons.get(l).toString();
								if(!finalStates.contains(newState)){
									finalStates.add(newState);
									newStates.add(newState);
								}
							}
						}	
					}
					else
						newState = "n";
					oneLine.add(newState);
				}
			}

			if(oneLine.size() > alphabet.size()){
				ArrayList<String> templist = new ArrayList<String>();
				for(int i = 0; i < alphabet.size(); ++i){
					ArrayList<String> tempList2 = new ArrayList<String>();
					for(int j = 0; j< oneLine.size()/alphabet.size(); ++j){
						if(!oneLine.get(i+(j*alphabet.size())).equals("n") && !tempList2.contains(oneLine.get(i+(j*alphabet.size()))))
							tempList2.add(oneLine.get(i+(j*alphabet.size())));
					}
					String concatString = "";
					for(int j = 0; j < tempList2.size(); ++j){
						concatString += tempList2.get(j);
					}
					if(concatString.equals(""))
						concatString = "n";
					templist.add(concatString);
				}
				oneLine = templist;
			}
			DFAtable.add(oneLine);
			
			
		}
		return DFAtable;
	}

	public static ArrayList<String> epsilonClosureMaker(ArrayList<Character> states, ArrayList<Character> initialStates, ArrayList<Character> alphabetInputs, ArrayList<Character> transitionedStates){
		ArrayList<String> epsilonClosures = new ArrayList<String>();
		ArrayList<Character> sortingList = new ArrayList<Character>();

		for(int i = 0; i<states.size(); ++i){
			String epsilonClosure = "";
			sortingList.add(states.get(i));
			for(int j = 0; j<initialStates.size();++j){
				if(states.get(i).equals(initialStates.get(j))){
					if(alphabetInputs.get(j).equals('E')){
						sortingList.add(transitionedStates.get(j));

					}
				}
			}
			Collections.sort(sortingList);
			for(char c: sortingList){
				epsilonClosure += c;
			}
			sortingList.clear();
			epsilonClosures.add(epsilonClosure);
		}

		for(int i = 0; i<epsilonClosures.size(); ++i){
			String updatedEpsilonClosures = epsilonClosures.get(i);
			if(epsilonClosures.get(i).length() != 1){
				char[] individuals = epsilonClosures.get(i).toCharArray();
				for(int j = 0; j<individuals.length; ++j){
					if(individuals[j] != (states.get(i)) && !(epsilonClosures.get(i).contains(epsilonClosures.get(states.indexOf(individuals[j]))))){
						updatedEpsilonClosures+= epsilonClosures.get(states.indexOf(individuals[j]));
					}
				}
				epsilonClosures.set(i, updatedEpsilonClosures);
			}
		}
		return epsilonClosures;
	}

	public static void main(String[] args){
		System.out.println("Please enter a File-Path");
		Scanner in = new Scanner (System.in);
		String filePath = in.nextLine();
		parser(filePath);

		ArrayList<Character> initialStates = new ArrayList<Character>();
		ArrayList<Character> alphabetInputs = new ArrayList<Character>();
		ArrayList<Character> transitionedStates = new ArrayList<Character>();
		for(int i =0; i < transitions.size(); ++i){
			initialStates.add(transitions.get(i++));
			alphabetInputs.add(transitions.get(i++));
			transitionedStates.add(transitions.get(i));
		}
		ArrayList<String> epsilonClosures = epsilonClosureMaker(states,initialStates,alphabetInputs,transitionedStates);
		ArrayList<ArrayList> nfaTable = nfaTableMaker(initialStates,alphabetInputs,transitionedStates);

		ArrayList<ArrayList> dfatable = dfaMaker(nfaTable,epsilonClosures);
		Graph DFA = new Graph();
		DFA.setAlphabet(alphabet);
		for(int i = 0; i < finalStates.size(); ++i){
			Node node = new Node(finalStates.get(i));
			node.setTransition(dfatable.get(i));
			DFA.addNode(node);
		}
		
		DFA.printGraph();
	}
}

