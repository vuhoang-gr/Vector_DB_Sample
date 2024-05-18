package com.example.searchapp.features.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch

data class TabInfo(
    @StringRes val tabName: Int, @DrawableRes val icon: Int? = null
) : TabInfoConvertible {
    override fun toTabInfo(): TabInfo = this
}

interface TabInfoConvertible {
    fun toTabInfo(): TabInfo
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun <T : TabInfoConvertible> TabLayout(
    modifier: Modifier = Modifier,
    contents: List<Pair<T, @Composable () -> Unit>>,
    pageState: PagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        contents.size
    },
    userScrollEnabled: Boolean = false,
    onTabSelected: (T) -> Unit = {},
) {

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = pageState.currentPage) {
            contents.forEachIndexed { index, (tabInfoConvertible, _) ->
                val tabInfo = tabInfoConvertible.toTabInfo()

                LeadingIconTab(
                    text = {
                        Text(
                            text = stringResource(id = tabInfo.tabName),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    icon = {
                        tabInfo.icon?.let {
                            Icon(
                                painter = painterResource(id = tabInfo.icon),
                                contentDescription = "${tabInfo.tabName} Tab",
                            )
                        }
                    },
                    selected = pageState.currentPage == index,
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = {
                        coroutineScope.launch {
                            pageState.scrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(
            state = pageState,
            pageSize = PageSize.Fill,
            userScrollEnabled = userScrollEnabled
        ) {
            contents[it].composable()
        }
    }

    LaunchedEffect(contents) {
        snapshotFlow { pageState.currentPage }.collect {
            val selectedTabInfo = contents[it].first
            onTabSelected(selectedTabInfo)
        }
    }
}

private val Pair<Any, @Composable (() -> Unit)>.composable
    get() = this.second



