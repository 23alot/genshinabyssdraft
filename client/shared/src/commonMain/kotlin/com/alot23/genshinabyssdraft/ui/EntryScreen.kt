package com.alot23.genshinabyssdraft.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.alot23.genshinabyssdraft.entity.GameInfo
import com.alot23.genshinabyssdraft.entity.LogInfo
import com.alot23.genshinabyssdraft.entity.Step
import com.alot23.genshinabyssdraft.gameConfig
import com.alot23.genshinabyssdraft.presentation.entry.CharactersConfiguration
import com.alot23.genshinabyssdraft.presentation.entry.EntryState
import com.alot23.genshinabyssdraft.presentation.entry.EntryViewModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.encodeToJsonElement
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.viewmodel.viewModel

fun RouteBuilder.EntryScreen(navigator: Navigator) = scene(route = "/entry") {
    val entryViewModel = viewModel(modelClass = EntryViewModel::class) {
        EntryViewModel()
    }
    val state by entryViewModel.state.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val viewState = state) {
            is EntryState.Login -> Login(viewModel = entryViewModel)
            is EntryState.Create -> Create(viewModel = entryViewModel, state = viewState)
            is EntryState.Created -> AfterCreate(viewModel = entryViewModel, gameInfo = viewState.gameInfo)
            is EntryState.LoggedIn -> navigator.navigate("/home?gameToken=${viewState.logInfo.gameToken}&playerToken=${viewState.logInfo.userToken}&role=${viewState.logInfo.role}")
        }
    }

}

@Composable
fun Login(viewModel: EntryViewModel) = Column {
    var loginToken by remember { mutableStateOf("") }
    TextField(value = loginToken, onValueChange = { loginToken = it } )
    Row {
        Button(onClick = { viewModel.onConfirmLogin(loginToken = loginToken) }) {
            Text(text = "Login")
        }
        Button(onClick = { viewModel.onCreateClick() }) {
            Text(text = "Create")
        }

    }
}

