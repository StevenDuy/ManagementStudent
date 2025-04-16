### 📄 `README.md`

# 📦 Assignment - Java Package

This repository contains a Java package exported from a NetBeans project. You can import and use this package in your own Java project using NetBeans.

---

## 🚀 Getting Started

These instructions will guide you through setting up your environment and using the code in NetBeans.

---

## 🔧 Prerequisites

Before you begin, make sure you have the following installed:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) (version 8 or higher)
- [NetBeans IDE](https://netbeans.apache.org/download/index.html)

---

## 🛠️ Installation Guide

### 1. Install Java JDK

- Go to the [JDK download page](https://www.oracle.com/java/technologies/javase-downloads.html).
- Download and install the version compatible with your system.
- Verify the installation:
  ```bash
  java -version

### 2. Install NetBeans

- Visit [https://netbeans.apache.org/download/index.html](https://netbeans.apache.org/download/index.html)
- Download and install the latest stable version.
- Open NetBeans after installation.

---

## 📁 How to Use This Package in NetBeans

### 1. Extract the package

- Download or clone this repository.
- Extract the `.rar` or `.zip` file if it’s compressed.

### 2. Create a new Java project in NetBeans

1. Open NetBeans.
2. Click on **File > New Project**.
3. Select **Java with Ant > Java Application** (or **Java with Maven**, if preferred).
4. Click **Next**.
5. Name your project (e.g., `MyAssignmentProject`) and click **Finish**.

### 3. Add the package to your project

1. In NetBeans, go to the **Projects** panel.
2. Right-click your project > **Open Project Folder**.
3. Navigate to the `src` directory.
4. Copy the extracted package folder (e.g., `myassignment`) into the `src` folder.
5. Back in NetBeans, right-click the project and select **Refresh** (or **Clean and Build**) to see the package appear.

---

## 📌 Important Notes

- If the package uses a different name than your current project’s structure (e.g., `package com.example.assignment;`), you will need to:
  - Rename the package folder accordingly.
  - Update the `package` declaration line at the top of each `.java` file to match the folder path.

### Example:
If you place the code in:
```
src/com/yourname/assignment/
```
then update the Java files like this:
```java
package com.yourname.assignment;
```

---

## ▶️ Running the Code

- Locate the class that contains the `main()` method.
- Right-click the file in NetBeans > **Run File**, or set it as the main class in project properties:
  1. Right-click the project > **Properties**
  2. Go to **Run**, and set the **Main Class**

---

## 📬 Contact

If you need help or have questions, feel free to reach out via [duyh030774@gmail.com].

---

## 📄 License

This project is provided for educational and demonstration purposes. You are free to modify and use it as needed.
```
