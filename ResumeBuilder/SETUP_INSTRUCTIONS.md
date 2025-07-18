# 🚀 Setup Instructions - AI-Powered Resume Builder

## Quick Start (3 Steps)

### Option 1: Maven Setup (Recommended)

```bash
# 1. Navigate to project directory
cd ResumeBuilder

# 2. Run with Maven (downloads JavaFX automatically)
mvn clean javafx:run

# OR use our Maven build script
./build-maven.sh run
```

### Option 2: Manual Setup

```bash
# 1. Navigate to project directory  
cd ResumeBuilder

# 2. Run the build script (downloads dependencies)
./build.sh run
```

## 📋 Prerequisites

Before running the application, ensure you have:

- ✅ **Java 17+** installed
- ✅ **Maven 3.6+** installed (for Maven method)
- ✅ **Git** (to clone the repository)

## 🔧 Detailed Setup

### Step 1: Verify Prerequisites

```bash
# Check Java version
java -version
# Should show: openjdk version "17.0.x" or higher

# Check Maven version  
mvn -version
# Should show: Apache Maven 3.6.x or higher
```

### Step 2: Choose Your Setup Method

#### Method A: Maven (Recommended)

**Advantages:**
- ✅ Automatic dependency management
- ✅ JavaFX downloaded automatically
- ✅ IDE integration ready
- ✅ Cross-platform compatibility

```bash
cd ResumeBuilder

# Clean build and run
mvn clean compile javafx:run

# OR use our script
./build-maven.sh run
```

#### Method B: Manual Build Script

**Advantages:**
- ✅ Works without Maven
- ✅ Downloads dependencies automatically
- ✅ Simpler for beginners

```bash
cd ResumeBuilder

# Run the application
./build.sh run

# OR just compile
./build.sh build
```

## 🛠️ IDE Setup

### IntelliJ IDEA

1. **Open Project:**
   ```
   File → Open → Select ResumeBuilder folder
   ```

2. **Import as Maven Project:**
   - Choose "Import Maven Projects" if prompted
   - Let IntelliJ download dependencies

3. **Run Configuration:**
   - Right-click `src/app/Main.java`
   - Select "Run Main.main()"
   - Or use Maven: Run → `javafx:run`

### VS Code

1. **Install Extensions:**
   - Extension Pack for Java
   - Maven for Java

2. **Open Project:**
   ```
   File → Open Folder → Select ResumeBuilder
   ```

3. **Run Application:**
   - Open `src/app/Main.java`
   - Click "Run" above the main method
   - Or use terminal: `mvn javafx:run`

### Eclipse

1. **Import Project:**
   ```
   File → Import → Existing Maven Projects
   Select ResumeBuilder folder
   ```

2. **Run Application:**
   - Right-click project → Run As → Java Application
   - Select `app.Main` as main class

## 🔍 Troubleshooting

### Common Issues & Solutions

#### 1. "JavaFX runtime components are missing"

**Solution A (Maven):**
```bash
mvn clean compile javafx:run
```

**Solution B (Manual):**
```bash
# Download JavaFX SDK and set path
export JAVAFX_HOME=/path/to/javafx-sdk
./build.sh run
```

#### 2. "Command 'mvn' not found"

**Ubuntu/Debian:**
```bash
sudo apt update && sudo apt install maven
```

**macOS:**
```bash
brew install maven
```

**Windows:**
- Download Maven from https://maven.apache.org/
- Add to PATH environment variable

#### 3. "Java version not supported"

**Check current version:**
```bash
java -version
```

**Install Java 17:**

**Ubuntu/Debian:**
```bash
sudo apt install openjdk-17-jdk
```

**macOS:**
```bash
brew install openjdk@17
```

**Windows:**
- Download from https://adoptium.net/

#### 4. Permission denied on scripts

```bash
chmod +x build.sh build-maven.sh
```

#### 5. "Main class not found"

```bash
# Ensure you're in the right directory
cd ResumeBuilder

# Clean and rebuild
mvn clean compile
./build-maven.sh run
```

## 🚀 Running the Application

### Quick Test

1. **Start the application:**
   ```bash
   mvn javafx:run
   ```

2. **Expected behavior:**
   - Login screen should appear
   - Modern UI with blue theme
   - "Continue as Guest" option available
   - No error messages in console

3. **Test workflow:**
   - Click "Continue as Guest"
   - Select a career role (e.g., Software Developer)
   - Go through the resume wizard
   - Export as PDF

### Performance Check

The application should:
- ✅ Start within 3-5 seconds
- ✅ UI should be responsive
- ✅ Navigation between screens should be smooth
- ✅ PDF export should work without errors

## 📁 Project Structure Overview

```
ResumeBuilder/
├── pom.xml                     # Maven configuration
├── build-maven.sh              # Maven build script
├── build.sh                    # Manual build script
├── JAVAFX_SETUP.md            # Detailed JavaFX setup guide
├── src/
│   └── app/
│       ├── Main.java          # Application entry point
│       ├── controller/        # UI controllers
│       ├── model/            # Data models
│       ├── exporter/         # PDF export functionality
│       └── utils/           # Utility classes
├── resources/
│   ├── fxml/                # UI layouts
│   ├── css/                 # Styling
│   ├── data/               # Application data
│   └── assets/             # Images, icons
└── output/                 # Generated resumes
```

## 🎯 Next Steps

After successful setup:

1. **Explore the application:**
   - Try different career roles
   - Test the resume wizard
   - Export sample resumes

2. **Customize for your needs:**
   - Modify role data in `resources/data/role_data.json`
   - Adjust styling in `resources/css/style.css`
   - Add new templates in the exporter

3. **Development:**
   - Read `JAVAFX_SETUP.md` for detailed development setup
   - Check the README.md for architecture overview
   - Look at the code structure for modifications

## 📞 Support

If you encounter issues:

1. **Check the troubleshooting section above**
2. **Review the detailed setup guide:** `JAVAFX_SETUP.md`
3. **Verify prerequisites are met**
4. **Try both Maven and manual build methods**

## 🎉 Success!

If you see the Resume Builder login screen, congratulations! 🎉

You now have a fully functional AI-Powered Resume Builder ready to help Tier-2/Tier-3 students create professional resumes.

**Happy resume building!** 📄✨