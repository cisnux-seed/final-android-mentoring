@file:OptIn(ExperimentalMaterial3Api::class)

package dev.cisnux.dicodingmentoring.ui.addmentor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.ui.MainViewModel
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.MentoringForm
import dev.cisnux.dicodingmentoring.utils.UiState
import kotlinx.coroutines.launch

@Composable
fun AddMentorScreen(
    navigateToMyProfile: () -> Unit,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    addMentorViewModel: AddMentorViewModel = hiltViewModel(),
) {
    val oneTimeUpdateState by rememberUpdatedState(mainViewModel::updateBottomState)
    LaunchedEffect(Unit) {
        oneTimeUpdateState(false)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    val mentoringForms = addMentorViewModel.mentoringForms
    val experienceLevelOptions = listOf(
        stringResource(R.string.beginner),
        stringResource(R.string.intermediate),
        stringResource(
            R.string.expert
        )
    )
    val maxFormSize = addMentorViewModel.maxFormSize
    val learningPathOptions = addMentorViewModel.learningPathOptions
    val addMentorState by addMentorViewModel.addMentorState

    if (addMentorState is UiState.Success) {
        navigateToMyProfile()
        mainViewModel.refreshMyProfileContent(true)
    }

    if (addMentorState is UiState.Error) {
        LaunchedEffect(snackbarHostState) {
            (addMentorState as UiState.Error).error?.message?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    AddMentorContent(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        onNavigateUp = navigateToMyProfile,
        body = { innerPadding ->
            AddMentorBody(
                learningPathOptions = learningPathOptions,
                experienceLevelOptions = experienceLevelOptions,
                mentoringForms = mentoringForms,
                onAddForm = addMentorViewModel::onAddForm,
                onRemoveForm = addMentorViewModel::onRemoveForm,
                onExperienceLevelOption = addMentorViewModel::onExperienceLevelOption,
                onLearningPathOption = addMentorViewModel::onLearningPathOption,
                onSkillChanged = addMentorViewModel::onSkillChanged,
                onCertificateUrlChanged = addMentorViewModel::onCertificateUrlChanged,
                maxFormSize = maxFormSize,
                onExpandedLearningChanged = addMentorViewModel::onExpandedLearningChanged,
                onExpandedLevelChanged = addMentorViewModel::onExpandedExperienceChanged,
                onCloseLearning = addMentorViewModel::onCloseLearning,
                onCloseExperience = addMentorViewModel::onCloseExperience,
                onJoin = {
                    if (mentoringForms.any {
                            it.skills.isBlank()
                        }) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("the skill form must be filled")
                        }
                        return@AddMentorBody
                    }
                    if (mentoringForms.any {
                            it.certificateUrl.isBlank()
                        }) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("the certificate urls form must be filled")
                        }
                        return@AddMentorBody
                    }
                    if (addMentorState !is UiState.Loading) {
                        addMentorViewModel.onJoin()
                    }
                },
                modifier = Modifier.padding(innerPadding),
                isLoading = addMentorState is UiState.Loading
            )
        })
}

