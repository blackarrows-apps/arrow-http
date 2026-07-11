package io.blackarrows.http.test

import io.blackarrows.http.errors.NetworkException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class FakeHttpRequestExecutorPatchTest {

    @Test
    fun patchJson_recordsCall() = runTest {
        val fake = FakeHttpRequestExecutor()
        val body = mapOf("title" to "updated")

        fake.patchJson(url = "https://api.example.com/posts/1", body = body)

        assertEquals(1, fake.calls.size)
        assertEquals(RecordedCall(FakeHttpMethod.PATCH, "https://api.example.com/posts/1", body = body), fake.calls.first())
    }

    @Test
    fun patchRaw_recordsCall() = runTest {
        val fake = FakeHttpRequestExecutor()
        val body = "raw-bytes"

        fake.patchRaw(url = "https://api.example.com/posts/1", body = body)

        assertEquals(1, fake.calls.size)
        assertEquals(RecordedCall(FakeHttpMethod.PATCH, "https://api.example.com/posts/1", body = body), fake.calls.first())
    }

    @Test
    fun patchJson_respectsStub() = runTest {
        val fake = FakeHttpRequestExecutor()
        val stubbed = FakeApiResponse.success("""{"id": 1}""")
        fake.stub("https://api.example.com/posts/1", stubbed)

        val response = fake.patchJson(url = "https://api.example.com/posts/1", body = "{}")

        assertSame(stubbed, response)
    }

    @Test
    fun patchJson_respectsStubbedException() = runTest {
        val fake = FakeHttpRequestExecutor()
        val exception = NetworkException("timeout")
        fake.stubException("https://api.example.com/posts/1", exception)

        val thrown = assertFailsWith<NetworkException> {
            fake.patchJson(url = "https://api.example.com/posts/1", body = "{}")
        }
        assertSame(exception, thrown)
    }
}
