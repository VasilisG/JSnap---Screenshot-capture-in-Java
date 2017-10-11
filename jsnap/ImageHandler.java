 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsnap;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Vasilis
 */
public class ImageHandler {

    private ArrayList<ImageCapture> imageCaptures; //List of all captured images.
    private File destinationFile; //The file in which all the images are going to be saved.
    private boolean isDirectoryDefined; //Flag variable that checks whether the user
                                    //has defined a directory for the images to be saved.
    private int currentIndex; //The index of the current screenshot displayed.
    
    private BufferedReader bReader;
    private File iniFile;
    private FileReader fReader;

    //Constructor

    public ImageHandler(){

        //Initialization code
        imageCaptures = new ArrayList<ImageCapture>();
        destinationFile = null;
        isDirectoryDefined = false;
        currentIndex = 0;     

        //Gets the path that we are going to create the destination folder
        
        /*String userHomeDir = System.getProperty("user.home");
        String pathName = userHomeDir + "\\Desktop\\JSnap_screenshots";
        Path path = Paths.get(pathName);*/

        /*
        Attempt to create the destination folder and attach its pathName to
        a File object. Upon creation, console will produce successful message.
        */
       /*try {
            if(!Files.exists(path)){
                Files.createDirectory(path);
                 System.out.println("Directory created");
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }*/

       destinationFile = new File("");

    }


    //Methods

    public int getTotalImages(){
        return this.imageCaptures.size();
    }

    public int getCurrentIndex(){
        return this.currentIndex;
    }

    public void setCurrentIndex(int currentIndex){
        this.currentIndex = currentIndex;
    }

    public ArrayList<ImageCapture> getImageCaptures(){
        return this.imageCaptures;
    }

    public File getDestinationFile() {
       return this.destinationFile;
    }

    public String getDestinationFileString() {
        
        if(!destinationFile.equals(null)){
            return this.destinationFile.toString();
        }
        else return "";
    }

    public void setDestinationFile(File destinationFile){
        this.destinationFile = destinationFile;
    }
    
    public void setDirectoryDefined(boolean isDirectoryDefined){
        this.isDirectoryDefined = isDirectoryDefined;
    }
    
    public boolean getDirectoryDefined(){
        return this.isDirectoryDefined;
    }

    public void addScreenshotImage(ImageCapture screenshotImage){
        imageCaptures.add(screenshotImage);
        currentIndex = imageCaptures.size()-1;
    }
    

    public void removeScreenshotImage(ImageCapture screenshotImage){

        System.out.println("Current index: " + currentIndex);

        imageCaptures.remove(screenshotImage);

        if(currentIndex >= (this.getImageCaptures().size()-1)){
            currentIndex = this.getImageCaptures().size() - 1;
        }

        if(this.getImageCaptures().size() == 1) currentIndex = 0;

        System.out.println("Current index after removal: " + currentIndex);

    }

    //Method used for debugging and prints the names of all screenshots taken.
    public void printScreenshotFilenames(){

        for(ImageCapture imageCapture : imageCaptures){
            System.out.println(imageCapture.getFileName());
        }
    }

    //Saves image to the specified destination file.
    public boolean saveImage(ImageCapture imageCapture){

        //NEED TO ADD AN IF STATEMENT TO CHECK WHETHER THE FILE HAS ALREADY BEEN SAVED. I ALSO NEED TO CHECK WHETHER THE DIRECTORY EXISTS.
        if(!imageCapture.isSaved() && getDirectoryDefined()){
            
            File imageFile = new File(destinationFile + "\\" + imageCapture.getFileName() + "." + imageCapture.getFileExtension());
            Path tempPath = Paths.get(imageFile.toURI());
            int copyCounter = 1;
            int counter = 0; //In case 'while' loop does not work properly.

        //Check if directory path already exists. If it's true, then it change the name of the file by adding an index in the end.
            while(Files.exists(tempPath) /*&& counter < 10*/){

                imageCapture.setFileName(imageCapture.getFileName()+ "(" + copyCounter + ")");
                imageFile = new File(destinationFile + "\\" + imageCapture.getFileName() + "." + imageCapture.getFileExtension());
                tempPath = Paths.get(imageFile.toURI());
                copyCounter++;
                counter++;
            }

            try {
                if(!imageCapture.isSaved()){
                    ImageIO.write(imageCapture.getScreenshotImage(), imageCapture.getFileExtension(), imageFile);
                }
            } catch (IOException ex) {
                Logger.getLogger(ImageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            imageCapture.setSaved(true);
            //System.out.println("Image " + imageCapture.getFileName() + " saved to directory: " + imageFile.toString());
            return true;
        }
        else return false;
    }

    //Saves all images to the specified destination file
    public void saveAllImages(){

        for(ImageCapture capture : imageCaptures){
            saveImage(capture);
        }
    }
}
