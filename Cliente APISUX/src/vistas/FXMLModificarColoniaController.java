/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import pojo.Colonia;
import pojo.Token;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLModificarColoniaController implements Initializable {

    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<Colonia> cbColonias;
    
    private String token;
    
    private ObservableList<Colonia> colonias;
    
    private Colonia coloniaSeleccionada;
    
    private String idColoniaEdicion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colonias = FXCollections.observableArrayList();
        try {
            // TODO
            cargarColonias();
        } catch (IOException ex) {
            mostrarAlerta("No existe conexion con el servidor", "Por el momento no se logro establecer la conexion, intente más tarde", Alert.AlertType.ERROR);
            Logger.getLogger(FXMLConsultaTuRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    public void setAccessToken(String token) {
        this.token = token;
    }
    
    private void cargarColonias() throws IOException {
        try {
            int responseCode = 0;
            int n = 1;
            while (responseCode != 404) {
                try {
                    String urlArm = "http://127.0.0.1:9090/colonias/" + Integer.toString(n);
                    URL url = new URL(urlArm);
                    HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                    String inputLine = in.readLine();
                    responseCode = conexion.getResponseCode();
                    System.out.println(responseCode);
                    ObjectMapper mapper = new ObjectMapper();
                    Colonia colonia = mapper.readValue(inputLine, Colonia.class);
                    colonias.add(colonia);
                    n++;
                } catch (java.io.FileNotFoundException e) {
                    responseCode = 404;
                    break;
                }
            }
            cbColonias.setItems(colonias);
            cbColonias.valueProperty().addListener(new ChangeListener<Colonia>() {
                @Override
                public void changed(ObservableValue<? extends Colonia> observable, Colonia oldValue, Colonia newValue) {
                    if (newValue != null) {
                        coloniaSeleccionada = newValue;
                        cargarColoniaSeleccionada(newValue);
                        idColoniaEdicion = newValue.getId();
                    }
                }
            });
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cargarColoniaSeleccionada(Colonia colonia) {
        tfNombre.setText(colonia.getNombre());
        tfCodigoPostal.setText(Integer.toString(colonia.getCodigoPostal()));
    }

    @FXML
    private void clicCerrar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicGuardar(ActionEvent event) throws IOException, ParseException {
        if(tfNombre.getText().isEmpty() || tfCodigoPostal.getText().isEmpty()){
            mostrarAlerta("Existen campos vacios", "Hay campos vacios necesarios, verificar la información", Alert.AlertType.ERROR);
        }else{
            Colonia colonia = new Colonia();
            colonia.setNombre(tfNombre.getText());
            colonia.setCodigoPostal(parseInt(tfCodigoPostal.getText()));
            String       patchUrl       = "http://127.0.0.1:9090/colonias/" + idColoniaEdicion;// put in your url
            Gson         gson          = new Gson();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPatch     patch          = new HttpPatch(patchUrl);
            patch.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
            patch.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            StringEntity postingString = new StringEntity(gson.toJson(colonia));
            patch.setEntity(postingString);
            patch.setHeader("Content-type", "application/json");
            HttpContext responseHandler = null;
            CloseableHttpResponse response = httpClient.execute(patch);
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity);
            if (response.getCode() == 200) {
                mostrarAlerta("Colonia editada", "La colonia ha sido editada", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                mostrarAlerta("Error", "No se pudo procesar la solicitud", Alert.AlertType.ERROR);                 
            }
            }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(mensaje);
        a.showAndWait();
    }
    
}