@Preview(showBackground = true)
@Composable
fun AddMentorContentPreview() {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val learningPathOptions = listOf(
        stringResource(id = R.string.android),
        stringResource(id = R.string.ios),
        stringResource(id = R.string.frontend),
        stringResource(id = R.string.backend),
        stringResource(id = R.string.cloud_computing),
        stringResource(id = R.string.machine_learning),
        stringResource(id = R.string.ui_ux),
    )
    val learningPathTracker = remember {
        mutableStateListOf(
            *learningPathOptions.subList(1, learningPathOptions.lastIndex + 1).toTypedArray()
        )
    }
    val experienceLevelOptions = listOf(
        stringResource(R.string.beginner), stringResource(R.string.intermediate), stringResource(
            R.string.expert
        )
    )
    val mentoringForms = remember {
        mutableStateListOf(
            MentoringForm(
                learningPath = learningPathOptions.first(),
                experienceLevel = experienceLevelOptions.first(),
                skills = "",
                certificateUrl = "",
                isLearningPathExpanded = false,
                isExperienceExpanded = false,
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()

    Surface {
        DicodingMentoringTheme {
            AddMentorContent(
                snackbarHostState = snackbarHostState,
                onNavigateUp = {},
                body = { innerPadding ->
                    AddMentorBody(
                        mentoringForms = mentoringForms,
                        onExperienceLevelOption = { experienceLevel, index ->
                            mentoringForms[index] =
                                mentoringForms[index].copy(experienceLevel = experienceLevel)
                        },
                        onAddForm = { selectedLearningPath ->
                            learningPathTracker.remove(selectedLearningPath)
                            val learningPath = learningPathTracker.first()
                            learningPathTracker.remove(learningPath)
                            mentoringForms.add(
                                MentoringForm(
                                    learningPath = learningPath,
                                    experienceLevel = experienceLevelOptions.first(),
                                    skills = "",
                                    certificateUrl = "",
                                    isLearningPathExpanded = false,
                                    isExperienceExpanded = false,
                                )
                            )
                        },
                        onRemoveForm = { selectedLearningPath ->
                            learningPathTracker.add(selectedLearningPath)
                            mentoringForms.removeLast()
                        },
                        onLearningPathOption = { learningPath, index ->
                            // remove old value
                            learningPathTracker.remove(learningPath)
                            // add new value
                            learningPathTracker.add(mentoringForms[index].learningPath)
                            mentoringForms[index] =
                                mentoringForms[index].copy(learningPath = learningPath)
                        },
                        onSkillChanged = { skill, index ->
                            mentoringForms[index] = mentoringForms[index].copy(skills = skill)
                        },
                        onCertificateUrlChanged = { certificateUrl, index ->
                            mentoringForms[index] =
                                mentoringForms[index].copy(certificateUrl = certificateUrl)
                        },
                        learningPathOptions = learningPathTracker,
                        experienceLevelOptions = experienceLevelOptions,
                        maxFormSize = learningPathOptions.size,
                        onCloseLearning = { index ->
                            mentoringForms[index] =
                                mentoringForms[index].copy(isLearningPathExpanded = false)
                        },
                        onCloseExperience = { index ->
                            mentoringForms[index] =
                                mentoringForms[index].copy(isExperienceExpanded = false)
                        },
                        onExpandedLearningChanged = { isExpanded, index ->
                            mentoringForms[index] =
                                mentoringForms[index].copy(isLearningPathExpanded = if (learningPathTracker.isEmpty()) false else isExpanded)
                        },
                        onExpandedLevelChanged = { isExpanded, index ->
                            mentoringForms[index] =
                                mentoringForms[index].copy(isExperienceExpanded = isExpanded)
                        },
                        modifier = Modifier.padding(innerPadding),
                        onJoin = {
                            if (mentoringForms.any {
                                    it.skills.isBlank()
                                }) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("the skill form must be filled")
                                }
                                return@AddMentorBody
                            }
                            if (mentoringForms.any {
                                    it.certificateUrl.isBlank()
                                }) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("the certificate urls form must be filled")
                                }
                                return@AddMentorBody
                            }
                            // do join
                        },
                        isLoading = false,
                    )
                }
            )
        }
    }
}

