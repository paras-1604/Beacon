package com.example.beacon


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.ui.theme.*
import com.example.data.AuthRepository
import com.example.data.repository.ContactRepositoryImpl
import com.example.data.repository.LocationRepositoryImpl
import com.example.data.repository.PreferencesRepositoryImpl
import com.example.presentation.components.BottomNavBar
import com.example.presentation.screens.auth.AuthScreen
import com.example.presentation.screens.contacts.ContactsScreen
import com.example.presentation.screens.contacts.ContactsViewModel
import com.example.presentation.screens.home.HomeScreen
import com.example.presentation.screens.home.HomeViewModel


class MainActivity : ComponentActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, "NEW CODE IS RUNNING", Toast.LENGTH_LONG).show()

        setContent {
            BeaconTheme {
                var isAuthenticated by remember { mutableStateOf(false) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    authRepository.signInAnonymously().onSuccess {
                        isAuthenticated = true
                    }.onFailure {
                        // Handle error (maybe show a message)
                    }
                    isLoading = false
                }

                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text("Loading...", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    isAuthenticated -> {
                        // Get application context for database
                        val context = LocalContext.current
                        val beaconApp = context.applicationContext as BeaconApplication

                        // Create repositories
                        val contactRepository = remember {
                            ContactRepositoryImpl(beaconApp.contactDatabase)
                        }
                        val preferencesRepository = remember {
                            PreferencesRepositoryImpl()
                        }
                        val locationRepository = remember {
                            LocationRepositoryImpl(
                                context,
                                beaconApp.contactDatabase
                            )
                        }

                        // Create ViewModels
                        val homeViewModel: HomeViewModel = remember {
                            HomeViewModel(preferencesRepository, locationRepository)
                        }
                        val contactsViewModel: ContactsViewModel = remember {
                            ContactsViewModel(contactRepository)
                        }

                        // Navigation
                        val navController = rememberNavController()

                        Scaffold(
                            bottomBar = { BottomNavBar(navController) }
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = "home",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("home") {
                                    HomeScreen(viewModel = homeViewModel)
                                }
                                composable("contacts") {
                                    ContactsScreen(viewModel = contactsViewModel)
                                }
                                composable("settings") {
                                    // Placeholder for settings
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Text("Settings", modifier = Modifier.align(Alignment.Center))
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        AuthScreen()
                    }
                }
            }
        }
    }
}
