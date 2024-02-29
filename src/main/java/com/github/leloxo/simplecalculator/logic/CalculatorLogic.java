package com.github.leloxo.simplecalculator.logic;

import javafx.scene.control.Label;

import java.util.Stack;

import static com.github.leloxo.simplecalculator.util.DecimalFormatter.dfFormat;

public class CalculatorLogic {
    private static final String ANSWER = "Ans";
    private static final String RESULT = "result";
    private static final String ZERO = "0";

    private final StringBuilder stringBuilderOut;
    private final StringBuilder stringBuilderInput;
    private final StringBuilder stringBuilderExp;
    private final Stack<Double> numStack;
    private final Stack<Character> operandStack;
    private final Stack<Double> resultStack;
    private double resultValue;
    private final Label resultLabel;
    private final Label expressionLabel;

    private boolean isDeleteButtonPressed = false;
    private boolean isAnsButtonPressed = false;
    private boolean isSignButtonPressed = false;

    // Constructor
    public CalculatorLogic(Label resultLabel, Label expressionLabel) {
        stringBuilderOut = new StringBuilder();
        stringBuilderInput = new StringBuilder();
        stringBuilderExp = new StringBuilder();
        numStack = new Stack<>();
        operandStack = new Stack<>();
        resultStack = new Stack<>();
        this.resultLabel = resultLabel;
        this.expressionLabel = expressionLabel;

        setInitialOutput();
    }

    // Initially set Output to 0
    private void setInitialOutput() {
        stringBuilderOut.append(ZERO);
        displayResultLabel();
    }

    /** HELPER METHODS */
    // Display result label
    private void displayResultLabel() {
        resultLabel.setText(stringBuilderOut.toString());
    }

    // Display expression Label
    private void displayExpressionLabel() {
        expressionLabel.setText(stringBuilderExp.toString());
    }

    // Delete expression string & reset expression label
    private void resetExpressionLabel() {
        stringBuilderExp.delete(0, stringBuilderExp.length());
        displayExpressionLabel();
    }

    // Display error message
    private void displayErrorMessage(String message) {
        expressionLabel.setText(message);
    }

    // Check if stringBuilderOut is zero
    private boolean isOutputZero() {
        return stringBuilderOut.toString().equals(ZERO) && stringBuilderOut.length() == 1;
    }

    // Check if last character is zero after operand
    private boolean isLastCharZeroAfterOperand() {
        return stringBuilderOut.charAt(stringBuilderOut.length()-1) == '0' && isOperand(stringBuilderOut.charAt(stringBuilderOut.length()-2));
    }

    // Check if char is Operand
    private boolean isOperand(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '×' || ch == '÷';
    }

    // Helper for handleSignButtonClick()
    public void toggleSignButtonPressed() {
        isSignButtonPressed = !isSignButtonPressed;
    }

    // Clear output until operand is reached
    private void clearOutputUntilOperand() {
        while (!isOperand(stringBuilderOut.charAt(stringBuilderOut.length()-1))) {
            stringBuilderOut.delete(stringBuilderOut.length()-1, stringBuilderOut.length());
            if (stringBuilderOut.isEmpty()) break;
        }
    }

    /** NUMBER INPUT HANDLING */
    public void handleNumberButtonClick(char number) {
        // Reset expression label after pressing a number button again
        resetExpressionLabel();

        // Check if deleting past operand
        if (isDeletingPastOperand()) {
            handleNumInputAfterDel(number);
        } else if (!stringBuilderOut.toString().equals(ANSWER)) {
            handleNumInput(number);
        }

        // Handle cases when a number button is pressed again
        if (isOutputZero()) {
            stringBuilderOut.replace(0,1, String.valueOf(number));
        } else if (isLastCharZeroAfterOperand()) {
            stringBuilderOut.replace(stringBuilderOut.length()-1,stringBuilderOut.length(), String.valueOf(number));
        } else if (stringBuilderOut.toString().equals(RESULT)) {
            // If result is displayed and numButton pressed again
            stringBuilderOut.replace(0, stringBuilderOut.length(), String.valueOf(number));
        } else if (!stringBuilderOut.toString().equals(ANSWER)) {
            // No number after ANS without operand in between
            stringBuilderOut.append(number);
        }
        displayResultLabel();
    }