@Composable
fun AddMentorContent(
    onNavigateUp: () -> Unit,
    snackbarHostState: SnackbarHostState,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(text = "Join Mentor") }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddMentorBodyPreview() {
    val learningPathOptions = listOf(
        stringResource(id = R.string.android),
        stringResource(id = R.string.ios),
        stringResource(id = R.string.frontend),
        stringResource(id = R.string.backend),
        stringResource(id = R.string.cloud_computing),
        stringResource(id = R.string.machine_learning),
        stringResource(id = R.string.ui_ux),
    )
    val learningPathTracker = remember {
        mutableStateListOf(
            *learningPathOptions.subList(1, learningPathOptions.lastIndex + 1).toTypedArray()
        )
    }
    val experienceLevelOptions = listOf(
        stringResource(R.string.beginner), stringResource(R.string.intermediate), stringResource(
            R.string.expert
        )
    )
    val mentoringForms = remember {
        mutableStateListOf(
            MentoringForm(
                learningPath = learningPathOptions.first(),
                experienceLevel = experienceLevelOptions.first(),
                skills = "",
                certificateUrl = "",
                isLearningPathExpanded = false,
                isExperienceExpanded = false,
            )
        )
    }

    Surface {
        DicodingMentoringTheme {
            AddMentorBody(
                mentoringForms = mentoringForms,
                onExperienceLevelOption = { experienceLevel, index ->
                    mentoringForms[index] =
                        mentoringForms[index].copy(experienceLevel = experienceLevel)
                },
                onAddForm = { selectedLearningPath ->
                    learningPathTracker.remove(selectedLearningPath)
                    val learningPath = learningPathTracker.first()
                    learningPathTracker.remove(learningPath)
                    mentoringForms.add(
                        MentoringForm(
                            learningPath = learningPath,
                            experienceLevel = experienceLevelOptions.first(),
                            skills = "",
                            certificateUrl = "",
                            isLearningPathExpanded = false,
                            isExperienceExpanded = false,
                        )
                    )
                },
                onRemoveForm = { selectedLearningPath ->
                    learningPathTracker.add(selectedLearningPath)
                    mentoringForms.removeLast()
                },
                onLearningPathOption = { learningPath, index ->
                    // remove old value
                    learningPathTracker.remove(learningPath)
                    // add new value
                    learningPathTracker.add(mentoringForms[index].learningPath)
                    mentoringForms[index] = mentoringForms[index].copy(learningPath = learningPath)
                },
                onSkillChanged = { skill, index ->
                    mentoringForms[index] = mentoringForms[index].copy(skills = skill)
                },
                onCertificateUrlChanged = { certificateUrl, index ->
                    mentoringForms[index] =
                        mentoringForms[index].copy(certificateUrl = certificateUrl)
                },
                learningPathOptions = learningPathTracker,
                experienceLevelOptions = experienceLevelOptions,
                maxFormSize = learningPathOptions.size,
                onCloseLearning = { index ->
                    mentoringForms[index] =
                        mentoringForms[index].copy(isLearningPathExpanded = false)
                },
                onCloseExperience = { index ->
                    mentoringForms[index] = mentoringForms[index].copy(isExperienceExpanded = false)
                },
                onExpandedLearningChanged = { isExpanded, index ->
                    mentoringForms[index] =
                        mentoringForms[index].copy(isLearningPathExpanded = if (learningPathTracker.isEmpty()) false else isExpanded)
                },
                onExpandedLevelChanged = { isExpanded, index ->
                    mentoringForms[index] =
                        mentoringForms[index].copy(isExperienceExpanded = isExpanded)
                },
                onJoin = {},
                isLoading = false,
            )
        }
    }
}

