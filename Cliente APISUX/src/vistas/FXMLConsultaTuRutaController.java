/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pojo.Colonia;
import pojo.Ruta;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLConsultaTuRutaController implements Initializable {

    @FXML
    private ComboBox<Colonia> cbColonias;
    @FXML
    private ComboBox<Colonia> cbColonias2;
    
    private ObservableList<Colonia> colonias;
    @FXML
    private TableColumn tcRutas;
    
    private ObservableList<String> cadenaColonias;
    
    private ObservableList<Ruta> rutas;
    @FXML
    private TableView<Ruta> tvRutas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colonias = FXCollections.observableArrayList();
        rutas = FXCollections.observableArrayList();
        tcRutas.setCellValueFactory(new PropertyValueFactory("nombre"));
        cadenaColonias = FXCollections.observableArrayList();
        try {
            // TODO
            cargarColonias();
        } catch (IOException ex) {
            mostrarAlerta("No existe conexion con el servidor", "Por el momento no se logro establecer la conexion, intente más tarde", Alert.AlertType.ERROR);
            Logger.getLogger(FXMLConsultaTuRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void cargarColonias() throws IOException {
        try {
            //TODO
            for (int i = 0;i<77;i++) {
                String n = Integer.toString(i+1);
                String urlArm = "http://127.0.0.1:9090/colonias/" + n;
                URL url = new URL(urlArm);
                URLConnection conexion = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String inputLine = in.readLine();
                ObjectMapper mapper = new ObjectMapper();
                Colonia colonia = mapper.readValue(inputLine, Colonia.class);
                colonias.add(colonia);
            }
            cbColonias.setItems(colonias);
            cbColonias2.setItems(colonias);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void clicCerrar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) cbColonias.getScene().getWindow();
        stage.close();
    } 

    @FXML
    private void clicConsultar(ActionEvent event) throws MalformedURLException, IOException {
        if (!cbColonias.getSelectionModel().isEmpty() && !cbColonias2.getSelectionModel().isEmpty()) {
            tvRutas.getItems().clear();
            Colonia coloniaOrigen = new Colonia();
            Colonia coloniaDestino = new Colonia();
            coloniaOrigen = cbColonias.getSelectionModel().getSelectedItem();
            coloniaDestino = cbColonias2.getSelectionModel().getSelectedItem();
            int responseCode = 0;
            int n = 1;
            while (responseCode != 404) {
                try {
                    String urlArm = "http://127.0.0.1:9090/rutas/" + Integer.toString(n);
                    URL url = new URL(urlArm);
                    HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                    String inputLine = in.readLine();
                    responseCode = conexion.getResponseCode();
                    System.out.println(responseCode);
                    ObjectMapper mapper = new ObjectMapper();
                    Ruta ruta = mapper.readValue(inputLine, Ruta.class);
                    String coloniasRuta = ruta.getColonias();
                    String strFinal = coloniasRuta.substring(1,coloniasRuta.length()-1);
                    String cols[] = strFinal.split(", ");
                    for (int i=0;i<cols.length;i++) {
                        Colonia aux = new Colonia();
                        aux.setNombre(cols[i]);
                        cadenaColonias.add(aux.getNombre());
                        System.out.println(cadenaColonias);
                    }
                    if (cadenaColonias.contains(coloniaOrigen.getNombre()) && cadenaColonias.contains(coloniaDestino.getNombre())) {
                        tvRutas.getItems().add(ruta);
                    }
                    cadenaColonias.clear();
                    n++;
                } catch (java.io.FileNotFoundException e) {
                    responseCode = 404;
                    break;
                }
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
