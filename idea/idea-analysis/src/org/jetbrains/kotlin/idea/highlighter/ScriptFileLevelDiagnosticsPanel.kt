/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("PackageDirectoryMismatch")
// TODO: move to scripting-idea module
package org.jetbrains.kotlin.idea.script

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications
import com.intellij.ui.LightColors
import org.jetbrains.kotlin.idea.core.script.IdeScriptReportSink

class ScriptFileLevelDiagnosticsPanel : EditorNotifications.Provider<EditorNotificationPanel>() {
    companion object {
        private val KEY = Key.create<EditorNotificationPanel>("kotlin.script.diagnostics.panel")
    }

    override fun getKey(): Key<EditorNotificationPanel> = KEY

    override fun createNotificationPanel(file: VirtualFile, fileEditor: FileEditor, project: Project): EditorNotificationPanel? {
        val reports = IdeScriptReportSink.getReports(file).takeIf { it.isNotEmpty() } ?: return null

        // TODO: render more than one file level diagnostic
        reports.find { it.location == null }
            ?.let {
                val panel = EditorNotificationPanel(LightColors.RED)
                panel.setText(it.message)

                for (provider in ScriptDiagnosticFixProvider.EP_NAME.extensions) {
                    provider.provideFix(it, panel)
                }

                return panel
            }

        return null
    }
}