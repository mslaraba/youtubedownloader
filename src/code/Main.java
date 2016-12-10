package code;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    private ProgressBar progress;
    private Button button;
    private Thread thread = new Thread();
    private Runnable r = null;
    private Process p;
    private BufferedReader reader;

    private final String youtube = "youtube-dl -ct ";
    private final String start = "--playlist-start=";
    private final String end = " --playlist-end=";
    private final String audioFormat = "--extract-audio --audio-format=mp3 ";
    private String directoryDownload = "";

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Youtube Downloader");
        primaryStage.getIcons().add(new Image("pictures/youtube.png"));
        /*primaryStage.setMaxHeight(500);
        primaryStage.setMaxWidth(700);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(5, 5, 5, 5));
        Scene scene = new Scene(grid, 700, 500);

        BackgroundImage i = new BackgroundImage(new Image("pictures/back.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT
                , null, null);
        grid.setBackground(new Background(i));
        grid.setPadding(new Insets(20, 20, 20, 20));


        TextField link = new TextField();
        link.setFont(Font.font("JF Flat", FontWeight.NORMAL, 20));
        link.setMinWidth(585);
        link.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        link.setFocusTraversable(false);
        link.setPromptText(" your link here");


        Button buttonHelp = new Button("help");
        buttonHelp.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        buttonHelp.setFont(Font.font("JF Flat", FontWeight.BOLD, 19));
        buttonHelp.setBackground(new Background(new BackgroundFill(Paint.valueOf("#66666666"), CornerRadii.EMPTY,
                new Insets(0, 0, 0, 0))));

        HBox hBox = new HBox(link, buttonHelp);


        TextField directory = new TextField();
        directory.setFont(Font.font("JF Flat", FontWeight.NORMAL, 20));
        directory.setMinWidth(585);
        directory.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        directory.setFocusTraversable(false);
        directory.setPromptText("choose directory");
        directory.setEditable(false);


        Button choose = new Button(" ... ");
        choose.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        choose.setFont(Font.font("JF Flat", FontWeight.BOLD, 19));
        choose.setBackground(new Background(new BackgroundFill(Paint.valueOf("#66666666"), CornerRadii.EMPTY,
                new Insets(0, 0, 0, 0))));

        choose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory =
                        directoryChooser.showDialog(primaryStage);

                if (selectedDirectory != null) {
                    directory.setText(selectedDirectory.getAbsolutePath());
                    directoryDownload = selectedDirectory.getAbsolutePath();
                }
            }
        });
        HBox hBoxDirectory = new HBox(directory, choose);


        ToggleGroup group = new ToggleGroup();

        RadioButton video = new RadioButton("video or full playlist");
        video.setFont(Font.font("JF Flat", FontWeight.NORMAL, 18));
        video.setToggleGroup(group);
        video.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        video.setSelected(true);
        RadioButton playlist = new RadioButton("custom playlist");
        playlist.setFont(Font.font("JF Flat", FontWeight.NORMAL, 18));
        playlist.setToggleGroup(group);
        playlist.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        TextArea from = new TextArea();
        from.setMaxHeight(10);
        from.setMaxWidth(60);
        from.setPromptText("from");
        from.setFont(Font.font("JF Flat", FontWeight.NORMAL, 16));
        from.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        from.setVisible(false);
        justNumber(from);
        TextArea to = new TextArea();
        to.setMaxHeight(10);
        to.setMaxWidth(60);
        justNumber(to);
        to.setPromptText("to");
        to.setFont(Font.font("JF Flat", FontWeight.NORMAL, 16));
        to.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        to.setVisible(false);
        HBox hBox1 = new HBox(playlist, from, to);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox1.setSpacing(10);

        RadioButton audio = new RadioButton("audio");
        audio.setFont(Font.font("JF Flat", FontWeight.NORMAL, 18));
        audio.setToggleGroup(group);
        audio.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());

        VBox vBox = new VBox(video, hBox1, audio);
        vBox.setSpacing(10);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (video.isSelected() == true) {
                    from.setVisible(false);
                    to.setVisible(false);
                } else if (playlist.isSelected() == true) {
                    from.setVisible(true);
                    to.setVisible(true);
                } else if (audio.isSelected() == true) {
                    from.setVisible(false);
                    to.setVisible(false);
                }
            }
        });


        progress = new ProgressBar();
        progress.setMinWidth(650);
        progress.setVisible(false);

        button = new Button("start");
        button.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        button.setFont(Font.font("JF Flat", FontWeight.BOLD, 25));
        button.setBackground(new Background(new BackgroundFill(Paint.valueOf("#66666666"), CornerRadii.EMPTY,
                new Insets(0, 0, 0, 0))));
        button.setMinWidth(650);

        TextArea result = new TextArea();
        result.setFont(Font.font("JF Flat", FontWeight.NORMAL, 15));
        result.setMaxHeight(60);
        result.getStylesheets().add(Main.class.getResource("style/style.css").toExternalForm());
        result.setEditable(false);

        grid.add(hBox, 0, 0);
        grid.add(hBoxDirectory, 0, 1);
        grid.add(vBox, 0, 2);
        grid.add(button, 0, 3);
        grid.add(progress, 0, 3);
        grid.add(result, 0, 4);
        grid.setVgap(10);
        grid.setHgap(10);
        primaryStage.setScene(scene);
        primaryStage.show();


        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                r = new Runnable() {
                    public void run() {
                        if (!directory.getText().equals("")) {
                            System.out.println("start download...");
                            if (video.isSelected() == true) {
                                executeCommand(youtube + link.getText(), directoryDownload, result);
                            } else if (playlist.isSelected() == true) {
                                if (from.getText().equals("") && !to.equals("")) {
                                    executeCommand(youtube + "--" + end + to.getText() + " " + link.getText(), directoryDownload, result);
                                } else if (to.getText().equals("") && !from.equals("")) {
                                    executeCommand(youtube + start + from.getText() + " " + link.getText(), directoryDownload, result);
                                } else if (!to.equals("") && !from.equals("")) {
                                    executeCommand(youtube + start + from.getText()
                                            + end + to.getText() + " " + link.getText(), directoryDownload, result);
                                }
                            } else if (audio.isSelected() == true) {
                                executeCommand(youtube + audioFormat + link.getText(), directoryDownload, result);
                            }
                            System.out.println("finish");
                        }
                    }
                };
                thread = new Thread(r);
                thread.start();
            }
        });

        // shadow Button

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

        buttonHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
        });*/

        //Parent root = FXMLLoader.load(getClass().getResource("design.fxml"));
        BorderPane pane = FXMLLoader.load(getClass().getResource("design.fxml"));
        Scene scene = new Scene(pane, 700, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    private void executeCommand(String command, String directory, TextArea result) {

        button.setVisible(false);
        progress.setVisible(true);


        try {
            p = Runtime.getRuntime().exec(command, null, new File(directory));
            //p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.setText(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            //p.destroy();
            button.setVisible(true);
            progress.setVisible(false);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        System.exit(1);

    }

}
