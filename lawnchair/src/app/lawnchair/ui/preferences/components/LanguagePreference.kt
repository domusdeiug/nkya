/*
 * Copyright 2026, Nkya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.lawnchair.ui.preferences.components

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import app.lawnchair.ui.preferences.components.controls.ListPreference
import app.lawnchair.ui.preferences.components.controls.ListPreferenceEntry
import com.android.launcher3.R

private const val SYSTEM_DEFAULT_TAG = ""

/** Languages Nkya ships, as (BCP-47 tag, display name). */
private val nkyaLanguages = listOf(
    "en" to "English",
    "lg" to "Luganda",
    "ach" to "Acholi",
    "nyn" to "Runyankole",
    "teo" to "Ateso",
    "lgg" to "Lugbara",
    "sw" to "Kiswahili",
)

/**
 * In-app language picker. Uses the per-app language API, which exists on Android 13+;
 * on older versions the row is hidden and the system language is used.
 */
@Composable
fun LanguagePreference(modifier: Modifier = Modifier) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    LanguagePreferenceApi33(modifier)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun LanguagePreferenceApi33(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val localeManager = remember(context) { context.getSystemService(LocaleManager::class.java) }
    var selectedTag by remember { mutableStateOf(currentLanguageTag(localeManager)) }
    val systemDefaultLabel = stringResource(id = R.string.language_system_default)

    val entries = remember(systemDefaultLabel) {
        val list = mutableListOf<ListPreferenceEntry<String>>()
        list.add(ListPreferenceEntry(value = SYSTEM_DEFAULT_TAG, label = { systemDefaultLabel }))
        for ((tag, name) in nkyaLanguages) {
            list.add(ListPreferenceEntry(value = tag, label = { name }))
        }
        list.toList()
    }

    ListPreference(
        entries = entries,
        value = selectedTag,
        onValueChange = { tag ->
            selectedTag = tag
            val locales = if (tag == SYSTEM_DEFAULT_TAG) {
                LocaleList.getEmptyLocaleList()
            } else {
                LocaleList.forLanguageTags(tag)
            }
            localeManager.setApplicationLocales(locales)
        },
        label = stringResource(id = R.string.language_label),
        modifier = modifier,
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun currentLanguageTag(localeManager: LocaleManager): String {
    val locales = localeManager.getApplicationLocales()
    return if (locales.isEmpty()) {
        SYSTEM_DEFAULT_TAG
    } else {
        locales.get(0).language.ifEmpty { SYSTEM_DEFAULT_TAG }
    }
}
