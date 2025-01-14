# TaskIT: Student Schedule Manager

## Description
**TaskIT** is a mobile application designed for students to efficiently manage their schedules, classes, and assignments. The app helps users stay on top of their academic commitments with features such as:

- Adding and managing class schedules.
- Viewing a weekly timetable with detailed class information.
- Notifications for upcoming classes.
- Integration with Google Maps for classroom location.
- Location-based notifications to provide a "Welcome to Politehnica" alert when near the university.
- Persistent storage of user preferences like theme color and dark mode.

---

## Features
- **Add Class**: Easily add class details including title, day, time, and location.
- **Class Notifications**: Receive reminders for upcoming classes.
- **Location Alerts**: Notify users when they are near Politehnica.
- **Customizable Theme**: Choose between different themes and enable/disable dark mode.
- **Weekly Timetable**: Display all classes for the current week.
- **Persistent Storage**: Save user preferences and class data securely.

---

## Components Used
### **Foreground Services**
- **Purpose**: To monitor user location in the background and send a location-based notification when the user is near Politehnica.
- **Implementation**: The `LocationForegroundService` runs continuously to check user location and trigger notifications.

### **Broadcast Receivers**
- **Purpose**: To handle system events and application-wide actions.
- **Implementation**: The `NotificationReceiver` triggers class reminders when the alarm event fires.

### **Intents**
- **Purpose**: To navigate between app components and pass data between activities.
- **Implementation**: Used for navigation, such as starting `AddClassActivity` or `ClassActivity`.

### **Activities**
- **Purpose**: Handle user interactions and display app content.
- **Implementation**: 
  - `MainActivity`: Main dashboard for managing schedules.
  - `AddClassActivity`: UI for adding a new class.
  - `ClassActivity`: Detailed view of a specific class.

### **Shared Preferences**
- **Purpose**: Store user preferences persistently.
- **Implementation**: Save theme color and dark mode settings.

### **Content Providers**
- **Purpose**: Share data between apps or components.
- **Implementation**: The `ClassScheduleProvider` exposes class schedules to other apps or components.

### **Room Database**
- **Purpose**: Store structured class data locally.
- **Implementation**: Used `Room` for storing class schedules via `ClassEntity` and `ClassDao`.

### **Notifications**
- **Purpose**: Notify users of upcoming classes and important events.
- **Implementation**: Notifications are triggered for classes and location-based events near Politehnica.

### **External API (Google Maps)**
- **Purpose**: Fetch location data to calculate proximity to Politehnica.
- **Implementation**: Used Google Maps API to provide location services and map-based functionality.

---

## GitHub Repository
[TaskIT GitHub Repository](https://github.com/year-3-projects-cti/MDAD.git)
