package com.gate.tracker.data.local

import com.gate.tracker.data.local.entity.BranchEntity
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.data.local.entity.SubjectEntity

object SyllabusData {
    
    fun getBranches() = listOf(
        BranchEntity(1, "CS", "Computer Science & IT", "Software & Networks", "🖥️", "#4A90E2"),
        BranchEntity(5, "CE", "Civil Engineering", "Structures & Water", "🏗️", "#27AE60"),
        BranchEntity(6, "DA", "Data Science & AI", "ML, AI & Statistics", "🤖", "#E74C3C")
    )
    
    fun getSubjectsForBranch(branchId: Int): List<SubjectEntity> = when (branchId) {
        1 -> csSubjects
        2 -> ecSubjects
        3 -> eeSubjects
        4 -> meSubjects
        5 -> ceSubjects
        6 -> daSubjects
        else -> emptyList()
    }
    
    fun getChaptersForSubjects(subjects: List<SubjectEntity>): List<ChapterEntity> {
        val chapters = mutableListOf<ChapterEntity>()
        subjects.forEach { subject ->
            chapters.addAll(getChaptersForSubject(subject.id, subject.name, subject.branchId))
        }
        return chapters
    }
    
    // Computer Science & IT Subjects
    private val csSubjects = listOf(
        SubjectEntity(id = 1, branchId = 1, name = "Engineering Mathematics", totalChapters = 24),
        SubjectEntity(id = 2, branchId = 1, name = "Digital Logic", totalChapters = 7),
        SubjectEntity(id = 3, branchId = 1, name = "Computer Organization & Architecture", totalChapters = 13),
        SubjectEntity(id = 4, branchId = 1, name = "Programming & Data Structures", totalChapters = 13),
        SubjectEntity(id = 5, branchId = 1, name = "Algorithms", totalChapters = 12),
        SubjectEntity(id = 6, branchId = 1, name = "Theory of Computation", totalChapters = 10),
        SubjectEntity(id = 7, branchId = 1, name = "Compiler Design", totalChapters = 11),
        SubjectEntity(id = 8, branchId = 1, name = "Operating System", totalChapters = 13),
        SubjectEntity(id = 9, branchId = 1, name = "Databases", totalChapters = 16),
        SubjectEntity(id = 10, branchId = 1, name = "Computer Networks", totalChapters = 25),
        SubjectEntity(id = 11, branchId = 1, name = "General Aptitude", totalChapters = 15)
    )
    
    // Electronics & Communication Subjects
    private val ecSubjects = listOf(
        SubjectEntity(id = 12, branchId = 2, name = "Engineering Mathematics", totalChapters = 6),
        SubjectEntity(id = 13, branchId = 2, name = "Networks, Signals & Systems", totalChapters = 15),
        SubjectEntity(id = 14, branchId = 2, name = "Electronic Devices", totalChapters = 6),
        SubjectEntity(id = 15, branchId = 2, name = "Analog Circuits", totalChapters = 12),
        SubjectEntity(id = 16, branchId = 2, name = "Digital Circuits", totalChapters = 13),
        SubjectEntity(id = 17, branchId = 2, name = "Control Systems", totalChapters = 9),
        SubjectEntity(id = 18, branchId = 2, name = "Communications", totalChapters = 10),
        SubjectEntity(id = 19, branchId = 2, name = "Electromagnetics", totalChapters = 7),
        SubjectEntity(id = 20, branchId = 2, name = "General Aptitude", totalChapters = 6)
    )
    
    // Electrical Engineering Subjects
    private val eeSubjects = listOf(
        SubjectEntity(id = 21, branchId = 3, name = "Engineering Mathematics", totalChapters = 6),
        SubjectEntity(id = 22, branchId = 3, name = "Electric Circuits", totalChapters = 10),
        SubjectEntity(id = 23, branchId = 3, name = "Electromagnetic Fields", totalChapters = 10),
        SubjectEntity(id = 24, branchId = 3, name = "Signals & Systems", totalChapters = 8),
        SubjectEntity(id = 25, branchId = 3, name = "Electrical Machines", totalChapters = 12),
        SubjectEntity(id = 26, branchId = 3, name = "Power Systems", totalChapters = 11),
        SubjectEntity(id = 27, branchId = 3, name = "Control Systems", totalChapters = 11),
        SubjectEntity(id = 28, branchId = 3, name = "Measurements", totalChapters = 8),
        SubjectEntity(id = 29, branchId = 3, name = "Analog & Digital Electronics", totalChapters = 9),
        SubjectEntity(id = 30, branchId = 3, name = "Power Electronics", totalChapters = 6),
        SubjectEntity(id = 31, branchId = 3, name = "General Aptitude", totalChapters = 6)
    )
    
    // Mechanical Engineering Subjects
    private val meSubjects = listOf(
        SubjectEntity(id = 32, branchId = 4, name = "Engineering Mathematics", totalChapters = 6),
        SubjectEntity(id = 33, branchId = 4, name = "Engineering Mechanics", totalChapters = 9),
        SubjectEntity(id = 34, branchId = 4, name = "Mechanics of Materials", totalChapters = 11),
        SubjectEntity(id = 35, branchId = 4, name = "Theory of Machines", totalChapters = 9),
        SubjectEntity(id = 36, branchId = 4, name = "Vibrations", totalChapters = 6),
        SubjectEntity(id = 37, branchId = 4, name = "Machine Design", totalChapters = 10),
        SubjectEntity(id = 38, branchId = 4, name = "Fluid Mechanics", totalChapters = 8),
        SubjectEntity(id = 39, branchId = 4, name = "Thermodynamics", totalChapters = 10),
        SubjectEntity(id = 40, branchId = 4, name = "Heat Transfer", totalChapters = 5),
        SubjectEntity(id = 41, branchId = 4, name = "Manufacturing Processes", totalChapters = 8),
        SubjectEntity(id = 42, branchId = 4, name = "Industrial Engineering", totalChapters = 6),
        SubjectEntity(id = 43, branchId = 4, name = "General Aptitude", totalChapters = 6)
    )
    
