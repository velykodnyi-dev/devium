package com.openclaw.mobile.data

import kotlinx.coroutines.delay

class MockBackendRepository {

    suspend fun getWorkspaces(): List<Workspace> {
        delay(500)
        return listOf(
            Workspace("ws1", "openclaw-backend", "/home/user/src/openclaw-backend", "2 mins ago"),
            Workspace("ws2", "openclaw-mobile", "/home/user/src/openclaw-mobile", "1 hour ago"),
            Workspace("ws3", "personal-blog", "/home/user/src/blog", "3 days ago")
        )
    }

    suspend fun getFileTree(workspaceId: String): FileNode {
        delay(300)
        return FileNode("root", "src", true, listOf(
            FileNode("f1", "main.py", false, null, FileStatus.MODIFIED, "root"),
            FileNode("f2", "utils.py", false, null, FileStatus.UNMODIFIED, "root"),
            FileNode("d1", "models", true, listOf(
                FileNode("f3", "user.py", false, null, FileStatus.ADDED, "d1")
            ), FileStatus.UNMODIFIED, "root")
        ))
    }

    suspend fun getFileContent(fileId: String): String {
        delay(400)
        return when (fileId) {
            "f1" -> """
def is_prime(n):
    if n <= 1:
        return False
    for i in range(2, int(n**0.5) + 1):
        if n % i == 0:
            return False
    return True

print("Test", is_prime(10))
            """.trimIndent()
            "f2" -> "def hello():\n    print('world')\n"
            "f3" -> "class User:\n    pass\n"
            else -> "Unknown content"
        }
    }

    suspend fun getSessions(): List<OpenClawSession> {
        delay(400)
        return listOf(
            OpenClawSession("s1", "Implement biometric auth", SessionStatus.WAITING_FOR_APPROVAL, "ws1", "10 mins ago"),
            OpenClawSession("s2", "Fix caching bug", SessionStatus.RUNNING, "ws2", "Just now"),
            OpenClawSession("s3", "Refactor models", SessionStatus.COMPLETED, "ws1", "2 days ago")
        )
    }

    suspend fun getSessionLogs(sessionId: String): List<SessionLog> {
        delay(200)
        return listOf(
            SessionLog("l1", "10:00:00", "Starting agent loop...", LogType.INFO),
            SessionLog("l2", "10:00:02", "Agent requested to read models/user.py", LogType.AGENT),
            SessionLog("l3", "10:00:05", "cat models/user.py", LogType.COMMAND),
            SessionLog("l4", "10:00:10", "Agent proposed changes. Waiting for approval.", LogType.INFO)
        )
    }

    suspend fun getDiagnostics(fileId: String): List<Diagnostic> {
        delay(100)
        if (fileId == "f1") {
            return listOf(
                Diagnostic(8, "Unused variable 'x' found", DiagnosticSeverity.WARNING)
            )
        }
        return emptyList()
    }

    suspend fun getSymbols(fileId: String): List<Symbol> {
        delay(100)
        if (fileId == "f1") {
            return listOf(
                Symbol("is_prime", "Function", 1)
            )
        }
        return emptyList()
    }

    suspend fun getDiff(sessionId: String): List<ChangedFile> {
        delay(300)
        return listOf(
            ChangedFile("f1", "main.py", listOf(
                DiffHunk("@@ -1,5 +1,6 @@\n def is_prime(n):\n+    # Added type hint\n     if n <= 1:\n         return False", 1, 5, 1, 6)
            ))
        )
    }
}
