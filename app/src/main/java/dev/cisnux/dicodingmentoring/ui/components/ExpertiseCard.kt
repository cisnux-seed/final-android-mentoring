package dev.cisnux.dicodingmentoring.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.cisnux.dicodingmentoring.ui.theme.DicodingMentoringTheme
import dev.cisnux.dicodingmentoring.utils.getLearningPathIcon

@Preview(showBackground = true)
@Composable
fun ExpertiseCardPreview() {
    Surface {
        DicodingMentoringTheme {
            ExpertiseCard(
                learningPath = "Android",
                skills = listOf(
                    "Object-Oriented Programming",
                    "Design Patter",
                    "Java",
                    "Kotlin",
                ),
                experienceLevel = "Beginner",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun ExpertiseCard(
    learningPath: String,
    skills: List<String>,
    experienceLevel: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onBackground,
                painter = painterResource(id = getLearningPathIcon(learningPath)),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("$learningPath - $experienceLevel", style = MaterialTheme.typography.titleMedium)
        }
        Column(
            modifier = Modifier
                .padding(start = 40.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            skills.forEach { skill ->
                Row {
                    Text("•", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(skill, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Divider(color = Color.Gray.copy(alpha = 0.4f), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}