package bigbird.screen

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.nostrino.client.RelayClient
import app.cash.nostrino.crypto.SecKey
import app.cash.nostrino.model.Filter
import app.cash.nostrino.model.TextNote
import bigbird.screen.HomeComponent.Model
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

interface HomeComponent {
    val model: Value<Model>

    data class Model(val secKey: SecKey, val notes: List<TextNote>)
}

class DefaultHomeComponent(
    componentContext: ComponentContext,
    secKey: SecKey,
) : HomeComponent {

    override val model: Value<Model> = MutableValue(Model(secKey, emptyList()))

    /*
    // TODO - this needs to happen somewhere else. No runBlocking!
    private val notes = runBlocking {
        val client = RelayClient("wss://damus.io")
        client.subscribe(Filter.globalFeedNotes)
        client.notes.take(100).toList().map { it.content() }.filterIsInstance<TextNote>()
    }
    */

}

@Composable
fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier,
) {
    val model by component.model.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(180, 180, 180))
            .padding(10.dp)
    ) {
        val stateVertical = rememberScrollState(0)
        val stateHorizontal = rememberScrollState(0)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
                .horizontalScroll(stateHorizontal)
        ) {
            Column {
                model.notes.forEach {
                    TextNoteBox(it.text)

                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 12.dp),
            adapter = rememberScrollbarAdapter(stateHorizontal)
        )
    }
}

@Composable
fun TextNoteBox(text: String) {
    Box(
        modifier = Modifier.height(32.dp)
            .width(400.dp)
            .background(color = Color(200, 0, 0, 20))
            .padding(start = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text)
    }
}
