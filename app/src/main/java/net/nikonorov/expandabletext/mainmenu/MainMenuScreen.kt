package net.nikonorov.expandabletext.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import net.nikonorov.expandabletext.exposition.ExpositionType

@Composable
fun MainMenuScreen(
    navigateToExpositionScreen: (ExpositionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { navigateToExpositionScreen(ExpositionType.CollapseExpand) }) {
                Text("Collapse/Expand")
            }
            Button(onClick = { navigateToExpositionScreen(ExpositionType.Collapse) }) {
                Text("Collapse Only")
            }
        }
    }
}