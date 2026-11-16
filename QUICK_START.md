# 🚀 Quick Start Guide

Get your Android VPN client running in minutes!

## ⬇️ Download & Setup

### Option 1: Clone from GitHub

```bash
git clone https://github.com/YOUR_USERNAME/android-vpn-client.git
cd android-vpn-client
```

### Option 2: Manual Setup

1. Download this code as ZIP
2. Extract to a folder
3. Open in Android Studio

## 🛠 Build & Run

### First Build

```bash
# In Android Studio:
# 1. Click "Sync Project with Gradle Files"
# 2. Wait for dependencies to download
# 3. Build the project (Build -> Make Project)

# Or from command line:
./gradlew clean assembleDebug
```

### Run on Device

1. **Enable USB Debugging** on your Android device:
   - Settings -> About phone -> Tap "Build number" 7 times
   - Settings -> Developer options -> Enable "USB debugging"

2. **Connect device** via USB

3. **Run the app**:
   - In Android Studio: Click the green "Run" button
   - Select your device
   - Wait for app to install and launch

### Run on Emulator

1. **Create an emulator**:
   - Tools -> Device Manager -> Create Virtual Device
   - Select a device (e.g., Pixel 6)
   - Select system image (Android 12+)
   - Finish

2. **Start the emulator**

3. **Run the app** (same as device)

## 📱 Using the App

### First Launch

1. Open the app
2. You'll see:
   - **Start VPN** button
   - **Stop VPN** button (disabled)
   - **Status** text

### Start VPN

1. Click **Start VPN**
2. You'll see a system dialog asking for VPN permission
3. Click **OK** or **Allow**
4. VPN will start!
5. Status will change to "Connected"

### Stop VPN

1. Click **Stop VPN**
2. VPN will stop
3. Status will change to "Disconnected"

## 🔍 Testing Your Connection

### Check if VPN is Working

1. **Visit whatismyip.com** in your browser
   - Should show different IP (if you have a proxy server configured)

2. **Use IP leak test**:
   - https://ipleak.net
   - https://dnsleaktest.com

### Test Ping

```bash
# On your Android device (via adb shell)
adb shell
ping -c 4 google.com
```

### Test Speed

Use speed test apps:
- Speedtest by Ookla
- Fast.com (by Netflix)

## 🛠 Customization

### Change VPN Configuration

Edit `app/src/main/java/com/example/vpnclient/vpn/BasicVpnService.kt`:

```kotlin
val builder = Builder()
builder.setSession("MyVPN")
    .addAddress("10.0.0.2", 24)  // Change client IP
    .addDnsServer("8.8.8.8")      // Change DNS
    .addRoute("0.0.0.0", 0)       // Change routes
    .setMtu(1500)                 // Change MTU
```

### Add Encryption

For a production VPN, add encryption by forwarding traffic through a secure proxy:

```kotlin
// In BasicVpnService.kt, modify startVpnThread method
private fun startVpnThread(vpnFileDescriptor: ParcelFileDescriptor) {
    vpnThread = Thread {
        val vpnInput = FileInputStream(vpnFileDescriptor.fileDescriptor)
        val vpnOutput = FileOutputStream(vpnFileDescriptor.fileDescriptor)
        
        // Connect to your secure proxy
        val proxySocket = Socket("your-proxy.com", 8080)
        val proxyInput = proxySocket.getInputStream()
        val proxyOutput = proxySocket.getOutputStream()
        
        val buffer = ByteArray(4096)
        while (running) {
            try {
                // Forward traffic through proxy
                val bytesRead = vpnInput.read(buffer)
                if (bytesRead > 0) {
                    proxyOutput.write(buffer, 0, bytesRead)
                }
                
                val proxyBytesRead = proxyInput.read(buffer)
                if (proxyBytesRead > 0) {
                    vpnOutput.write(buffer, 0, proxyBytesRead)
                }
            } catch (e: Exception) {
                break
            }
        }
        
        vpnInput.close()
        vpnOutput.close()
        proxySocket.close()
    }.apply { start() }
}
```

### Add Features

Some ideas:
- Connection statistics (data usage, duration)
- Auto-connect on startup
- Multiple VPN profiles
- Split tunneling
- Kill switch

## 🐞 Troubleshooting

### VPN Permission Denied

**Problem**: User clicks "Deny" on VPN permission dialog

**Solution**: 
1. Go to Settings -> Apps -> VPN Client -> Permissions
2. Enable VPN permission
3. Restart the app

### VPN Not Connecting

**Checklist**:
1. [ ] Is the VPN service running?
2. [ ] Are there any errors in Logcat?

**Debug**:
```bash
adb logcat | grep -i vpn
```

### App Crashes on Start

**Debug**:
```bash
adb logcat
```

Check for errors and stack traces.

## 🎯 Next Steps

1. **Add encryption**: Implement TLS or other encryption
2. **Add server**: Set up a proxy server to route traffic through
3. **Add WireGuard**: For better performance (see below)
4. **Add OpenVPN**: For compatibility (see below)

### Add WireGuard Support

To add WireGuard for better performance:

1. Add dependency to `app/build.gradle`:
```gradle
implementation 'com.wireguard.android:tunnel:1.0.20211029'
```

2. Create a new service for WireGuard
3. Update MainActivity to select between VPN types

### Add OpenVPN Support

To add OpenVPN:

1. Add dependency to `app/build.gradle`:
```gradle
implementation 'de.blinkt:openvpn:0.7.33'
```

2. Create a new service extending OpenVPNService
3. Add OpenVPN configuration

## 📚 Resources

- [Android VpnService Documentation](https://developer.android.com/reference/android/net/VpnService)
- [Android VPN Sample Code](https://github.com/android/network-samples)

## 🎉 Success!

Once you have:
- ✅ App installed on device
- ✅ VPN permission granted
- ✅ Connection established

You're ready to go! Test different configurations and customize for your needs.
