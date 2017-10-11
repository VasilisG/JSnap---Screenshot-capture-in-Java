/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsnap;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Vasilis
 */
public class NotifyFrame extends JFrame {

    //Attributes and components

    private JPanel panel;
    private JLabel label;
    private JButton saveButton;
    private JButton discardButton;
    private JButton cancelButton;

    private ButtonListener listener;

    private ImageHandler imageHandler;
    private MainFrame mainFrame;

    //Constructor

    public NotifyFrame(ImageHandler inputHandler, MainFrame inputFrame) {

        imageHandler = inputHandler;
        mainFrame = inputFrame;

        initComponents();
        addComponentsToPanel();
        addListenerToButtons();

        this.setContentPane(panel);

        this.setTitle("Unsaved images");
        this.setSize(400,150);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Method that initializes all components contained in the frame.
    public void initComponents(){

        panel = new JPanel();

        label = new JLabel("There are unsaved images in the application.");

        saveButton = new JButton("Save images");
        discardButton = new JButton("Discard images");
        cancelButton = new JButton("Cancel");
    }

    //Method that adds all components to main panel
    public void addComponentsToPanel(){

        panel.add(Box.createRigidArea(new Dimension(300,20)));
        panel.add(label);
        //panel.add(Box.createHorizontalStrut(300));
        panel.add(Box.createRigidArea(new Dimension(300,30)));
        panel.add(saveButton);
        panel.add(discardButton);
        panel.add(cancelButton);
    }

    //Method that initializes the ButtonListener object and adds it to each
    //component.
    public void addListenerToButtons(){

        listener = new ButtonListener();

        saveButton.addActionListener(listener);
        discardButton.addActionListener(listener);
        cancelButton.addActionListener(listener);
    }

    //Inner class of the NotifyFrame class that listens to events triggered by
    //buttons and produces actions according to them.
    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            if(event.getSource() == saveButton){
                imageHandler.saveAllImages();
            }

            if(event.getSource() == discardButton){
                dispose();
                mainFrame.dispose();
            }

            if(event.getSource() == cancelButton){
                dispose();
            }
        }

    }

}
