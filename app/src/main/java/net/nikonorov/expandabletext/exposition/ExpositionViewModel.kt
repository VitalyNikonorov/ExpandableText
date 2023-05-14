package net.nikonorov.expandabletext.exposition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpositionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExpositionUiState(text = text))
    val uiState: StateFlow<ExpositionUiState> = _uiState.asStateFlow()

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
        ): T {
            return ExpositionViewModel() as T
        }
    }
}

private const val text =
    """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eget mi proin sed libero enim. Viverra accumsan in nisl nisi scelerisque. Quisque egestas diam in arcu cursus euismod. Rhoncus mattis rhoncus urna neque. Augue lacus viverra vitae congue. Senectus et netus et malesuada fames ac turpis egestas. Egestas maecenas pharetra convallis posuere morbi leo urna. Quam vulputate dignissim suspendisse in est ante. Integer malesuada nunc vel risus. Magna etiam tempor orci eu lobortis elementum. Vitae elementum curabitur vitae nunc sed velit dignissim sodales ut. Euismod quis viverra nibh cras pulvinar mattis nunc.

Sit amet nulla facilisi morbi tempus. Nulla facilisi nullam vehicula ipsum a arcu. Aliquam sem fringilla ut morbi. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices. Turpis massa sed elementum tempus egestas sed. Ornare quam viverra orci sagittis eu volutpat. Ut porttitor leo a diam sollicitudin tempor id. Arcu risus quis varius quam. Turpis cursus in hac habitasse platea. Amet consectetur adipiscing elit duis tristique sollicitudin nibh sit amet."""
