# 🌱 KissanSathi — ESP32 NPK Sensor Module

> Soil Nutrient Monitoring Module for the **KissanSathi Smart Agriculture System**

This module connects an **RS485 Modbus NPK Soil Sensor** with an **ESP32** to measure soil Nitrogen (N), Phosphorus (P), and Potassium (K).

The ESP32 reads the sensor data, performs basic validation/filtering, and sends the measurements over Wi-Fi to the KissanSathi Spring Boot backend.

---

## 📌 System Architecture

```text
┌─────────────────────────┐
│    NPK Soil Sensor      │
│                         │
│  Nitrogen (N)           │
│  Phosphorus (P)         │
│  Potassium (K)          │
└────────────┬────────────┘
             │
             │ RS485 / Modbus RTU
             ▼
┌─────────────────────────┐
│        MAX485           │
│    RS485 ↔ UART         │
└────────────┬────────────┘
             │ UART
             ▼
┌─────────────────────────┐
│         ESP32           │
│                         │
│  Sensor Reading         │
│  Validation             │
│  Filtering              │
│  Wi-Fi Communication    │
└────────────┬────────────┘
             │ Wi-Fi / HTTP
             ▼
┌─────────────────────────┐
│     Spring Boot API     │
│                         │
│  Data Processing        │
│  Storage                │
│  Recommendation Engine  │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       MySQL DB          │
└─────────────────────────┘
```

---

# 🎯 Objective

The objective of this module is to provide real-time soil nutrient data to the KissanSathi application.

The sensor measures:

* Nitrogen (N)
* Phosphorus (P)
* Potassium (K)

The ESP32 acts as the edge controller between the soil sensor and the KissanSathi backend.

---

# 🧰 Hardware Requirements

| Component             | Purpose                 |
| --------------------- | ----------------------- |
| ESP32 DevKit          | Microcontroller + Wi-Fi |
| RS485 NPK Soil Sensor | Measures N, P and K     |
| MAX485 Module         | RS485 ↔ UART conversion |
| Jumper Wires          | Connections             |
| External Power Supply | Sensor power            |
| Soil Sample           | Measurement/testing     |

> **Important:** Check the sensor's operating voltage and wiring from its datasheet before powering it.

---

# 💻 Software Stack

| Component             | Technology                   |
| --------------------- | ---------------------------- |
| Microcontroller       | ESP32                        |
| Firmware              | C/C++                        |
| Development Framework | Arduino / ESP32 Arduino Core |
| Sensor Protocol       | Modbus RTU                   |
| Communication         | RS485                        |
| Network               | Wi-Fi                        |
| Backend               | Spring Boot                  |
| Backend Language      | Java                         |
| Database              | MySQL                        |
| Mobile Application    | Android / Kotlin             |

---

# 🔌 Communication Flow

```text
NPK Sensor
    │
    │ RS485
    ▼
MAX485
    │
    │ UART
    ▼
ESP32
    │
    │ Wi-Fi
    ▼
Spring Boot
    │
    ▼
MySQL
    │
    ▼
Recommendation Engine
    │
    ▼
KissanSathi Android App
```

---

# 🔧 RS485 Wiring

Typical MAX485 connection:

| MAX485 | ESP32              |
| ------ | ------------------ |
| RO     | ESP32 RX           |
| DI     | ESP32 TX           |
| RE     | GPIO               |
| DE     | GPIO               |
| VCC    | Appropriate supply |
| GND    | GND                |

Sensor side:

| NPK Sensor | MAX485              |
| ---------- | ------------------- |
| A / D+     | A                   |
| B / D-     | B                   |
| GND        | GND                 |
| VCC        | Sensor-rated supply |

> **Do not blindly use these connections for every sensor. Verify the sensor and MAX485 module datasheets before powering the hardware.**

---

# 📡 Modbus RTU

The sensor communicates using **Modbus RTU over RS485**.

The ESP32 acts as the Modbus master and the NPK sensor acts as the slave.

```text
ESP32
  │
  │ Modbus Request
  ▼
NPK Sensor
  │
  │ Modbus Response
  ▼
ESP32
```

Typical parameters may include:

```text
Baud Rate : Sensor-specific
Data Bits : 8
Parity    : Sensor-specific
Stop Bits : Sensor-specific
Slave ID  : Sensor-specific
```

These values **must be taken from the purchased sensor's datasheet**.

