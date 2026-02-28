package com.openclaw.mobile.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Home : Screen("home")
    object WorkspaceList : Screen("workspaces")
    object WorkspaceBrowser : Screen("workspace/{workspaceId}") {
        fun createRoute(workspaceId: String) = "workspace/$workspaceId"
    }
    object FileViewer : Screen("file/{fileId}") {
        fun createRoute(fileId: String) = "file/$fileId"
    }
    object SessionList : Screen("sessions")
    object SessionDetail : Screen("session/{sessionId}") {
        fun createRoute(sessionId: String) = "session/$sessionId"
    }
    object Settings : Screen("settings")
    object DiffReview : Screen("diff/{sessionId}") {
        fun createRoute(sessionId: String) = "diff/$sessionId"
    }
    object EditScreen : Screen("edit/{fileId}") {
        fun createRoute(fileId: String) = "edit/$fileId"
    }
}
