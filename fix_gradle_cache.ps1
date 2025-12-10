# Complete Gradle Cache Fix Script
Write-Host "Stopping all Gradle daemons..." -ForegroundColor Yellow
gradle --stop

Write-Host "`nKilling any remaining Java/Gradle processes..." -ForegroundColor Yellow
Get-Process -Name "java" -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 2

Write-Host "`nDeleting corrupted transforms-3 cache..." -ForegroundColor Yellow
$cachePath = "$env:USERPROFILE\.gradle\caches\transforms-3"
if (Test-Path $cachePath) {
    Remove-Item -Path $cachePath -Recurse -Force
    Write-Host "Deleted: $cachePath" -ForegroundColor Green
} else {
    Write-Host "Cache already clean!" -ForegroundColor Green
}

Write-Host "`nCleaning project build directory..." -ForegroundColor Yellow
if (Test-Path "app\build") {
    Remove-Item -Path "app\build" -Recurse -Force
    Write-Host "Deleted: app\build" -ForegroundColor Green
}

Write-Host "`n✅ Cache cleanup complete!" -ForegroundColor Green
Write-Host "`nNext steps:" -ForegroundColor Cyan
Write-Host "1. Open Android Studio" -ForegroundColor White
Write-Host "2. Let it sync (this will take a few minutes)" -ForegroundColor White
Write-Host "3. Try building again" -ForegroundColor White
