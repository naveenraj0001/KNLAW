package org.rec.bitforge.knlaw.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.rec.bitforge.knlaw.BuildConfig

object GeminiAI {

    private const val API_KEY = BuildConfig.GEMINI_API_KEY

    private val model = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = API_KEY
    )

    // Chat session (memory)
    private var chat = model.startChat()

    // Coroutine job for cancellation
    private var currentJob: Job? = null

    interface StreamCallback {
        fun onChunk(text: String)
        fun onComplete()
        fun onError(error: String)
    }

    // ✅ MAIN STREAM FUNCTION (with memory)
    @JvmStatic
    fun sendMessageStream(prompt: String, callback: StreamCallback) {

        // cancel previous request if running
        currentJob?.cancel()

        currentJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = chat.sendMessageStream(prompt)

                response.collect { chunk ->
                    val text = chunk.text
                    if (!text.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            callback.onChunk(text)
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    callback.onComplete()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onError(e.message ?: "Error")
                }
            }
        }
    }

    // ✅ CLEAR CHAT MEMORY
    @JvmStatic
    fun clearChat() {
        chat = model.startChat()
    }

    // ✅ CANCEL CURRENT STREAM
    @JvmStatic
    fun cancelCurrent() {
        currentJob?.cancel()
    }

    // ✅ SEND NON-STREAM (simple response)
    @JvmStatic
    fun sendMessage(prompt: String, callback: (String) -> Unit, onError: (String) -> Unit) {

        currentJob?.cancel()

        currentJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = chat.sendMessage(prompt)
                val text = response.text ?: "No response"

                withContext(Dispatchers.Main) {
                    callback(text)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e.message ?: "Error")
                }
            }
        }
    }

    // ✅ OPTIONAL: SET SYSTEM PROMPT (like ChatGPT behavior)
    @JvmStatic
    fun setSystemPrompt(systemPrompt: String) {
        chat = model.startChat(
            history = listOf(
                content(role = "user") { text(systemPrompt) }
            )
        )
    }
}