/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsnap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Vasilis
 */
public class MainFrame extends JFrame implements NativeKeyListener{

    //Properties

    private static final int WIDTH = 1250; //width of the frame
    private static final int HEIGHT = 800; //height of the frame
    private static final String title = "JSnap"; //title of the frame
    /*
    The size of the actual screen in which the application in going to
    take screenshots.
    */
    private static Dimension screenRatio;

    /*
    All the components needed for the GUI.
    */

    private JPanel imagePanel;
    private JPanel buttonPanel;
    private JPanel panel;
    private JPanel thumbPanel;
    private JLabel infoLabel;
    private JTextField infoField;
    private JLabel directoryLabel;
    private JTextField directoryField;
    private JLabel thumbLabel;
    private JButton renameButton;
    private JButton saveButton;
    private JButton saveAllButton;
    private JButton deleteButton;
    private JButton setDirectoryButton;

    //Reference to the MainFrame object itself, so that it can be used as an
    //argument in the FrameListener inner class.
    private MainFrame mainFrame;

    private ArrayList<JButton> thumbNailButtons; //List of buttons with screenshots.
    private JButton button; //The button that will be added every time a new screenshot is taken.
    private ArrayList<JLabel> thumbNailLabels; //List of labels with screenshots.
    private JLabel label; //The label that will be added every time.
    private JLabel imageLabel; //The label tha will display in the imagePanel.

    private ButtonListener buttonListener;
    private FrameListener frameListener;
    
    /*
    All objects needed for the manipulation of images.
    */
    private ImageHandler imageHandler;
    private ImageCapture imageCapture;

    BufferedImage capturedImage = null;
    BufferedImage ratioImage = null;

    //Contructor
    public MainFrame(){

        //Creates a new event dispatcher for the swing components.
        GlobalScreen.setEventDispatcher(new SwingDispatchService());
        
        //Setting the reference to the MainFrame object and adding the FrameListener.
        frameListener = new FrameListener();
        this.addWindowListener(frameListener);
        mainFrame = this;

        //Get the dimensions of the actual screen.
        screenRatio = getScreenRatio();

        //Initialize the image handler where all screenshots will be saved.
        imageHandler = new ImageHandler();

        //Initialize all of Swing components in our application.
        initPanels();
        initComponents();
        initThumbPanel();

        //Add the GUI components to the panel.
        addComponentsToPanel(buttonPanel);

        //Add the thumbnail panel and the main panel to the frame.
        JScrollPane pane = new JScrollPane(thumbPanel);
        pane.setSize(new Dimension(10,screenRatio.height));
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        this.add(pane, BorderLayout.WEST);
        this.add(panel);

        this.setVisible(true);
        this.setTitle(title);
        this.setSize(screenRatio.width, screenRatio.height);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*
    This method initializes all the components needed for
    the GUI in the application.
    */
    public void initComponents(){

        infoLabel = new JLabel("Image name:          ");

        infoField = new JTextField(30);
        infoField.setEditable(false);

        directoryLabel = new JLabel("Current directory: ");

        directoryField = new JTextField(30);
        directoryField.setEditable(false);
        directoryField.setText(imageHandler.getDestinationFileString());

        thumbLabel = new JLabel("  Thumbnails");
        thumbLabel.setToolTipText("A list with thumbnails of all screenshots."
                + " Click on a thumbnail to view the screenshot.");

        buttonListener = new ButtonListener();

        renameButton = new JButton("Rename image");
        renameButton.setEnabled(false);
        renameButton.addActionListener(buttonListener);

        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(buttonListener);

        saveAllButton = new JButton("Save All");
        saveAllButton.setEnabled(false);
        saveAllButton.addActionListener(buttonListener);

        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(buttonListener);

        setDirectoryButton = new JButton("Set Directory");
        setDirectoryButton.addActionListener(buttonListener);
    }

    //Method that sets the background color of all buttons used in MainFrame class.
    public void setColorToButtons(Color color){

        renameButton.setBackground(color);
        saveButton.setBackground(color);
        saveAllButton.setBackground(color);
        deleteButton.setBackground(color);
        setDirectoryButton.setBackground(color);

    }

    /*
    It instantiates an object of class JToolBar and it provides it with
    the appropriate format.
    */
    public void initThumbPanel(){

        thumbPanel = new JPanel();
        thumbPanel.setPreferredSize(new Dimension(220, HEIGHT));
        thumbPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        thumbPanel.add(thumbLabel);

        thumbNailButtons = new ArrayList<JButton>();

        thumbNailLabels = new ArrayList<JLabel>();
    }


    /*
    This method initializes all the panels that are to be used within the main
    panel of the frame and it provides them with the appropriate format.
    */
    public void initPanels(){

        imagePanel = new JPanel();
        imagePanel.setLayout(null);
        imagePanel.setPreferredSize(new Dimension(screenRatio.width/2,screenRatio.height/2));
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imagePanel.setBackground(new Color(169,169,169));

        label = new JLabel();

        //imageLabel = new JLabel();

        imagePanel.add(label);

        Border etchedBorder;
        etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(WIDTH-500,180));
        buttonPanel.setBorder(etchedBorder);

        panel = new JPanel();
        panel.setBackground(new Color(220,220,220));

        panel.add(imagePanel, BorderLayout.CENTER);
        panel.add(Box.createRigidArea(new Dimension(WIDTH,20)));
        panel.add(buttonPanel, BorderLayout.CENTER);
    }

