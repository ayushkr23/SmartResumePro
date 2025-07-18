#!/bin/bash

# AI-Powered Resume Builder - Maven Build Script
# For Tier-2/Tier-3 Students

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
APP_NAME="AI Resume Builder"
PROJECT_NAME="ai-resume-builder"

print_header() {
    echo -e "${BLUE}"
    echo "=========================================="
    echo "  AI-Powered Resume Builder (Maven)"
    echo "  Build & Run Script"
    echo "=========================================="
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
        if [ "$JAVA_VERSION" -ge 17 ]; then
            print_success "Java $JAVA_VERSION found"
        else
            print_error "Java 17 or higher required. Found Java $JAVA_VERSION"
            exit 1
        fi
    else
        print_error "Java not found. Please install Java 17 or higher."
        exit 1
    fi
    
    # Check Maven
    if command -v mvn &> /dev/null; then
        MVN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f3)
        print_success "Maven $MVN_VERSION found"
    else
        print_error "Maven not found. Please install Maven 3.6 or higher."
        echo "Install with: sudo apt install maven (Ubuntu) or brew install maven (macOS)"
        exit 1
    fi
    
    # Check if pom.xml exists
    if [ ! -f "pom.xml" ]; then
        print_error "pom.xml not found. Please run this script from the ResumeBuilder directory."
        exit 1
    fi
}

clean_build() {
    print_step "Cleaning previous build..."
    mvn clean > /dev/null 2>&1 || true
    print_success "Clean completed"
}

compile_application() {
    print_step "Compiling application with Maven..."
    
    if mvn compile; then
        print_success "Compilation successful"
    else
        print_error "Compilation failed"
        exit 1
    fi
}

run_tests() {
    print_step "Running tests..."
    
    if mvn test; then
        print_success "All tests passed"
    else
        print_error "Some tests failed"
        # Don't exit, allow running despite test failures
    fi
}

package_application() {
    print_step "Packaging application..."
    
    if mvn package -DskipTests; then
        print_success "Packaging successful"
    else
        print_error "Packaging failed"
        exit 1
    fi
}

run_application() {
    print_step "Starting AI-Powered Resume Builder..."
    echo ""
    
    # Try to run with JavaFX plugin
    if mvn javafx:run; then
        print_success "Application ran successfully"
    else
        print_error "Failed to run application with Maven"
        echo ""
        echo "Trying alternative run method..."
        
        # Alternative: run with java command
        if [ -f "target/ai-resume-builder.jar" ]; then
            java -jar target/ai-resume-builder.jar
        else
            print_error "Could not find compiled JAR file"
            exit 1
        fi
    fi
}

create_executable_jar() {
    print_step "Creating executable JAR with dependencies..."
    
    if mvn assembly:single; then
        print_success "Executable JAR created: target/ai-resume-builder-jar-with-dependencies.jar"
        echo ""
        echo "To run the JAR:"
        echo "java -jar target/ai-resume-builder-jar-with-dependencies.jar"
    else
        print_error "Failed to create executable JAR"
    fi
}

install_dependencies() {
    print_step "Installing/updating dependencies..."
    
    if mvn dependency:resolve; then
        print_success "Dependencies resolved"
    else
        print_error "Failed to resolve dependencies"
        exit 1
    fi
}

show_project_info() {
    print_step "Project Information:"
    mvn help:effective-pom | grep -A 5 -B 5 "artifactId\|version\|name" | head -20
}

show_dependencies() {
    print_step "Project Dependencies:"
    mvn dependency:tree | head -30
}

setup_ide() {
    print_step "Setting up IDE configurations..."
    
    # IntelliJ IDEA
    if [ ! -d ".idea" ]; then
        mkdir -p .idea
        cat > .idea/misc.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectRootManager" version="2" languageLevel="JDK_17" default="true" project-jdk-name="17" project-jdk-type="JavaSDK">
    <output url="file://\$PROJECT_DIR\$/out" />
  </component>
</project>
EOF
        print_success "IntelliJ IDEA configuration created"
    fi
    
    # VS Code
    if [ ! -d ".vscode" ]; then
        mkdir -p .vscode
        cat > .vscode/launch.json << EOF
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch Resume Builder",
            "request": "launch",
            "mainClass": "app.Main",
            "projectName": "ai-resume-builder"
        }
    ]
}
EOF
        cat > .vscode/settings.json << EOF
{
    "java.configuration.updateBuildConfiguration": "automatic",
    "java.compile.nullAnalysis.mode": "automatic"
}
EOF
        print_success "VS Code configuration created"
    fi
}

check_javafx() {
    print_step "Checking JavaFX availability..."
    
    # Try to find JavaFX in Maven dependencies
    if mvn dependency:tree | grep -q "javafx"; then
        print_success "JavaFX found in Maven dependencies"
    else
        print_error "JavaFX not found in dependencies"
        echo "Please check your pom.xml configuration"
    fi
}

show_usage() {
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build      - Clean, compile, and package the application"
    echo "  run        - Compile and run the application"
    echo "  test       - Run tests"
    echo "  package    - Create JAR packages"
    echo "  clean      - Clean build artifacts"
    echo "  deps       - Install/update dependencies"
    echo "  info       - Show project information"
    echo "  ide-setup  - Setup IDE configurations"
    echo "  help       - Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 build"
    echo "  $0 run"
    echo "  $0 package"
}

# Main execution
print_header

case "${1:-run}" in
    "build")
        check_prerequisites
        clean_build
        install_dependencies
        compile_application
        package_application
        print_success "Build completed successfully!"
        echo -e "${BLUE}Run the application with: $0 run${NC}"
        ;;
    "run")
        check_prerequisites
        check_javafx
        compile_application
        print_success "Build completed! Starting application..."
        echo ""
        run_application
        ;;
    "test")
        check_prerequisites
        install_dependencies
        run_tests
        ;;
    "package")
        check_prerequisites
        compile_application
        package_application
        create_executable_jar
        ;;
    "clean")
        clean_build
        ;;
    "deps")
        check_prerequisites
        install_dependencies
        ;;
    "info")
        check_prerequisites
        show_project_info
        show_dependencies
        ;;
    "ide-setup")
        setup_ide
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