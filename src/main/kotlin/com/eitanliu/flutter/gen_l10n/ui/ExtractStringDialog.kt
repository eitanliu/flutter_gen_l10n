@file:Suppress("DialogTitleCapitalization")

package com.eitanliu.flutter.gen_l10n.ui

import com.eitanliu.flutter.gen_l10n.PluginBundle
import com.eitanliu.intellij.compat.binding.bindTabTransferFocus
import com.eitanliu.intellij.compat.extensions.createPropertyGraph
import com.eitanliu.intellij.compat.extensions.propertyRef
import com.eitanliu.intellij.compat.extensions.value
import com.google.gson.*
import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign

/**
 * Extract String Dialog
 */
class ExtractStringDialog(
    project: Project,
    private val stringKey: String = "",
    private val stringValue: String = "",
    val doOkAction: (dialog: ExtractStringDialog) -> Boolean
) : DialogWrapper(
    project, true,
) {

    val graph = Graph(this).afterPropagation(disposable) {
        okAction.isEnabled = inputIsValidJson(stringValue.value).takeIf {
            stringKey.value.trim().isNotEmpty() && stringValue.value.isNotEmpty()
        } ?: false
    }

    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()

    init {
        title = PluginBundle.message("msg.extract.arb")
        setOKButtonText("Generate")
        okAction.apply {
            putValue(DEFAULT_ACTION, false)
            isEnabled = stringKey.trim().isNotEmpty() && inputIsValidJson(stringValue)
        }
        init()
    }

    override fun createCenterPanel() = panel {
        row {
            label(PluginBundle.message("msg.string.key"))
        }
        row {
            textField().apply {
                bindText(graph.stringKey)
                horizontalAlign(HorizontalAlign.FILL)
                applyToComponent {
                    myPreferredFocusedComponent = this
                }
            }
        }
        row {
            label(PluginBundle.message("msg.string.value"))
        }
        row {
            resizableRow()
            textArea().apply {
                bindText(graph.stringValue)
                applyToComponent {
                    rows = 2
                    bindTabTransferFocus()
                }
                horizontalAlign(HorizontalAlign.FILL)
                verticalAlign(VerticalAlign.FILL)
            }
        }

    }.apply {
        // preferredSize = JBDimension(200, 130)
        // withPreferredSize(200, 130)
        withMinimumHeight(130)
    }

    private fun handleFormatJSONString() {
        val currentText = graph.stringValue.value
        if (currentText.isNotEmpty()) {
            try {
                val jsonElement = prettyGson.fromJson(currentText, JsonElement::class.java)
                val formatJSON = prettyGson.toJson(jsonElement)
                graph.stringValue.value = formatJSON
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun inputIsValidJson(string: String) = try {
        val jsonElement = JsonParser.parseString(string)
        (jsonElement.isJsonObject || jsonElement.isJsonArray)
    } catch (e: JsonSyntaxException) {
        false
    }

    override fun doOKAction() {
        val stringKey = graph.stringKey.value
        val stringValue = graph.stringValue.value

        if (stringKey.isEmpty()) {
            Messages.showErrorDialog("Error", "String Key must not null or empty")
            return
        }

        if (stringValue.isEmpty()) {
            Messages.showErrorDialog("Error", "String Value must not null or empty")
            return
        }

        if (doOkAction(this)) {
            super.doOKAction()
        }
    }

    fun showDialog(): ExtractStringDialog {
        show()
        return this
    }

    class Graph(private val data: ExtractStringDialog) {
        private val propertyGraph: PropertyGraph = createPropertyGraph()
        private val disposable = data.disposable

        val stringKey = propertyGraph.propertyRef(data::stringKey)
        val stringValue = propertyGraph.propertyRef(data::stringValue)

        fun afterPropagation(disposable: Disposable? = null, listener: Graph.() -> Unit) = apply {
            propertyGraph.afterPropagation(disposable) { listener() }
        }
    }
}