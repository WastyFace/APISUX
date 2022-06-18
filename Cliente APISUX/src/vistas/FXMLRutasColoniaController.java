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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pojo.Colonia;
import pojo.Ruta;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLRutasColoniaController implements Initializable {

    @FXML
    private ComboBox<Colonia> cbColonias;
    @FXML
    private TextField tfCodigoPostal;
    
    private ObservableList<Colonia> colonias;
    
    private Colonia coloniaSeleccionada;
    @FXML
    private TableColumn tcRutas;
    
    private ObservableList<String> cadenaColonias;
    @FXML
    private TableView<Ruta> tvRutas;
    
    private ObservableList<Ruta> rutas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colonias = FXCollections.observableArrayList();
        rutas = FXCollections.observableArrayList();
        tcRutas.setCellValueFactory(new PropertyValueFactory("nombre"));
        cadenaColonias = FXCollections.observableArrayList();
        try {
            // TODO
            cargarColonias();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConsultaTuRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                        tvRutas.getItems().clear();
                        coloniaSeleccionada = newValue;
                        cargarColoniaSeleccionada(newValue);
                        try {
                            cargarRutas(newValue);
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLRutasColoniaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
        tfCodigoPostal.setText(Integer.toString(colonia.getCodigoPostal()));
    }
    
    private void cargarRutas(Colonia coloniaCargada) throws MalformedURLException, IOException {
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
                }
                if (cadenaColonias.contains(coloniaCargada.getNombre())) {
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

    @FXML
    private void clicCerrar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) cbColonias.getScene().getWindow();
        stage.close();
    }    
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
