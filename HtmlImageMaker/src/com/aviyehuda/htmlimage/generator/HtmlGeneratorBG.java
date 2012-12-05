package com.aviyehuda.htmlimage.generator;


public class HtmlGeneratorBG extends HtmlGenerator{
	
	@Override
	public String openCell(String color) {
		
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<td bgcolor='");
		sb.append(color);
		sb.append("' >");
		return sb.toString();
	}

}
