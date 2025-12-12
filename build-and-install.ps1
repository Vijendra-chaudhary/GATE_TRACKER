# GATE Tracker - Build and Install Script
# Location: C:\Users\thevi\Downloads\GATE_CS\build-and-install.ps1

Write-Host "================================" -ForegroundColor Cyan
Write-Host "GATE Tracker - Build & Install" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# Clean previous build
Write-Host "Cleaning previous build..." -ForegroundColor Yellow
.\gradlew.bat clean

# Build the app
Write-Host ""
Write-Host "Building APK..." -ForegroundColor Yellow
.\gradlew.bat assembleDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Build successful!" -ForegroundColor Green
    
    # Install on device
    Write-Host ""
    Write-Host "Installing on device..." -ForegroundColor Yellow
    .\gradlew.bat installDebug
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "Installation successful!" -ForegroundColor Green
        Write-Host ""
        Write-Host "APK Location: app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor Cyan
        
        # Launch the app
        Write-Host ""
        $launch = Read-Host "Launch the app now? (Y/N)"
        if ($launch -eq "Y" -or $launch -eq "y") {
            Write-Host "Launching GATE Tracker..." -ForegroundColor Yellow
            adb shell am start -n com.gate.tracker/.MainActivity
            Write-Host "App launched!" -ForegroundColor Green
        }
    }
    else {
        Write-Host ""
        Write-Host "Installation failed!" -ForegroundColor Red
        Write-Host "Make sure your device is connected and USB debugging is enabled." -ForegroundColor Yellow
    }
}
else {
    Write-Host ""
    Write-Host "Build failed!" -ForegroundColor Red
    Write-Host "Check the error messages above." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
