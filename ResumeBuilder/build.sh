#!/bin/bash

# AI-Powered Resume Builder - Build Script
# For Tier-2/Tier-3 Students

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
APP_NAME="ResumeBuilder"
MAIN_CLASS="app.Main"
OUTPUT_DIR="out"
LIB_DIR="lib"
SRC_DIR="src"
RESOURCES_DIR="resources"

# JavaFX configuration (adjust paths as needed)
JAVAFX_VERSION="17.0.2"
JAVAFX_PATH="${JAVAFX_HOME:-/usr/share/java}"

print_header() {
    echo -e "${BLUE}"
    echo "=================================="
    echo "  AI-Powered Resume Builder"
    echo "  Build & Run Script"
    echo "=================================="
    echo -e "${NC}"
}

print_step() {
    echo -e "${YELLOW}[STEP]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_prerequisites() {
    print_step "Checking prerequisites..."
    
    # Check Java version
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
        if [ "$JAVA_VERSION" -ge 11 ]; then
            print_success "Java $JAVA_VERSION found"
        else
            print_error "Java 11 or higher required. Found Java $JAVA_VERSION"
            exit 1
        fi
    else
        print_error "Java not found. Please install Java 11 or higher."
        exit 1
    fi
    
    # Check if JavaFX is available
    if [ ! -f "$JAVAFX_PATH/javafx-controls-11.jar" ]; then
        print_error "JavaFX not found at $JAVAFX_PATH"
        echo "Please set JAVAFX_HOME environment variable or install JavaFX"
        echo "Download from: https://openjfx.io/"
        exit 1
    else
        print_success "JavaFX found at $JAVAFX_PATH"
    fi
}

setup_directories() {
    print_step "Setting up directories..."
    
    # Create output directory
    mkdir -p "$OUTPUT_DIR"
    mkdir -p "$LIB_DIR"
    mkdir -p "output"
    mkdir -p "resources/assets/qr"
    
    print_success "Directories created"
}

download_dependencies() {
    print_step "Checking dependencies..."
    
    # Check if PDFBox is available (for PDF generation)
    PDFBOX_JAR="$LIB_DIR/pdfbox-app-2.0.29.jar"
    if [ ! -f "$PDFBOX_JAR" ]; then
        print_step "Downloading Apache PDFBox..."
        curl -L "https://archive.apache.org/dist/pdfbox/2.0.29/pdfbox-app-2.0.29.jar" -o "$PDFBOX_JAR"
        print_success "PDFBox downloaded"
    fi
    
    # Create classpath
    CLASSPATH="$OUTPUT_DIR"
    if [ -d "$LIB_DIR" ] && [ "$(ls -A $LIB_DIR)" ]; then
        CLASSPATH="$CLASSPATH:$LIB_DIR/*"
    fi
    
    print_success "Dependencies ready"
}

compile_application() {
    print_step "Compiling application..."
    
    # Find all Java source files
    JAVA_FILES=$(find "$SRC_DIR" -name "*.java")
    
    if [ -z "$JAVA_FILES" ]; then
        print_error "No Java source files found in $SRC_DIR"
        exit 1
    fi
    
    # Compile with JavaFX modules
    # Build classpath with JavaFX JAR files
    JAVAFX_CLASSPATH="$JAVAFX_PATH/javafx-base-11.jar:$JAVAFX_PATH/javafx-controls-11.jar:$JAVAFX_PATH/javafx-fxml-11.jar:$JAVAFX_PATH/javafx-graphics-11.jar"
    FULL_CLASSPATH="$CLASSPATH:$JAVAFX_CLASSPATH"
    
    javac \
        -cp "$FULL_CLASSPATH" \
        -d "$OUTPUT_DIR" \
        $JAVA_FILES
    
    if [ $? -eq 0 ]; then
        print_success "Compilation successful"
    else
        print_error "Compilation failed"
        exit 1
    fi
}

