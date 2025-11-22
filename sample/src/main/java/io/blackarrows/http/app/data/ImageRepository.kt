package io.blackarrows.http.app.data

import android.util.Log
import io.blackarrows.http.io.HttpRequestExecutor
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ImageRepository(
    private val httpExecutor: HttpRequestExecutor
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getRandomImages(page: Int = 1, limit: Int = 10): Result<List<ImageModel>> {
        return try {
            val response = httpExecutor.getJson(
                url = "https://picsum.photos/v2/list",
                queryParams = mapOf(
                    "page" to page.toString(),
                    "limit" to limit.toString()
                ),
                authRequired = false
            )

            val bodyString = response.body?.decodeToString() ?: "[]"
            val images = json.decodeFromString(ListSerializer(ImageModel.serializer()), bodyString)

            Log.d("ImageRepository", "Fetched ${images.size} images from Picsum API")
            Result.success(images)
        } catch (e: Exception) {
            Log.e("ImageRepository", "Error fetching images", e)
            Result.failure(e)
        }
    }
}