val testPlayerConfig = "[{\"title\":\"1\",\"items\":{\"1\":{\"const\":0,\"level\":66},\"2\":{\"const\":0,\"level\":1},\"3\":{\"const\":0,\"level\":42},\"4\":{\"const\":0,\"level\":30},\"5\":{\"const\":0,\"level\":2},\"6\":{\"const\":0,\"level\":17},\"7\":{\"const\":0,\"level\":5},\"8\":{\"const\":0,\"level\":18},\"9\":{\"const\":0,\"level\":33},\"10\":{\"const\":0,\"level\":21},\"11\":{\"const\":0,\"level\":7},\"12\":{\"const\":0,\"level\":55},\"13\":{\"const\":0,\"level\":22},\"14\":{\"const\":0,\"level\":34},\"15\":{\"const\":0,\"level\":8},\"16\":{\"const\":0,\"level\":37},\"17\":{\"const\":0,\"level\":46},\"18\":{\"const\":0,\"level\":70},\"19\":{\"const\":0,\"level\":71},\"20\":{\"const\":0,\"level\":26},\"21\":{\"const\":0,\"level\":39},\"22\":{\"const\":0,\"level\":60},\"23\":{\"const\":0,\"level\":50},\"24\":{\"const\":0,\"level\":61},\"25\":{\"const\":0,\"level\":72},\"26\":{\"const\":0,\"level\":62},\"27\":{\"const\":0,\"level\":11},\"28\":{\"const\":0,\"level\":65},\"29\":{\"const\":0,\"level\":52},\"30\":{\"const\":0,\"level\":12},\"31\":{\"const\":0,\"level\":74},\"32\":{\"const\":0,\"level\":27},\"33\":{\"const\":0,\"level\":19},\"34\":{\"const\":0,\"level\":13},\"35\":{\"const\":0,\"level\":56},\"36\":{\"const\":0,\"level\":23},\"37\":{\"const\":0,\"level\":14},\"38\":{\"const\":0,\"level\":58},\"39\":{\"const\":0,\"level\":40},\"40\":{\"const\":0,\"level\":38},\"41\":{\"const\":0,\"level\":49},\"42\":{\"const\":0,\"level\":35},\"43\":{\"const\":0,\"level\":15},\"44\":{\"const\":0,\"level\":10},\"45\":{\"const\":0,\"level\":68},\"46\":{\"const\":0,\"level\":67},\"47\":{\"const\":0,\"level\":73},\"48\":{\"const\":0,\"level\":28},\"49\":{\"const\":0,\"level\":41},\"50\":{\"const\":0,\"level\":45},\"51\":{\"const\":0,\"level\":53},\"52\":{\"const\":0,\"level\":36},\"53\":{\"const\":0,\"level\":59},\"54\":{\"const\":0,\"level\":81},\"55\":{\"const\":0,\"level\":77},\"56\":{\"const\":0,\"level\":32},\"57\":{\"const\":0,\"level\":82},\"58\":{\"const\":0,\"level\":43},\"59\":{\"const\":0,\"level\":31},\"60\":{\"const\":0,\"level\":48},\"61\":{\"const\":0,\"level\":80},\"62\":{\"const\":0,\"level\":24},\"63\":{\"const\":0,\"level\":63},\"64\":{\"const\":0,\"level\":54},\"65\":{\"const\":0,\"level\":83},\"66\":{\"const\":0,\"level\":75},\"67\":{\"const\":0,\"level\":4},\"68\":{\"const\":0,\"level\":25},\"69\":{\"const\":0,\"level\":78},\"70\":{\"const\":0,\"level\":76},\"71\":{\"const\":0,\"level\":79},\"72\":{\"const\":0,\"level\":57},\"73\":{\"const\":0,\"level\":9},\"74\":{\"const\":0,\"level\":20},\"75\":{\"const\":0,\"level\":51},\"76\":{\"const\":0,\"level\":29},\"77\":{\"const\":0,\"level\":47},\"78\":{\"const\":0,\"level\":16},\"79\":{\"const\":0,\"level\":44},\"80\":{\"const\":0,\"level\":3},\"81\":{\"const\":0,\"level\":69},\"82\":{\"const\":0,\"level\":6},\"83\":{\"const\":0,\"level\":64},\"84\":{\"const\":-1,\"level\":\"\"}}},{\"title\":\"123\",\"items\":{\"1\":{\"const\":0,\"level\":\"\"},\"2\":{\"const\":0,\"level\":\"\"},\"3\":{\"const\":0,\"level\":\"\"},\"4\":{\"const\":0,\"level\":\"\"},\"5\":{\"const\":0,\"level\":\"\"},\"6\":{\"const\":0,\"level\":\"\"},\"7\":{\"const\":0,\"level\":\"\"},\"8\":{\"const\":0,\"level\":\"\"},\"9\":{\"const\":0,\"level\":\"\"},\"10\":{\"const\":0,\"level\":\"\"},\"11\":{\"const\":0,\"level\":\"\"},\"12\":{\"const\":0,\"level\":\"\"},\"13\":{\"const\":0,\"level\":\"\"},\"14\":{\"const\":0,\"level\":\"\"},\"15\":{\"const\":0,\"level\":\"\"},\"16\":{\"const\":0,\"level\":\"\"},\"17\":{\"const\":0,\"level\":\"\"},\"18\":{\"const\":0,\"level\":\"\"},\"19\":{\"const\":0,\"level\":\"\"},\"20\":{\"const\":0,\"level\":\"\"},\"21\":{\"const\":0,\"level\":\"\"},\"22\":{\"const\":0,\"level\":\"\"},\"23\":{\"const\":0,\"level\":\"\"},\"24\":{\"const\":0,\"level\":\"\"},\"25\":{\"const\":0,\"level\":\"\"},\"26\":{\"const\":0,\"level\":\"\"},\"27\":{\"const\":0,\"level\":\"\"},\"28\":{\"const\":0,\"level\":\"\"},\"29\":{\"const\":0,\"level\":\"\"},\"30\":{\"const\":0,\"level\":\"\"},\"31\":{\"const\":0,\"level\":\"\"},\"32\":{\"const\":0,\"level\":\"\"},\"33\":{\"const\":0,\"level\":\"\"},\"34\":{\"const\":0,\"level\":\"\"},\"35\":{\"const\":0,\"level\":\"\"},\"36\":{\"const\":0,\"level\":\"\"},\"37\":{\"const\":0,\"level\":\"\"},\"38\":{\"const\":0,\"level\":\"\"},\"39\":{\"const\":0,\"level\":\"\"},\"40\":{\"const\":0,\"level\":\"\"},\"41\":{\"const\":0,\"level\":\"\"},\"42\":{\"const\":0,\"level\":\"\"},\"43\":{\"const\":0,\"level\":\"\"},\"44\":{\"const\":0,\"level\":\"\"},\"45\":{\"const\":0,\"level\":\"\"},\"46\":{\"const\":0,\"level\":\"\"},\"47\":{\"const\":0,\"level\":\"\"},\"48\":{\"const\":0,\"level\":\"\"},\"49\":{\"const\":0,\"level\":\"\"},\"50\":{\"const\":0,\"level\":\"\"},\"51\":{\"const\":0,\"level\":\"\"},\"52\":{\"const\":0,\"level\":\"\"},\"53\":{\"const\":0,\"level\":\"\"},\"54\":{\"const\":0,\"level\":\"\"},\"55\":{\"const\":0,\"level\":\"\"},\"56\":{\"const\":0,\"level\":\"\"},\"57\":{\"const\":0,\"level\":\"\"},\"58\":{\"const\":0,\"level\":\"\"},\"59\":{\"const\":0,\"level\":\"\"},\"60\":{\"const\":0,\"level\":\"\"},\"61\":{\"const\":0,\"level\":\"\"},\"62\":{\"const\":0,\"level\":\"\"},\"63\":{\"const\":0,\"level\":\"\"},\"64\":{\"const\":0,\"level\":\"\"},\"65\":{\"const\":0,\"level\":\"\"},\"66\":{\"const\":0,\"level\":\"\"},\"67\":{\"const\":0,\"level\":\"\"},\"68\":{\"const\":0,\"level\":\"\"},\"69\":{\"const\":0,\"level\":\"\"},\"70\":{\"const\":0,\"level\":\"\"},\"71\":{\"const\":0,\"level\":\"\"},\"72\":{\"const\":0,\"level\":\"\"},\"73\":{\"const\":0,\"level\":\"\"},\"74\":{\"const\":0,\"level\":\"\"},\"75\":{\"const\":0,\"level\":\"\"},\"76\":{\"const\":0,\"level\":\"\"},\"77\":{\"const\":0,\"level\":\"\"},\"78\":{\"const\":0,\"level\":\"\"},\"79\":{\"const\":0,\"level\":\"\"},\"80\":{\"const\":0,\"level\":\"\"},\"81\":{\"const\":0,\"level\":\"\"},\"82\":{\"const\":0,\"level\":\"\"},\"83\":{\"const\":0,\"level\":\"\"},\"84\":{\"const\":0,\"level\":\"\"}}}]"
val testGameList = gameConfig.map { it.toString() }
val testGameConfig = Json.encodeToJsonElement(testGameList).toString()


