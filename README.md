# Android VPN Client

A simple Android VPN client implementation using **VpnService API** for basic VPN functionality.

## 🚀 Features

- **Basic VPN Service**: Simple implementation using Android's `VpnService` API
- **Easy to use**: Simple UI with start/stop buttons
- **Lightweight**: Minimal dependencies, fast performance
- **Extensible**: Easy to add your own VPN protocols

## 📱 Quick Start

### Prerequisites

- Android Studio (latest version)
- Android SDK 24+ (API 24: Android 7.0 Nougat)
- Kotlin plugin
- Java 8+

### Build & Run

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/android-vpn-client.git
cd android-vpn-client

# Open in Android Studio
# or build from command line
./gradlew assembleDebug
```

### Run on Device

1. Enable USB Debugging on your Android device
2. Connect device via USB
3. Run the app from Android Studio

## 🛠 Project Structure

```
android-vpn-client/
├── app/
│   ├── build.gradle              # App configuration
│   ├── src/main/
│   │   ├── AndroidManifest.xml   # Manifest with permissions
│   │   ├── java/com/example/vpnclient/
│   │   │   ├── MainActivity.kt          # Main UI
│   │   │   └── vpn/
│   │   │       └── BasicVpnService.kt    # VPN implementation
│   │   └── res/                         # Resources
│   │       ├── layout/activity_main.xml  # UI layout
│   │       └── values/                   # Strings, colors, themes
│   └── ...
├── build.gradle                   # Project configuration
├── settings.gradle                # Gradle settings
├── gradle.properties              # Gradle properties
├── .gitignore                     # Git ignore
├── README.md                      # This file
└── QUICK_START.md                 # Quick start guide
```

## 📋 Usage

1. Install the app on your Android device (API 24+)
2. Open the app
3. Click **Start VPN**
4. Grant VPN permission when prompted
5. VPN will start and route all traffic through the virtual interface

## 🔧 How It Works

### BasicVpnService

The `BasicVpnService` class implements Android's `VpnService` API to:

1. **Create a VPN interface** using `VpnService.Builder`
2. **Configure network settings**:
   - Client IP address: `10.0.0.2/24`
   - DNS server: `8.8.8.8` (Google DNS)
   - Route all traffic: `0.0.0.0/0`
   - MTU: `1500`

3. **Forward packets** between the VPN interface and the network

### MainActivity

Simple UI with:
- Start VPN button
- Stop VPN button
- Connection status display

## 📝 Customization

### Change VPN Configuration

Edit `BasicVpnService.kt` to change:
- IP address
- DNS servers
- Routes
- MTU size

```kotlin
val builder = Builder()
builder.setSession("MyVPN")
    .addAddress("10.0.0.2", 24)  // Client IP
    .addDnsServer("8.8.8.8")      // DNS
    .addRoute("0.0.0.0", 0)       // Route all traffic
    .setMtu(1500)                 // MTU
```

### Add Encryption

For a production VPN, you should add encryption. Here's a simple example using a proxy:

```kotlin
// In BasicVpnService.kt
private fun startVpnThread(vpnFileDescriptor: ParcelFileDescriptor) {
    vpnThread = Thread {
        val vpnInput = FileInputStream(vpnFileDescriptor.fileDescriptor)
        val vpnOutput = FileOutputStream(vpnFileDescriptor.fileDescriptor)
        
        // Connect to your proxy server
        val proxySocket = Socket("your-proxy-server.com", 8080)
        val proxyInput = proxySocket.getInputStream()
        val proxyOutput = proxySocket.getOutputStream()
        
        val buffer = ByteArray(4096)
        while (running) {
            try {
                // Read from VPN and forward to proxy
                val bytesRead = vpnInput.read(buffer)
                if (bytesRead > 0) {
                    proxyOutput.write(buffer, 0, bytesRead)
                }
                
                // Read from proxy and forward to VPN
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

### Add Your Own VPN Protocol

1. Create a new service class extending `VpnService`
2. Implement your protocol logic
3. Update `MainActivity` to use your service

## 🔒 Security Notes

1. **This is a basic implementation** - it routes traffic but doesn't encrypt it by default
2. **For production use**, add encryption (TLS, IPsec, etc.)
3. **Validate all inputs** from users
4. **Use secure key generation** for any cryptographic operations
5. **Follow Android security best practices**

## 📊 Performance Tips

1. **Buffer size**: Use optimal buffer sizes (4096-8192 bytes)
2. **Thread priority**: Set thread priority to `Thread.MAX_PRIORITY` for VPN thread
3. **Batch processing**: Process packets in batches when possible
4. **Avoid blocking**: Use non-blocking I/O for better performance

## 🐛 Troubleshooting

### VPN Permission Denied

**Solution**:
1. Go to Settings -> Apps -> VPN Client -> Permissions
2. Enable VPN permission
3. Restart the app

### VPN Not Connecting

**Checklist**:
1. [ ] Is the VPN service running?
2. [ ] Are there any errors in Logcat?
3. [ ] Is the configuration correct?

**Debug**:
```bash
adb logcat | grep -i vpn
```

### App Crashes

**Debug**:
```bash
adb logcat
```

Look for errors and stack traces.

## 📚 Resources

- [Android VpnService Documentation](https://developer.android.com/reference/android/net/VpnService)
- [Android VPN Sample](https://github.com/android/network-samples)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📄 License

MIT License - Feel free to use this code for your projects!
