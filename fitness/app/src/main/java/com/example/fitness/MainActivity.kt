package com.example.fitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitness.ui.theme.FitnessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetupNavGraph(navController = navController)
                }
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "imc_calculator_screen") {
        composable("imc_calculator_screen") {
            IMCCalculatorScreen(navController)
        }
        composable("imc_result_screen/{result}") { backStackEntry ->
            val result = backStackEntry.arguments?.getString("result") ?: ""
            IMCResultScreen(result = result)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCCalculatorScreen(navController: NavHostController) {
    var weight by remember { mutableStateOf(TextFieldValue("")) }
    var height by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Calculadora de IMC") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Altura (m)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val weightValue = weight.text.toDoubleOrNull() ?: 0.0
                    val heightValue = height.text.toDoubleOrNull() ?: 0.0
                    if (weightValue > 0 && heightValue > 0) {
                        val imc = weightValue / (heightValue * heightValue)
                        val result = "Seu IMC é: %.2f".format(imc)
                        navController.navigate("imc_result_screen/$result")
                    } else {
                        // Handle invalid input
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular o IMC")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCResultScreen(result: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Resultado do IMC") }
            )
        }
    ) { innerPadding ->
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            visible = true
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    Text(text = "Resultado do IMC", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = result, fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIMCCalculatorScreen() {
    FitnessTheme {
        IMCCalculatorScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIMCResultScreen() {
    FitnessTheme {
        IMCResultScreen(result = "Seu IMC é: 22.5")
    }
}