    // Civil Engineering Subjects
    private val ceSubjects = listOf(
        SubjectEntity(id = 44, branchId = 5, name = "Engineering Mathematics", totalChapters = 5),
        SubjectEntity(id = 45, branchId = 5, name = "Structural Engineering", totalChapters = 6),
        SubjectEntity(id = 46, branchId = 5, name = "Geotechnical Engineering", totalChapters = 5),
        SubjectEntity(id = 47, branchId = 5, name = "Water Resources Engineering", totalChapters = 5),
        SubjectEntity(id = 48, branchId = 5, name = "Environmental Engineering", totalChapters = 5),
        SubjectEntity(id = 49, branchId = 5, name = "Transportation Engineering", totalChapters = 5),
        SubjectEntity(id = 50, branchId = 5, name = "Surveying", totalChapters = 5),
        SubjectEntity(id = 51, branchId = 5, name = "General Aptitude", totalChapters = 6)
    )
    
    // Data Science & AI Subjects
    private val daSubjects = listOf(
        SubjectEntity(id = 52, branchId = 6, name = "Probability & Statistics", totalChapters = 8),
        SubjectEntity(id = 53, branchId = 6, name = "Linear Algebra", totalChapters = 6),
        SubjectEntity(id = 54, branchId = 6, name = "Calculus & Optimization", totalChapters = 6),
        SubjectEntity(id = 55, branchId = 6, name = "Programming & Data Structures", totalChapters = 8),
        SubjectEntity(id = 56, branchId = 6, name = "Database Management", totalChapters = 6),
        SubjectEntity(id = 57, branchId = 6, name = "Machine Learning", totalChapters = 8),
        SubjectEntity(id = 58, branchId = 6, name = "Artificial Intelligence", totalChapters = 6),
        SubjectEntity(id = 59, branchId = 6, name = "General Aptitude", totalChapters = 6)
    )
    
    private fun getChaptersForSubject(subjectId: Int, subjectName: String, branchId: Int): List<ChapterEntity> {
        return when (branchId) {
            1 -> getCSChapters(subjectId, subjectName)
            2 -> getECChapters(subjectId, subjectName)
            3 -> getEEChapters(subjectId, subjectName)
            4 -> getMEChapters(subjectId, subjectName)
            5 -> getCEChapters(subjectId, subjectName)
            6 -> getDAChapters(subjectId, subjectName)
            else -> emptyList()
        }
    }
    
