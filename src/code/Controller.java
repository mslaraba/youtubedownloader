package code;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Thread thread = new Thread();
    private Runnable r = null;
    private Process p;
    private BufferedReader reader;

    private final String youtube = "youtube-dl -ct ";
    private final String start_playlist = "--playlist-start=";
    private final String end_playlist = " --playlist-end=";
    private final String audioFormat = "--extract-audio --audio-format=mp3 ";
    private String directoryDownload = "";

    @FXML
    private TextArea link, directory, from, to, result;
    @FXML
    private RadioButton video, playlist, audio;
    @FXML
    private ToggleGroup groupRadio;
    @FXML
    private Button start, help, chooser;

    private boolean state = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // shadow Button
        shadow(start);
        shadow(help);
        shadow(chooser);

        groupRadio.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (video.isSelected()) {
                    from.setVisible(false);
                    to.setVisible(false);
                } else if (playlist.isSelected()) {
                    from.setVisible(true);
                    to.setVisible(true);
                } else if (audio.isSelected()) {
                    from.setVisible(false);
                    to.setVisible(false);
                }
            }
        });
        justNumber(from);
        justNumber(to);
    }

    public void start_download(ActionEvent event) throws IOException {
        if (!directory.getText().equals("") && state) {
            state = false;
            start.setText("Download ...");
            r = new Runnable() {
                public void run() {
                    if (!directory.getText().equals("")) {
                        System.out.println("start download...");
                        if (video.isSelected() == true) {
                            executeCommand(youtube + link.getText(), directoryDownload, result);
                        } else if (playlist.isSelected() == true) {
                            if (from.getText().equals("") && !to.equals("")) {
                                executeCommand(youtube + "--" + end_playlist + to.getText() + " " + link.getText(), directoryDownload, result);
                            } else if (to.getText().equals("") && !from.equals("")) {
                                executeCommand(youtube + start_playlist + from.getText() + " " + link.getText(), directoryDownload, result);
                            } else if (!to.equals("") && !from.equals("")) {
                                executeCommand(youtube + start_playlist + from.getText()
                                        + end_playlist + to.getText() + " " + link.getText(), directoryDownload, result);
                            }
                        } else if (audio.isSelected() == true) {
                            executeCommand(youtube + audioFormat + link.getText(), directoryDownload, result);
                        }
                        System.out.println("finish");
                        state = false;
                    }
                }
            };
            thread = new Thread(r);
            thread.start();
        } else {
            state = true;
            if (p != null) {
                reader.close();
                p.destroy();
            }
            thread.interrupt();
        }
    }

    public void help(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("how to use Youtube Downloader");
        alert.setHeaderText(null);
        alert.setContentText("It is a very easy software to download videos or playlists from youtube\n" +
                " just copy the link of the video or the playlist and paste it in the field  'your link here' and click start\n" +
                " If you want to change the download location just copy this folder to the desired location and follow the previous steps.");
        alert.setResizable(true);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("style/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        dialogPane.setMinSize(700, 180);
        alert.setResizable(false);
        alert.show();
    }

    public void choose(ActionEvent event) throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("design.fxml"));
        Scene scene = new Scene(pane, 700, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory =
                directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            directory.setText(selectedDirectory.getAbsolutePath());
            directoryDownload = selectedDirectory.getAbsolutePath();
        }
    }

    private void executeCommand(String command, String directory, TextArea result) {

        try {
            p = Runtime.getRuntime().exec(command, null, new File(directory));
            //p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.setText(line);
            }

        } catch (Exception e) {
            p.destroy();
            thread.interrupt();
        } finally {
        }
    }

    public void justNumber(TextArea text) {

        text.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    char ch = text.getText().charAt(oldValue.intValue());
                    // Check if the new character is the number or other's
                    if (!(ch >= '0' && ch <= '9')) {
                        // if it's not number then just setText to previous one
                        text.setText(text.getText().substring(0, text.getText().length() - 1));
                    }
                }
            }
        });
    }

    public void shadow(Button button) {
        DropShadow shadow = new DropShadow();
        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        button.setEffect(shadow);
                    }
                });
        button.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        button.setEffect(null);
                    }
                });
    }

    public void print(Object o) {
        System.out.println(o);
    }
}
