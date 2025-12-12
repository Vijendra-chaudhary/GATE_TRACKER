Write-Host "Fixing Android Studio Build Issues..." -ForegroundColor Green

# Step 1: Stop all Gradle daemons
Write-Host "`n1. Stopping all Gradle daemons..." -ForegroundColor Yellow
& .\gradlew.bat --stop

# Step 2: Clear problematic Gradle caches
Write-Host "`n2. Clearing Gradle transforms cache..." -ForegroundColor Yellow
Remove-Item -Path "$env:USERPROFILE\.gradle\caches\transforms-3" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$env:USERPROFILE\.gradle\caches\jars-*" -Recurse -Force -ErrorAction SilentlyContinue
Write-Host "Caches cleared!" -ForegroundColor Green

# Step 3: Clean the project
Write-Host "`n3. Cleaning the project..." -ForegroundColor Yellow
& .\gradlew.bat clean

# Step 4: Build again
Write-Host "`n4. Building the project..." -ForegroundColor Yellow
& .\gradlew.bat assembleDebug

Write-Host "`nDone! Now follow these steps in Android Studio:" -ForegroundColor Green
Write-Host "  1. Close Android Studio completely" -ForegroundColor Cyan
Write-Host "  2. Reopen your project" -ForegroundColor Cyan
Write-Host "  3. Go to File > Invalidate Caches > Invalidate and Restart" -ForegroundColor Cyan
Write-Host "  4. After restart, go to File > Sync Project with Gradle Files" -ForegroundColor Cyan
Write-Host "`nThe build should now work without errors!" -ForegroundColor Green