    // Append the given number to the input string
    private void handleNumInput(char number) {
        stringBuilderInput.append(number);
    }

    // Handle number input after deletion
    private void handleNumInputAfterDel(char number) {
        // Combine the displayed number with the given number
        stringBuilderInput.append(stringBuilderOut);
        stringBuilderInput.append(number);
        // Pop the existing number from the stack, so that the new number can be added
        numStack.pop();
        isDeleteButtonPressed = false;
    }

    // Push the number from the input string onto the number stack and reset the input string
    private void pushNumber() {
        String number = stringBuilderInput.toString();
        numStack.push(Double.valueOf(number));

        // Reset the input string
        stringBuilderInput.delete(0, stringBuilderInput.length());
    }

    // Check if deleting past operand
    private boolean isDeletingPastOperand() {
        return isDeleteButtonPressed &&
                !numStack.isEmpty() &&
                !isOperand(stringBuilderOut.charAt(stringBuilderOut.length()-stringBuilderInput.length()-1)) &&
                !stringBuilderOut.toString().equals(ANSWER) &&
                (!stringBuilderOut.toString().equals(ZERO) &&
                        stringBuilderOut.length() == 1);
    }

    /** "." DECIMAL BUTTON HANDLING */
    public void handleDecimalButtonClick() {
        stringBuilderInput.append(".");
        stringBuilderOut.append(".");
        displayResultLabel();
    }

    /** "±" SIGN BUTTON HANDLING */
    public void handleSignButtonClick() {
        // If isSignButtonPressed is true, handle changing positive numbers to negative, otherwise handle changing negative numbers to positive
        if (isSignButtonPressed) {
            handlePositiveToNegative();
        } else {
            handleNegativeToPositive();
        }
        displayResultLabel();
    }

    // Handle changing positive numbers to negative
    private void handlePositiveToNegative() {
        // If the displayed output equals the most recent result
        if (stringBuilderOut.toString().equals(RESULT) || (!resultStack.isEmpty() && stringBuilderOut.toString().equals(dfFormat(resultStack.peek())))) {
            double result = resultStack.peek();
            // Replace the displayed result with its negative value
            stringBuilderOut.replace(0, stringBuilderOut.length(), "-" + dfFormat(result));
            // Update the number stack accordingly
            if (!numStack.isEmpty()) {
                numStack.pop();
            }
            numStack.push(-result);
        } else if (isOperand(stringBuilderOut.charAt(stringBuilderOut.length()-1)) || isOutputZero() || isLastCharZeroAfterOperand()) {
            // If the last character is an operand or if the displayed output is zero, reset isSignButtonPressed
            isSignButtonPressed = false;
        } else {
            insertNegativeSign();
        }
    }

    // Insert a negative sign into the input and output strings
    private void insertNegativeSign() {
        if (!stringBuilderInput.isEmpty()) {
            stringBuilderInput.insert(0, '-');
        }

        if (stringBuilderOut.length() > 1) {
            int insertIndex = stringBuilderOut.length()-stringBuilderInput.length()+1;
            stringBuilderOut.insert(insertIndex, '-');
        } else {
            stringBuilderOut.insert(stringBuilderOut.length()-1, '-');
        }
    }

    // Handle changing negative numbers to positive
    private void handleNegativeToPositive() {
        // If the displayed output equals the negative version of the most recent result
        if (!resultStack.isEmpty() && stringBuilderOut.toString().equals("-"+ dfFormat(resultStack.peek()))) {
            // Remove the negative sign from the output and update the number stack
            int negativeSignIndex = stringBuilderOut.indexOf("-");
            if (negativeSignIndex != -1) {
                stringBuilderOut.deleteCharAt(negativeSignIndex);
            }
            numStack.pop();
            numStack.push(resultStack.peek());
        } else if (isOperand(stringBuilderOut.charAt(stringBuilderOut.length()-1)) || isOutputZero() || isLastCharZeroAfterOperand()) {
            // If the last character is an operand or if the displayed output is zero, reset isSignButtonPressed
            isSignButtonPressed = false;
        } else {
            removeNegativeSign();
        }
    }

