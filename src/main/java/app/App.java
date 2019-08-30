package app;

import controller.MainWindowController;
import gui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Julio Arakaki
 * @version v1.0 2018/05/05
 */

public class App extends Application{
    public static void main (String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage palco) throws Exception {
        MainWindowController controller = new MainWindowController();
        new MainWindow(controller, palco);
    }
}

