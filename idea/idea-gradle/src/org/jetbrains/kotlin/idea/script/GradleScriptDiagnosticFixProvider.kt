/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.script

import com.intellij.ide.actions.ShowFilePathAction
import com.intellij.ui.EditorNotificationPanel
import org.jetbrains.kotlin.idea.actions.ShowKotlinGradleDslLogs
import kotlin.script.experimental.api.ScriptDiagnostic

class GradleScriptDiagnosticFixProvider : ScriptDiagnosticFixProvider {
    override fun provideFix(diagnostic: ScriptDiagnostic, panel: EditorNotificationPanel) {
        if (!ShowFilePathAction.isSupported()) return

        if (gradleMessagesForQuickFix.any { diagnostic.message.contains(it) }) {
            panel.createActionLabel(ShowKotlinGradleDslLogs.NAME, ShowKotlinGradleDslLogs.ID)
        }
    }

    private val gradleMessagesForQuickFix = arrayListOf(
        "This script caused build configuration to fail",
        "see IDE logs for more information",
        "Script dependencies resolution failed"
    )
}