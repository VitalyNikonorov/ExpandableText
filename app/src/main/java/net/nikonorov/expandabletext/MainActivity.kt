package net.nikonorov.expandabletext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import net.nikonorov.expandabletext.ui.theme.ExpandableTextTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpandableTextTheme {
                val style: TextStyle = MaterialTheme.typography.body1
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    ExpandableText(text = text,
                        expandText = "... more",
                        expandColor = MaterialTheme.colors.secondary,
                        style = style,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp))
                }
            }
        }
    }
}

private const val text =
    """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eget mi proin sed libero enim. Viverra accumsan in nisl nisi scelerisque. Quisque egestas diam in arcu cursus euismod. Rhoncus mattis rhoncus urna neque. Augue lacus viverra vitae congue. Senectus et netus et malesuada fames ac turpis egestas. Egestas maecenas pharetra convallis posuere morbi leo urna. Quam vulputate dignissim suspendisse in est ante. Integer malesuada nunc vel risus. Magna etiam tempor orci eu lobortis elementum. Vitae elementum curabitur vitae nunc sed velit dignissim sodales ut. Euismod quis viverra nibh cras pulvinar mattis nunc.

Sit amet nulla facilisi morbi tempus. Nulla facilisi nullam vehicula ipsum a arcu. Aliquam sem fringilla ut morbi. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices. Turpis massa sed elementum tempus egestas sed. Ornare quam viverra orci sagittis eu volutpat. Ut porttitor leo a diam sollicitudin tempor id. Arcu risus quis varius quam. Turpis cursus in hac habitasse platea. Amet consectetur adipiscing elit duis tristique sollicitudin nibh sit amet."""
