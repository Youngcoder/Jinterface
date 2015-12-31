package com.gtja.jinterface;

import java.util.HashMap;
import java.util.Map;

public class GlobalNodesView {
	private static Map<String,AbstractNode> nodeMap = new HashMap<String,AbstractNode>();
	
	private static GlobalNodesView instance = new GlobalNodesView();
	
	private GlobalNodesView(){
	}
	
	public static GlobalNodesView getInstance() {
		return instance;
	}

	
	public static Map<String,AbstractNode> getAllNodes() {
		return nodeMap;
	}
	
	public static void showAll() {
		System.out.println("-----All Nodes-----");
		for(String node : getAllNodes().keySet()){
			AbstractNode n = nodeMap.get(node);
			if(n instanceof JNode){ 
				System.out.println("node name: " + node + " jnode");
			}else{
				System.out.println("node name: " + node + " not jnode");
			}
		}
	}
	
	public static boolean contains(String node) {
		return nodeMap.containsKey(node);
	}
	
	public static AbstractNode getNode(String name) {
		return nodeMap.get(name);
	}
	
	public static void addNode(AbstractNode node) {
		nodeMap.put(node.node(), node);
	}
	
	public static void removeNode(String nodename) {
		nodeMap.remove(nodename);
	}
		
			

}
