package com.openclaw.mobile.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openclaw.mobile.navigation.Screen
import com.openclaw.mobile.ui.auth.AuthScreen
import com.openclaw.mobile.ui.home.HomeScreen
import com.openclaw.mobile.ui.workspace.WorkspaceListScreen
import com.openclaw.mobile.ui.workspace.WorkspaceBrowserScreen
import com.openclaw.mobile.ui.fileviewer.FileViewerScreen
import com.openclaw.mobile.ui.session.SessionListScreen
import com.openclaw.mobile.ui.session.SessionDetailScreen
import com.openclaw.mobile.ui.settings.SettingsScreen
import com.openclaw.mobile.ui.diff.DiffReviewScreen
import com.openclaw.mobile.ui.fileviewer.EditScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(onLoginSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToWorkspaces = { navController.navigate(Screen.WorkspaceList.route) },
                onNavigateToSessions = { navController.navigate(Screen.SessionList.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onSessionClick = { sessionId -> navController.navigate(Screen.SessionDetail.createRoute(sessionId)) }
            )
        }
        composable(Screen.WorkspaceList.route) {
            WorkspaceListScreen(
                onWorkspaceClick = { workspaceId -> navController.navigate(Screen.WorkspaceBrowser.createRoute(workspaceId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.WorkspaceBrowser.route) { backStackEntry ->
            val workspaceId = backStackEntry.arguments?.getString("workspaceId") ?: ""
            WorkspaceBrowserScreen(
                workspaceId = workspaceId,
                onFileClick = { fileId -> navController.navigate(Screen.FileViewer.createRoute(fileId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.FileViewer.route) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getString("fileId") ?: ""
            FileViewerScreen(
                fileId = fileId,
                onEditClick = { navController.navigate(Screen.EditScreen.createRoute(fileId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.EditScreen.route) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getString("fileId") ?: ""
            EditScreen(
                fileId = fileId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SessionList.route) {
            SessionListScreen(
                onSessionClick = { sessionId -> navController.navigate(Screen.SessionDetail.createRoute(sessionId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SessionDetail.route) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            SessionDetailScreen(
                sessionId = sessionId,
                onViewDiff = { navController.navigate(Screen.DiffReview.createRoute(sessionId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.DiffReview.route) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            DiffReviewScreen(
                sessionId = sessionId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
