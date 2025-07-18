# 🚀 JavaFX Setup Guide for AI-Powered Resume Builder

This guide will help you set up JavaFX properly to run the AI-Powered Resume Builder application.

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+** (recommended) or manual setup
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🔧 Method 1: Maven Setup (Recommended)

### Step 1: Verify Java Installation
```bash
java -version
# Should show Java 17 or higher

javac -version
# Should show Java 17 or higher
```

### Step 2: Install Maven (if not already installed)

#### Windows:
```bash
# Using Chocolatey
choco install maven

# Or download from https://maven.apache.org/download.cgi
```

#### macOS:
```bash
# Using Homebrew
brew install maven

# Or using MacPorts
sudo port install maven3
```

#### Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install maven
```

#### Linux (RHEL/CentOS/Fedora):
```bash
sudo yum install maven
# or
sudo dnf install maven
```

### Step 3: Run the Application with Maven
```bash
cd ResumeBuilder

# Clean and compile
mvn clean compile

# Run the application
mvn javafx:run

# Or create a runnable JAR
mvn clean package
```

## 🔧 Method 2: Manual JavaFX Setup

### Step 1: Download JavaFX SDK

1. Go to [https://openjfx.io/](https://openjfx.io/)
2. Download JavaFX SDK for your platform (Windows, macOS, Linux)
3. Extract to a directory (e.g., `/opt/javafx` or `C:\javafx`)

### Step 2: Set Environment Variables

#### Windows:
```cmd
set JAVAFX_HOME=C:\javafx\javafx-sdk-21.0.1
set PATH_TO_FX=%JAVAFX_HOME%\lib
```

#### macOS/Linux:
```bash
export JAVAFX_HOME=/opt/javafx/javafx-sdk-21.0.1
export PATH_TO_FX=$JAVAFX_HOME/lib
```

### Step 3: Compile and Run Manually
```bash
# Navigate to project directory
cd ResumeBuilder

# Compile with JavaFX
javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml \
      -cp "lib/*" -d out src/app/**/*.java src/app/*.java

# Copy resources
cp -r resources/* out/

# Run the application
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml \
     -cp "out:lib/*" app.Main
```

## 🛠️ IDE Setup

### IntelliJ IDEA

1. **Open Project:**
   - File → Open → Select `ResumeBuilder` folder
   - Import as Maven project

2. **Configure JavaFX:**
   - File → Project Structure → Libraries
   - Add JavaFX SDK library if not using Maven
   - Go to Run → Edit Configurations
   - Add VM Options: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`

3. **Maven Configuration:**
   - Open Maven panel (View → Tool Windows → Maven)
   - Click Reload All Maven Projects
   - Run configuration: `javafx:run`

### Eclipse

1. **Import Project:**
   - File → Import → Existing Maven Projects
   - Select `ResumeBuilder` folder

2. **Configure JavaFX:**
   - Right-click project → Properties → Java Build Path
   - Add External JARs from JavaFX SDK lib folder
   - In Run Configurations, add VM arguments:
     `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`

### VS Code

1. **Install Extensions:**
   - Extension Pack for Java
   - JavaFX Support

2. **Configure Launch:**
   Create `.vscode/launch.json`:
   ```json
   {
       "version": "0.2.0",
       "configurations": [
           {
               "type": "java",
               "name": "Launch Resume Builder",
               "request": "launch",
               "mainClass": "app.Main",
               "projectName": "ai-resume-builder",
               "vmArgs": "--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml"
           }
       ]
   }
   ```

## 🐳 Docker Setup (Advanced)

Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim

# Install JavaFX
RUN apt-get update && apt-get install -y \
    openjfx \
    maven \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

# Build application
RUN mvn clean package

# Run application
CMD ["mvn", "javafx:run"]
```

## 📱 Platform-Specific Setup

### Windows

#### Using our build script:
```cmd
cd ResumeBuilder
build.bat
```

#### Manual setup:
```cmd
# Set JavaFX path
set JAVAFX_HOME=C:\javafx\javafx-sdk-21.0.1

# Run build script
.\build.sh run
```

### macOS

#### Using Homebrew:
```bash
# Install dependencies
brew install openjdk@17 maven openjfx

# Set JAVA_HOME
export JAVA_HOME=/opt/homebrew/opt/openjdk@17

# Run application
cd ResumeBuilder
./build.sh run
```

### Linux

#### Ubuntu/Debian:
```bash
# Install dependencies
sudo apt update
sudo apt install openjdk-17-jdk maven openjfx

# Run application
cd ResumeBuilder
./build.sh run
```

#### Arch Linux:
```bash
# Install dependencies
sudo pacman -S jdk17-openjdk maven java-openjfx

# Run application
cd ResumeBuilder
./build.sh run
```

## 🔍 Troubleshooting

### Common Issues

#### 1. "Error: JavaFX runtime components are missing"
**Solution:** Add JavaFX modules to your runtime:
```bash
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
```

#### 2. "Module not found" errors
**Solution:** Ensure JavaFX is in module path:
```bash
java --module-path /path/to/javafx/lib --list-modules
# Should show javafx.controls, javafx.fxml, etc.
```

#### 3. "Main class not found"
**Solution:** Verify classpath and main class:
```bash
java -cp "out:lib/*" app.Main
```

#### 4. FXML loading errors
**Solution:** Ensure FXML files are in the classpath:
```bash
# Check if resources are copied
ls out/fxml/
```

#### 5. Maven build failures
**Solution:** Clear Maven cache and rebuild:
```bash
mvn clean install -U
```

### Environment Variables Check

Create a test script to verify your setup:
```bash
#!/bin/bash
echo "Java Version:"
java -version

echo -e "\nJavaFX Home:"
echo $JAVAFX_HOME

echo -e "\nJavaFX Modules:"
java --module-path $JAVAFX_HOME/lib --list-modules | grep javafx

echo -e "\nMaven Version:"
mvn -version
```

## 🚀 Quick Start Commands

### Maven (Recommended):
```bash
cd ResumeBuilder
mvn clean javafx:run
```

### Build Script:
```bash
cd ResumeBuilder
./build.sh run
```

### Manual (with JavaFX installed):
```bash
cd ResumeBuilder
java --module-path $JAVAFX_HOME/lib --add-modules javafx.controls,javafx.fxml \
     -cp "out:lib/*" app.Main
```

## 🎯 Verification

To verify your setup is working:

1. **Run the test:**
   ```bash
   mvn test
   ```

2. **Check JavaFX modules:**
   ```bash
   java --module-path $JAVAFX_HOME/lib --list-modules | grep javafx
   ```

3. **Launch the application:**
   ```bash
   mvn javafx:run
   ```

If you see the login screen with the Resume Builder interface, your setup is complete! 🎉

## 📚 Additional Resources

- [Official JavaFX Documentation](https://openjfx.io/openjfx-docs/)
- [JavaFX Maven Plugin](https://github.com/openjfx/javafx-maven-plugin)
- [JavaFX CSS Reference](https://openjfx.io/javadoc/17/javafx.graphics/javafx/scene/doc-files/cssref.html)
- [Scene Builder Download](https://gluonhq.com/products/scene-builder/)

## 🆘 Need Help?

If you're still having issues:

1. Check the [Troubleshooting](#troubleshooting) section above
2. Verify all [Prerequisites](#prerequisites) are met
3. Try the [Maven setup](#method-1-maven-setup-recommended) method
4. Check the project's issue tracker for known problems

Happy coding! 🎓✨