package com.aviyehuda.htmlimage.generator;


public class HtmlGeneratorFG extends HtmlGenerator{

	
	@Override
	public String openCell(String color) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<td style='color:");
		
		sb.append(color);
		
		sb.append(";' >");
		
		return sb.toString();
	}

}
