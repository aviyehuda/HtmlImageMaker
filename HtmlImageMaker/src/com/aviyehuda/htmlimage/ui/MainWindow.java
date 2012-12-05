package com.aviyehuda.htmlimage.ui;

import java.io.File;

import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Checkbox;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtkx.WTKX;
import org.apache.pivot.wtkx.WTKXSerializer;

import com.aviyehuda.htmlimage.HtmlImageMaker;

public class MainWindow implements Application {
    private Window window = null;

    @WTKX private PushButton browseButtonIn = null;
    @WTKX private PushButton browseButtonOut = null;
    @WTKX private TextInput inputImage = null;
    @WTKX private TextInput outputHtml = null;
    @WTKX private PushButton generateButton = null;
    @WTKX private TextInput innerText = null;
    @WTKX private Checkbox foregroundCB = null; 

    @Override
    public void startup(Display display, Map<String, String> properties)
        throws Exception {
        WTKXSerializer wtkxSerializer = new WTKXSerializer();

        window = (Window)wtkxSerializer.readObject(getClass().getResource("file_browsing.wtkx"));
      
        wtkxSerializer.bind(this, MainWindow.class);

      
        browseButtonIn.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {

                final FileBrowserSheet fileBrowserSheet =
                    new FileBrowserSheet(FileBrowserSheet.Mode.OPEN);
                
                
                //TODO: add file filter
                
                if(inputImage != null && inputImage.getText().length() > 0 &&
                        inputImage.getText().indexOf("\\") >0  ){
                    try{
                        String text = inputImage.getText();
                        int index = text.lastIndexOf("\\");
                        
                        fileBrowserSheet.setRootDirectory(
                                new File(text.substring(0,index)));
                        
                     
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ;
                
                fileBrowserSheet.open(window, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                            Sequence<File> selectedFiles = fileBrowserSheet.getSelectedFiles();

                            if(selectedFiles.getLength() > 0){
                            	String fileName = selectedFiles.get(0).getAbsoluteFile().toString();
                            	
                            	if( fileName.endsWith(".png")|| 
                            		fileName.endsWith(".gif")|| 
                            		fileName.endsWith(".jpg")|| 
                            		fileName.endsWith(".bmp")|| 
                            		fileName.endsWith(".tif")||
                            		fileName.endsWith(".PNG")|| 
                                    fileName.endsWith(".GIF")|| 
                                    fileName.endsWith(".JPG")|| 
                                    fileName.endsWith(".BMP")|| 
                                    fileName.endsWith(".TIF")){
                            	inputImage.setText(fileName);
                            	}
                            else{
                            	Alert.alert(MessageType.ERROR, "Please select image", window);
                            }
                            }
                        } 
                        
                        
                        
                    }
                });
            }
        });

        
        browseButtonOut.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {

                final FileBrowserSheet fileBrowserSheet =
                    new FileBrowserSheet(FileBrowserSheet.Mode.SAVE_TO);
                
                fileBrowserSheet.open(window, new SheetCloseListener() {
                    @Override
                    public void sheetClosed(Sheet sheet) {
                        if (sheet.getResult()) {
                            Sequence<File> selectedFiles = fileBrowserSheet.getSelectedFiles();

                            if(selectedFiles.getLength() > 0){
                            	File file = selectedFiles.get(0);
                            	String fileName = file.getAbsolutePath().toString();
                            	if(file.isDirectory()){
                            		fileName = fileName+"\\out.html";
                            	}
                            	
                            	
                            	if(fileName.endsWith(".html") || 
                            		fileName.endsWith(".htm")){
                            		outputHtml.setText(fileName);
                            	}
                            else{
                            	Alert.alert(MessageType.ERROR, "Please select image", window);
                            }
                            }
                        } 
                        
                        
                        
                    }
                });
            }
        });
        
        
        generateButton.getButtonPressListeners().add(new ButtonPressListener() {
			
			@Override
			public void buttonPressed(Button arg0) {
			    
			    if(outputHtml.getText() == null || outputHtml.getText().length() == 0 ){
			        Alert.alert(MessageType.ERROR, "Please insert output location.", window);
			        return;
			    }
				HtmlImageMaker.generate(inputImage.getText(), 
						outputHtml.getText(), innerText.getText(), foregroundCB.isSelected());
				
				Alert.alert(MessageType.INFO, "HTML file generated: " + outputHtml.getText(), window);
				
			}
		});
        
        window.open(display);
    }

    @Override
    public boolean shutdown(boolean optional) {
        if (window != null) {
            window.close();
        }

        return false;
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }

    public static void main(String[] args) {
        DesktopApplicationContext.main(MainWindow.class, args);
    }
}

