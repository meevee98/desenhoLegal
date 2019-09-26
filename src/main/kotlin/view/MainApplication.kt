package view

import controller.MainWindowController
import javafx.application.Application
import javafx.stage.Stage

class MainApplication: Application() {

    override fun start(stage: Stage) {
        val controller = MainWindowController()
        MainWindow(controller, stage)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MainApplication::class.java)
        }
    }
}