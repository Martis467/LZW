package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MainUI implements Initializable {

    // Inputs
    @FXML
    private TextField dictionarySize;
    @FXML
    private ComboBox dictionaryTypeSelector;
    @FXML
    private TabPane rootPane;
    @FXML
    private TextField compressorFilePathText;
    @FXML
    private TextField decompressorFilePathText;

    // Outputs for compression
    @FXML
    private Text compressorFile;
    @FXML
    private Text compressorEncoded;
    @FXML
    private Text compressorComparison;

    // Outputs for decompression
    @FXML
    private Text decompressorFile;
    @FXML
    private Text decompressorDecoded;
    @FXML
    private Text decompressorComparison;

    private URL compressionFilePathUrl;
    private URL decompressionFilePathUrl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideFields();
        dictionarySize.setText("");
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Drop",
                        "Keep going"
                );
        dictionaryTypeSelector.setItems(options);
    }


    public void selectFileForCompression(ActionEvent actionEvent) {
        try {
            File selectedFile = choosePath();
            compressorFile.setText(String.valueOf(selectedFile.length()));
            compressorFile.setVisible(true);
            compressionFilePathUrl = Paths.get(selectedFile.toURI()).toUri().toURL();
            compressorFilePathText.setText(compressionFilePathUrl.getPath().substring(1).replace("%20", " "));
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (Exception e)
        {

        }
    }

    public void compressFile(ActionEvent actionEvent) {
        if(compressorFilePathText.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a file to compress");
            alert.showAndWait();
            return;
        }
    }

    public void selectFileForDecompression(ActionEvent actionEvent) {
        try {
            File selectedFile = choosePath();
            decompressorFile.setVisible(true);
            decompressorFile.setText(String.valueOf(selectedFile.length()));
            decompressionFilePathUrl = Paths.get(selectedFile.toURI()).toUri().toURL();
            decompressorFilePathText.setText(decompressionFilePathUrl.getPath().substring(1).replace("%20", " "));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (Exception e)
        {

        }
    }

    public void decompressFile(ActionEvent actionEvent) {
        if(decompressorFilePathText.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a file to decompress");
            alert.showAndWait();
            return;
        }
    }

    private File choosePath()
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose file");
        File defaultDir = new File(System.getProperty("user.dir"));
        fc.setInitialDirectory(defaultDir);
        File selectedFile = fc.showOpenDialog(rootPane.getScene().getWindow());
        System.out.println(selectedFile.getPath());
        return selectedFile;
    }


    private void hideFields() {
        compressorFile.setVisible(false);
        compressorEncoded.setVisible(false);
        compressorComparison.setVisible(false);

        decompressorFile.setVisible(false);
        decompressorDecoded.setVisible(false);
        decompressorComparison.setVisible(false);
    }
}
