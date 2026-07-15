import kotlin.test.Test
import kotlin.test.assertNotNull

class PublishedClientSmokeTest {
    @Test
    fun constructsAndClosesPlatformClientWithoutNetworkTraffic() {
        val client = constructPublishedClient()
        try {
            assertNotNull(constructPublishedExecutor(client))
        } finally {
            client.close()
        }
    }
}
