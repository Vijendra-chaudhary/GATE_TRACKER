# Quick Build Script (No Clean)
# Location: C:\Users\thevi\Downloads\GATE_CS\quick-build.ps1

Write-Host "Building GATE Tracker..." -ForegroundColor Cyan

.\gradlew.bat assembleDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Build successful!" -ForegroundColor Green
    Write-Host "APK: app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor Cyan
}
else {
    Write-Host ""
    Write-Host "Build failed!" -ForegroundColor Red
}