    // Remove the negative sign from the input and output strings
    private void removeNegativeSign() {
        if (!stringBuilderInput.isEmpty()) {
            int negativeSignIndex = stringBuilderInput.indexOf("-");
            if (negativeSignIndex != -1) {
                stringBuilderInput.deleteCharAt(negativeSignIndex);
            }
        }
        if (!stringBuilderOut.isEmpty()) {
            int lastNegativeSignIndex = stringBuilderOut.lastIndexOf(stringBuilderInput.toString());
            if (lastNegativeSignIndex != -1) {
                stringBuilderOut.deleteCharAt(lastNegativeSignIndex - 1);
            }
        }
    }

    /** OPERAND INPUT HANDLING */
    public void handleOperandButtonClick(char operand) {
        // Reset expression label after result
        if (stringBuilderOut.toString().equals(RESULT)) {
            resetExpressionLabel();
        }

        // If there's no input yet
        if (stringBuilderInput.isEmpty()) {
            // Handle cases when input is empty
            handleEmptyInput();
        } else {
            // Push the current number onto the stack
            pushNumber();
        }

        // Handle consecutive operands
        if (!operandStack.isEmpty() && isOperand(stringBuilderOut.charAt(stringBuilderOut.length()-1))) {
            // Pop the previous operand
            operandStack.pop();
            // Push the new operand onto the stack
            char newOperand = pushOperandAndDisplay(operand);
            stringBuilderOut.replace(stringBuilderOut.length()-1,stringBuilderOut.length(), String.valueOf(newOperand));
        } else {
            // If it's the first operand push it onto the stack
            char newOperand = pushOperandAndDisplay(operand);
            stringBuilderOut.append(newOperand);
        }

        displayResultLabel();
    }

    // Push the operand onto the stack
    private void pushOperand(char operand) {
        operandStack.push(operand);
    }

    // Push the operand onto the stack and update the display
    private char pushOperandAndDisplay(char operand) {
        pushOperand(operand);
        // Replace '/' with '÷' and '*' with '×'
        if (operand=='/') operand = '÷';
        if (operand=='*') operand = '×';
        return operand;
    }

    // Handle cases when input is empty
    private void handleEmptyInput() {
        // If the output is zero, push zero onto the stack
        if (isOutputZero()) {
            numStack.push(0.0);
        } else if (!resultStack.isEmpty() && stringBuilderOut.toString().equals(RESULT)) {
            // If there is a previous result, continue with that result
            stringBuilderOut.replace(0, stringBuilderOut.length(), dfFormat(resultStack.getLast()));
            numStack.push(resultStack.pop());
        }
    }

    /** "Ans" BUTTON HANDLING */
    public void handleAnsButtonClick() {
        // Set the Ans button pressed flag to true
        isAnsButtonPressed = true;
        if (!resultStack.isEmpty() && stringBuilderInput.isEmpty()) {
            if (isOutputZero()) {
                stringBuilderOut.replace(0, stringBuilderOut.length(), ANSWER);
            } else if (stringBuilderOut.toString().equals(RESULT)) {
                stringBuilderOut.replace(0, stringBuilderOut.length(), ANSWER);
            } else if (isLastCharZeroAfterOperand()) {
                stringBuilderOut.replace(stringBuilderOut.length()-1,stringBuilderOut.length(), ANSWER);
            } else {
                stringBuilderOut.append(ANSWER);
            }
            displayResultLabel();
        }
    }

    /** "x²" SQUARE BUTTON HANDLING */
    public void handleSquareButtonClick() {
        if (!stringBuilderInput.isEmpty() || stringBuilderOut.toString().equals(RESULT)) {
            if (stringBuilderOut.toString().equals(RESULT)) {
                stringBuilderInput.append(resultStack.getFirst());
            }

            // Perform calculation
            double num = Double.parseDouble(stringBuilderInput.toString());
            double squaredNum = num * num;

            // Update Input/Output and Display Result
            updateInputAndOutput(dfFormat(squaredNum));
        }
    }

