package io.blackarrows.http.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class TestOption(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onNavigate: (String) -> Unit
) {
    val testOptions = listOf(
        TestOption(
            title = "GET JSON",
            description = "Fetch random images from Picsum API",
            icon = Icons.Default.List,
            route = "get_json"
        ),
        TestOption(
            title = "POST JSON",
            description = "Create a new post on JSONPlaceholder",
            icon = Icons.Default.Create,
            route = "post_json"
        ),
        TestOption(
            title = "GET Raw/Binary",
            description = "Download a file as raw bytes",
            icon = Icons.Default.Info,
            route = "get_raw"
        ),
        TestOption(
            title = "PUT JSON",
            description = "Update an existing post",
            icon = Icons.Default.Edit,
            route = "put_json"
        ),
        TestOption(
            title = "DELETE",
            description = "Delete a post by ID",
            icon = Icons.Default.Delete,
            route = "delete_json"
        ),
        TestOption(
            title = "POST Form Data",
            description = "Submit form-urlencoded data",
            icon = Icons.Default.Send,
            route = "post_form"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HTTP Library Test Suite") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Select a test to run:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(testOptions) { option ->
                TestOptionCard(
                    option = option,
                    onClick = { onNavigate(option.route) }
                )
            }
        }
    }
}

@Composable
fun TestOptionCard(
    option: TestOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
