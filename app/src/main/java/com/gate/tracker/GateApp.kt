package com.gate.tracker

import android.app.Application
import com.gate.tracker.data.local.GateDatabase
import com.gate.tracker.data.repository.GateRepository

class GateApp : Application() {
    
    lateinit var database: GateDatabase
        private set
    
    lateinit var repository: GateRepository
        private set
    
    override fun onCreate() {
        super.onCreate()
        database = GateDatabase.getInstance(this)
        val driveManager = com.gate.tracker.data.drive.DriveManager(this)
        repository = GateRepository(database, driveManager)
    }
}