    /** "√x" SQUARE-ROOT BUTTON HANDLING */
    public void handleRootButtonClick() {
        if (!stringBuilderInput.isEmpty() || stringBuilderOut.toString().equals(RESULT)) {
            if (stringBuilderOut.toString().equals(RESULT)) {
                stringBuilderInput.append(resultStack.getFirst());
            }

            // Perform calculation
            double num = Double.parseDouble(stringBuilderInput.toString());
            double rootNum;
            if (num >= 0) {
                rootNum = Math.sqrt(num);
            } else {
                displayErrorMessage("Error: Root of a negative number not allowed");
                return;
            }

            // Update Input/Output and Display Result
            updateInputAndOutput(dfFormat(rootNum));
        }
    }

    // Update Input/Output and Display Result
    private void updateInputAndOutput(String newInput) {
        stringBuilderInput.replace(0, stringBuilderInput.length(), newInput);
        clearOutputUntilOperand();
        stringBuilderOut.append(newInput);
        displayResultLabel();
    }

    /** RESULT BUTTON HANDLING */
    // Calculate result
    public void handleEqualsButtonClick() {
        // If the ANS button was pressed and there's a result in the stack, push it back to numStack
        if (isAnsButtonPressed && !resultStack.isEmpty()) {
            numStack.push(resultStack.pop());
            isAnsButtonPressed = false;
        }

        // If there is a second number as input push the number on the stack
        if (stringBuilderOut.length() != 1 && !stringBuilderInput.isEmpty()) {
            pushNumber();
        }

        // Debugging output
        System.out.println("Calculation (=): ");
        System.out.println("numStack: " + numStack);
        System.out.println("operandStack: " + operandStack);

        // Calculate the result based on the given operand and operands
        while (!operandStack.isEmpty()) {
            // Perform multiplication and division first
            if (operandStack.contains('*') || operandStack.contains('/') || operandStack.contains('%')) {
                int idx = operandStack.indexOf('*');
                if (idx == -1 || (operandStack.contains('/') && operandStack.indexOf('/') < idx)) {
                    idx = operandStack.indexOf('/');
                }
                if (idx == -1 || (operandStack.contains('%') && operandStack.indexOf('%') < idx)) {
                    idx = operandStack.indexOf('%');
                }

                double num2 = numStack.remove(idx + 1);
                double num1 = numStack.remove(idx);
                char operand = operandStack.remove(idx);

                switch (operand) {
                    case '*':
                        resultValue = num1 * num2;
                        break;
                    case '/':
                        if (num2 != 0) {
                            resultValue = num1 / num2;
                        } else {
                            displayErrorMessage("Error: Division by zero!");
                            return;
                        }
                        break;
                    case '%':
                        resultValue = num1 % num2;
                        break;
                }
                numStack.add(idx, resultValue);
            } else {
                // No multiplication or division left, perform addition and subtraction
                double num2 = numStack.remove(1);
                double num1 = numStack.remove(0);
                char operand = operandStack.removeFirst();

                switch (operand) {
                    case '+':
                        resultValue = num1 + num2;
                        break;
                    case '-':
                        resultValue = num1 - num2;
                        break;
                }
                numStack.addFirst(resultValue);
            }
        }

        // Debugging output
        System.out.println("Result: " + numStack);

        // Display the final result
        if (numStack.size() == 1) {
            // The final result is the last number in the number stack after all calculations are complete
            double finalValue = numStack.pop();

            // Save the result in the result stack
            resultStack.push(finalValue);

            // Remove old results from the result stack
            if (resultStack.size() > 1) {
                resultStack.removeFirst();
            }

            displayResult(finalValue);
        }

        // Debugging output
        System.out.println("resultStack: " + resultStack + "\n");
    }

