# GATE Exam Preparation Tracker App - Development Specification

## 📱 Application Overview

Create a mobile application using **Kotlin** that helps students prepare for the GATE (Graduate Aptitude Test in Engineering) examination. The app should track study progress across multiple engineering branches, manage syllabus completion, and provide exam countdown features.

## 🎯 Core Features

### 1. Multi-Branch Support
- Support **6 popular GATE engineering branches**:
  1. **Computer Science & IT (CS)** - 11 subjects, 160 chapters
  2. **Electronics & Communication (EC)** - 9 subjects, ~70 chapters
  3. **Electrical Engineering (EE)** - 11 subjects, ~80 chapters
  4. **Mechanical Engineering (ME)** - 12 subjects, ~70 chapters
  5. **Civil Engineering (CE)** - 8 subjects, ~40 chapters
  6. **Data Science & AI (DA)** - 8 subjects, ~50 chapters

- **Total: 400+ chapters across all branches**

### 2. First-Time User Experience
- When the app is launched for the first time, show a **Branch Selection Screen**
- Display all 7 branches with:
  - Branch code (e.g., "CS", "EC", "EE")
  - Full name (e.g., "Computer Science & IT")
  - Brief description
  - Unique emoji icon (🖥️ for CS, 📡 for EC, ⚡ for EE, ⚙️ for ME, 🏗️ for CE, 🎛️ for IN, 🤖 for DA)
  - Branch-specific color theme
- Include a **search bar** to filter branches
- Allow user to select ONE branch
- Once selected, load the complete syllabus for that branch
- Remember the selection so the user goes directly to the Dashboard on subsequent launches

### 3. Dashboard Screen
- Display the **selected branch name and icon** in the app bar
- Show three main cards:
  1. **Countdown Card**: Days remaining until exam date with motivational messages
  2. **Overall Progress Card**: Total completed chapters / total chapters with percentage
  3. **Subject List**: All subjects for the selected branch

- Each subject card should show:
  - Subject name
  - Progress bar (visual indicator)
  - Completed chapters / Total chapters
  - Clickable to view chapter details

### 4. Subject Detail Screen
- Show all chapters for a selected subject
- Each chapter should display:
  - Chapter name
  - Checkbox to mark as complete/incomplete
  - Order/sequence number
- Automatically update the subject's progress when chapters are marked
- Show subject progress at the top
- Allow toggling chapter completion status with smooth animations

### 5. Exam Date Management
- Provide an **Exam Date Picker** screen
- Default exam date: **February 8, 2026** (GATE exam date)
- Allow users to change the exam date
- Display countdown in days on the dashboard
- Motivational messages based on days remaining:
  - More than 180 days: "You have plenty of time! Start strong! 💪"
  - 90-180 days: "Keep up the good work! 📚"
  - 30-90 days: "Focus mode activated! 🎯"
  - Less than 30 days: "Final sprint! Give it your all! 🚀"

### 6. Data Persistence
- Store all data locally on the device
- Track:
  - Selected branch
  - All subjects and chapters for the selected branch
  - Chapter completion status
  - Exam date
  - First launch status (has user selected branch?)
- Data should persist across app restarts
- Support branch switching (clear old data, load new syllabus)
-will include drive backup later

### 7. Progress Tracking
- Calculate overall progress: (completed chapters / total chapters) × 100%
- Calculate per-subject progress
- Update progress in real-time when chapters are marked
- Display progress using visual progress bars

## 📚 Complete Syllabus Data

### Computer Science & IT (CS)
1. **Engineering Mathematics** (24 chapters): Logic, Sets, Graph Theory, Combinatorics, Linear Algebra, Calculus, Probability
2. **Digital Logic** (7 chapters): Boolean Algebra, Circuits, Number Systems
3. **Computer Organization & Architecture** (13 chapters): Instructions, Pipelining, Memory, I/O
4. **Programming & Data Structures** (13 chapters): C Programming, Arrays, Trees, Graphs, Heaps
5. **Algorithms** (12 chapters): Sorting, Searching, Complexity, DP, Greedy, Graph Algorithms
6. **Theory of Computation** (10 chapters): Automata, Grammars, Turing Machines
7. **Compiler Design** (11 chapters): Lexical Analysis, Parsing, Code Generation
8. **Operating System** (13 chapters): Processes, Threads, Scheduling, Memory Management
9. **Databases** (16 chapters): ER Model, SQL, Normalization, Transactions, Indexing
10. **Computer Networks** (25 chapters): OSI, TCP/IP, Routing, Protocols
11. **General Aptitude** (15 chapters): English, Verbal, Numerical, Logical Reasoning

