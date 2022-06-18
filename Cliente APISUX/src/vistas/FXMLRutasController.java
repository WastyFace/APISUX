/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pojo.Colonia;
import pojo.Ruta;

/**
 * FXML Controller class
 *
 * @author andrea
 */
public class FXMLRutasController implements Initializable {

    @FXML
    private ComboBox<Ruta> cbRutas;
    
    @FXML
    private TextArea taRecorrido;
    @FXML
    private ImageView ivImagenRuta;
    @FXML
    private TableView<Colonia> tvColonias;
    @FXML
    private TableColumn tvRutas;
    
    private ObservableList<Colonia> colonias;
    
    private ObservableList<Ruta> rutas;
    
    private Colonia coloniaSeleccionada;
    
    private String idColoniaEdicion;
    
    private Ruta rutaSeleccionada;
    
    private String idRutaSeleccion;
    
    private Path destinationPath;
    
    private Path sourcePath;
    
    private File selectedFile = null;
    
    private ObservableList<String> cadenaColonias;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colonias = FXCollections.observableArrayList();
        rutas = FXCollections.observableArrayList();
        tvRutas.setCellValueFactory(new PropertyValueFactory("nombre"));
        cadenaColonias = FXCollections.observableArrayList();
        try {
            // TODO
            cargarRutas();
        } catch (IOException ex) {
            mostrarAlerta("No existe conexion con el servidor", "Por el momento no se logro establecer la conexion, intente más tarde", Alert.AlertType.ERROR);
            Logger.getLogger(FXMLConsultaTuRutaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void cargarRutas() throws IOException {
        try {
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
                    rutas.add(ruta);
                    n++;
                } catch (java.io.FileNotFoundException e) {
                    responseCode = 404;
                    break;
                }
            }
            cbRutas.setItems(rutas);
            cbRutas.valueProperty().addListener(new ChangeListener<Ruta>() {
                @Override
                public void changed(ObservableValue<? extends Ruta> observable, Ruta oldValue, Ruta newValue) {
                    if (newValue != null) {
                        rutaSeleccionada = newValue;
                        cargarRutaSeleccionada(newValue);
                        idRutaSeleccion = newValue.getId();
                    }
                }
            });
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cargarRutaSeleccionada(Ruta ruta) {
        taRecorrido.setText(ruta.getRecorrido());
        File file = new File (rutaSeleccionada.getImgPath());
        Image image = new Image(file.toURI().toString());
        ivImagenRuta.setImage(image);
        String coloniasRuta = rutaSeleccionada.getColonias();
        String strFinal = coloniasRuta.substring(1,coloniasRuta.length()-1);
        String cols[] = strFinal.split(", ");
        for (int i=0;i<cols.length;i++) {
            Colonia aux = new Colonia();
            aux.setNombre(cols[i]);
            cadenaColonias.add(aux.getNombre());
            tvColonias.getItems().add(aux);
        }
    }

    @FXML
    private void clicCerrar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) taRecorrido.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
