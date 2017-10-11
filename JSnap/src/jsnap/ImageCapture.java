/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsnap;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Vasilis
 */
public class ImageCapture{

    //Properties
    private static int totalScreenshots = 0;

    private static final double SCALE = 0.50d; //The scaling factor for the image
                                               //to be drawn in the panel.
    private static final double THUMBNAIL_SCALE = 0.10d; //The scaling factor of
                                                         // the thumbnail.
    private static final String fileExtension = "png";

    private boolean saved; //Variable to check whether a screenshot has been saved or not.
    private String fileName; //The name of the file containing the screenshot.

    private BufferedImage screenshot; //The image to be captured and saved.
    private BufferedImage ratioImage; //The scaled image to be displayed.
    private ImageIcon ratioIcon; //The icon that will be placed in the label to be shown.
    private ImageIcon thumbnail; //The thumbnail to be added in the toolbar.

    //Constructor

    public ImageCapture(){

        totalScreenshots++;

        this.saved = false;
        this.fileName = "JSnap_screenshot_" + totalScreenshots;
    }

    //Methods

    public void setSaved(boolean saved){
        this.saved = saved;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public boolean isSaved(){
        return this.saved;
    }

    public String getFileName(){
        return this.fileName;
    }

    public String getFileExtension(){
        return this.fileExtension;
    }

    public BufferedImage getScreenshotImage(){
        return this.screenshot;
    }

    public BufferedImage getRatioImage(){
        return this.ratioImage;
    }

    public void setRatioImage(BufferedImage ratioImage){
        this.ratioImage = ratioImage;
    }

    public ImageIcon getRatioIcon(){
        return this.ratioIcon;
    }

    public void setRatioIcon(ImageIcon ratioIcon){
        this.ratioIcon = ratioIcon;
    }

    public ImageIcon getThumbnail(){
        return this.thumbnail;
    }

    public void setThumbnail(ImageIcon thumbnail){
        this.thumbnail = thumbnail;
    }

    public double getScale(){
        return this.SCALE;
    }

    public double getThumbNailScale(){
        return this.THUMBNAIL_SCALE;
    }

    public void captureScreenshot(){
        try {
            screenshot = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException ex) {
            Logger.getLogger(ImageCapture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


       /*
       First, we create the matrix transformation that we want to apply to our screenshot,
       which is scaling by the value determined by the "scale" parameter.

       After we create the tranformation, we create the new buffered image that we wish
       to apply the transformation and we use the filter method of the AffineTransformationOp
       class to apply the scaling.

       After the operation, we simply return the newly created image.
       */

    public BufferedImage createThumbNail(BufferedImage screenshot, double scale){

       int width = screenshot.getWidth();
       int height = screenshot.getHeight();

       ratioImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

       AffineTransform affineTransform = new AffineTransform();
       affineTransform.scale(scale, scale);

       AffineTransformOp scaleOperation = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);

       ratioImage = scaleOperation.filter(screenshot, ratioImage);

       return ratioImage;
    }

    /*
    Method that creates that takes as input a BufferedImage object and creates
    the corresponding ImageIcon object to be used in thumbnail buttons.
    */

    public ImageIcon createImageIcon(BufferedImage inputImage){

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        Image tempImage = inputImage.getScaledInstance(dim.width/10, dim.height/10, Image.SCALE_SMOOTH);

        thumbnail = new ImageIcon(tempImage);

        return thumbnail;
    }

    /*
    Method that takes as input the ratio image which is an instance of a
    BufferedImage object and creates an object of ImageIcon class.
    */

    public ImageIcon createRatioIcon(BufferedImage inputImage){

        ImageIcon icon = new ImageIcon(inputImage);
        ratioIcon = icon;

        return ratioIcon;
    }
}
