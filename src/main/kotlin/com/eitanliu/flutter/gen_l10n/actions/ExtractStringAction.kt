package com.eitanliu.flutter.gen_l10n.actions

import com.eitanliu.flutter.gen_l10n.PluginBundle
import com.eitanliu.flutter.gen_l10n.ui.ExtractStringDialog
import com.eitanliu.intellij.compat.extensions.ApplicationScope
import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import com.jetbrains.lang.dart.psi.DartStringLiteralExpression
import io.flutter.pub.PubRoot
import io.flutter.pub.PubRootCache
import kotlinx.coroutines.launch

class ExtractStringAction : PsiElementBaseIntentionAction(), HighPriorityAction {
    override fun getText(): String {
        return PluginBundle.message("msg.extract.arb")
    }

    override fun getFamilyName(): String {
        return "ExtractStringArb"
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiElement: PsiElement) {
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
        val stringExpression = PsiTreeUtil.getParentOfType(psiElement, DartStringLiteralExpression::class.java)
        val pubRoot = getPubRoot(project, psiElement) ?: return
        if (stringExpression == null) return

        val markedValue = stringExpression.text.substring(1, stringExpression.textLength - 1)
        val className = "AppLocalizations"
        val templateLocal = "app_en.arb"
        val arbDir = "lib/l10n"
        val allLocals = listOf("app_en.arb", "app_zh.arb")
        val sortedAllLocals = sequence<String> {
            yield(templateLocal)
            yieldAll(allLocals.asSequence().filter { local -> local != templateLocal })
        }
        val selectedLocals: MutableMap<String, Boolean> = linkedMapOf()
        sortedAllLocals.forEach { local ->
            selectedLocals[local] = local == templateLocal
        }

        ApplicationScope.launch {
            val dialog = ExtractStringDialog(project, markedValue, markedValue) { dialog ->
                dialog.graph.stringKey
                true
            }.showDialog()
        }
    }

    override fun isAvailable(project: Project, editor: Editor?, psiElement: PsiElement): Boolean {
        val pubRoot = getPubRoot(project, psiElement)
        if (editor == null || pubRoot == null) return false

        val enable = true

        val stringExpression = PsiTreeUtil.getParentOfType(
            psiElement, DartStringLiteralExpression::class.java
        )
        return stringExpression != null && enable
    }

    private fun getPubRoot(project: Project, psiElement: PsiElement): PubRoot? {
        val pubRootCache = PubRootCache.getInstance(project)
        val file = psiElement.containingFile.virtualFile
        return pubRootCache.getRoot(file)
    }
}
