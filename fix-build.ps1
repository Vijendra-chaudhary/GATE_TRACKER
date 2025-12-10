# Fix Build Issues Script
# Location: C:\Users\thevi\Downloads\GATE_CS\fix-build.ps1

Write-Host "Fixing Gradle build issues..." -ForegroundColor Cyan
Write-Host ""

# Stop all Gradle daemons
Write-Host "1. Stopping all Gradle daemons..." -ForegroundColor Yellow
.\gradlew.bat --stop
Start-Sleep -Seconds 2

# Kill any stuck Java processes (Gradle)
Write-Host ""
Write-Host "2. Killing stuck Gradle processes..." -ForegroundColor Yellow
Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object { $_.Path -like "*gradle*" } | Stop-Process -Force -ErrorAction SilentlyContinue

# Wait a bit
Start-Sleep -Seconds 2

# Try to delete build folder
Write-Host ""
Write-Host "3. Cleaning build directory..." -ForegroundColor Yellow
if (Test-Path "app\build") {
    try {
        Remove-Item -Path "app\build" -Recurse -Force -ErrorAction Stop
        Write-Host "Build directory cleaned!" -ForegroundColor Green
    }
    catch {
        Write-Host "Could not delete build directory automatically." -ForegroundColor Yellow
        Write-Host "Please close File Explorer and any other programs accessing the project folder." -ForegroundColor Yellow
        Write-Host ""
        $continue = Read-Host "Press Enter when ready to try again, or type 'skip' to skip cleaning"
        if ($continue -ne "skip") {
            Remove-Item -Path "app\build" -Recurse -Force -ErrorAction SilentlyContinue
        }
    }
}

# Build without clean
Write-Host ""
Write-Host "4. Building APK (without clean)..." -ForegroundColor Yellow
.\gradlew.bat assembleDebug --no-daemon

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Build successful!" -ForegroundColor Green
    Write-Host "APK: app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor Cyan
    Write-Host ""
    
    $install = Read-Host "Install on device? (Y/N)"
    if ($install -eq "Y" -or $install -eq "y") {
        .\gradlew.bat installDebug
        if ($LASTEXITCODE -eq 0) {
            Write-Host "Installation successful!" -ForegroundColor Green
        }
    }
}
else {
    Write-Host ""
    Write-Host "Build failed! Try opening the project in Android Studio instead." -ForegroundColor Red
}

Write-Host ""
Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
