package com.example.arogyanidhi.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arogyanidhi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToEligibility: () -> Unit,
    onNavigateToSchemes: () -> Unit,
    onNavigateToHospitals: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val dashboardActions by viewModel.dashboardActions.collectAsState()
    val quickMenuActions by viewModel.quickMenuActions.collectAsState()
    val selectedAction by viewModel.selectedAction.collectAsState()
    val selectedQuickAction by viewModel.selectedQuickAction.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "Dashboard Logo Animation")
    val translationX by infiniteTransition.animateFloat(
        initialValue = -150f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Logo Translation"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "AROGYA NIDHI", 
                        fontWeight = FontWeight.ExtraBold, 
                        letterSpacing = 2.sp, 
                        color = NeonBlue,
                        modifier = Modifier.graphicsLayer {
                            this.translationX = translationX
                        }
                    )
                },
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = NeonBlue)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBlue.copy(alpha = 0.8f))
            )
        },
        containerColor = DarkBlue
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Brush.verticalGradient(colors = listOf(DarkBlue, Black)))) {
            Box(modifier = Modifier.size(300.dp).offset(x = (-100).dp, y = (-100).dp).background(NeonBlue.copy(alpha = 0.1f), CircleShape).blur(50.dp))

            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                HeaderSection(userProfile?.name ?: "User", onNavigateToProfile)
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = "Quick Services", color = NeonBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(dashboardActions) { action ->
                        DashboardCard(
                            title = action.title,
                            icon = action.icon,
                            color = action.color,
                            onClick = {
                                when (action) {
                                    DashboardAction.Hospitals -> onNavigateToHospitals()
                                    DashboardAction.Schemes -> onNavigateToSchemes()
                                    DashboardAction.Eligibility -> onNavigateToEligibility()
                                    else -> viewModel.selectAction(action)
                                }
                            }
                        )
                    }
                }
            }
        }

        if (selectedAction != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.selectAction(null) },
                containerColor = CardBg,
                dragHandle = { BottomSheetDefaults.DragHandle(color = NeonBlue) }
            ) {
                GenericFormContent(
                    title = selectedAction!!.title,
                    icon = selectedAction!!.icon,
                    color = selectedAction!!.color,
                    fields = selectedAction!!.formFields,
                    onSubmit = { data -> viewModel.submitDashboardForm(selectedAction!!, data) }
                )
            }
        }

        if (selectedQuickAction != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.selectQuickAction(null) },
                containerColor = CardBg,
                dragHandle = { BottomSheetDefaults.DragHandle(color = NeonBlue) }
            ) {
                GenericFormContent(
                    title = selectedQuickAction!!.title,
                    icon = selectedQuickAction!!.icon,
                    color = selectedQuickAction!!.color,
                    fields = selectedQuickAction!!.formFields,
                    onSubmit = { data -> viewModel.submitQuickMenuForm(selectedQuickAction!!, data) }
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = CardBg,
                dragHandle = { BottomSheetDefaults.DragHandle(color = NeonBlue) }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding().verticalScroll(rememberScrollState())
                ) {
                    Text("Quick Menu", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = NeonBlue)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    quickMenuActions.forEach { action ->
                        if (action is QuickMenuAction.Logout) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.White.copy(alpha = 0.1f))
                        }
                        
                        MenuOption(
                            icon = action.icon,
                            title = action.title,
                            color = action.color,
                            onClick = {
                                showBottomSheet = false
                                if (action is QuickMenuAction.History) {
                                    onNavigateToHistory()
                                } else if (action is QuickMenuAction.Settings || action is QuickMenuAction.Logout) {
                                    onNavigateToSettings()
                                } else {
                                    viewModel.selectQuickAction(action)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenericFormContent(
    title: String,
    icon: ImageVector,
    color: Color,
    fields: List<FormField>,
    onSubmit: (Map<String, String>) -> Unit
) {
    val formData = remember { mutableStateMapOf<String, String>() }
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding().verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        fields.forEach { field ->
            when (field.type) {
                FieldType.TEXT, FieldType.TEXT_AREA -> {
                    OutlinedTextField(
                        value = formData[field.label] ?: field.initialValue,
                        onValueChange = { formData[field.label] = it },
                        label = { Text(field.label) },
                        placeholder = { Text(field.placeholder, color = TextGray.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        minLines = if (field.type == FieldType.TEXT_AREA) 4 else 1,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = color,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                            focusedLabelColor = color,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White
                        )
                    )
                }
                FieldType.ACTION_BUTTON -> {
                    OutlinedButton(
                        onClick = { /* Internal field action */ },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = color)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(field.actionLabel ?: "ACTION", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                FieldType.RADIO_GROUP -> {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(text = field.label, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val selectedOption = formData[field.label] ?: field.options.firstOrNull() ?: ""
                        if (formData[field.label] == null && selectedOption.isNotEmpty()) {
                            formData[field.label] = selectedOption
                        }

                        field.options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (option == selectedOption),
                                        onClick = { formData[field.label] = option },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (option == selectedOption),
                                    onClick = null,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = color,
                                        unselectedColor = Color.White.copy(alpha = 0.5f)
                                    )
                                )
                                Text(
                                    text = option,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSubmit(formData.toMap()) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = color)
        ) {
            Text("SUBMIT", fontWeight = FontWeight.Bold, color = Black)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun HeaderSection(userName: String, onProfileClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Hello,", style = MaterialTheme.typography.bodyLarge, color = TextGray)
            Text(text = userName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
        Box(modifier = Modifier.size(50.dp).clip(CircleShape).border(1.dp, NeonBlue, CircleShape).clickable { onProfileClick() }, contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Person, contentDescription = "Profile", tint = NeonBlue, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun DashboardCard(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.height(110.dp).fillMaxWidth().border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = GlassWhite
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontSize = 13.sp)
        }
    }
}

@Composable
fun MenuOption(icon: ImageVector, title: String, color: Color, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
    }
}