    // Display the result value and update the output string
    private void displayResult(double resultValue) {
        // Print result
        resultLabel.setText(dfFormat(resultValue));

        // Print expression
        stringBuilderExp.replace(0, stringBuilderExp.length(), stringBuilderOut + "=");
        displayExpressionLabel();

        // Set output string to result
        stringBuilderOut.replace(0, stringBuilderOut.length(), RESULT);
    }

    /** CLEAR AND DELETE BUTTON HANDLING */
    // Handle the "C" button click event
    public void handleClearButtonClick() {
        // Clear all stacks
        numStack.clear();
        operandStack.clear();

        // Delete input string
        stringBuilderInput.delete(0, stringBuilderInput.length());

        // Reset output to zero and display result label
        stringBuilderOut.replace(0, stringBuilderOut.length(), ZERO);
        displayResultLabel();

        // Reset expression label
        resetExpressionLabel();

        // reset isSignButtonPressed
        isSignButtonPressed = false;

        // Debugging output
        System.out.println("Clear (C)");
        System.out.println("in: " + stringBuilderInput);
        System.out.println("out: " + stringBuilderOut);
        System.out.println("numStack: " + numStack);
        System.out.println("operandStack: " + operandStack);
        System.out.println("resultStack: " + resultStack + "\n");
    }

    // Handle the "CE" button click event
    public void handleClearRecentButtonClick() {
        // Clear output string
        if (!stringBuilderOut.isEmpty() && !stringBuilderInput.isEmpty()) {
            clearOutputUntilOperand();
            stringBuilderOut.append(ZERO);
        } else if (stringBuilderOut.toString().equals(RESULT)) {
            clearOutputUntilOperand();
            stringBuilderOut.append(ZERO);
        }
        // Clear input string
        if (!stringBuilderInput.isEmpty()) {
            stringBuilderInput.delete(0, stringBuilderInput.length());
        }

        // Reset expression label
        resetExpressionLabel();
        // Display result label
        displayResultLabel();

        // Reset isSignButtonPressed
        isSignButtonPressed = false;

        // Debugging output
        System.out.println("Clear (CE)");
        System.out.println("in: " + stringBuilderInput);
        System.out.println("out: " + stringBuilderOut);
        System.out.println("numStack: " + numStack);
        System.out.println("operandStack: " + operandStack);
        System.out.println("resultStack: " + resultStack + "\n");
    }

    // Handle the "⌫" button click event
    public void handleDeleteButtonClick() {
        // Set the delete button pressed flag to true
        isDeleteButtonPressed = true;

        // Check if the last character in the output string is not an operand
        if (!isOperand(stringBuilderOut.charAt(stringBuilderOut.length()-1))) {
            // Check if the input string is empty and the output string has only one character
            if (stringBuilderInput.isEmpty() && stringBuilderOut.length() == 1 && !numStack.isEmpty()) {
                numStack.pop();
            }
        } else {
            if (!operandStack.isEmpty()){
                operandStack.pop();
            }
        }

        // Delete the last character from the input string
        if (!stringBuilderInput.isEmpty()) {
            stringBuilderInput.delete(stringBuilderInput.length()-1, stringBuilderInput.length());
        }

        // Delete the last character from the output string
        if (!stringBuilderOut.isEmpty()) {
            // If the output string has only one character, replace it with zero
            if (stringBuilderOut.length() == 1) {
                stringBuilderOut.replace(0, stringBuilderOut.length(), ZERO);
            } else if (stringBuilderOut.toString().equals(RESULT)) {
                clearOutputUntilOperand();
                stringBuilderOut.append(ZERO);
            } else {
                stringBuilderOut.delete(stringBuilderOut.length()-1, stringBuilderOut.length());
            }
        }

        // Reset expression label
        resetExpressionLabel();
        // Display result label
        displayResultLabel();

        // Debugging output
        System.out.println("Clear (⌫)");
        System.out.println("in: " + stringBuilderInput);
        System.out.println("out: " + stringBuilderOut);
        System.out.println("numStack: " + numStack);
        System.out.println("operandStack: " + operandStack);
        System.out.println("resultStack: " + resultStack + "\n");
    }
}