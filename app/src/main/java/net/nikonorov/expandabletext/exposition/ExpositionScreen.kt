package net.nikonorov.expandabletext.exposition

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.nikonorov.expandabletext.ExpandableText

@Composable
fun ExpositionScreen(
    type: ExpositionType,
    modifier: Modifier = Modifier,
    viewModel: ExpositionViewModel = viewModel(factory = ExpositionViewModel.Factory()),
) {
    val uiState by viewModel.uiState.collectAsState()
    val expandText = "... more"
    val collapseText = when (type) {
        ExpositionType.Collapse -> null
        ExpositionType.CollapseExpand -> "less"
    }
    val style: TextStyle = MaterialTheme.typography.body1
    ExpandableText(text = uiState.text,
        expandText = expandText,
        expandColor = MaterialTheme.colors.secondary,
        collapseText = collapseText,
        collapseColor = MaterialTheme.colors.secondary,
        style = style,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp))
}
