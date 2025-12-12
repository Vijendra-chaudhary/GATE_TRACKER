# Git Setup Script for GATE Tracker
# Run this after restarting your terminal

Write-Host "Setting up Git for GATE Tracker..." -ForegroundColor Cyan

# Initialize Git repository
Write-Host "`n[1/4] Initializing Git repository..." -ForegroundColor Yellow
git init

# Configure Git user
Write-Host "[2/4] Configuring Git user..." -ForegroundColor Yellow
git config user.name "GATE Tracker User"
git config user.email "user@gatetracker.local"

# Add all files
Write-Host "[3/4] Adding all files to Git..." -ForegroundColor Yellow
git add .

# Create initial commit
Write-Host "[4/4] Creating initial commit..." -ForegroundColor Yellow
git commit -m "Initial commit: GATE Tracker with mandatory login and auto-backup features"

Write-Host "`n✅ Git setup complete!" -ForegroundColor Green
Write-Host "`nYou can now use these commands:" -ForegroundColor Cyan
Write-Host "  git status          - Check what files changed"
Write-Host "  git diff            - See what changed in files"
Write-Host "  git checkout HEAD -- <file>  - Restore a corrupted file"
Write-Host "  git commit -am 'message'     - Save changes"
Write-Host ""
