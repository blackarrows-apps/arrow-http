package io.blackarrows.http.ktor

import kotlin.test.Test
import kotlin.test.assertNotNull

class DarwinClientSmokeTest {
    @Test
    fun createsAndClosesDarwinClientWithoutNetworkTraffic() {
        val client = createHttpClient()
        try {
            assertNotNull(client)
        } finally {
            client.close()
        }
    }
}
