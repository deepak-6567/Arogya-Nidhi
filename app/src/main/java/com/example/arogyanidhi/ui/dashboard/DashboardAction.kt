package com.example.arogyanidhi.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.arogyanidhi.ui.theme.NeonBlue
import com.example.arogyanidhi.ui.theme.NeonPurple

enum class FieldType {
    TEXT,
    TEXT_AREA,
    ACTION_BUTTON,
    RADIO_GROUP
}

data class FormField(
    val label: String,
    val placeholder: String = "",
    val type: FieldType = FieldType.TEXT,
    val initialValue: String = "",
    val actionLabel: String? = null,
    val options: List<String> = emptyList()
)

sealed class DashboardAction(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val formFields: List<FormField> = emptyList()
) {
    object Hospitals : DashboardAction("Hospitals", Icons.Default.LocalHospital, NeonBlue, listOf(
        FormField("Search Location", "Enter city or area"),
        FormField("Hospital Type", type = FieldType.RADIO_GROUP, options = listOf("Government", "Private", "All")),
        FormField("Find Near Me", type = FieldType.ACTION_BUTTON, actionLabel = "USE GPS")
    ))
    
    object Schemes : DashboardAction("Schemes", Icons.AutoMirrored.Filled.Assignment, NeonPurple, listOf(
        FormField("Target Group", type = FieldType.RADIO_GROUP, options = listOf("Women", "Children", "Seniors", "General")),
        FormField("State", "Enter your state")
    ))
    
    object Eligibility : DashboardAction("Check Eligibility", Icons.Default.Verified, Color(0xFF00E676), listOf(
        FormField("Gender", type = FieldType.RADIO_GROUP, options = listOf("Male", "Female", "Other")),
        FormField("Annual Income", type = FieldType.RADIO_GROUP, options = listOf("< 1 Lakh", "1-5 Lakh", "> 5 Lakh")),
        FormField("Verify ID", type = FieldType.ACTION_BUTTON, actionLabel = "UPLOAD AADHAAR")
    ))
    
    object AiChat : DashboardAction("AI Health Chat", Icons.Default.SmartToy, Color(0xFFFFD600), listOf(
        FormField("Urgency", type = FieldType.RADIO_GROUP, options = listOf("Normal", "Moderate", "Emergency")),
        FormField("Detailed Symptoms", "Describe your symptoms", type = FieldType.TEXT_AREA)
    ))
    
    object Pharmacy : DashboardAction("Pharmacy", Icons.Default.Medication, Color(0xFFFF5252), listOf(
        FormField("Delivery Type", type = FieldType.RADIO_GROUP, options = listOf("Standard", "Express")),
        FormField("Medicine Name", "Enter medicine name"),
        FormField("Full Address", "Delivery details", type = FieldType.TEXT_AREA)
    ))
    
    object Ambulance : DashboardAction("Ambulance", Icons.Default.Emergency, Color.Red, listOf(
        FormField("Ambulance Type", type = FieldType.RADIO_GROUP, options = listOf("Basic", "Advanced (ICU)", "Air")),
        FormField("Pickup Address", "Current location", type = FieldType.TEXT_AREA),
        FormField("Emergency Call", type = FieldType.ACTION_BUTTON, actionLabel = "CALL NOW")
    ))
    
    object BloodBank : DashboardAction("Blood Bank", Icons.Default.Bloodtype, Color(0xFFE91E63), listOf(
        FormField("Blood Group", type = FieldType.RADIO_GROUP, options = listOf("A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-")),
        FormField("Reason", "Explain the emergency", type = FieldType.TEXT_AREA)
    ))
    
    object TeleConsult : DashboardAction("Tele-Consult", Icons.Default.VideoCall, Color(0xFF00B0FF), listOf(
        FormField("Doctor Specialization", "e.g. Dermatologist"),
        FormField("Consultation Type", type = FieldType.RADIO_GROUP, options = listOf("Video Call", "Voice Call", "Chat")),
        FormField("Select Date", type = FieldType.ACTION_BUTTON, actionLabel = "OPEN CALENDAR")
    ))
}

sealed class QuickMenuAction(
    val title: String,
    val icon: ImageVector,
    val color: Color = Color.White,
    val formFields: List<FormField> = emptyList()
) {
    object Settings : QuickMenuAction("App Settings", Icons.Default.Settings, NeonBlue, listOf(
        FormField("Theme Mode", type = FieldType.RADIO_GROUP, options = listOf("Dark", "Light", "System")),
        FormField("Bio", "Short bio", type = FieldType.TEXT_AREA)
    ))
    
    object Notifications : QuickMenuAction("Notifications", Icons.Default.Notifications, Color.White, listOf(
        FormField("Status", type = FieldType.RADIO_GROUP, options = listOf("Enabled", "Disabled")),
        FormField("Frequency", type = FieldType.RADIO_GROUP, options = listOf("Immediate", "Daily Summary"))
    ))
    
    object History : QuickMenuAction("Medical History", Icons.Default.History, Color.White, listOf(
        FormField("Sort By", type = FieldType.RADIO_GROUP, options = listOf("Date", "Category", "Doctor"))
    ))
    
    object Share : QuickMenuAction("Share App", Icons.Default.Share, Color.White, listOf(
        FormField("Share Via", type = FieldType.RADIO_GROUP, options = listOf("WhatsApp", "Email", "SMS"))
    ))
    
    object Help : QuickMenuAction("Help & Support", Icons.AutoMirrored.Filled.Help, Color.White, listOf(
        FormField("Urgency", type = FieldType.RADIO_GROUP, options = listOf("Low", "Medium", "High")),
        FormField("Description", "Describe your issue", type = FieldType.TEXT_AREA)
    ))
    
    object About : QuickMenuAction("About Arogya Nidhi", Icons.Default.Info, Color.White, listOf(
        FormField("App Environment", type = FieldType.RADIO_GROUP, options = listOf("Production", "Beta", "Dev"))
    ))
    
    object Logout : QuickMenuAction("Logout", Icons.AutoMirrored.Filled.Logout, Color.Red, listOf(
        FormField("Confirm Logout?", type = FieldType.RADIO_GROUP, options = listOf("Yes, logout", "No, stay logged in"))
    ))
}
