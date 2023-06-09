package dev.cisnux.dicodingmentoring.ui.registerprofile

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.state.ToggleableState
import kotlinx.parcelize.Parcelize

@Composable
fun rememberLearningPathsCheckBoxState(
) = rememberSaveable {
    mutableStateOf(
        InterestsCheckBoxState(
            checkBox1State = false,
            checkBox2State = false,
            checkBox3State = false,
            checkBox4State = false,
            checkBox5State = false,
            checkBox6State = false,
            checkBox7State = false
        )
    )
}


@Stable
@Parcelize
data class InterestsCheckBoxState(
    val checkBox1State: Boolean,
    val checkBox2State: Boolean,
    val checkBox3State: Boolean,
    val checkBox4State: Boolean,
    val checkBox5State: Boolean,
    val checkBox6State: Boolean,
    val checkBox7State: Boolean,
) : Parcelable

val InterestsCheckBoxState.parentState
    get() = if (checkBox1State and checkBox2State
        and checkBox3State and checkBox4State
        and checkBox5State and checkBox6State
        and checkBox7State
    ) ToggleableState.On
    else if (!checkBox1State and !checkBox2State
        and !checkBox3State and !checkBox4State
        and !checkBox5State and !checkBox6State
        and !checkBox7State
    ) ToggleableState.Off
    else ToggleableState.Indeterminate