package dev.cisnux.dicodingmentoring.ui.creatementoring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun CreateMentoringScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToMentoring: () -> Unit,
    createMentoringViewModel: CreateMentoringViewModel = hiltViewModel(),
) {
    val oneTimeUpdateState by rememberUpdatedState(mainViewModel::updateBottomState)
    val navigateToMentoringState by rememberUpdatedState(navigateToMentoring)
    LaunchedEffect(Unit) {
        oneTimeUpdateState(false)
    }
    val title by createMentoringViewModel.title
    val description by createMentoringViewModel.description
    val mentoringType by createMentoringViewModel.mentoringType
    val mentoringDate by createMentoringViewModel.mentoringDate
    val mentoringTime by createMentoringViewModel.mentoringTime
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val isCreateMentoringSuccess by createMentoringViewModel.isCreateMentoringSuccess

    if (isCreateMentoringSuccess) {
        LaunchedEffect(Unit){
            navigateToMentoringState()
        }
    }

    CreateMentoringContent(
        modifier = modifier,
        navigateUp = navigateUp,
        body = { innerPadding ->
            CreateMentoringBody(
                title = title,
                onTitleChanged = createMentoringViewModel::onTitleChanged,
                description = description,
                onDescriptionChanged = createMentoringViewModel::onDescriptionChanged,
                onMentoringType = createMentoringViewModel::onMentoringTypeSelected,
                mentoringType = mentoringType,
                radioTitles = listOf(
                    stringResource(id = R.string.chat),
                    stringResource(id = R.string.video_chat)
                ),
                onMentoringDateChanged = createMentoringViewModel::onMentoringDateChanged,
                mentoringDate = mentoringDate,
                modifier = Modifier.padding(innerPadding),
                mentoringTime = mentoringTime,
                onMentoringTimeChanged = createMentoringViewModel::onMentoringTimeChanged,
                onCrateMentoring = {
                    if (title.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("the problem title must be filled")
                        }
                        return@CreateMentoringBody
                    }

                    if (description.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("the description must be filled")
                        }
                        return@CreateMentoringBody
                    }

                    if (mentoringDate.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("the mentoring date must be filled with correct date")
                        }
                        return@CreateMentoringBody
                    }

                    if (mentoringTime.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("the mentoring time be filled with correct date")
                        }
                        return@CreateMentoringBody
                    }
                    createMentoringViewModel.createMentoringSession()
                }
            )
        },
        snackbarHostState = snackbarHostState
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateMentoringContentPreview() {
    Surface {
        DicodingMentoringTheme {
            CreateMentoringContent(
                navigateUp = { /*TODO*/ },
                body = { innerPadding ->
                    CreateMentoringBody(
                        title = "",
                        onTitleChanged = {},
                        description = "",
                        onDescriptionChanged = {},
                        onMentoringType = {},
                        mentoringType = "",
                        mentoringDate = "",
                        modifier = Modifier.padding(innerPadding),
                        onMentoringDateChanged = {},
                        radioTitles = listOf(
                            stringResource(id = R.string.chat),
                            stringResource(id = R.string.video_chat)
                        ),
                        mentoringTime = "",
                        onMentoringTimeChanged = {},
                        onCrateMentoring = {}
                    )
                },
                snackbarHostState = SnackbarHostState()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMentoringBody(
    title: String,
    onTitleChanged: (String) -> Unit,
    mentoringType: String,
    mentoringDate: String,
    mentoringTime: String,
    onMentoringDateChanged: (Long?) -> Unit,
    onMentoringTimeChanged: (Long?) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    onMentoringType: (String) -> Unit,
    radioTitles: List<String>,
    onCrateMentoring: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var openDatePickerDialog by remember { mutableStateOf(false) }
    var openTimePickerDialog by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val timePickerState = rememberTimePickerState()
    val showingTimePicker = remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChanged,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Enter your problem title")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Enter your problem description")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = mentoringDate,
            readOnly = true,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Select the date for mentoring")
            },
            leadingIcon = {
                IconButton(onClick = {
                    openDatePickerDialog = true
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_date_24),
                        contentDescription = "select date mentoring"
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
        if (openDatePickerDialog) {
            val datePickerState = rememberDatePickerState()
            val confirmEnabled = remember {
                derivedStateOf { datePickerState.selectedDateMillis != null }
            }

            DatePickerDialog(
                onDismissRequest = {
                    openDatePickerDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDatePickerDialog = false
                            onMentoringDateChanged(datePickerState.selectedDateMillis)
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDatePickerDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = mentoringTime,
            readOnly = true,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Select the time for mentoring")
            },
            leadingIcon = {
                IconButton(
                    enabled = mentoringDate.isNotBlank(),
                    onClick = {
                        openTimePickerDialog = true
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time_24),
                        contentDescription = "select date mentoring"
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
        if (openTimePickerDialog) {
            TimePickerDialog(
                title = if (showingTimePicker.value) {
                    "Select Time "
                } else {
                    "Enter Time"
                },
                onCancel = { openTimePickerDialog = false },
                onConfirm = {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    cal.set(Calendar.MINUTE, timePickerState.minute)
                    cal.isLenient = false
                    onMentoringTimeChanged(cal.timeInMillis)
                    openTimePickerDialog = false
                },
                toggle = {
                    if (configuration.screenHeightDp > 400) {
                        // Make this take the entire viewport. This will guarantee that Screen readers
                        // focus the toggle first.
                        Box(
                            Modifier
                                .fillMaxSize()
                                .semantics {
                                    @Suppress("DEPRECATION")
                                    isContainer = true
                                }
                        ) {
                            IconButton(
                                modifier = Modifier
                                    // This is a workaround so that the Icon comes up first
                                    // in the talkback traversal order. So that users of a11y
                                    // services can use the text input. When talkback traversal
                                    // order is customizable we can remove this.
                                    .size(64.dp, 72.dp)
                                    .align(Alignment.BottomStart)
                                    .zIndex(5f),
                                onClick = { showingTimePicker.value = !showingTimePicker.value }) {
                                val icon = painterResource(
                                    id = if (showingTimePicker.value) {
                                        R.drawable.ic_keyboard_24
                                    } else {
                                        R.drawable.ic_schedule_24
                                    }
                                )
                                Icon(
                                    painter = icon,
                                    contentDescription = if (showingTimePicker.value) {
                                        "Switch to Text Input"
                                    } else {
                                        "Switch to Touch Input"
                                    }
                                )
                            }
                        }
                    }
                }
            ) {
                if (showingTimePicker.value && configuration.screenHeightDp > 400) {
                    TimePicker(state = timePickerState)
                } else {
                    TimeInput(state = timePickerState)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Mentoring Type:"
        )
        radioTitles.forEach { title ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
            ) {
                RadioButton(
                    selected = title == mentoringType,
                    onClick = {
                        onMentoringType(title)
                    },
                    modifier = Modifier.semantics { contentDescription = title }
                )
                Text(text = title)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            shape = MaterialTheme.shapes.small,
            onClick = onCrateMentoring,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Mentoring")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMentoringContent(
    navigateUp: () -> Unit,
    snackbarHostState: SnackbarHostState,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back to detail mentor page"
                        )
                    }
                },
                title = {
                    Text(text = "Create Mentoring")
                }
            )
        },
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}