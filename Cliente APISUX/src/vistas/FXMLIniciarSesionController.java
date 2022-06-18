/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import pojo.Token;
import pojo.User;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLIniciarSesionController implements Initializable {

    @FXML
    private PasswordField pfContrasenia;
    @FXML
    private TextField tfUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicIngresar(ActionEvent event) throws IOException, ParseException {
        if(tfUsuario.getText().isEmpty() || pfContrasenia.getText().isEmpty()){
            mostrarAlerta("Error", "Es necesario escribir tu usuario y contraseña", Alert.AlertType.ERROR);
        }else{
            User user = new User();
            user.setUsername(tfUsuario.getText());
            user.setPassword(pfContrasenia.getText());
            String       postUrl       = "http://127.0.0.1:9090/token";// put in your url
            Gson         gson          = new Gson();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost     post          = new HttpPost(postUrl);
            StringEntity postingString = new StringEntity(gson.toJson(user));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpContext responseHandler = null;
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity);
            Token token = gson.fromJson(str, Token.class);
            System.out.println(token.getAccess_token());
            if (response.getCode() == 200) {
                try {
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMenuPrincipalAdmin.fxml"));
                  Parent root = loader.load();
                  FXMLMenuPrincipalAdminController controladorForm = loader.getController();
                  controladorForm.setAccessToken(token.getAccess_token());
                  Scene escenaFormulario = new Scene(root);
                  Stage escenario = new Stage();
                  escenario.setScene(escenaFormulario);
                  escenario.initModality(Modality.APPLICATION_MODAL);
                  escenario.showAndWait();
              } catch (IOException ex) {
                  Logger.getLogger(FXMLMenuPrincipalAdminController.class.getName()).log(Level.SEVERE, null, ex);
              }
            } else {
                mostrarAlerta("Error", "Usuario y/o contraseña incorrectos", Alert.AlertType.ERROR);                 
            }
        }
    }

    @FXML
    private void clicCerrar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) pfContrasenia.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