@Composable
fun Create(viewModel: EntryViewModel, state: EntryState.Create) = Column {
    var configs by remember { mutableStateOf(testPlayerConfig) }
    var firstConfig by remember { mutableStateOf<CharactersConfiguration?>(null) }
    var secondConfig by remember { mutableStateOf<CharactersConfiguration?>(null) }
    var gameConfig by remember { mutableStateOf(testGameConfig) }
    var immuneCharacters by remember { mutableStateOf("Furina;Nahida") }
    Row {
        TextField(value = configs, onValueChange = { configs = it }, maxLines = 1, modifier = Modifier.width(100.dp))
        Button(onClick = { viewModel.applyConfigs(configs = configs) }) {
            Text(text = "Apply")
        }
    }
    ConfigurationChooser(text = "First", configuration = firstConfig, characters = state.configurations, onClick = { config -> firstConfig = config})
    ConfigurationChooser(text = "Second", configuration = secondConfig, characters = state.configurations, onClick = { config -> secondConfig = config})

    TextField(value = gameConfig, onValueChange = { gameConfig = it }, maxLines = 2 )
    TextField(value = immuneCharacters, onValueChange = { immuneCharacters = it }, maxLines = 2 )
    Row {
        Button(onClick = { viewModel.onLoginClick() }) {
            Text(text = "Login")
        }
        Button(
            enabled = firstConfig != null && secondConfig != null,
            onClick = { viewModel.onConfirmCreate(firstConfig!!, secondConfig!!, gameConfig, immuneCharacters) }
        ) {
            Text(text = "Create")
        }
    }
}

