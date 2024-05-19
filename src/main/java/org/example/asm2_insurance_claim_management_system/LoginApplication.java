package org.example.asm2_insurance_claim_management_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.asm2_insurance_claim_management_system.SingletonHibernate.HibernateSingleton;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the SessionFactory when the application starts
        HibernateSingleton.getSessionFactory();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Insurance Claim System!");
        stage.setScene(scene);
        stage.show();

    }
    @Override
    public void stop() throws Exception {
        // Close the SessionFactory when the application shuts down
        HibernateSingleton.shutdown();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}