    /*
    It adds all the Swing components that have been created for the GUI of this
    application into the panel taken as parameter.
    */

    public void addComponentsToPanel(JPanel panel){

        panel.add(Box.createRigidArea(new Dimension(WIDTH-200,20)));

        panel.add(infoLabel);
        panel.add(infoField);

        panel.add(Box.createRigidArea(new Dimension(WIDTH-250,10)));

        panel.add(directoryLabel);
        panel.add(directoryField);

        panel.add(Box.createRigidArea(new Dimension(WIDTH,35)));

        panel.add(renameButton);
        panel.add(saveButton);
        panel.add(saveAllButton);
        panel.add(deleteButton);
        panel.add(setDirectoryButton);
    }

    /*
    Method for getting the dimensions of the actual screen.
    */
    public Dimension getScreenRatio(){

        Dimension dim1, dim2;
        dim1= Toolkit.getDefaultToolkit().getScreenSize();

        dim2 = new Dimension(dim1.width,dim1.height);

        return dim2;
    }

    /*
    Method for updating control buttons (enabling or disabling) according to
    the number of screenshots that are stored.
    */

    public void updateControlButtons(){

        if(imageHandler.getTotalImages() == 0){

            if(renameButton.isEnabled() && saveButton.isEnabled() && saveAllButton.isEnabled()){

                renameButton.setEnabled(false);
                saveButton.setEnabled(false);
                saveAllButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        }
        else {
            if(!renameButton.isEnabled() && !saveButton.isEnabled() && !saveAllButton.isEnabled()){

                renameButton.setEnabled(true);
                saveButton.setEnabled(true);
                saveAllButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        }
    }

    /*
    Method for updating the control panel and the imagePanel component with the appropriate
    image information.
    */
    public void updatePanelInfo(int imageIndex){

        if(imageIndex == -1){
            infoField.setText("");
            label.setIcon(null);
        }
        else{
            if(imageHandler.getTotalImages() != 0){
                infoField.setText(imageHandler.getImageCaptures().get(imageIndex % imageHandler.getTotalImages()).getFileName());
                label.setIcon(imageHandler.getImageCaptures().get(imageIndex % imageHandler.getTotalImages()).getRatioIcon());
            }
            else{
                infoField.setText("");
                label.setIcon(null);
            }
        }
    }

    //Method of getting the infoField component of the main frame.
    public JTextField getInfoField(){
        return this.infoField;
    }

    //Method of returning the instance of the main frame.
    public MainFrame getMainFrame(){
        return this;
    }

    /*
    Unimplemented methods of the NativeKeyListener interface that will listen to
    native keyboard events globally.
    */

    //No need to implement this method.
    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {}

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {

        if(nativeEvent.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN){

            /*
            Creates an instance of class ImageCapture and captures a screenshot.
            */

            imageCapture = new ImageCapture();

            imageCapture.captureScreenshot();
            imageCapture.createThumbNail(imageCapture.getScreenshotImage(), imageCapture.getScale());
            imageCapture.createImageIcon(imageCapture.getScreenshotImage());
            imageCapture.createRatioIcon(imageCapture.getRatioImage());

            //Adding the screenshot to list with screenshots.
            imageHandler.addScreenshotImage(imageCapture);

            /*
            Adjusts the size of the label that will contain the image and
            then it sets the icon of the image to be the newly created thumbnail.

            Finally, it adds the label to the list of labels containing all
            captured screenshots.
            */
            label.setSize(new Dimension(imageCapture.getRatioImage().getWidth(), imageCapture.getRatioImage().getHeight()));
            label.setIcon(imageCapture.getRatioIcon());

            thumbNailLabels.add(label);

            /*
            Creates a new button that will be placed in the thumbnail panel.
            */
            button = new JButton();
            button.addActionListener(buttonListener);

            thumbNailButtons.add(button);
            //System.out.println("Button " + (thumbNailButtons.size()-1) + " added to thumbPanel.");
            /*
            Solved the dynamic addition of Swing components. Also adding
            the image icon to the button and consequently the button to the list.

            Moreover, it checks whether the size of thumbPanel is big enough
            to view all the buttons. If not, it increases the size of it.
            */
            thumbPanel.add(thumbNailButtons.get(thumbNailButtons.size()-1));

            button.setPreferredSize(new Dimension(screenRatio.width/10, screenRatio.height/10));
            button.setIcon(imageCapture.getThumbnail());

            if(thumbNailButtons.size() >= 7){
                thumbPanel.setPreferredSize(new Dimension(220, (thumbNailButtons.size() + 1) * getScreenRatio().height / 10));
            }

            //Printing the name of the newly created image in the infoField.
            infoField.setText(imageCapture.getFileName());

            //Enable or disable the control buttons depending on whether there
            //images or not.
            updateControlButtons();

            //Returns the main frame of the application to its visible state.
        }
    }

    //No need to implement this method.
    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {}

    
    /*
    Inner class of the MainFrame class in order to handle button events.
    */
    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            //If user clicks the "rename" button.
            if(event.getSource() == renameButton){
                new RenameFrame(imageHandler.getImageCaptures().get(imageHandler.getCurrentIndex()), getMainFrame());
            }
            /////////////////////////////////////

            //If user clicks the "save" button.
            if(event.getSource() == saveButton){
                if(!imageHandler.saveImage(imageHandler.getImageCaptures().get(imageHandler.getCurrentIndex()))){
                    JOptionPane.showMessageDialog(mainFrame,"File already exists or missing destination directory. Please provide a different name or set a directory.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else JOptionPane.showMessageDialog(mainFrame,"Image saved.", "Update", JOptionPane.INFORMATION_MESSAGE);
            }
            /////////////////////////////////////

            //If user clicks the "save all" button.
            if(event.getSource() == saveAllButton){
                imageHandler.saveAllImages();
                JOptionPane.showMessageDialog(mainFrame,"Images saved.", "Update", JOptionPane.INFORMATION_MESSAGE);
            }
            /////////////////////////////////////

            //If user clicks the "delete" button.
            if(event.getSource() == deleteButton){

                int index = imageHandler.getCurrentIndex();

                imageHandler.removeScreenshotImage(imageHandler.getImageCaptures().get(imageHandler.getCurrentIndex()));

                System.out.println("Index variable value: " + index);

                if(index == -1){

                    thumbPanel.getComponent(index+2).setVisible(false);
                    thumbPanel.remove(index+2);
                    thumbNailLabels.remove(index+1);
                    thumbNailButtons.remove(index+1);
                }

                else{

                    thumbPanel.getComponent(index+1).setVisible(false);
                    thumbPanel.remove(index+1);
                    thumbNailLabels.remove(index);
                    thumbNailButtons.remove(index);
                }

                thumbPanel.validate();

                updatePanelInfo(index);

            }
            ////////////////////////////////////////////

            //If user clicks the "set directory" button.
            if(event.getSource() == setDirectoryButton){

                JOptionPane.showMessageDialog(mainFrame,
                        "Changing directory will only apply on images that are not saved.",
                        "Warning",JOptionPane.WARNING_MESSAGE);

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int selectedItem = fileChooser.showOpenDialog(mainFrame);

                if(selectedItem == JFileChooser.APPROVE_OPTION){

                    imageHandler.setDestinationFile(fileChooser.getSelectedFile());
                    imageHandler.setDirectoryDefined(true);
                    directoryField.setText(imageHandler.getDestinationFileString());
                }
            }

            /*
            We check whether one of the dynamically created buttons is pressed
            so that we change the image on the imagePanel.
            */
            for(int i=0; i<thumbNailButtons.size(); i++){

                JButton tempButton = thumbNailButtons.get(i);
                if(event.getSource() == tempButton){
                    //System.out.println("Button " + i + " pressed.");
                    imageHandler.setCurrentIndex(i);
                    updatePanelInfo(i);
                    break;
                }
            }

            updateControlButtons();

            thumbPanel.revalidate();
            imagePanel.validate();

        }

    }


    /*
    Inner class of the MainFrame in order to capture events when the user
    tries to exit the program without saving all the pictures.
    */

    public class FrameListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent event) {

            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Could not create native keyboard hook.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            GlobalScreen.addNativeKeyListener(mainFrame);

        }

        @Override
        public void windowClosing(WindowEvent event) {

            for(ImageCapture capture : imageHandler.getImageCaptures()){
                if(!capture.isSaved()){
                    mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    new NotifyFrame(imageHandler, mainFrame);
                    break;
                }
            }
        }

        @Override
        public void windowClosed(WindowEvent event) {

            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.runFinalization();
            System.exit(0);
        }

        @Override
        public void windowIconified(WindowEvent event) {}

        @Override
        public void windowDeiconified(WindowEvent event) {}

        @Override
        public void windowActivated(WindowEvent event) {}

        @Override
        public void windowDeactivated(WindowEvent event) {}

    }

}
