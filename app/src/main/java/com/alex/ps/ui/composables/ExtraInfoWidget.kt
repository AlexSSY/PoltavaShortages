package com.alex.ps.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ExtraInfoWidget(
    modifier: Modifier = Modifier,
    extra: List<Int>
) {
    if (extra.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            extra.forEach { extra ->
                ToastWidget(text = stringResource(id = extra))
            }
        }
    }
}