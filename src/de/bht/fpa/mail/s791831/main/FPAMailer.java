/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Lena
 */
public class FPAMailer extends Application {
        
    
    @Override
    public void start(Stage stage) throws Exception {
        
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        
        Parent root = FXMLLoader.load(getClass().getResource("/de/bht/fpa/mail/s791831/view/FXMLMainView.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("FPA Mailer");
        stage.show();
    }
   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
