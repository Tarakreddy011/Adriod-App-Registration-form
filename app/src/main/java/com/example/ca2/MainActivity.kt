package com.example.ca2

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "registration"
                ) {
                    composable("registration") {
                        RegistrationScreen(navController)
                    }

                    composable(
                        route = "confirmation/{title}/{id}/{sessions}",
                        arguments = listOf(
                            navArgument("title") { type = NavType.StringType },
                            navArgument("id") { type = NavType.StringType },
                            navArgument("sessions") { type = NavType.StringType }
                        )
                    ) { backStack ->
                        val title = backStack.arguments?.getString("title")
                        val id = backStack.arguments?.getString("id")
                        val sessions = backStack.arguments?.getString("sessions")
                        ConfirmationScreen(title, id, sessions)
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationScreen(navController: androidx.navigation.NavController) {
    var title by remember { mutableStateOf("") }
    var workshopId by remember { mutableStateOf("") }
    var sessions by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Workshop Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        TextField(
            value = workshopId,
            onValueChange = { workshopId = it },
            label = { Text("Workshop ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        TextField(
            value = sessions,
            onValueChange = { sessions = it },
            label = { Text("Number of Sessions") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
            Text("Confirm Registration Details")
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            val sessionNumber = sessions.toIntOrNull() ?: 0

            if (sessionNumber > 0 && isChecked) {
                navController.navigate("confirmation/$title/$workshopId/$sessions")
            } else {
                Toast.makeText(
                    context,
                    "Please enter valid details and confirm the registration.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }) {
            Text("Register")
        }
    }
}

@Composable
fun ConfirmationScreen(title: String?, id: String?, sessions: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Registration Confirmed", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))
        Text(text = "Workshop Title: $title")
        Text(text = "Workshop ID: $id")
        Text(text = "Number of Sessions: $sessions")
    }
}
