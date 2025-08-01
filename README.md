Overview
This Android application demonstrates an Augmented Reality (AR) experience for visualizing different drill types in 3D space. The app allows users to select a drill type from a spinner and then view it in AR by placing it on detected surfaces.

Prerequisites
Hardware Requirements
Android device with ARCore support (check official list)

Camera capability

Minimum SDK: 24 (Android 7.0)

Recommended SDK: 30+ (Android 11+)

Software Requirements
Android Studio (latest version)

Google Play Services for AR (will be prompted to install if not present)

ARCore 1.40.0 or later

Installation
Clone the repository:

bash
git clone https://github.com/Mayank74-tech/AR-App.git
Open in Android Studio:

Launch Android Studio

Select "Open an Existing Project"

Navigate to the cloned repository folder

Build the project:

Click "Build" > "Make Project"

Wait for Gradle to sync and build dependencies

Running the App
Option 1: Using Android Studio
Connect your Android device via USB (with USB debugging enabled) or start an emulator

Select your device from the dropdown menu in the toolbar

Click the "Run" button (green triangle)

Option 2: Direct APK Installation
Build the APK:

"Build" > "Build Bundle(s)/APK(s)" > "Build APK"

Transfer the APK to your device

Install and run the app

App Usage
Main Screen:

Select a drill type from the spinner (Drill 1, Drill 2, or Drill 3)

Tap the "Start AR Experience" button

AR View:

Move your device slowly to allow surface detection

When surfaces appear (as grid-like planes), tap to place the selected drill

Use gestures to:

Move: Drag with one finger

Rotate: Twist with two fingers

Scale: Pinch in/out

Returning:

Press the back button to exit AR view and return to the selection screen

Troubleshooting
Common Issues
ARCore not supported:

Check if your device is on the supported devices list

Ensure Google Play Services for AR is installed (available on Play Store)

App crashes on launch:

Verify camera permissions are granted

Check for adequate lighting in your environment

Restart the app and device

Surfaces not detected:

Move the device slowly in well-lit areas

Avoid shiny or featureless surfaces

Clean the camera lens

Logging Issues
If you encounter persistent problems:

Note the exact steps to reproduce

Check Logcat output in Android Studio

Open an issue on the GitHub repository with:

Device model

Android version

Detailed error description

Customization
Changing Drill Models
To modify or add drill models:

Replace the cube model in ARActivity.java with your 3D model

Update the spinner options in MainActivity.java

Adjust the material properties in getDrillColor()

Styling
Edit these files to change the app appearance:

res/values/colors.xml - Color scheme

res/values/styles.xml - Theme and component styles

res/layout/activity_main.xml - Main screen layout

Dependencies
This app uses:

ARCore (com.google.ar:core:1.40.0)

Sceneform UX (com.google.ar.sceneform.ux:sceneform-ux:1.17.1)

AndroidX libraries

Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Contact
For questions or support, contact: mayanksaini7455@gmail.com