### Electronics & Communication (EC)
1. **Engineering Mathematics** (6 chapters)
2. **Networks, Signals & Systems** (15 chapters)
3. **Electronic Devices** (6 chapters)
4. **Analog Circuits** (12 chapters)
5. **Digital Circuits** (13 chapters)
6. **Control Systems** (9 chapters)
7. **Communications** (10 chapters)
8. **Electromagnetics** (7 chapters)
9. **General Aptitude** (6 chapters)

### Electrical Engineering (EE)
1. **Engineering Mathematics** (6 chapters)
2. **Electric Circuits** (10 chapters)
3. **Electromagnetic Fields** (10 chapters)
4. **Signals & Systems** (8 chapters)
5. **Electrical Machines** (12 chapters)
6. **Power Systems** (11 chapters)
7. **Control Systems** (11 chapters)
8. **Measurements** (8 chapters)
9. **Analog & Digital Electronics** (9 chapters)
10. **Power Electronics** (6 chapters)
11. **General Aptitude** (6 chapters)

### Mechanical Engineering (ME)
1. **Engineering Mathematics** (6 chapters)
2. **Engineering Mechanics** (9 chapters)
3. **Mechanics of Materials** (11 chapters)
4. **Theory of Machines** (9 chapters)
5. **Vibrations** (6 chapters)
6. **Machine Design** (10 chapters)
7. **Fluid Mechanics** (8 chapters)
8. **Thermodynamics** (10 chapters)
9. **Heat Transfer** (5 chapters)
10. **Manufacturing Processes** (8 chapters)
11. **Industrial Engineering** (6 chapters)
12. **General Aptitude** (6 chapters)

### Civil Engineering (CE)
1. **Engineering Mathematics** (5 chapters)
2. **Structural Engineering** (6 chapters)
3. **Geotechnical Engineering** (5 chapters)
4. **Water Resources Engineering** (5 chapters)
5. **Environmental Engineering** (5 chapters)
6. **Transportation Engineering** (5 chapters)
7. **Surveying** (5 chapters)
8. **General Aptitude** (6 chapters)


### Data Science & AI (DA)
1. **Probability & Statistics** (8 chapters)
2. **Linear Algebra** (6 chapters)
3. **Calculus & Optimization** (6 chapters)
4. **Programming & Data Structures** (8 chapters)
5. **Database Management** (6 chapters)
6. **Machine Learning** (8 chapters)
7. **Artificial Intelligence** (6 chapters)
8. **General Aptitude** (6 chapters)

## 🎨 UI/UX Requirements

