package com.github.leloxo.simplecalculator.util;

import javafx.scene.Scene;
import java.util.Objects;

public class CSSLoader {
    public static void loadCSS(Scene scene) {
        if (scene != null) {
            scene.getStylesheets().add(Objects.requireNonNull(CSSLoader.class.getResource("/css/styles.css")).toExternalForm());
        }
    }
}