copy_resources() {
    print_step "Copying resources..."
    
    if [ -d "$RESOURCES_DIR" ]; then
        cp -r "$RESOURCES_DIR"/* "$OUTPUT_DIR/" 2>/dev/null || true
        print_success "Resources copied"
    else
        print_success "No resources directory found, skipping"
    fi
}

create_manifest() {
    print_step "Creating manifest..."
    
    # Create META-INF directory if it doesn't exist
    mkdir -p "$OUTPUT_DIR/META-INF"
    
    cat > "$OUTPUT_DIR/META-INF/MANIFEST.MF" << EOF
Manifest-Version: 1.0
Main-Class: $MAIN_CLASS
Class-Path: lib/
EOF
    
    print_success "Manifest created"
}

run_application() {
    print_step "Starting application..."
    
    # Build runtime classpath with JavaFX JAR files
    JAVAFX_CLASSPATH="$JAVAFX_PATH/javafx-base-11.jar:$JAVAFX_PATH/javafx-controls-11.jar:$JAVAFX_PATH/javafx-fxml-11.jar:$JAVAFX_PATH/javafx-graphics-11.jar"
    RUNTIME_CLASSPATH="$OUTPUT_DIR:$LIB_DIR/*:$JAVAFX_CLASSPATH"
    
    java \
        -cp "$RUNTIME_CLASSPATH" \
        "$MAIN_CLASS"
}

create_jar() {
    print_step "Creating JAR file..."
    
    JAR_NAME="$APP_NAME.jar"
    
    # Create JAR
    jar -cfm "$JAR_NAME" "$OUTPUT_DIR/META-INF/MANIFEST.MF" -C "$OUTPUT_DIR" .
    
    if [ $? -eq 0 ]; then
        print_success "JAR created: $JAR_NAME"
    else
        print_error "JAR creation failed"
        exit 1
    fi
}

create_run_script() {
    print_step "Creating run script..."
    
    cat > "run.sh" << EOF
#!/bin/bash
# Generated run script for $APP_NAME

JAVAFX_PATH="\${JAVAFX_HOME:-$JAVAFX_PATH}"

java \\
    --module-path "\$JAVAFX_PATH/lib" \\
    --add-modules javafx.controls,javafx.fxml \\
    -cp "$OUTPUT_DIR:$LIB_DIR/*" \\
    "$MAIN_CLASS"
EOF
    
    chmod +x "run.sh"
    print_success "Run script created: run.sh"
}

show_usage() {
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build    - Compile the application"
    echo "  run      - Compile and run the application"
    echo "  jar      - Create executable JAR file"
    echo "  clean    - Clean build artifacts"
    echo "  help     - Show this help message"
    echo ""
    echo "Environment Variables:"
    echo "  JAVAFX_HOME - Path to JavaFX installation"
    echo ""
    echo "Examples:"
    echo "  $0 build"
    echo "  $0 run"
    echo "  JAVAFX_HOME=/opt/javafx $0 run"
}

clean_build() {
    print_step "Cleaning build artifacts..."
    
    rm -rf "$OUTPUT_DIR"
    rm -f "*.jar"
    rm -f "run.sh"
    
    print_success "Clean completed"
}

# Main execution
print_header

case "${1:-run}" in
    "build")
        check_prerequisites
        setup_directories
        download_dependencies
        compile_application
        copy_resources
        create_run_script
        print_success "Build completed successfully!"
        echo -e "${BLUE}Run the application with: ./run.sh${NC}"
        ;;
    "run")
        check_prerequisites
        setup_directories
        download_dependencies
        compile_application
        copy_resources
        print_success "Build completed! Starting application..."
        echo ""
        run_application
        ;;
    "jar")
        check_prerequisites
        setup_directories
        download_dependencies
        compile_application
        copy_resources
        create_manifest
        create_jar
        print_success "JAR build completed!"
        echo -e "${BLUE}Run with: java --module-path \$JAVAFX_HOME/lib --add-modules javafx.controls,javafx.fxml -jar $APP_NAME.jar${NC}"
        ;;
    "clean")
        clean_build
        ;;
    "help"|"-h"|"--help")
        show_usage
        ;;
    *)
        print_error "Unknown command: $1"
        show_usage
        exit 1
        ;;
esac