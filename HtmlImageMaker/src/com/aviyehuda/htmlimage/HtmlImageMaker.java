package com.aviyehuda.htmlimage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import org.apache.pivot.wtk.DesktopApplicationContext;

import com.aviyehuda.htmlimage.generator.HtmlGenerator;
import com.aviyehuda.htmlimage.generator.HtmlGeneratorBG;
import com.aviyehuda.htmlimage.generator.HtmlGeneratorFG;
import com.aviyehuda.htmlimage.ui.MainWindow;

public class HtmlImageMaker {

	private static String toHtmlColor(Color color){
		
		StringBuilder sb = new StringBuilder(7);
		
		sb.append("#");
		sb.append(getHexColor(color.getRed()));
		sb.append(getHexColor(color.getGreen()));
		sb.append(getHexColor(color.getBlue()));
		
		return sb.toString();
		
	}
	
	
private static String toHtmlColor(int [] color){
		
		StringBuilder sb = new StringBuilder(7);
		
		sb.append("#");
		sb.append(getHexColor(color[0]));
		sb.append(getHexColor(color[1]));
		sb.append(getHexColor(color[2]));
		
		return sb.toString();
		
	}
	
	
	
	private static String getHexColor(int color) {
		String hexcolors = "0123456789ABCDEF";
		
		String result = "" +
			hexcolors.charAt((color/16)) + hexcolors.charAt((color%16));
		
		return result;
	}


	public static BufferedImage loadImage(String ref) {
		BufferedImage bimg = null;
		try {

			bimg = ImageIO.read(new File(ref));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}
	
	public static void main(String[] args) {
		DesktopApplicationContext.main(MainWindow.class, args);
	}
	
	public static void generate(String imageName, String outHtmlFileName, 
			String text, boolean foregroundColor) {
		
		try{
			HtmlGenerator htmlGenerator;	
			Robot robot = null;
			Raster imageRaster = null;
			
			double width;
			double height;
			
			int len = 0;
			
			if(text != null){
				len= text.length();
			}
			
			int [] nullIntArr = null;
			
			boolean isFromScreen;
			if(imageName == null || imageName.length() == 0){
				isFromScreen = true;
				
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				
				width = screen.getWidth();
				height = screen.getHeight();
				robot = new Robot();
				
			}
			else { //from image
				imageRaster = loadImage(imageName).getData();
				isFromScreen = false;
				width = imageRaster.getWidth();
				height = imageRaster.getHeight();
			}
			
			if(!foregroundColor){
				htmlGenerator = new HtmlGeneratorBG();
			}
			else {
				htmlGenerator = new HtmlGeneratorFG();
			}
		
			
			StringBuilder sb = new StringBuilder(20000);
			
			sb.append(htmlGenerator.openTable());
			for(int y=0;y<height;y++){
			
				sb.append(htmlGenerator.openRow());
				for(int x=0;x<width;x++){
				
					if(isFromScreen){
						sb.append(
								htmlGenerator.openCell(
								  toHtmlColor(robot.getPixelColor(x, y))));
					}
					else{
						sb.append(htmlGenerator.openCell(
								toHtmlColor(
										imageRaster.getPixel(x, y,nullIntArr))));
					}	
					
					if(text != null && text.trim().length() > 0){
						sb.append(text.charAt(x%len));
						sb.append("&nbsp;&nbsp;");
					}
					
					sb.append(htmlGenerator.closeCell());
				}
				sb.append(htmlGenerator.closeRow());
			}
			sb.append(htmlGenerator.closeTable());
			
			
			// Write to file
			File file = new File(outHtmlFileName);
			FileWriter fw = new FileWriter(file);
			fw.write(sb.toString());
			
			 java.awt.Desktop.getDesktop().open(file);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
