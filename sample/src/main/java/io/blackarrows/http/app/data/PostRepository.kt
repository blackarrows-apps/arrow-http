package io.blackarrows.http.app.data

import android.util.Log
import io.blackarrows.http.io.HttpRequestExecutor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostRepository(
    private val httpExecutor: HttpRequestExecutor
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun createPost(title: String, body: String): Result<Post> {
        return try {
            Log.d("PostRepository", "Creating post: title=$title")

            val requestBody = CreatePostRequest(
                userId = 1,
                title = title,
                body = body
            )

            val response = httpExecutor.postJson(
                url = "https://jsonplaceholder.typicode.com/posts",
                body = requestBody,
                authRequired = false
            )

            Log.d("PostRepository", "Response statusCode=${response.statusCode}")

            val bodyString = response.body?.decodeToString() ?: "{}"
            Log.d("PostRepository", "Response body: $bodyString")

            val post = json.decodeFromString<Post>(bodyString)
            Log.d("PostRepository", "Successfully created post with id=${post.id}")

            Result.success(post)
        } catch (e: Exception) {
            Log.e("PostRepository", "Error creating post", e)
            Result.failure(e)
        }
    }

    suspend fun updatePost(id: Int, title: String, body: String): Result<Post> {
        return try {
            Log.d("PostRepository", "Updating post: id=$id")

            val requestBody = Post(
                id = id,
                userId = 1,
                title = title,
                body = body
            )

            val response = httpExecutor.putJson(
                url = "https://jsonplaceholder.typicode.com/posts/$id",
                body = requestBody,
                authRequired = false
            )

            Log.d("PostRepository", "Response statusCode=${response.statusCode}")

            val bodyString = response.body?.decodeToString() ?: "{}"
            val post = json.decodeFromString<Post>(bodyString)
            Log.d("PostRepository", "Successfully updated post with id=${post.id}")

            Result.success(post)
        } catch (e: Exception) {
            Log.e("PostRepository", "Error updating post", e)
            Result.failure(e)
        }
    }

    suspend fun deletePost(id: Int): Result<Unit> {
        return try {
            Log.d("PostRepository", "Deleting post: id=$id")

            val response = httpExecutor.deleteJson(
                url = "https://jsonplaceholder.typicode.com/posts/$id",
                authRequired = false
            )

            Log.d("PostRepository", "Response statusCode=${response.statusCode}")

            if (response.isSuccessful()) {
                Log.d("PostRepository", "Successfully deleted post")
                Result.success(Unit)
            } else {
                Result.failure(Exception("Delete failed with status ${response.statusCode}"))
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Error deleting post", e)
            Result.failure(e)
        }
    }

    suspend fun submitForm(name: String, email: String, message: String): Result<String> {
        return try {
            Log.d("PostRepository", "Submitting form data")

            val response = httpExecutor.postForm(
                url = "https://httpbin.org/post",
                formParams = mapOf(
                    "name" to name,
                    "email" to email,
                    "message" to message
                ),
                authRequired = false
            )

            Log.d("PostRepository", "Response statusCode=${response.statusCode}")

            val bodyString = response.body?.decodeToString() ?: "{}"
            Log.d("PostRepository", "Form submission response: ${bodyString.take(200)}")

            Result.success(bodyString)
        } catch (e: Exception) {
            Log.e("PostRepository", "Error submitting form", e)
            Result.failure(e)
        }
    }

    suspend fun downloadRawData(url: String): Result<ByteArray> {
        return try {
            Log.d("PostRepository", "Downloading raw data from: $url")

            val response = httpExecutor.getRaw(
                url = url,
                authRequired = false
            )

            Log.d("PostRepository", "Response statusCode=${response.statusCode}")
            Log.d("PostRepository", "Downloaded ${response.body?.size ?: 0} bytes")

            response.body?.let {
                Result.success(it)
            } ?: Result.failure(Exception("No data received"))
        } catch (e: Exception) {
            Log.e("PostRepository", "Error downloading raw data", e)
            Result.failure(e)
        }
    }
}
