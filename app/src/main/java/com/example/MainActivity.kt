package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.NexellaViewModel
import com.example.ui.components.ConnectionSuccessDialog
import com.example.ui.components.CreateOpportunityDialog
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.NexellaBottomNav
import com.example.ui.components.NexellaHeader
import com.example.ui.components.NexellaResponsiveLayout
import com.example.ui.components.RegisterUserDialog
import com.example.ui.components.SyncProgressBar
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.ConnectionsScreen
import com.example.ui.screens.EllaAssistantScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MeetingsScreen
import com.example.ui.screens.ProfileAdminScreen
import com.example.ui.theme.NexellaTheme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Brush
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaGoldLight
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleDark

class MainActivity : ComponentActivity() {

    private val viewModel: NexellaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NexellaTheme {
                NexellaMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NexellaMainApp(viewModel: NexellaViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val usersList by viewModel.usersList.collectAsState()
    val adminUsersList by viewModel.adminUsersList.collectAsState()
    val opportunitiesList by viewModel.opportunitiesList.collectAsState()
    val connectionsList by viewModel.connectionsList.collectAsState()
    val meetingsList by viewModel.meetingsList.collectAsState()
    val ellaMessages by viewModel.ellaMessages.collectAsState()
    val isEllaLoading by viewModel.isEllaLoading.collectAsState()
    val adminMetrics by viewModel.adminMetrics.collectAsState()
    val latestConnection by viewModel.latestCreatedConnection.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedNeighborhood by viewModel.selectedNeighborhood.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isImobiliarioOnly by viewModel.isImobiliarioOnly.collectAsState()

    val userMessage by viewModel.userMessage.collectAsState()
    val isSupabaseSyncing by viewModel.isSupabaseSyncing.collectAsState()
    val supabaseStatusText by viewModel.supabaseStatusText.collectAsState()
    val isLoadingData by viewModel.isLoadingData.collectAsState()
    val isActionLoading by viewModel.isActionLoading.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    // Dialog state controllers
    var showEllaModal by remember { mutableStateOf(false) }
    var showCreateOppModal by remember { mutableStateOf(false) }
    var showRegisterModal by remember { mutableStateOf(false) }
    var showEditProfileModal by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.testTag("app_snackbar_host")
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !showEllaModal,
                enter = fadeIn(animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)) +
                        scaleIn(initialScale = 0.8f, animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)),
                exit = fadeOut(animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)) +
                       scaleOut(targetScale = 0.8f, animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing))
            ) {
                FloatingActionButton(
                    onClick = { showEllaModal = true },
                    containerColor = NexellaPurple,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(28.dp),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier
                        .border(
                            border = BorderStroke(
                                1.5.dp,
                                Brush.linearGradient(listOf(NexellaGold, NexellaGoldLight, NexellaGold))
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .testTag("floating_ella_fab")
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(NexellaPurple, NexellaPurpleDark)
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(NexellaGold, CircleShape),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Ella",
                                tint = NexellaPurpleDark,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Ella 💜",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Mentora IA",
                                color = NexellaGold,
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            SyncProgressBar(
                isSyncing = isSupabaseSyncing,
                statusText = supabaseStatusText
            )

            NexellaResponsiveLayout(
                selectedTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) },
                currentUser = currentUser,
                onOpenElla = { showEllaModal = true },
                onOpenProfile = { viewModel.selectTab(4) },
                modifier = Modifier.weight(1f)
            ) { responsivePadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(responsivePadding)
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showEllaModal,
                        enter = fadeIn(animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)) +
                                scaleIn(initialScale = 0.90f, animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)) +
                               scaleOut(targetScale = 0.95f, animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing))
                    ) {
                        // ELLA Dedicated Fullscreen/Dialog UI
                        EllaAssistantScreen(
                            messages = ellaMessages,
                            isLoading = isEllaLoading,
                            onSendMessage = { prompt -> viewModel.sendEllaPrompt(prompt) },
                            onClose = { showEllaModal = false },
                            onClearChat = { viewModel.clearEllaChat() },
                            onShowToast = { msg -> viewModel.showMessage(msg) }
                        )
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = !showEllaModal,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 200))
                    ) {
                        Crossfade(targetState = currentTab, label = "tab_fade") { tab ->
                            when (tab) {
                                0 -> HomeScreen(
                                    currentUser = currentUser,
                                    opportunities = opportunitiesList,
                                    meetings = meetingsList,
                                    onNavigateTab = { viewModel.selectTab(it) },
                                    onOpenCreateOpportunity = { showCreateOppModal = true },
                                    onOpenElla = { showEllaModal = true },
                                    onOpenRegister = { showRegisterModal = true },
                                    onCreateConnectionWithUser = { user ->
                                        viewModel.createConnection(user, "Página Inicial")
                                    },
                                    onJoinMeeting = { meeting ->
                                        viewModel.joinMeeting(meeting.id)
                                    },
                                    onFilterNeighborhood = { neighborhood ->
                                        viewModel.updateNeighborhoodFilter(neighborhood)
                                    },
                                    isLoading = isLoadingData
                                )

                                1 -> CommunityScreen(
                                    users = usersList,
                                    searchQuery = searchQuery,
                                    selectedNeighborhood = selectedNeighborhood,
                                    selectedCategory = selectedCategory,
                                    isImobiliarioOnly = isImobiliarioOnly,
                                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                    onNeighborhoodSelect = { viewModel.updateNeighborhoodFilter(it) },
                                    onCategorySelect = { viewModel.updateCategoryFilter(it) },
                                    onToggleImobiliario = { viewModel.toggleImobiliarioOnly(it) },
                                    onCreateConnection = { user ->
                                        viewModel.createConnection(user, "Comunidade Nexella")
                                    },
                                    isLoading = isLoadingData
                                )

                                2 -> ConnectionsScreen(
                                    connections = connectionsList,
                                    opportunities = opportunitiesList,
                                    onOpenCreateOpportunity = { showCreateOppModal = true },
                                    onUpdateConnectionImpact = { id, impact ->
                                        viewModel.updateConnectionImpact(id, impact)
                                    },
                                    initialSubTab = 0
                                )

                                3 -> ConnectionsScreen(
                                    connections = connectionsList,
                                    opportunities = opportunitiesList,
                                    onOpenCreateOpportunity = { showCreateOppModal = true },
                                    onUpdateConnectionImpact = { id, impact ->
                                        viewModel.updateConnectionImpact(id, impact)
                                    },
                                    initialSubTab = 1
                                )

                                4 -> ProfileAdminScreen(
                                    currentUser = currentUser,
                                    allUsers = adminUsersList,
                                    adminMetrics = adminMetrics,
                                    onSwitchUser = { viewModel.switchCurrentUser(it) },
                                    onOpenRegisterModal = { showRegisterModal = true },
                                    onOpenEditProfileModal = { showEditProfileModal = true },
                                    onApproveUser = { viewModel.adminApproveUser(it) },
                                    onSuspendUser = { viewModel.adminSuspendUser(it) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- DIALOGS ---

        // 1. Success Connection Dialog Popup
        latestConnection?.let { conn ->
            ConnectionSuccessDialog(
                connection = conn,
                onDismiss = { viewModel.dismissConnectionSuccessDialog() }
            )
        }

        // 2. Create Opportunity Dialog
        if (showCreateOppModal) {
            CreateOpportunityDialog(
                onDismiss = { showCreateOppModal = false },
                onCreateOpportunity = { title, desc, cat, neigh, type, imob ->
                    viewModel.createOpportunity(title, desc, cat, neigh, type, imob)
                },
                isSubmitting = isActionLoading
            )
        }

        // 3. Register User / Onboarding & Login Dialog
        if (showRegisterModal) {
            RegisterUserDialog(
                onDismiss = { showRegisterModal = false },
                onRegister = { name, bName, cat, neigh, serv, desc, insta, whats, proc, ofec, isCor, creci, email, pass ->
                    viewModel.registerNewUser(
                        name, bName, cat, neigh, serv, desc, insta, whats, proc, ofec, isCor, creci, email, pass
                    )
                },
                onLogin = { emailOrName ->
                    viewModel.loginUser(emailOrName) { _, _ -> }
                },
                isSubmitting = isActionLoading
            )
        }

        // 4. Edit Profile Dialog (Supabase + Local Room DB Persistence)
        val activeUser = currentUser
        if (showEditProfileModal && activeUser != null) {
            EditProfileDialog(
                currentUser = activeUser,
                onDismiss = { showEditProfileModal = false },
                onSaveProfile = { updatedUser ->
                    viewModel.updateUserProfile(updatedUser)
                    showEditProfileModal = false
                },
                isSubmitting = isActionLoading,
                isSupabaseConfigured = viewModel.isSupabaseConfigured
            )
        }
    }
}
