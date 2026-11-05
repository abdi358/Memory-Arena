# 🧠 Memory Arena

<p align="center">
  <img src="app/src/main/res/drawable/app_logo.png" width="180" alt="Memory Arena Logo" style="border-radius: 28px;" />
</p>

<p align="center">
  <strong>Think Fast. Remember More. Win Together.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Kotlin-2.4-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/API-30%2B-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Release-v1.0.0-green?style=for-the-badge" />
</p>

---

## 🚀 Instant Installation (No Code / No Build Needed!)

You don't need Android Studio or any coding knowledge to play **Memory Arena**! You can install the pre-compiled application directly onto your Android phone or tablet:

### 📥 [Download Latest Release APK](https://github.com/abdi358/Memory-Arena/releases/latest)

### 📲 Step-by-Step Install Guide:
1. **Download APK**: Tap the link above on your Android phone or tablet, or download the `MemoryArena-v1.0.0.apk` from the [Releases](https://github.com/abdi358/Memory-Arena/releases) page.
2. **Open the File**: Once downloaded, tap the download notification or open it from your **Files** / **Downloads** app.
3. **Allow Installation**:
   * If your device prompts *"For your security, your phone is not allowed to install unknown apps from this source"*, tap **Settings**.
   * Toggle **Allow from this source** to ON.
4. **Install**: Tap **Install** and then tap **Open**.
5. **Enjoy the game!** Gather your friends around a table and start playing.

---

## 🌟 Game Overview

**Memory Arena** is a modern, fast-paced tabletop multiplayer memory game built natively for Android. Designed specifically for pass-and-play or flat-table play, players sit face-to-face around a single device and compete to flip, remember, and match pairs of cards before the timer runs out.

---

## ✨ Key Features

### 👥 1 to 4 Player Modes
* **🎮 1 Player (Solo Challenge)**: Practice your memorization against the 20-second countdown timer.
* **⚔️ 2 Players (1v1 Duel)**: Diagonal surround battle on a 16-card grid (4×4).
* **⚡ 3 Players (Trio Clash)**: 3-way arena showdown on a 16-card grid (4×4).
* **🔥 4 Players (Tabletop Arena)**: Full 4-corner battle with **2 additional rows** — 24 cards (4×6, 12 pairs)!

### 🔄 Tabletop Head-to-Head Rotation
* When playing tabletop across from a friend, the top 2 player pods are **automatically inverted $180^\circ$**.
* The player sitting on the other side of the table can read their name, avatar, score, timer ring, and active turn status completely right-side up from their seat!

### 🎨 4 Signature Player Colors
Each player is separated by a dedicated signature color throughout the entire game:
* 🔴 **Player 1**: Vivid Red (`#FFFF4D4D` / `#DC2626`)
* 🟢 **Player 2**: Neon Green (`#00E676` / `#16A34A`)
* 🔵 **Player 3**: Electric Sky Blue (`#38BDF8` / `#0284C7`)
* 🟡 **Player 4**: Cyber Gold Yellow (`#FFD600` / `#D97706`)

Matched cards permanently illuminate in the aura color of the player who matched them!

### ☀️ Outdoor Sunlight Mode & 🌙 Cyberpunk Neon Night Mode
Playing outside at the park, patio, or on a sunny day?
* **🌙 Cyberpunk Night Mode**: Deep neon glow interface (`#131220`) for standard and indoor play.
* **☀️ Outdoor Sunlight Mode**: High-contrast, anti-glare slate background (`#F1F5F9`) with crisp deep navy card backs and pure white card fronts that prevent glare and remain razor-sharp under direct sunlight.
* Toggle between modes anytime from the Match Setup screen or with the live HUD toggle button during an active game!

### ✏️ Custom & Editable Player Names
* Set custom names for each player before the match starts.
* Each player's avatar initial and status tag (`P1`, `P2`, `P3`, `P4`) update dynamically.

### ⏱️ Turn Timer & Combo Scoring
* **20-Second Countdown**: Each player has 20 seconds to make their moves. A live radial progress ring tracks remaining time.
* **Combo Multiplier**: Score bonus points for consecutive matches ($100 \text{ pts} + \text{combo} \times 20$).
* **Extra Turn Bonus**: Successfully finding a pair grants an immediate bonus turn!

---

## 📱 System Requirements

* **OS Version**: Android 11.0 (API Level 30) or higher
* **Screen**: Optimized for all Android phones and tablets
* **Architecture**: Universal (supports ARM64, ARMv7, x86, and x86_64)

---

## 🛠️ Building from Source (For Developers)

If you'd like to build the project locally using Android Studio:

1. **Clone the repository**:
   ```bash
   git clone git@github.com:abdi358/Memory-Arena.git
   cd Memory-Arena
   ```
2. **Open in Android Studio**:
   * Open Android Studio Ladybug / Meerkat or later.
   * Select **Open** and choose the `Memory-Arena` directory.
3. **Build the APK**:
   ```bash
   ./gradlew assembleDebug
   ```
4. The generated APK will be at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.
