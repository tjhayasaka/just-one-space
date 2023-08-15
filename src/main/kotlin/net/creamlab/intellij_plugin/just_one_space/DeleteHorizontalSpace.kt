package net.creamlab.intellij_plugin.just_one_space

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import org.jetbrains.annotations.NotNull

class DeleteHorizontalSpace : AnAction() {
    override fun update(@NotNull event: AnActionEvent) {
    }

    override fun actionPerformed(@NotNull event: AnActionEvent) {
        val editor = event.getRequiredData(CommonDataKeys.EDITOR)
        val project = event.getRequiredData(CommonDataKeys.PROJECT)
        val caret = editor.caretModel.currentCaret

        val runnable = Runnable {
            ApplicationManager.getApplication().runWriteAction {
                editor.document.let {
                    val chars = it.charsSequence
                    val offset0 = caret.offset.let { o_ ->
                        var o = o_
                        while (o > 0 && chars[o - 1] == ' ') {
                            o--
                        }
                        o
                    }
                    val offset1 = caret.offset.let { o_ ->
                        var o = o_
                        while (o < chars.length && chars[o] == ' ') {
                            o++
                        }
                        o
                    }
                    if (offset0 == offset1) {
                        // there's already no space.  do nothing.
                    } else {
                        it.deleteString(offset0, offset1)
                    }
                }
            }
        }

        CommandProcessor.getInstance().executeCommand(project, runnable, "", this)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
