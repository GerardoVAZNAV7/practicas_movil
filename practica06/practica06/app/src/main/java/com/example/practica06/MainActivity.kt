package com.example.practica06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                PracticaApp()
            }
        }
    }
}

@Composable
fun PracticaApp() {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val titulos = listOf("LazyColumn", "RecyclerView")

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = tab) {
                titulos.forEachIndexed { index, titulo ->
                    Tab(
                        selected = tab == index,
                        onClick = { tab = index },
                        text = { Text(titulo) }
                    )
                }
            }
            when (tab) {
                0 -> ListaScreen()
                else -> RecyclerScreen()
            }
        }
    }
}
