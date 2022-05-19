package main

import main.ui.ConsoleViewController
import main.ui.ConsoleViewControllerImpl

fun main(args: Array<String>) {
    val consoleViewController: ConsoleViewController = ConsoleViewControllerImpl()
    consoleViewController.run()
}