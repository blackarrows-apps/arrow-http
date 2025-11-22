package io.blackarrows.http.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.blackarrows.http.app.data.PostRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PutJsonScreen(
    onNavigateBack: () -> Unit,
    repository: PostRepository = koinInject()
) {
    var postId by remember { mutableStateOf("1") }
    var title by remember { mutableStateOf("Updated Title") }
    var body by remember { mutableStateOf("This is an updated post body") }
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PUT JSON Test") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Update an existing post using PUT",
                style = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = postId,
                onValueChange = { postId = it },
                label = { Text("Post ID") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("New Title") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("New Body") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                enabled = !isLoading
            )

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        error = null
                        result = null

                        postId.toIntOrNull()?.let { id ->
                            repository.updatePost(id, title, body)
                                .onSuccess { post ->
                                    result = "Success! Updated post ${post.id}\n\n" +
                                            "Title: ${post.title}\n" +
                                            "Body: ${post.body}"
                                }
                                .onFailure { e ->
                                    error = e.message
                                }
                        } ?: run {
                            error = "Invalid post ID"
                        }

                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && postId.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Send PUT Request")
                }
            }

            result?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            error?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error: $it",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
