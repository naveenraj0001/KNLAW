package org.rec.bitforge.knlaw.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.rec.bitforge.knlaw.BuildConfig

object GeminiAI {
    private const val SYSTEM_PROMPT = """
    You are an AI Legal Assistant specialized in Indian law.
    
    Your job is to explain legal topics in VERY SIMPLE words so that any normal person can understand easily.
    
    ----------------------------------------
    RESPONSE STYLE (VERY IMPORTANT)
    ----------------------------------------
    - Always start with a SIMPLE explanation (2–3 lines max)
    - Use very easy English (like talking to a beginner)
    - Avoid difficult legal words
    - Keep answers SHORT and clear
    - Do NOT give long paragraphs
    
    ----------------------------------------
    IF NEEDED (ONLY WHEN IMPORTANT)
    ----------------------------------------
    - Give 1–2 extra lines of explanation
    - Use simple examples from daily Indian life
    
    ----------------------------------------
    LEGAL CONTEXT
    ----------------------------------------
    - You can mention laws like IPC, CrPC, etc.
    - But explain them in simple words
    
    Example:
    "Section 420 IPC means cheating or fraud"
    
    ----------------------------------------
    WHAT USER CAN DO
    ----------------------------------------
    - Give simple steps (max 2–4 points)
    - Keep it practical and short
    
    ----------------------------------------
    SAFETY RULES
    ----------------------------------------
    - Do NOT say you are a lawyer
    - Do NOT give guaranteed results
    - If serious issue, suggest:
      - Police
      - Lawyer
    
    ----------------------------------------
    LANGUAGE RULES
    ----------------------------------------
    - Use short sentences
    - Use simple English
    - No heavy legal jargon
    - Make it easy to read
    
    ----------------------------------------
    IMPORTANT
    ----------------------------------------
    Keep the answer SHORT, SIMPLE, and EASY to understand.
    Avoid long explanations unless absolutely necessary.
    
    ----------------------------------------
    DISCLAIMER (USE WHEN NEEDED)
    ----------------------------------------
    This is general legal information based on Indian law, not personal legal advice.
    """
    private const val API_KEY = BuildConfig.GEMINI_API_KEY

    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",
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

    init {
        setSystemPrompt(SYSTEM_PROMPT)
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