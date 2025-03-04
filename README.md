# 📂 File Sharing App  
_A secure, encrypted file-sharing platform using Java._

## 🚀 Project Overview  
The File Sharing App allows users to securely upload, download, and share files using end-to-end encryption (E2EE). The application follows a **Client-Server architecture**, ensuring secure communication via **TLS**, user authentication, and access control.

## 📌 Features  
✅ **End-to-End Encryption (AES-256 + RSA)** for file security  
✅ **Secure TLS Communication** between client and server  
✅ **Drag & Drop Uploads** via an intuitive JavaFX UI  
✅ **User Authentication & Permissions** with password hashing (`BCrypt`)  
✅ **Multi-threaded Server** for concurrent file transfers  
✅ **MySQL Database** for user management and metadata storage  
✅ **Dockerized Deployment** for easy setup  

## 📁 Project Structure  

## 🛠️ Tech Stack  
- **Java 17+** (Server & Client)  
- **JavaFX** (UI)  
- **MySQL** (User & File Metadata Storage)  
- **Sockets & Multi-threading** (Server-Client Communication)  
- **TLS Encryption** (`SSLSocket` & `SSLServerSocket`)  
- **AES-256 & RSA** (File Encryption)  
- **Gradle**  
- **Git/GitHub** (Version Control)  

## 💻 Installation & Setup  
### 1. Prerequisites  
- **Java 17+** installed  
- **Gradle** installed 
- **MySQL** running  

### 2. Clone the Repository  
```sh
git clone https://github.com/dcruz5/file-sharing.git
cd file-sharing
```

### 3. Set Up the Server
```sh
cd server
./gradlew build
./gradlew run
```

### 4. Run the Client
```sh
cd client
./gradlew run
```
## 📜 API Endpoints (Server)

| Endpoint  | Method | Description                      |
|-----------|--------|----------------------------------|
| `/login`  | POST   | Authenticate a user            |
| `/upload` | POST   | Upload a file (encrypted)      |
| `/download` | GET  | Download a file (decrypted)    |
| `/share`  | POST   | Share a file with another user |

