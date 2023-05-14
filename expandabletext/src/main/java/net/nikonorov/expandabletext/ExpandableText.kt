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
const val EXPANDED_SPAN = "expanded_span"
const val LINE_EXTRA_SPACE = 5

@Composable
fun ExpandableText(
    text: String,
    expandText: String,
    modifier: Modifier = Modifier,
    expandColor: Color = Color.Unspecified,
    collapseText: String? = null,
    collapseColor: Color = Color.Unspecified,
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
        val expandState = SpanState(expandText, expandColor)
        val collapseState = collapseText?.let { SpanState(it, collapseColor) }
        val state = rememberState(text, expandState, collapseState, trimLineRange, style)

        ClickableText(text = state.annotatedString, style = style, onClick = { position ->
            val annotation = state.getClickableAnnotation(position)
            when (annotation?.tag) {
                COLLAPSED_SPAN -> state.expandState = State.ExpandState.Expanded
                EXPANDED_SPAN -> state.expandState = State.ExpandState.Collapsed
                else -> Unit
            }
        })
    }
}

@Composable
private fun rememberState(
    text: String,
    expandSpanState: SpanState,
    collapseSpanState: SpanState?,
    lastLineRange: IntRange?,
    style: TextStyle,
): State {
    return remember(text, expandSpanState, collapseSpanState, lastLineRange, style) {
        State(
            text = text,
            expandSpanState = expandSpanState,
            collapseSpanState = collapseSpanState,
            lastLineTrimRange = lastLineRange,
            style = style,
        )
    }
}

private data class SpanState(
    val text: String,
    val color: Color,
)

private class State(
    text: String,
    expandSpanState: SpanState,
    collapseSpanState: SpanState?,
    lastLineTrimRange: IntRange?,
    style: TextStyle,
) {
    enum class ExpandState {
        Collapsed, Expanded,
    }

    private val defaultAnnotatedText = buildAnnotatedString { append(text) }
    private val collapsedAnnotatedText: AnnotatedString
    private val expandedAnnotatedText: AnnotatedString

    init {
        collapsedAnnotatedText = lastLineTrimRange?.let {
            val lastLineLen = lastLineTrimRange.last - lastLineTrimRange.first + 1
            val expandTextLen = getSafeLength(expandSpanState.text)
            val collapsedText =
                text.take(lastLineTrimRange.last + 1).dropLast(minOf(lastLineLen, expandTextLen + LINE_EXTRA_SPACE))
            val collapsedTextLen = getSafeLength(collapsedText)
            val expandSpanStyle = style.merge(TextStyle(color = expandSpanState.color)).toSpanStyle()
            buildAnnotatedString {
                append(collapsedText)
                append(expandSpanState.text)
                addStyle(expandSpanStyle, start = collapsedTextLen, end = collapsedTextLen + expandTextLen)
                addStringAnnotation(tag = COLLAPSED_SPAN,
                    annotation = "",
                    start = collapsedTextLen,
                    end = collapsedTextLen + expandTextLen)
            }
        } ?: defaultAnnotatedText

        expandedAnnotatedText = collapseSpanState?.let { span ->
            val collapseStyle = style.merge(TextStyle(color = span.color)).toSpanStyle()
            val textLen = getSafeLength(text)
            val collapsePostfix = "\n${span.text}"
            val collapseLen = getSafeLength(collapsePostfix)
            buildAnnotatedString {
                append(text)
                append(collapsePostfix)
                addStyle(collapseStyle, start = textLen, end = textLen + collapseLen)
                addStringAnnotation(tag = EXPANDED_SPAN,
                    annotation = "",
                    start = textLen,
                    end = textLen + collapseLen)
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
                ExpandState.Expanded -> expandedAnnotatedText
            }
        }
        get() = _expandState.value

    fun getClickableAnnotation(position: Int): AnnotatedString.Range<String>? {
        return annotatedString.getStringAnnotations(position, position).firstOrNull {
            it.tag == COLLAPSED_SPAN || it.tag == EXPANDED_SPAN
        }
    }
}

private fun getSafeLength(text: String): Int {
    val iterator = BreakIterator.getCharacterInstance()
    iterator.setText(text)
    return iterator.last()
}
