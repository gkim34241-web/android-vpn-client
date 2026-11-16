package com.example.vpnclient

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var startVpnButton: Button
    private lateinit var stopVpnButton: Button
    private lateinit var statusText: TextView
    private lateinit var pingText: TextView
    private lateinit var speedText: TextView
    
    private var isVpnRunning = false
    private val VPN_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        startVpnButton = findViewById(R.id.startVpnButton)
        stopVpnButton = findViewById(R.id.stopVpnButton)
        statusText = findViewById(R.id.statusText)
        pingText = findViewById(R.id.pingText)
        speedText = findViewById(R.id.speedText)
        
        updateUI()
        
        startVpnButton.setOnClickListener {
            startVpn()
        }
        
        stopVpnButton.setOnClickListener {
            stopVpn()
        }
    }
    
    private fun startVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, VPN_REQUEST_CODE)
        } else {
            startService(Intent(this, vpn.BasicVpnService::class.java))
            isVpnRunning = true
            updateUI()
        }
    }
    
    private fun stopVpn() {
        stopService(Intent(this, vpn.BasicVpnService::class.java))
        isVpnRunning = false
        updateUI()
    }
    
    private fun updateUI() {
        startVpnButton.isEnabled = !isVpnRunning
        stopVpnButton.isEnabled = isVpnRunning
        statusText.text = getString(R.string.vpn_status, 
            if (isVpnRunning) getString(R.string.connected) else getString(R.string.disconnected))
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            startService(Intent(this, vpn.BasicVpnService::class.java))
            isVpnRunning = true
            updateUI()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (isVpnRunning) {
            stopVpn()
        }
    }
}
