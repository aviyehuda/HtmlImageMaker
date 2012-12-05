package com.aviyehuda.htmlimage.generator;

public abstract class HtmlGenerator {
	public String openTable(){
		return "<table border='0' cellpdding=0 cellspacing=0  >\n";
	}
	public String closeTable(){
		return "</table>";
	}
	
	public String openRow(){
		return "<tr>";
	}
	public String closeRow(){
		return "</tr>\n";
	}
	
	public String closeCell(){
		return "</td>";
	}
	
	public abstract String openCell(String color);
	
}