@Composable
fun AddMentorBody(
    learningPathOptions: List<String>,
    experienceLevelOptions: List<String>,
    mentoringForms: List<MentoringForm>,
    onAddForm: (selectedLearningPath: String) -> Unit,
    onRemoveForm: (selectedLearningPath: String) -> Unit,
    onExperienceLevelOption: (experienceLevel: String, index: Int) -> Unit,
    onLearningPathOption: (learningPath: String, index: Int) -> Unit,
    onSkillChanged: (skill: String, index: Int) -> Unit,
    onCertificateUrlChanged: (certificateUrl: String, index: Int) -> Unit,
    maxFormSize: Int,
    onExpandedLearningChanged: (expanded: Boolean, index: Int) -> Unit,
    onExpandedLevelChanged: (expanded: Boolean, index: Int) -> Unit,
    onCloseLearning: (index: Int) -> Unit,
    onCloseExperience: (index: Int) -> Unit,
    onJoin: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(mentoringForms) { formIndex, mentoringForm ->
            ExpertiseFormItem(
                learningPathOptions = learningPathOptions,
                learningPath = mentoringForm.learningPath,
                onLearningPathOption = { learningPath ->
                    onLearningPathOption(learningPath, formIndex)
                },
                experienceOptions = experienceLevelOptions,
                experienceLevel = mentoringForm.experienceLevel,
                onExperienceLevelOption = { experienceLevel ->
                    onExperienceLevelOption(experienceLevel, formIndex)
                },
                skill = mentoringForm.skills,
                onSkillChanged = { skill ->
                    onSkillChanged(skill, formIndex)
                },
                certificateUrl = mentoringForm.certificateUrl,
                onCertificateUrlChanged = { certificateUrl ->
                    onCertificateUrlChanged(certificateUrl, formIndex)
                },
                onAddForm = {
                    onAddForm(mentoringForm.learningPath)
                },
                onRemoveForm = {
                    onRemoveForm(mentoringForm.learningPath)
                },
                formFraction = when {
                    formIndex == 0 && mentoringForms.size == 1 -> 0.9f
                    formIndex == maxFormSize - 1 -> 0.9f
                    formIndex == mentoringForms.lastIndex -> 0.8f
                    else -> 1f
                },
                showAddButton =
                formIndex == mentoringForms.lastIndex
                        && mentoringForms.size < maxFormSize,
                showRemoveButton = mentoringForms.size > 1 && formIndex == mentoringForms.lastIndex,
                onCloseLearning = {
                    onCloseLearning(formIndex)
                },
                onCloseExperience = {
                    onCloseExperience(formIndex)
                },
                onExpandedLearningChanged = {
                    onExpandedLearningChanged(it, formIndex)
                },
                onExpandedLevelChanged = {
                    onExpandedLevelChanged(it, formIndex)
                },
                isExperienceExpanded = mentoringForm.isExperienceExpanded,
                isLearningPathExpanded = mentoringForm.isLearningPathExpanded,

                )
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Button(
                onClick = onJoin,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!isLoading)
                    Text(text = "Join")
                else CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExpertiseFormItemPreview() {
    Surface {
        MaterialTheme {
            ExpertiseFormItem(
                experienceOptions = listOf(
                    stringResource(R.string.beginner),
                    stringResource(R.string.intermediate),
                    stringResource(R.string.expert)
                ),
                learningPathOptions = listOf(
                    stringResource(id = R.string.android),
                    stringResource(id = R.string.ios),
                    stringResource(id = R.string.frontend),
                    stringResource(id = R.string.backend),
                    stringResource(id = R.string.cloud_computing),
                    stringResource(id = R.string.machine_learning),
                    stringResource(id = R.string.ui_ux),
                ),
                experienceLevel = "",
                learningPath = "",
                onExperienceLevelOption = {},
                onLearningPathOption = {},
                onAddForm = { /*TODO*/ },
                onRemoveForm = { /*TODO*/ },
                formFraction = 0.8f,
                showAddButton = true,
                showRemoveButton = true,
                modifier = Modifier.padding(8.dp),
                skill = "",
                onSkillChanged = {},
                certificateUrl = "",
                onCertificateUrlChanged = {},
                onCloseLearning = {},
                onCloseExperience = {},
                onExpandedLearningChanged = {},
                onExpandedLevelChanged = {},
                isExperienceExpanded = false,
                isLearningPathExpanded = false,
            )
        }
    }
}

@Composable
fun ExpertiseFormItem(
    learningPathOptions: List<String>,
    learningPath: String,
    onLearningPathOption: (learningPath: String) -> Unit,
    experienceOptions: List<String>,
    experienceLevel: String,
    onExperienceLevelOption: (experienceLevel: String) -> Unit,
    skill: String,
    onSkillChanged: (skill: String) -> Unit,
    certificateUrl: String,
    onCertificateUrlChanged: (certificateUrl: String) -> Unit,
    onAddForm: () -> Unit,
    onRemoveForm: () -> Unit,
    formFraction: Float,
    showAddButton: Boolean,
    showRemoveButton: Boolean,
    isLearningPathExpanded: Boolean,
    isExperienceExpanded: Boolean,
    onExpandedLearningChanged: (expanded: Boolean) -> Unit,
    onExpandedLevelChanged: (expanded: Boolean) -> Unit,
    onCloseLearning: () -> Unit,
    onCloseExperience: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(formFraction)
                .wrapContentHeight()
        ) {
            ExposedDropdownMenuBox(
                expanded = isLearningPathExpanded,
                onExpandedChange = onExpandedLearningChanged
            ) {
                OutlinedTextField(
                    value = learningPath,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    maxLines = 1,
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLearningPathExpanded) },
                    placeholder = {
                        Text(text = "Learning Path")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )
                ExposedDropdownMenu(
                    expanded = isLearningPathExpanded,
                    onDismissRequest = onCloseLearning
                ) {
                    learningPathOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                onLearningPathOption(selectionOption)
                                onCloseLearning()
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = isExperienceExpanded,
                onExpandedChange = onExpandedLevelChanged
            ) {
                OutlinedTextField(
                    value = experienceLevel,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    maxLines = 1,
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExperienceExpanded) },
                    placeholder = {
                        Text(text = "Experience Level")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )
                ExposedDropdownMenu(
                    expanded = isExperienceExpanded,
                    onDismissRequest = onCloseExperience
                ) {
                    experienceOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                onExperienceLevelOption(selectionOption)
                                onCloseExperience()
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = skill,
                onValueChange = onSkillChanged,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter your skill")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = certificateUrl,
                onValueChange = onCertificateUrlChanged,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter your certificate url")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        }
        AnimatedVisibility(visible = showAddButton) {
            IconButton(onClick = onAddForm, modifier = Modifier.offset(y = 4.dp)) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null
                )
            }
        }
        AnimatedVisibility(visible = showRemoveButton) {
            IconButton(onClick = onRemoveForm, modifier = Modifier.offset(y = 4.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove_circle_24),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null
                )
            }
        }
    }
}