### Modern Material Design
- Use Material Design 3 principles
- Clean, minimalist interface
- Smooth animations (fade-in/out, slide transitions)
- Branch-specific color themes:
  - CS: Blue (#4A90E2)
  - EC: Purple (#9B59B6)
  - EE: Orange (#F39C12)
  - ME: Dark Orange (#E67E22)
  - CE: Green (#27AE60)
  - IN: Teal (#1ABC9C)
  - DA: Red (#E74C3C)

### Navigation
- **Branch Selection Screen** (first launch only)
- **Dashboard Screen** (main screen)
- **Subject Detail Screen** (shows chapters)
- **Exam Date Picker Screen** (calendar picker)

### Components
- **BranchCard**: Displays branch info with selection state
- **SubjectCard**: Shows subject with progress bar
- **ChapterItem**: Checkbox item with chapter name
- **ProgressCard**: Overall progress visualization
- **CountdownCard**: Exam countdown with motivational message

## 🗄️ Data Architecture

### Database Tables
1. **branches**
   - id (primary key, auto-increment)
   - code (string: "CS", "EC", etc.)
   - name (string: full branch name)
   - description (string: brief description)
   - icon (string: emoji)
   - colorHex (string: color code)

2. **subjects**
   - id (primary key, auto-increment)
   - branchId (foreign key to branches)
   - name (string: subject name)
   - totalChapters (integer)
   - completedChapters (integer, default 0)

3. **chapters**
   - id (primary key, auto-increment)
   - subjectId (foreign key to subjects)
   - name (string: chapter name)
   - orderIndex (integer: sequence number)
   - isCompleted (boolean, default false)

4. **exam_date**
   - id (primary key, auto-increment)
   - examDate (long: timestamp in milliseconds)

5. **user_preference**
   - id (primary key, auto-increment)
   - selectedBranchId (foreign key to branches, nullable)
   - isFirstLaunch (boolean, default true)


## ⚙️ Technical Requirements

### Language
- **Kotlin** (required)

### Offline-First
- All data stored locally
- No internet required after installation
- Pre-populate all 6 branches on first launch
- Load selected branch syllabus into database

### Performance
- Smooth animations
- Instant UI updates
- Efficient database queries with indexing
- Lightweight app size

## 🔄 User Workflows

### First Launch Flow
1. App opens → Branch Selection Screen
2. User sees 7 branches with search functionality
3. User selects a branch (e.g., "Computer Science & IT")
4. User taps "Continue" button
5. App loads CS syllabus (11 subjects, 160 chapters) into database
6. App sets user preference (selectedBranchId, isFirstLaunch = false)
7. Navigate to Dashboard Screen
8. Show default exam date (Feb 8, 2026) with countdown

### Subsequent Launch Flow
1. App opens → Check user preference
2. If branch selected → Go directly to Dashboard
3. Dashboard shows selected branch's subjects and progress

### Tracking Progress Flow
1. User taps a subject card on Dashboard
2. Navigate to Subject Detail Screen
3. User sees all chapters with checkboxes
4. User taps checkbox to mark chapter as complete
5. Update chapter.isCompleted = true in database
6. Update subject.completedChapters count
7. Update UI with new progress percentage
8. Navigate back to Dashboard
9. Dashboard reflects updated overall progress

### Changing Exam Date Flow
1. User taps calendar icon in Dashboard top bar
2. Navigate to Exam Date Picker Screen
3. User selects new date
4. User taps "Save Date" button
5. Update exam_date table
6. Navigate back to Dashboard
7. Countdown card updates with new date


## 🎯 Success Criteria

1. ✅ User can select from 7 GATE branches on first launch
2. ✅ App loads complete syllabus (400+ chapters) for selected branch
3. ✅ User can view all subjects on Dashboard
4. ✅ User can mark chapters as complete/incomplete
5. ✅ Progress updates automatically and accurately
6. ✅ Exam countdown displays correctly
7. ✅ Data persists across app restarts
8. ✅ UI is smooth, responsive, and visually appealing
9. ✅ Search functionality works on branch selection
10. ✅ No crashes or data loss

## 📱 Example App Flow

```
Launch App
  ↓
[Branch Selection Screen]
- Welcome message: "Welcome to GATE Exam Tracker! 🎓"
- Search bar
- 7 branch cards displayed
- User selects "Computer Science & IT"
- Taps "Continue" button
  ↓
[Dashboard Screen]
- Top bar: "GATE Exam Tracker" | "🖥️ Computer Science & IT"
- Countdown Card: "430 days remaining" | "You have plenty of time! Start strong! 💪"
- Progress Card: "0 / 160 chapters completed" | "0%"
- Subjects list:
  * Engineering Mathematics (0/24)
  * Digital Logic (0/7)
  * Computer Organization (0/13)
  * Programming & Data Structures (0/13)
  * ... (11 subjects total)
- User taps "Engineering Mathematics"
  ↓
[Subject Detail Screen - Engineering Mathematics]
- Progress: 0/24 chapters
- Chapter list:
  ☐ Propositional and First-Order Logic
  ☐ Sets, Relations, Functions
  ☐ Partial Orders and Lattices
  ... (24 chapters)
- User checks "Propositional and First-Order Logic"
- Progress updates to 1/24
- User checks "Sets, Relations, Functions"
- Progress updates to 2/24
- User navigates back
  ↓
[Dashboard Screen]
- Progress Card now shows: "2 / 160 chapters completed" | "1.25%"
- Engineering Mathematics shows (2/24)
```

## 🚀 Additional Features (Optional Enhancements)

- Dark mode support
- Daily study reminders/notifications
- Notes for each chapter
- Practice questions integration
- Study streaks and achievements/badges
- Weekly/monthly progress reports
- Export progress as PDF
- Backup & restore to cloud
- Multiple branch support (allow tracking multiple branches)
- Custom syllabus (add own subjects/chapters)


---

**This specification provides complete details to recreate the GATE Exam Preparation Tracker app. All syllabus data, features, UI requirements, and technical architecture are documented above.**
