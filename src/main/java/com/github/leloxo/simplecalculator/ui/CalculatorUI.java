package com.github.leloxo.simplecalculator.ui;

import com.github.leloxo.simplecalculator.logic.CalculatorLogic;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CalculatorUI {
    private final GridPane grid;
    private Label resultLabel;
    private final CalculatorLogic calculatorLogic;

    public CalculatorUI() {
        grid = createGridPane();
        initializeUI();
        calculatorLogic = new CalculatorLogic(resultLabel);
    }

    public GridPane getGrid() { return grid; }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(0, 5, 0, 5)); //top, right, bottom, left
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("root");
        return grid;
    }

    private void initializeUI() {
        setResultLabel();
        addAllButtons();
    }

    private void setResultLabel() {
        resultLabel = new Label();
        resultLabel.getStyleClass().add("result-label");
        GridPane.setConstraints(resultLabel, 0, 0, 4, 1); // occupies 4 columns and 1 row
        GridPane.setHalignment(resultLabel, HPos.RIGHT);
        grid.getChildren().add(resultLabel);
    }

    private void addAllButtons() {
        // Operand Buttons
        setButton(createOperandButton("+"), 3, 5, e -> calculatorLogic.handleOperandButtonClick('+'));
        setButton(createOperandButton("-"), 3, 4, e -> calculatorLogic.handleOperandButtonClick('-'));
        setButton(createOperandButton("×"), 3, 3, e -> calculatorLogic.handleOperandButtonClick('*'));
        setButton(createOperandButton("÷"), 3, 2, e -> calculatorLogic.handleOperandButtonClick('/'));
        setButton(createOperandButton("mod"), 0, 2, e -> calculatorLogic.handleOperandButtonClick('%'));
        setButton(createOperandButton("x²"), 1, 2, e -> calculatorLogic.handleSquareButtonClick());
        setButton(createOperandButton("√x"), 2, 2, e -> calculatorLogic.handleRootButtonClick());
        setButton(createOperandButton("Ans"), 0, 1, e -> calculatorLogic.handleAnsButtonClick());

        // Number Buttons
        setButton(createNumberButton("1"), 0, 5, e -> calculatorLogic.handleNumberButtonClick('1'));
        setButton(createNumberButton("2"), 1, 5, e -> calculatorLogic.handleNumberButtonClick('2'));
        setButton(createNumberButton("3"), 2, 5, e -> calculatorLogic.handleNumberButtonClick('3'));
        setButton(createNumberButton("4"), 0, 4, e -> calculatorLogic.handleNumberButtonClick('4'));
        setButton(createNumberButton("5"), 1, 4, e -> calculatorLogic.handleNumberButtonClick('5'));
        setButton(createNumberButton("6"), 2, 4, e -> calculatorLogic.handleNumberButtonClick('6'));
        setButton(createNumberButton("7"), 0, 3, e -> calculatorLogic.handleNumberButtonClick('7'));
        setButton(createNumberButton("8"), 1, 3, e -> calculatorLogic.handleNumberButtonClick('8'));
        setButton(createNumberButton("9"), 2, 3, e -> calculatorLogic.handleNumberButtonClick('9'));
        setButton(createNumberButton("0"), 1, 6, e -> calculatorLogic.handleNumberButtonClick('0'));
        setButton(createNumberButton("."), 2, 6, e -> calculatorLogic.handleDecimalButtonClick());
        setButton(createNumberButton("±"), 0, 6, e -> {
            calculatorLogic.toggleSignButtonPressed();
            calculatorLogic.handleSignButtonClick();
        });

        // Other Buttons
        setButton(createEqualsButton(), 3, 6, e -> calculatorLogic.handleEqualsButtonClick());
        setButton(createClearButton("C"), 1, 1, e -> calculatorLogic.handleClearButtonClick());
        setButton(createClearButton("CE"), 2, 1, e -> calculatorLogic.handleClearRecentButtonClick());
        setButton(createClearButton("⌫"), 3, 1, e -> calculatorLogic.handleDeleteButtonClick());
    }

    private void setButton(Button button, int column, int row, EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
        GridPane.setConstraints(button, column, row);
        grid.getChildren().add(button);
    }

    private Button createOperandButton(String text) {
        return createButton(text, "action-button");
    }

    private Button createNumberButton(String text) {
        return createButton(text, "number-button");
    }

    private Button createClearButton(String text) {
        return createButton(text, "action-button");
    }

    private Button createEqualsButton() {
        return createButton("=", "equals-button");
    }

    private Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }
}
