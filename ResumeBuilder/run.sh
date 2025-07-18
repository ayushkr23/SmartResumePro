#!/bin/bash
# Generated run script for ResumeBuilder

JAVAFX_PATH="${JAVAFX_HOME:-/usr/share/java}"

java \
    --module-path "$JAVAFX_PATH/lib" \
    --add-modules javafx.controls,javafx.fxml \
    -cp "out:lib/*" \
    "app.Main"