---

# 🧾 Sensor Register Configuration

The exact register addresses depend on the sensor model.

Configure them here:

```cpp
#define NPK_SLAVE_ID        <SLAVE_ID>

#define NITROGEN_REGISTER   <N_REGISTER>
#define PHOSPHORUS_REGISTER <P_REGISTER>
#define POTASSIUM_REGISTER  <K_REGISTER>
```

Example structure:

```cpp
uint16_t nitrogen;
uint16_t phosphorus;
uint16_t potassium;
```

> Never assume register addresses from another NPK sensor. Different manufacturers/models can use different Modbus maps.

---

# 🧠 ESP32 Firmware Responsibilities

The ESP32 firmware performs the following operations:

### 1. Initialize hardware

```text
ESP32
 ↓
UART
 ↓
RS485
```

### 2. Initialize Modbus

```text
Set Slave ID
Set Baud Rate
Initialize Modbus RTU
```

### 3. Request NPK values

```text
Read Nitrogen
Read Phosphorus
Read Potassium
```

### 4. Validate readings

```text
Check communication
Check response
Check range
Check invalid values
```

### 5. Filter measurements

Multiple readings can be collected to reduce transient noise.

```text
Reading 1
Reading 2
Reading 3
Reading 4
Reading 5
     ↓
Median / Average
     ↓
Final Reading
```

### 6. Send data to backend

```text
ESP32
  ↓
Wi-Fi
  ↓
HTTP POST
  ↓
Spring Boot
```

---

# 📦 Example Sensor Payload

The ESP32 can send data in JSON format:

```json
{
  "nitrogen": 82,
  "phosphorus": 36,
  "potassium": 118
}
```

With additional information:

```json
{
  "deviceId": "ESP32-NPK-001",
  "nitrogen": 82,
  "phosphorus": 36,
  "potassium": 118,
  "timestamp": "2026-09-25T15:30:00Z"
}
```

---

# 🌐 Backend API

Example endpoint:

```http
POST /api/sensor/npk
```

Request:

```json
{
  "deviceId": "ESP32-NPK-001",
  "nitrogen": 82,
  "phosphorus": 36,
  "potassium": 118
}
```

Spring Boot receives the measurements and stores them in MySQL.

---

# 🗄️ Database Structure

Example table:

```sql
CREATE TABLE soil_npk_readings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id VARCHAR(100),
    nitrogen DOUBLE,
    phosphorus DOUBLE,
    potassium DOUBLE,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

# 📊 Data Processing Pipeline

```text
Raw Sensor Data
       ↓
Communication Validation
       ↓
Range Validation
       ↓
Noise Filtering
       ↓
Calibration / Correction
       ↓
Validated NPK
       ↓
Database
       ↓
Recommendation Engine
```

---

# 🧪 Calibration

Sensor calibration should be performed according to the manufacturer's instructions and, where possible, verified against reference soil-test measurements.

Recommended validation process:

```text
Collect Soil Sample
       ↓
Take Multiple Sensor Readings
       ↓
Obtain Reference/Laboratory Values
       ↓
Compare Sensor vs Reference
       ↓
Calculate Correction
       ↓
Validate on Additional Samples
```

For a calibration model:

```text
Reference_N = a × Sensor_N + b
Reference_P = c × Sensor_P + d
Reference_K = e × Sensor_K + f
```

The coefficients should be derived from actual reference measurements rather than arbitrary values.

---

# 🛡️ Reading Validation

The firmware should reject or flag readings when:

* Modbus communication fails
* Sensor does not respond
* CRC/error response occurs
* Reading is outside the expected range
* Sensor returns an invalid value
* Multiple consecutive readings are inconsistent

Example:

```cpp
if (readingValid) {
    sendToBackend();
} else {
    logSensorError();
}
```

---

# 📡 Wi-Fi Communication

The ESP32 connects to the configured Wi-Fi network.

```cpp
const char* ssid = "YOUR_WIFI";
const char* password = "YOUR_PASSWORD";
```

The backend URL should be configurable:

```cpp
const char* serverUrl =
    "http://YOUR_SERVER/api/sensor/npk";
