package net.nikonorov.expandabletext

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Constraints
import java.text.BreakIterator

const val COLLAPSED_SPAN = "collapsed_span"
const val LINE_EXTRA_SPACE = 5

@Composable
fun ExpandableText(
    text: String,
    expandText: String,
    modifier: Modifier = Modifier,
    expandColor: Color = Color.Unspecified,
    maxLinesCollapsed: Int = 5,
    style: TextStyle = TextStyle.Default,
) {
    BoxWithConstraints(modifier) {
        val paragraph = Paragraph(
            text = text,
            style = style,
            constraints = Constraints(maxWidth = constraints.maxWidth),
            density = LocalDensity.current,
            fontFamilyResolver = LocalFontFamilyResolver.current,
        )

        val trimLineRange: IntRange? = if (paragraph.lineCount > maxLinesCollapsed) {
            paragraph.getLineStart(maxLinesCollapsed - 1)..paragraph.getLineEnd(maxLinesCollapsed - 1)
        } else {
            null
        }
        val state = rememberState(text, expandText, expandColor, trimLineRange)
        ClickableText(text = state.annotatedString, style = style, onClick = { position ->
            val annotation = state.getClickableAnnotation(position)
            when (annotation?.tag) {
                COLLAPSED_SPAN -> state.expandState = State.ExpandState.Expanded
                else -> Unit
            }
        })
    }
}

@Composable
private fun rememberState(
    text: String,
    expandText: String,
    expandTextColor: Color,
    lastLineRange: IntRange?,
): State {
    return remember(text, lastLineRange) {
        State(
            text = text,
            expandText = expandText,
            expandTextColor = expandTextColor,
            lastLineTrimRange = lastLineRange,
        )
    }
}

private class State(
    text: String,
    expandText: String,
    expandTextColor: Color,
    lastLineTrimRange: IntRange?,
) {
    enum class ExpandState {
        Collapsed, Expanded,
    }

    private val defaultAnnotatedText = buildAnnotatedString { append(text) }
    private val collapsedAnnotatedText: AnnotatedString

    init {
        collapsedAnnotatedText = lastLineTrimRange?.let {
            val lastLineLen = lastLineTrimRange.last - lastLineTrimRange.first + 1
            val expandTextLen = getSafeLength(expandText)
            val collapsedText =
                text.take(lastLineTrimRange.last + 1).dropLast(minOf(lastLineLen, expandTextLen + LINE_EXTRA_SPACE))
            val collapsedTextLen = getSafeLength(collapsedText)
            val expandSpanStyle = TextStyle(color = expandTextColor).toSpanStyle()
            buildAnnotatedString {
                append(collapsedText)
                append(expandText)
                addStyle(expandSpanStyle, start = collapsedTextLen, end = collapsedTextLen + expandTextLen)
                addStringAnnotation(tag = COLLAPSED_SPAN,
                    annotation = "",
                    start = collapsedTextLen,
                    end = collapsedTextLen + expandTextLen)
            }
        } ?: defaultAnnotatedText
    }

    var annotatedString: AnnotatedString by mutableStateOf(collapsedAnnotatedText)
        private set
    private val _expandState = mutableStateOf(ExpandState.Collapsed)
    var expandState: ExpandState
        set(value) {
            _expandState.value = value
            annotatedString = when (value) {
                ExpandState.Collapsed -> collapsedAnnotatedText
                ExpandState.Expanded -> defaultAnnotatedText
            }
        }
        get() = _expandState.value

    fun getClickableAnnotation(position: Int): AnnotatedString.Range<String>? {
        return annotatedString.getStringAnnotations(position, position).firstOrNull {
            it.tag == COLLAPSED_SPAN
        }
    }
}

private fun getSafeLength(text: String): Int {
    val iterator = BreakIterator.getCharacterInstance()
    iterator.setText(text)
    return iterator.last()
}