@Composable
private fun ConfigurationChooser(text: String, configuration: CharactersConfiguration?, characters: List<CharactersConfiguration>?, onClick: ((CharactersConfiguration) -> Unit)) = Column {
    var isExpanded by remember { mutableStateOf(false) }
    Row {
        Text(text = text)
        Button(onClick = { isExpanded = !isExpanded}) {
            Text(text = configuration?.name ?: "Pick")
        }
    }
    if (characters != null) {
        CharactersDropdownMenu(isExpanded = isExpanded, characters = characters, onClick = onClick, onDismissRequest = { isExpanded = false })
    }

}

@Composable
fun CharactersDropdownMenu(isExpanded: Boolean, characters: List<CharactersConfiguration>, onClick: ((CharactersConfiguration) -> Unit), onDismissRequest: (() -> Unit)) = DropdownMenu(expanded = isExpanded, onDismissRequest = onDismissRequest) {
    characters.forEach { config ->
        DropdownMenuItem(onClick = { onClick(config) }) {
            Text(text = config.name)
        }
    }
}

@Composable
fun AfterCreate(viewModel: EntryViewModel, gameInfo: GameInfo) = Column {
    val clipboardManager = LocalClipboardManager.current
    Row {
        SelectionContainer {
            Text(text = "First token: ${gameInfo.gameToken};${gameInfo.firstToken}")
        }
        Button(onClick = { clipboardManager.setText(AnnotatedString("${gameInfo.gameToken};${gameInfo.firstToken}")) }) {
            Text(text = "Copy")
        }
    }
    Row {
        SelectionContainer {
            Text(text = "Second token: ${gameInfo.gameToken};${gameInfo.secondToken}")
        }
        Button(onClick = { clipboardManager.setText(AnnotatedString("${gameInfo.gameToken};${gameInfo.secondToken}")) }) {
            Text(text = "Copy")
        }
    }
    Row {
        SelectionContainer {
            Text(text = "Observer token: ${gameInfo.gameToken};${gameInfo.observerToken}")
        }
        Button(onClick = { clipboardManager.setText(AnnotatedString("${gameInfo.gameToken};${gameInfo.observerToken}")) }) {
            Text(text = "Copy")
        }
    }
    Button(onClick = { clipboardManager.setText(AnnotatedString("${gameInfo.gameToken};${gameInfo.firstToken}\n${gameInfo.gameToken};${gameInfo.secondToken}\n${gameInfo.gameToken};${gameInfo.observerToken}"))}) {
        Text(text = "Copy all")
    }
    Button(onClick = { viewModel.onLoginClick() }) {
        Text(text = "Login")
    }
    }

@Composable
fun AfterLogin(viewModel: EntryViewModel, logInfo: LogInfo) = SelectionContainer {
    Column {
        Text(text = logInfo.gameToken)
        Text(text = logInfo.userToken)
        Text(text = logInfo.role.toString())
    }
}