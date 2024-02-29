package com.github.leloxo.simplecalculator;

import com.github.leloxo.simplecalculator.ui.CalculatorUI;
import com.github.leloxo.simplecalculator.util.CSSLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static final int DEFAULT_WINDOW_WIDTH = 305;
    private static final int DEFAULT_WINDOW_HEIGHT = 435;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calculator");

        CalculatorUI calculatorUI = new CalculatorUI();
        Scene scene = new Scene(calculatorUI.getGrid(), DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
        CSSLoader.loadCSS(scene);

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon3.png")));
        primaryStage.getIcons().add(image);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}