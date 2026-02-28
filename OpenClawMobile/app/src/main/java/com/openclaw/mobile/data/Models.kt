package com.openclaw.mobile.data

data class Workspace(
    val id: String,
    val name: String,
    val path: String,
    val lastAccessed: String
)

data class FileNode(
    val id: String,
    val name: String,
    val isDirectory: Boolean,
    val children: List<FileNode>? = null,
    val status: FileStatus = FileStatus.UNTRACKED,
    val parentId: String? = null
)

enum class FileStatus {
    MODIFIED, ADDED, DELETED, UNTRACKED, UNMODIFIED
}

data class OpenClawSession(
    val id: String,
    val title: String,
    val status: SessionStatus,
    val workspaceId: String,
    val lastUpdated: String
)

enum class SessionStatus {
    IDLE, RUNNING, WAITING_FOR_INPUT, WAITING_FOR_APPROVAL, COMPLETED, FAILED
}

data class SessionLog(
    val id: String,
    val timestamp: String,
    val message: String,
    val type: LogType
)

enum class LogType {
    INFO, ERROR, COMMAND, AGENT
}

data class ChangedFile(
    val fileId: String,
    val name: String,
    val diffHunks: List<DiffHunk>
)

data class DiffHunk(
    val content: String,
    val oldStart: Int,
    val oldLines: Int,
    val newStart: Int,
    val newLines: Int
)

data class Diagnostic(
    val line: Int,
    val message: String,
    val severity: DiagnosticSeverity
)

enum class DiagnosticSeverity {
    ERROR, WARNING, INFO
}

data class Symbol(
    val name: String,
    val kind: String,
    val line: Int
)
