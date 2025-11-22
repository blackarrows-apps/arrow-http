package io.blackarrows.http.app.di

import io.blackarrows.http.app.data.ImageRepository
import io.blackarrows.http.app.data.PostRepository
import io.blackarrows.http.app.ui.ImageViewModel
import io.blackarrows.http.io.interceptors.AuthRefresher
import io.blackarrows.http.ktor.di.httpModule
import io.blackarrows.http.providers.HeaderProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // Header provider (no auth required for Picsum API)
    single<HeaderProvider>(named("authHeaderProvider")) {
        object : HeaderProvider {
            override suspend fun getHeaders(vararg additional: Pair<String, String>): Map<String, String> {
                return mapOf(*additional)
            }

            override fun invalidate() {
                // No-op for this example
            }
        }
    }

    // Auth refresher (not needed for this example, but required by httpModule)
    single<AuthRefresher> {
        object : AuthRefresher {
            override suspend fun refreshToken(): Result<String> {
                return Result.success("")
            }
        }
    }

    // Include the HTTP module from the library
    includes(httpModule)

    // Repositories
    single { ImageRepository(get()) }
    single { PostRepository(get()) }

    // ViewModels
    viewModel { ImageViewModel(get()) }
}