    private fun getCSChapters(subjectId: Int, subjectName: String): List<ChapterEntity> = when (subjectName) {
        "Engineering Mathematics" -> listOf(
            // Discrete Mathematics (7 chapters)
            ChapterEntity(subjectId = subjectId, name = "Propositional and First-Order Logic", orderIndex = 1, category = "Discrete Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Sets, Relations, Functions", orderIndex = 2, category = "Discrete Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Partial Orders and Lattices", orderIndex = 3, category = "Discrete Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Monoids, Groups", orderIndex = 4, category = "Discrete Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Graphs: Connectivity, Matching, Coloring", orderIndex = 5, category = "Discrete Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Combinatorics: Counting, Recurrence Relations", orderIndex = 6, category = "Discrete Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Generating Functions", orderIndex = 7, category = "Discrete Mathematics"),
            
            // Linear Algebra (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Linear Algebra: Matrices, Determinants", orderIndex = 8, category = "Linear Algebra"),
            ChapterEntity(subjectId = subjectId, name = "System of Linear Equations", orderIndex = 9, category = "Linear Algebra"),
            ChapterEntity(subjectId = subjectId, name = "Eigenvalues and Eigenvectors", orderIndex = 10, category = "Linear Algebra"),
            ChapterEntity(subjectId = subjectId, name = "LU Decomposition", orderIndex = 11, category = "Linear Algebra"),
            
            // Calculus (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Calculus: Limits, Continuity, Differentiability", orderIndex = 12, category = "Calculus"),
            ChapterEntity(subjectId = subjectId, name = "Maxima and Minima", orderIndex = 13, category = "Calculus"),
            ChapterEntity(subjectId = subjectId, name = "Mean Value Theorem", orderIndex = 14, category = "Calculus"),
            ChapterEntity(subjectId = subjectId, name = "Integration", orderIndex = 15, category = "Calculus"),
            
            // Probability & Statistics (9 chapters)
            ChapterEntity(subjectId = subjectId, name = "Probability: Sample Space, Events", orderIndex = 16, category = "Probability & Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Independent Events, Bayes Theorem", orderIndex = 17, category = "Probability & Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Random Variables", orderIndex = 18, category = "Probability & Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Uniform, Normal, Exponential Distributions", orderIndex = 19, category = "Probability & Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Poisson Distributions", orderIndex = 20, category = "Probability & Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Mean, Median, Mode, Standard Deviation", orderIndex = 21, category = "Probability & Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Conditional Probability and Expectation", orderIndex = 22, category = "Probability & Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Central Limit Theorem", orderIndex = 23, category = "Probability & Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Hypothesis Testing", orderIndex = 24, category = "Probability & Statistics")
        )
        
        "Digital Logic" -> listOf(
            "Boolean Algebra",
            "Minimization of Boolean Functions",
            "Logic Gates",
            "Number Representations and Computer Arithmetic",
            "Combinational Circuits",
            "Sequential Circuits",
            "Finite State Machines"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Computer Organization & Architecture" -> listOf(
            // Basic Architecture (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Machine Instructions and Addressing Modes", orderIndex = 1, category = "Basic Architecture"),
            ChapterEntity(subjectId = subjectId, name = "ALU, Data Path, and Control Unit", orderIndex = 2, category = "Basic Architecture"),
            ChapterEntity(subjectId = subjectId, name = "Instruction Pipelining", orderIndex = 3, category = "Basic Architecture"),
            
            // Pipelining (1 chapter)
            ChapterEntity(subjectId = subjectId, name = "Pipeline Hazards", orderIndex = 4, category = "Pipelining"),
            
            // Memory Hierarchy (5 chapters)
            ChapterEntity(subjectId = subjectId, name = "Memory Hierarchy: Cache, Main Memory", orderIndex = 5, category = "Memory Hierarchy"),
            ChapterEntity(subjectId = subjectId, name = "Secondary Storage", orderIndex = 6, category = "Memory Hierarchy"),
            ChapterEntity(subjectId = subjectId, name = "Cache Organization and Policies", orderIndex = 7, category = "Memory Hierarchy"),
            ChapterEntity(subjectId = subjectId, name = "Virtual Memory", orderIndex = 8, category = "Memory Hierarchy"),
            ChapterEntity(subjectId = subjectId, name = "TLB (Translation Lookaside Buffer)", orderIndex = 9, category = "Memory Hierarchy"),
            
            // I/O & Advanced (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "I/O Interface", orderIndex = 10, category = "I/O & Advanced"),
            ChapterEntity(subjectId = subjectId, name = "Interrupt and DMA Handling", orderIndex = 11, category = "I/O & Advanced"),
            ChapterEntity(subjectId = subjectId, name = "RISC and CISC Architectures", orderIndex = 12, category = "I/O & Advanced"),
            ChapterEntity(subjectId = subjectId, name = "Parallel Processing and Multicore", orderIndex = 13, category = "I/O & Advanced")
        )
        
        "Programming & Data Structures" -> listOf(
            // C Programming (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "C Programming Basics", orderIndex = 1, category = "C Programming"),
            ChapterEntity(subjectId = subjectId, name = "Recursion", orderIndex = 2, category = "C Programming"),
            ChapterEntity(subjectId = subjectId, name = "Arrays and Strings", orderIndex = 3, category = "C Programming"),
            
            // Linear Data Structures (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Linked Lists", orderIndex = 4, category = "Linear Data Structures"),
            ChapterEntity(subjectId = subjectId, name = "Stacks and Queues", orderIndex = 5, category = "Linear Data Structures"),
            
            // Trees (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Trees: Binary Trees, BST", orderIndex = 6, category = "Trees"),
            ChapterEntity(subjectId = subjectId, name = "Balanced Trees (AVL, Red-Black)", orderIndex = 7, category = "Trees"),
            ChapterEntity(subjectId = subjectId, name = "Heaps and Priority Queues", orderIndex = 8, category = "Trees"),
            
            // Graphs & Hashing (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Hashing and Hash Tables", orderIndex = 9, category = "Graphs & Hashing"),
            ChapterEntity(subjectId = subjectId, name = "Graphs: Representation", orderIndex = 10, category = "Graphs & Hashing"),
            ChapterEntity(subjectId = subjectId, name = "Graph Traversal (BFS, DFS)", orderIndex = 11, category = "Graphs & Hashing"),
            ChapterEntity(subjectId = subjectId, name = "Spanning Trees", orderIndex = 12, category = "Graphs & Hashing"),
            
            // Complexity (1 chapter)
            ChapterEntity(subjectId = subjectId, name = "Time and Space Complexity", orderIndex = 13, category = "Complexity")
        )
        
        "Algorithms" -> listOf(
            // Sorting & Searching (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Searching: Linear, Binary", orderIndex = 1, category = "Sorting & Searching"),
            ChapterEntity(subjectId = subjectId, name = "Sorting: Bubble, Selection, Insertion", orderIndex = 2, category = "Sorting & Searching"),
            ChapterEntity(subjectId = subjectId, name = "Merge Sort, Quick Sort", orderIndex = 3, category = "Sorting & Searching"),
            ChapterEntity(subjectId = subjectId, name = "Heap Sort", orderIndex = 4, category = "Sorting & Searching"),
            
            // Algorithm Design (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Asymptotic Analysis: Big-O, Theta, Omega", orderIndex = 5, category = "Algorithm Design"),
            ChapterEntity(subjectId = subjectId, name = "Divide and Conquer", orderIndex = 6, category = "Algorithm Design"),
            ChapterEntity(subjectId = subjectId, name = "Greedy Algorithms", orderIndex = 7, category = "Algorithm Design"),
            ChapterEntity(subjectId = subjectId, name = "Dynamic Programming", orderIndex = 8, category = "Algorithm Design"),
            
            // Graph Algorithms (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Graph Algorithms: Shortest Path (Dijkstra, Bellman-Ford)", orderIndex = 9, category = "Graph Algorithms"),
            ChapterEntity(subjectId = subjectId, name = "Minimum Spanning Tree (Prim, Kruskal)", orderIndex = 10, category = "Graph Algorithms"),
            ChapterEntity(subjectId = subjectId, name = "Topological Sorting", orderIndex = 11, category = "Graph Algorithms"),
            
            // Complexity Theory (1 chapter)
            ChapterEntity(subjectId = subjectId, name = "NP-Completeness and Reduction", orderIndex = 12, category = "Complexity Theory")
        )
        
        "Theory of Computation" -> listOf(
            // Finite Automata (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Regular Expressions and Languages", orderIndex = 1, category = "Finite Automata"),
            ChapterEntity(subjectId = subjectId, name = "Finite Automata (DFA, NFA)", orderIndex = 2, category = "Finite Automata"),
            ChapterEntity(subjectId = subjectId, name = "Equivalence of DFA and NFA", orderIndex = 3, category = "Finite Automata"),
            
            // Pushdown Automata (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Context-Free Grammars and Languages", orderIndex = 4, category = "Pushdown Automata"),
            ChapterEntity(subjectId = subjectId, name = "Pushdown Automata", orderIndex = 5, category = "Pushdown Automata"),
            
            // Turing Machines (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Turing Machines and Their Variants", orderIndex = 6, category = "Turing Machines"),
            ChapterEntity(subjectId = subjectId, name = "Decidability and Halting Problem", orderIndex = 7, category = "Turing Machines"),
            ChapterEntity(subjectId = subjectId, name = "Undecidability", orderIndex = 8, category = "Turing Machines"),
            
            // Complexity Classes (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "P, NP, NP-Complete, NP-Hard", orderIndex = 9, category = "Complexity Classes"),
            ChapterEntity(subjectId = subjectId, name = "Chomsky Hierarchy", orderIndex = 10, category = "Complexity Classes")
        )
        
        "Compiler Design" -> listOf(
            // Lexical Analysis (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Lexical Analysis: Tokens, Patterns", orderIndex = 1, category = "Lexical Analysis"),
            ChapterEntity(subjectId = subjectId, name = "Regular Expressions for Lexical Analysis", orderIndex = 2, category = "Lexical Analysis"),
            ChapterEntity(subjectId = subjectId, name = "Finite Automata for Lexical Analyzer", orderIndex = 3, category = "Lexical Analysis"),
            
            // Syntax Analysis (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Syntax Analysis: Parsing", orderIndex = 4, category = "Syntax Analysis"),
            ChapterEntity(subjectId = subjectId, name = "Top-Down Parsing (Recursive Descent, LL)", orderIndex = 5, category = "Syntax Analysis"),
            ChapterEntity(subjectId = subjectId, name = "Bottom-Up Parsing (LR, SLR, LALR)", orderIndex = 6, category = "Syntax Analysis"),
            ChapterEntity(subjectId = subjectId, name = "Syntax-Directed Translation", orderIndex = 7, category = "Syntax Analysis"),
            
            // Semantic & Code Generation (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Type Checking", orderIndex = 8, category = "Semantic & Code Generation"),
            ChapterEntity(subjectId = subjectId, name = "Run-Time Environments", orderIndex = 9, category = "Semantic & Code Generation"),
            ChapterEntity(subjectId = subjectId, name = "Intermediate Code Generation", orderIndex = 10, category = "Semantic & Code Generation"),
            ChapterEntity(subjectId = subjectId, name = "Code Optimization", orderIndex = 11, category = "Semantic & Code Generation")
        )
        
        "Operating System" -> listOf(
            // Process Management (5 chapters)
            ChapterEntity(subjectId = subjectId, name = "Processes and Threads", orderIndex = 1, category = "Process Management"),
            ChapterEntity(subjectId = subjectId, name = "Process States and Scheduling", orderIndex = 2, category = "Process Management"),
            ChapterEntity(subjectId = subjectId, name = "CPU Scheduling Algorithms (FCFS, SJF, Round Robin)", orderIndex = 3, category = "Process Management"),
            ChapterEntity(subjectId = subjectId, name = "Priority Scheduling", orderIndex = 4, category = "Process Management"),
            ChapterEntity(subjectId = subjectId, name = "Process Synchronization", orderIndex = 5, category = "Process Management"),
            
            // Synchronization & Deadlocks (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Critical Section Problem", orderIndex = 6, category = "Synchronization & Deadlocks"),
            ChapterEntity(subjectId = subjectId, name = "Semaphores and Monitors", orderIndex = 7, category = "Synchronization & Deadlocks"),
            ChapterEntity(subjectId = subjectId, name = "Deadlocks: Detection, Prevention, Avoidance", orderIndex = 8, category = "Synchronization & Deadlocks"),
            
            // Memory Management (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Memory Management: Paging", orderIndex = 9, category = "Memory Management"),
            ChapterEntity(subjectId = subjectId, name = "Segmentation", orderIndex = 10, category = "Memory Management"),
            ChapterEntity(subjectId = subjectId, name = "Virtual Memory Management", orderIndex = 11, category = "Memory Management"),
            ChapterEntity(subjectId = subjectId, name = "Page Replacement Algorithms", orderIndex = 12, category = "Memory Management"),
            
            // File Systems (1 chapter)
            ChapterEntity(subjectId = subjectId, name = "File Systems and Disk Management", orderIndex = 13, category = "File Systems")
        )
        
        "Databases" -> listOf(
            // Data Modeling (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "ER Model: Entities, Relationships", orderIndex = 1, category = "Data Modeling"),
            ChapterEntity(subjectId = subjectId, name = "Relational Model: Schema, Keys", orderIndex = 2, category = "Data Modeling"),
            
            // Relational Algebra & SQL (5 chapters)
            ChapterEntity(subjectId = subjectId, name = "Relational Algebra", orderIndex = 3, category = "Relational Algebra & SQL"),
            ChapterEntity(subjectId = subjectId, name = "Tuple Relational Calculus", orderIndex = 4, category = "Relational Algebra & SQL"),
            ChapterEntity(subjectId = subjectId, name = "SQL: DDL, DML, DCL", orderIndex = 5, category = "Relational Algebra & SQL"),
            ChapterEntity(subjectId = subjectId, name = "Joins and Set Operations", orderIndex = 6, category = "Relational Algebra & SQL"),
            ChapterEntity(subjectId = subjectId, name = "Nested Queries and Subqueries", orderIndex = 7, category = "Relational Algebra & SQL"),
            
            // Normalization (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Functional Dependencies", orderIndex = 8, category = "Normalization"),
            ChapterEntity(subjectId = subjectId, name = "Normalization: 1NF, 2NF, 3NF, BCNF", orderIndex = 9, category = "Normalization"),
            
            // Transactions (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Transactions and ACID Properties", orderIndex = 10, category = "Transactions"),
            ChapterEntity(subjectId = subjectId, name = "Concurrency Control: Lock-Based Protocols", orderIndex = 11, category = "Transactions"),
            ChapterEntity(subjectId = subjectId, name = "Timestamp-Based Protocols", orderIndex = 12, category = "Transactions"),
            ChapterEntity(subjectId = subjectId, name = "Serializability and Recoverability", orderIndex = 13, category = "Transactions"),
            
            // Indexing & Optimization (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Indexing: B-Trees, B+ Trees", orderIndex = 14, category = "Indexing & Optimization"),
            ChapterEntity(subjectId = subjectId, name = "Hashing for Indexing", orderIndex = 15, category = "Indexing & Optimization"),
            ChapterEntity(subjectId = subjectId, name = "Query Optimization", orderIndex = 16, category = "Indexing & Optimization")
        )
        
        "Computer Networks" -> listOf(
            // Physical & Data Link Layer (7 chapters)
            ChapterEntity(subjectId = subjectId, name = "OSI Model and TCP/IP Model", orderIndex = 1, category = "Physical & Data Link Layer"),
            ChapterEntity(subjectId = subjectId, name = "Physical Layer: Transmission Media", orderIndex = 2, category = "Physical & Data Link Layer"),
            ChapterEntity(subjectId = subjectId, name = "Data Link Layer: Framing, Error Detection", orderIndex = 3, category = "Physical & Data Link Layer"),
            ChapterEntity(subjectId = subjectId, name = "Error Correction Techniques", orderIndex = 4, category = "Physical & Data Link Layer"),
            ChapterEntity(subjectId = subjectId, name = "Flow Control: Stop-and-Wait, Sliding Window", orderIndex = 5, category = "Physical & Data Link Layer"),
            ChapterEntity(subjectId = subjectId, name = "MAC Protocols: ALOHA, CSMA/CD", orderIndex = 6, category = "Physical & Data Link Layer"),
            ChapterEntity(subjectId = subjectId, name = "Ethernet and WiFi", orderIndex = 7, category = "Physical & Data Link Layer"),
            
            // Network Layer (5 chapters)
            ChapterEntity(subjectId = subjectId, name = "Network Layer: IP Addressing", orderIndex = 8, category = "Network Layer"),
            ChapterEntity(subjectId = subjectId, name = "Subnetting and CIDR", orderIndex = 9, category = "Network Layer"),
            ChapterEntity(subjectId = subjectId, name = "Routing Algorithms: Distance Vector, Link State", orderIndex = 10, category = "Network Layer"),
            ChapterEntity(subjectId = subjectId, name = "Routing Protocols: RIP, OSPF, BGP", orderIndex = 11, category = "Network Layer"),
            ChapterEntity(subjectId = subjectId, name = "IPv4 and IPv6", orderIndex = 12, category = "Network Layer"),
            
            // Transport Layer (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Transport Layer: TCP and UDP", orderIndex = 13, category = "Transport Layer"),
            ChapterEntity(subjectId = subjectId, name = "TCP Connection Management", orderIndex = 14, category = "Transport Layer"),
            ChapterEntity(subjectId = subjectId, name = "Congestion Control", orderIndex = 15, category = "Transport Layer"),
            ChapterEntity(subjectId = subjectId, name = "Flow Control in TCP", orderIndex = 16, category = "Transport Layer"),
            
            // Application Layer (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Application Layer: HTTP, FTP, SMTP", orderIndex = 17, category = "Application Layer"),
            ChapterEntity(subjectId = subjectId, name = "DNS and DHCP", orderIndex = 18, category = "Application Layer"),
            
            // Network Security (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Network Security: Encryption, Authentication", orderIndex = 19, category = "Network Security"),
            ChapterEntity(subjectId = subjectId, name = "Firewalls and VPN", orderIndex = 20, category = "Network Security"),
            
            // Advanced Topics (5 chapters)
            ChapterEntity(subjectId = subjectId, name = "Socket Programming", orderIndex = 21, category = "Advanced Topics"),
            ChapterEntity(subjectId = subjectId, name = "Network Performance Metrics", orderIndex = 22, category = "Advanced Topics"),
            ChapterEntity(subjectId = subjectId, name = "QoS (Quality of Service)", orderIndex = 23, category = "Advanced Topics"),
            ChapterEntity(subjectId = subjectId, name = "Mobile and Wireless Networks", orderIndex = 24, category = "Advanced Topics"),
            ChapterEntity(subjectId = subjectId, name = "Network Management (SNMP)", orderIndex = 25, category = "Advanced Topics")
        )
        
        "General Aptitude" -> listOf(
            // Verbal Ability (6 chapters)
            ChapterEntity(subjectId = subjectId, name = "Verbal Ability: Grammar", orderIndex = 1, category = "Verbal Ability"),
            ChapterEntity(subjectId = subjectId, name = "Sentence Completion", orderIndex = 2, category = "Verbal Ability"),
            ChapterEntity(subjectId = subjectId, name = "Verbal Analogies", orderIndex = 3, category = "Verbal Ability"),
            ChapterEntity(subjectId = subjectId, name = "Word Groups", orderIndex = 4, category = "Verbal Ability"),
            ChapterEntity(subjectId = subjectId, name = "Critical Reasoning", orderIndex = 5, category = "Verbal Ability"),
            ChapterEntity(subjectId = subjectId, name = "Verbal Deduction", orderIndex = 6, category = "Verbal Ability"),
            
            // Numerical & Logical Ability (9 chapters)
            ChapterEntity(subjectId = subjectId, name = "Numerical Ability: Number Systems", orderIndex = 7, category = "Numerical & Logical Ability"),
            ChapterEntity(subjectId = subjectId, name = "Percentages and Ratios", orderIndex = 8, category = "Numerical & Logical Ability"),
            ChapterEntity(subjectId = subjectId, name = "Speed, Time, Distance", orderIndex = 9, category = "Numerical & Logical Ability"),
            ChapterEntity(subjectId = subjectId, name = "Profit and Loss", orderIndex = 10, category = "Numerical & Logical Ability"),
            ChapterEntity(subjectId = subjectId, name = "Data Interpretation: Tables, Graphs", orderIndex = 11, category = "Numerical & Logical Ability"),
            ChapterEntity(subjectId = subjectId, name = "Logical Reasoning: Puzzles", orderIndex = 12, category = "Numerical & Logical Ability"),
            ChapterEntity(subjectId = subjectId, name = "Series Completion", orderIndex = 13, category = "Numerical & Logical Ability"),
            ChapterEntity(subjectId = subjectId, name = "Coding-Decoding", orderIndex = 14, category = "Numerical & Logical Ability"),
            ChapterEntity(subjectId = subjectId, name = "Spatial Aptitude", orderIndex = 15, category = "Numerical & Logical Ability")
        )
        
        else -> emptyList()
    }
    
