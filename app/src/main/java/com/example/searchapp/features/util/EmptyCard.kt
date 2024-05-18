import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.searchapp.ui.theme.shape
import com.example.searchapp.ui.theme.spacing

@Composable
internal fun EmptyCard(
    onClick: () -> Unit,
    message: String,
){
    val strokeStyle = MaterialTheme.shape.dashedStroke
    val cornerRadius = MaterialTheme.shape.cornerRadius
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .drawBehind {
                drawRoundRect(
                    color = Color.Gray,
                    style = strokeStyle,
                    cornerRadius = cornerRadius
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.large)
                .padding(vertical = 100.dp),
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
internal fun EmptyCardPreview(){
    Surface {
        EmptyCard(
            onClick = {},
            message = "Empty message"
        )
    }
}