```

> Do not commit real Wi-Fi credentials or API secrets to GitHub.

---

# 📁 Suggested Project Structure

```text
kissan-sathi-npk/
│
├── firmware/
│   ├── src/
│   │   ├── main.cpp
│   │   ├── npk_sensor.cpp
│   │   ├── npk_sensor.h
│   │   ├── modbus.cpp
│   │   ├── modbus.h
│   │   ├── wifi_manager.cpp
│   │   └── wifi_manager.h
│   │
│   ├── include/
│   │   └── config.h
│   │
│   └── README.md
│
├── backend/
│   └── ...
│
└── README.md
```

---

# 🚀 Development Roadmap

## Phase 1 — ESP32 Setup

* [ ] Install ESP32 board support
* [ ] Test ESP32
* [ ] Test Serial Monitor
* [ ] Test Wi-Fi connection

## Phase 2 — RS485

* [ ] Connect MAX485
* [ ] Test UART
* [ ] Verify A/B lines
* [ ] Test Modbus communication

## Phase 3 — NPK Sensor

* [ ] Connect NPK sensor
* [ ] Configure Modbus parameters
* [ ] Read Nitrogen
* [ ] Read Phosphorus
* [ ] Read Potassium

## Phase 4 — Data Processing

* [ ] Validate readings
* [ ] Add filtering
* [ ] Implement calibration
* [ ] Test repeated measurements

## Phase 5 — Backend

* [ ] Create Spring Boot API
* [ ] Receive ESP32 data
* [ ] Store NPK readings
* [ ] Connect Recommendation Engine

## Phase 6 — Android

* [ ] Display NPK values
* [ ] Display soil status
* [ ] Generate fertilizer recommendation
* [ ] Show historical readings

---

# 🧪 Testing Strategy

Testing should be performed at multiple levels.

### Hardware Test

```text
Sensor → MAX485 → ESP32
```

### Communication Test

```text
ESP32 → Modbus Request
Sensor → Modbus Response
```

### Network Test

```text
ESP32 → Wi-Fi → Spring Boot
```

### Database Test

```text
Spring Boot → MySQL
```

### Application Test

```text
MySQL
  ↓
Recommendation Engine
  ↓
Android
```

---

# 🔐 Security

Do not upload the following to GitHub:

```text
Wi-Fi password
API keys
Database password
JWT secrets
Production server credentials
```

Use configuration files or environment variables instead.

---

# 🌱 KissanSathi Integration

The NPK module is one component of the complete KissanSathi system.

```text
                  KISSANSATHI
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
     ESP32          Weather         Mandi
        │             API            Data
        ▼
     NPK Data
        │
        └──────────────┐
                       ▼
               Recommendation
                   Engine
                       │
             ┌─────────┴─────────┐
             ▼                   ▼
        Fertilizer           Advisory
        Recommendation        Data
             │                   │
             └─────────┬─────────┘
                       ▼
                 Android App
```

---

# 🎓 Major Project Value

This module demonstrates:

* IoT-based soil monitoring
* Embedded systems
* ESP32 programming
* RS485 communication
* Modbus RTU
* Sensor data acquisition
* Data validation
* Sensor calibration
* Wi-Fi communication
* REST API integration
* Spring Boot backend
* MySQL data storage
* Smart fertilizer recommendation

---

# ⚠️ Important Notes

1. **Always use the exact datasheet of the purchased NPK sensor.**
2. Do not assume Modbus register addresses from another sensor.
3. Verify sensor operating voltage before connecting it.
4. RS485 A/B labels can differ between manufacturers.
5. Calibration coefficients should come from experimental/reference data.
6. Do not use a single sensor reading as the basis for a critical recommendation.
7. Keep raw sensor data and processed/calibrated data separately when possible.

---

# 🛠️ Technologies

```text
ESP32
C/C++
Arduino ESP32 Core
RS485
Modbus RTU
Wi-Fi
HTTP/REST
Spring Boot
Java
MySQL
Android
Kotlin
```

---

# 👨‍💻 Project

**KissanSathi — Smart Agriculture & Fertilizer Recommendation System**

The NPK sensor module provides real-time soil nutrient measurements that can be consumed by the KissanSathi Recommendation Engine to support crop-specific fertilizer recommendations.

---

## 📌 Status

```text
[ ] ESP32 setup
[ ] NPK sensor purchased
[ ] RS485 communication
[ ] Modbus integration
[ ] NPK reading
[ ] Calibration
[ ] Backend integration
[ ] Database integration
[ ] Recommendation Engine integration
[ ] Android integration
```