    private fun getECChapters(subjectId: Int, subjectName: String): List<ChapterEntity> = when (subjectName) {
        "Engineering Mathematics" -> listOf(
            "Linear Algebra",
            "Calculus",
            "Differential Equations",
            "Probability and Statistics",
            "Complex Variables",
            "Numerical Methods"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Networks, Signals & Systems" -> listOf(
            "Network Graphs",
            "KCL and KVL",
            "Node and Mesh Analysis",
            "Transient Response",
            "Sinusoidal Steady-State Analysis",
            "Resonance",
            "Frequency Domain Analysis",
            "Two-Port Networks",
            "Continuous-Time Signals",
            "Discrete-Time Signals",
            "LTI Systems",
            "Fourier Series and Transform",
            "Laplace Transform",
            "Z-Transform",
            "Transfer Functions and Frequency Response"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Electronic Devices" -> listOf(
            "Energy Bands in Semiconductors",
            "PN Junction Diodes",
            "Zener Diodes",
            "BJT (Bipolar Junction Transistor)",
            "MOSFET",
            "LED, Photodiode, Solar Cell"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Analog Circuits" -> listOf(
            "Small Signal Equivalent Circuits",
            "BJT Amplifiers",
            "MOSFET Amplifiers",
            "Frequency Response of Amplifiers",
            "Operational Amplifiers",
            "Feedback Amplifiers",
            "Oscillators",
            "Rectifiers and Power Supplies",
            "Voltage Regulators",
            "Filters: Low-Pass, High-Pass, Band-Pass",
            "Active Filters",
            "Phase-Locked Loop (PLL)"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Digital Circuits" -> listOf(
            "Number Systems",
            "Boolean Algebra",
            "Logic Gates",
            "Combinational Circuits: Adders, Subtractors",
            "Multiplexers and Demultiplexers",
            "Encoders and Decoders",
            "Sequential Circuits: Flip-Flops",
            "Registers and Counters",
            "Finite State Machines",
            "A/D and D/A Converters",
            "Semiconductor Memories",
            "Microprocessor Basics",
            "Instruction Set and Programming"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Control Systems" -> listOf(
            "Block Diagrams and Signal Flow Graphs",
            "Time Domain Analysis",
            "Frequency Domain Analysis",
            "Routh-Hurwitz Criterion",
            "Nyquist Criterion",
            "Bode Plot",
            "Root Locus",
            "Compensators",
            "State Variable Analysis"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Communications" -> listOf(
            "Amplitude Modulation (AM)",
            "Frequency Modulation (FM)",
            "Phase Modulation (PM)",
            "Pulse Modulation",
            "Digital Modulation: ASK, FSK, PSK",
            "Information Theory and Coding",
            "Error Control Coding",
            "Satellite Communication",
            "Optical Fiber Communication",
            "Mobile Communication"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Electromagnetics" -> listOf(
            "Electrostatics",
            "Magnetostatics",
            "Maxwell's Equations",
            "Electromagnetic Waves",
            "Transmission Lines",
            "Waveguides",
            "Antennas"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "General Aptitude" -> listOf(
            "Verbal Ability",
            "Numerical Ability",
            "Logical Reasoning",
            "Data Interpretation",
            "Analytical Ability",
            "Spatial Aptitude"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        else -> emptyList()
    }
    
    private fun getEEChapters(subjectId: Int, subjectName: String): List<ChapterEntity> = when (subjectName) {
        "Engineering Mathematics" -> listOf(
            "Linear Algebra",
            "Calculus",
            "Differential Equations",
            "Probability and Statistics",
            "Vector Calculus",
            "Numerical Methods"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Electric Circuits" -> listOf(
            "Network Theorems",
            "Transient Response",
            "Sinusoidal Steady-State Analysis",
            "Resonance",
            "Three-Phase Circuits",
            "Coupled Circuits",
            "Two-Port Networks",
            "Network Topology",
            "Filters",
            "Magnetically Coupled Circuits"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Electromagnetic Fields" -> listOf(
            "Electrostatics",
            "Magnetostatics",
            "Maxwell's Equations",
            "Electromagnetic Waves",
            "Poynting Vector",
            "Transmission Lines",
            "Smith Chart",
            "Waveguides",
            "Electromagnetic Interference",
            "Grounding and Shielding"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Signals & Systems" -> listOf(
            "Continuous-Time and Discrete-Time Signals",
            "LTI Systems",
            "Fourier Series",
            "Fourier Transform",
            "Laplace Transform",
            "Z-Transform",
            "Sampling Theorem",
            "Discrete Fourier Transform (DFT)"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Electrical Machines" -> listOf(
            "DC Machines: Construction and Principle",
            "DC Motor Characteristics",
            "DC Generator Characteristics",
            "Transformers: Principle and Construction",
            "Transformer Testing and Efficiency",
            "Three-Phase Induction Motors",
            "Single-Phase Induction Motors",
            "Synchronous Machines",
            "Stepper Motors",
            "Brushless DC Motors",
            "Special Machines",
            "Machine Testing and Performance"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Power Systems" -> listOf(
            "Power Generation",
            "Transmission Line Parameters",
            "Performance of Transmission Lines",
            "Distribution Systems",
            "Per-Unit System",
            "Fault Analysis",
            "Circuit Breakers",
            "Protection Schemes",
            "Power System Stability",
            "Load Flow Studies",
            "Economic Load Dispatch"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Control Systems" -> listOf(
            "Transfer Functions",
            "Block Diagrams",
            "Signal Flow Graphs",
            "Time Domain Analysis",
            "Frequency Domain Analysis",
            "Routh-Hurwitz Criterion",
            "Root Locus",
            "Bode Plot",
            "Nyquist Criterion",
            "Compensators",
            "State Space Analysis"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Measurements" -> listOf(
            "Measurement of Voltage and Current",
            "Measurement of Power and Energy",
            "Measurement of Resistance",
            "Measurement of Inductance and Capacitance",
            "AC and DC Bridges",
            "Instrument Transformers",
            "Digital Instruments",
            "Transducers and Sensors"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Analog & Digital Electronics" -> listOf(
            "Diode Circuits",
            "Transistor Amplifiers",
            "Operational Amplifiers",
            "Oscillators",
            "Number Systems and Logic Gates",
            "Combinational and Sequential Circuits",
            "A/D and D/A Converters",
            "Microprocessor Basics",
            "Memory Devices"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Power Electronics" -> listOf(
            "Power Semiconductor Devices",
            "Rectifiers",
            "Inverters",
            "Choppers",
            "AC-AC Converters",
            "PWM Techniques"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "General Aptitude" -> listOf(
            "Verbal Ability",
            "Numerical Ability",
            "Logical Reasoning",
            "Data Interpretation",
            "Analytical Ability",
            "Spatial Aptitude"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        else -> emptyList()
    }
    
    private fun getMEChapters(subjectId: Int, subjectName: String): List<ChapterEntity> = when (subjectName) {
        "Engineering Mathematics" -> listOf(
            "Linear Algebra",
            "Calculus",
            "Differential Equations",
            "Probability and Statistics",
            "Vector Calculus",
            "Numerical Methods"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Engineering Mechanics" -> listOf(
            "Free-Body Diagrams",
            "Equilibrium of Rigid Bodies",
            "Trusses and Frames",
            "Friction",
            "Centroid and Center of Gravity",
            "Moment of Inertia",
            "Kinematics of Particles",
            "Dynamics of Particles",
            "Work-Energy Principle"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Mechanics of Materials" -> listOf(
            "Stress and Strain",
            "Elastic Constants",
            "Axial Loading",
            "Shear Force and Bending Moment",
            "Bending Stress",
            "Shear Stress in Beams",
            "Deflection of Beams",
            "Torsion",
            "Thin and Thick Cylinders",
            "Principal Stresses",
            "Theories of Failure"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Theory of Machines" -> listOf(
            "Mechanisms and Machines",
            "Kinematic Chains",
            "Velocity and Acceleration Analysis",
            "Gears and Gear Trains",
            "Flywheels",
            "Governors",
            "Balancing of Rotating Masses",
            "Balancing of Reciprocating Masses",
            "Cams and Followers"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Vibrations" -> listOf(
            "Free Vibration of Single DOF Systems",
            "Forced Vibration of Single DOF Systems",
            "Vibration Isolation",
            "Critical Speed of Shafts",
            "Multi-DOF Systems",
            "Modal Analysis"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Machine Design" -> listOf(
            "Design for Static Loading",
            "Design for Dynamic Loading",
            "Fatigue and Fracture",
            "Design of Fasteners",
            "Design of Shafts and Keys",
            "Design of Couplings",
            "Design of Bearings",
            "Design of Springs",
            "Design of Gears",
            "Welded and Riveted Joints"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Fluid Mechanics" -> listOf(
            "Fluid Properties",
            "Fluid Statics",
            "Buoyancy and Flotation",
            "Fluid Kinematics",
            "Fluid Dynamics: Bernoulli's Equation",
            "Flow Through Pipes",
            "Boundary Layer Theory",
            "Dimensional Analysis"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Thermodynamics" -> listOf(
            "Laws of Thermodynamics",
            "Thermodynamic Systems and Processes",
            "Heat and Work Transfer",
            "Properties of Pure Substances",
            "Ideal and Real Gases",
            "Thermodynamic Cycles",
            "Carnot, Rankine, Otto, Diesel Cycles",
            "Refrigeration Cycles",
            "Psychrometry",
            "Combustion and Fuels"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Heat Transfer" -> listOf(
            "Conduction",
            "Convection",
            "Radiation",
            "Heat Exchangers",
            "Boiling and Condensation"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Manufacturing Processes" -> listOf(
            "Metal Casting",
            "Forming Processes",
            "Joining Processes: Welding",
            "Machining Processes",
            "Non-Conventional Machining",
            "Metrology and Inspection",
            "Computer Integrated Manufacturing",
            "Additive Manufacturing"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Industrial Engineering" -> listOf(
            "Production Planning and Control",
            "Inventory Control",
            "Operations Research",
            "Work Study",
            "Quality Control",
            "Total Quality Management (TQM)"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "General Aptitude" -> listOf(
            "Verbal Ability",
            "Numerical Ability",
            "Logical Reasoning",
            "Data Interpretation",
            "Analytical Ability",
            "Spatial Aptitude"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        else -> emptyList()
    }
    
    private fun getCEChapters(subjectId: Int, subjectName: String): List<ChapterEntity> = when (subjectName) {
        "Engineering Mathematics" -> listOf(
            // Core Mathematics (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Linear Algebra", orderIndex = 1, category = "Core Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Calculus", orderIndex = 2, category = "Core Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Differential Equations", orderIndex = 3, category = "Core Mathematics"),
            
            // Applied Mathematics (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Probability and Statistics", orderIndex = 4, category = "Applied Mathematics"),
            ChapterEntity(subjectId = subjectId, name = "Numerical Methods", orderIndex = 5, category = "Applied Mathematics")
        )
        
        "Structural Engineering" -> listOf(
            // Structural Analysis (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Analysis of Statically Determinate Structures", orderIndex = 1, category = "Structural Analysis"),
            ChapterEntity(subjectId = subjectId, name = "Analysis of Statically Indeterminate Structures", orderIndex = 2, category = "Structural Analysis"),
            ChapterEntity(subjectId = subjectId, name = "Deflection of Structures", orderIndex = 3, category = "Structural Analysis"),
            ChapterEntity(subjectId = subjectId, name = "Influence Lines", orderIndex = 4, category = "Structural Analysis"),
            
            // Structural Design (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Design of Steel Structures", orderIndex = 5, category = "Structural Design"),
            ChapterEntity(subjectId = subjectId, name = "Design of Concrete Structures", orderIndex = 6, category = "Structural Design")
        )
        
        "Geotechnical Engineering" -> listOf(
            "Soil Properties and Classification",
            "Permeability and Seepage",
            "Compaction and Consolidation",
            "Shear Strength of Soil",
            "Foundation Engineering"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Water Resources Engineering" -> listOf(
            "Hydrology",
            "Groundwater",
            "Open Channel Flow",
            "Hydraulic Structures",
            "Irrigation Engineering"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Environmental Engineering" -> listOf(
            "Water Supply Engineering",
            "Wastewater Engineering",
            "Air Pollution",
            "Solid Waste Management",
            "Environmental Impact Assessment"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Transportation Engineering" -> listOf(
            "Highway Engineering",
            "Traffic Engineering",
            "Railway Engineering",
            "Airport Engineering",
            "Geometric Design"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Surveying" -> listOf(
            "Leveling",
            "Theodolite Surveying",
            "Tacheometry",
            "Triangulation",
            "GPS and Remote Sensing"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "General Aptitude" -> listOf(
            "Verbal Ability",
            "Numerical Ability",
            "Logical Reasoning",
            "Data Interpretation",
            "Analytical Ability",
            "Spatial Aptitude"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        else -> emptyList()
    }
    
    private fun getDAChapters(subjectId: Int, subjectName: String): List<ChapterEntity> = when (subjectName) {
        "Probability & Statistics" -> listOf(
            // Probability Theory (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Probability Axioms", orderIndex = 1, category = "Probability Theory"),
            ChapterEntity(subjectId = subjectId, name = "Random Variables", orderIndex = 2, category = "Probability Theory"),
            ChapterEntity(subjectId = subjectId, name = "Probability Distributions", orderIndex = 3, category = "Probability Theory"),
            
            // Statistical Methods (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Expectation and Variance", orderIndex = 4, category = "Statistical Methods"),
            ChapterEntity(subjectId = subjectId, name = "Hypothesis Testing", orderIndex = 5, category = "Statistical Methods"),
            ChapterEntity(subjectId = subjectId, name = "Sampling Theory", orderIndex = 6, category = "Statistical Methods"),
            
            // Advanced Statistics (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Regression Analysis", orderIndex = 7, category = "Advanced Statistics"),
            ChapterEntity(subjectId = subjectId, name = "Bayesian Statistics", orderIndex = 8, category = "Advanced Statistics")
        )
        
        "Linear Algebra" -> listOf(
            // Fundamentals (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Vectors and Matrices", orderIndex = 1, category = "Fundamentals"),
            ChapterEntity(subjectId = subjectId, name = "Linear Transformations", orderIndex = 2, category = "Fundamentals"),
            ChapterEntity(subjectId = subjectId, name = "Vector Spaces", orderIndex = 3, category = "Fundamentals"),
            
            // Advanced Concepts (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Eigenvalues and Eigenvectors", orderIndex = 4, category = "Advanced Concepts"),
            ChapterEntity(subjectId = subjectId, name = "Matrix Decomposition (SVD, QR)", orderIndex = 5, category = "Advanced Concepts"),
            ChapterEntity(subjectId = subjectId, name = "Principal Component Analysis (PCA)", orderIndex = 6, category = "Advanced Concepts")
        )
        
        "Calculus & Optimization" -> listOf(
            // Calculus (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Differential Calculus", orderIndex = 1, category = "Calculus"),
            ChapterEntity(subjectId = subjectId, name = "Integral Calculus", orderIndex = 2, category = "Calculus"),
            ChapterEntity(subjectId = subjectId, name = "Multivariable Calculus", orderIndex = 3, category = "Calculus"),
            
            // Optimization (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Gradient Descent", orderIndex = 4, category = "Optimization"),
            ChapterEntity(subjectId = subjectId, name = "Lagrange Multipliers", orderIndex = 5, category = "Optimization"),
            ChapterEntity(subjectId = subjectId, name = "Convex Optimization", orderIndex = 6, category = "Optimization")
        )
        
        "Programming & Data Structures" -> listOf(
            // Core Programming (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Python Programming", orderIndex = 1, category = "Core Programming"),
            ChapterEntity(subjectId = subjectId, name = "Object-Oriented Programming", orderIndex = 2, category = "Core Programming"),
            
            // Data Structures (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Arrays and Strings", orderIndex = 3, category = "Data Structures"),
            ChapterEntity(subjectId = subjectId, name = "Linked Lists", orderIndex = 4, category = "Data Structures"),
            ChapterEntity(subjectId = subjectId, name = "Trees and Graphs", orderIndex = 5, category = "Data Structures"),
            ChapterEntity(subjectId = subjectId, name = "Hashing", orderIndex = 6, category = "Data Structures"),
            
            // Practical Applications (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Algorithms and Complexity", orderIndex = 7, category = "Practical Applications"),
            ChapterEntity(subjectId = subjectId, name = "Data Manipulation with Pandas", orderIndex = 8, category = "Practical Applications")
        )
        
        "Database Management" -> listOf(
            "Relational Model",
            "SQL Queries",
            "Normalization",
            "Indexing",
            "Transactions and Concurrency",
            "NoSQL Databases"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        "Machine Learning" -> listOf(
            // Learning Paradigms (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Supervised Learning", orderIndex = 1, category = "Learning Paradigms"),
            ChapterEntity(subjectId = subjectId, name = "Unsupervised Learning", orderIndex = 2, category = "Learning Paradigms"),
            
            // Algorithms (4 chapters)
            ChapterEntity(subjectId = subjectId, name = "Linear and Logistic Regression", orderIndex = 3, category = "Algorithms"),
            ChapterEntity(subjectId = subjectId, name = "Decision Trees and Random Forests", orderIndex = 4, category = "Algorithms"),
            ChapterEntity(subjectId = subjectId, name = "Support Vector Machines", orderIndex = 5, category = "Algorithms"),
            ChapterEntity(subjectId = subjectId, name = "Neural Networks", orderIndex = 6, category = "Algorithms"),
            
            // Model Improvement (2 chapters)
            ChapterEntity(subjectId = subjectId, name = "Model Evaluation and Validation", orderIndex = 7, category = "Model Improvement"),
            ChapterEntity(subjectId = subjectId, name = "Ensemble Methods", orderIndex = 8, category = "Model Improvement")
        )
        
        "Artificial Intelligence" -> listOf(
            // Core AI (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Search Algorithms", orderIndex = 1, category = "Core AI"),
            ChapterEntity(subjectId = subjectId, name = "Knowledge Representation", orderIndex = 2, category = "Core AI"),
            ChapterEntity(subjectId = subjectId, name = "Logic and Reasoning", orderIndex = 3, category = "Core AI"),
            
            // Applied AI (3 chapters)
            ChapterEntity(subjectId = subjectId, name = "Natural Language Processing", orderIndex = 4, category = "Applied AI"),
            ChapterEntity(subjectId = subjectId, name = "Computer Vision", orderIndex = 5, category = "Applied AI"),
            ChapterEntity(subjectId = subjectId, name = "Reinforcement Learning", orderIndex = 6, category = "Applied AI")
        )
        
        "General Aptitude" -> listOf(
            "Verbal Ability",
            "Numerical Ability",
            "Logical Reasoning",
            "Data Interpretation",
            "Analytical Ability",
            "Spatial Aptitude"
        ).mapIndexed { index, chapter -> ChapterEntity(subjectId = subjectId, name = chapter, orderIndex = index + 1) }
        
        else -> emptyList()
    }
}
