package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.material.AppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

sealed interface ScaffoldPartData
sealed interface ScaffoldPartChange

// region TopBar
data class TopBarData(
    val screen: Screen,
    val title: StrOrRes?,
    val modifier: Modifier = Modifier,
    val textAlign: TextAlign = TextAlign.Start,
    val upButton: UpButton? = null,
    val menuItems: List<MenuItem>? = null,
    val elevation: Dp = AppBarDefaults.TopAppBarElevation
) : ScaffoldPartData

data class TopBarChange(
    val screen: Screen? = null,
    val title: Optional<StrOrRes>? = null,
    val modifier: Modifier? = null,
    val textAlign: TextAlign? = null,
    val upButton:  Optional<UpButton>? = null,
    val menuItems:  Optional<List<MenuItem>>? = null,
    val elevation: Dp? = null
) : ScaffoldPartChange {
    companion object {
        fun default(screen: Screen? = null, title: StrOrRes? = null): TopBarChange = TopBarChange(
            screen = screen,
            title = Optional.ofNullable(title),
            modifier = Modifier,
            textAlign = TextAlign.Start,
            upButton = null,
            menuItems = null,
            elevation = AppBarDefaults.TopAppBarElevation
        )
    }

    constructor(screen: Screen, title: StrOrRes? = null) : this(screen, title?.let { Optional.of(it) })

    constructor(screen: Screen, title: StrOrRes? = null, upButton: UpButton) :
            this(screen, title?.let { Optional.of(it) }, upButton = Optional.ofNullable(upButton))

    constructor(screen: Screen, title: StrOrRes? = null, menuItems: List<MenuItem>) :
            this(screen, title?.let { Optional.of(it) }, menuItems = Optional.of(menuItems))

    constructor(screen: Screen, title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) :
            this(screen, title?.let { Optional.of(it) }, upButton = Optional.ofNullable(upButton), menuItems = Optional.ofNullable(menuItems))
}

fun TopBarChange.toData(): TopBarData = TopBarData(
    screen = screen!!,
    title = title?.getOrNull(),
    modifier = modifier ?: Modifier,
    textAlign = textAlign ?: TextAlign.Start,
    upButton = upButton?.getOrNull(),
    menuItems = menuItems?.getOrNull(),
    elevation = elevation ?: AppBarDefaults.TopAppBarElevation,
)

/**
 * For non-nullable values, null change means no change, change value means replace.
 * For nullable values, change null Optional means no change, empty Optional means remove, Optional value means replace.
 */
fun TopBarData.updateWith(change: TopBarChange): TopBarData = copy(
    screen = change.screen ?: screen,
    title = title.updateWithOptional(change.title),
    modifier = change.modifier ?: modifier,
    textAlign = change.textAlign ?: textAlign,
    upButton = upButton.updateWithOptional(change.upButton),
    menuItems = menuItems.updateWithOptional(change.menuItems),
    elevation = change.elevation ?: elevation,
)
// endregion TopBar

// region Drawer
data class DrawerData(val items: List<DrawerItem>) : ScaffoldPartData
/**
 * For non-nullable values, null means no change, value means replace.
 * For nullable values, null Optional means no change. Empty optional means remove. Optional with value means replace.
 */
data class DrawerChange(val items: List<DrawerItem>?) : ScaffoldPartChange

fun DrawerChange.toData(): DrawerData = DrawerData(items = items!!,)

fun DrawerData.updateWith(change: DrawerChange): DrawerData = copy(
    items = change.items ?: items
)
// endregion Drawer

// region ScaffoldChange
data class ScaffoldChange(
    val screenChange: Screen,
    val topBarChange: Optional<TopBarChange>? = null,
    val drawerChange: Optional<DrawerChange>? = null,
) {
    constructor(screenChange: Screen, drawerChange: DrawerChange) :
            this(screenChange = screenChange, drawerChange = Optional.of(drawerChange))
    constructor(screenChange: Screen, topBarChange: TopBarChange, drawerChange: DrawerChange? = null) :
            this(screenChange = screenChange, topBarChange = Optional.of(topBarChange), drawerChange = Optional.ofNullable(drawerChange))
}
// endregion ScaffoldChange

// region mapping utils
private inline fun <reified T, reified R> T.updateWith(other: R?): T = when (other) {
    null -> this
    is T -> other
    is Optional<*> -> other.getOrNull() as T
    else -> throw IllegalArgumentException("Failed to update $this with $other - types don't match!")
}

private inline fun <reified T> T?.updateWithOptional(other: Optional<T>?): T? =
    if (other == null) this
    else (other as Optional<*>).getOrNull() as T?

inline fun <reified C : ScaffoldPartChange, reified D : ScaffoldPartData> Optional<C>?.toData(current: D?): D? =
    if (this == null) current
    else {
        val change = (this as Optional<*>).getOrNull() as C?
        if (change == null) null
        else if (current != null) updateDataWithChange(current, change)
        else changeToData(change)
    }

inline fun <reified D : ScaffoldPartData> updateDataWithChange(data: D, change: ScaffoldPartChange): D =
    when (change) {
        is TopBarChange -> (data as TopBarData).updateWith(change) as D
        is DrawerChange -> (data as DrawerData).updateWith(change) as D
    }

inline fun <reified D : ScaffoldPartData> changeToData(change: ScaffoldPartChange): D =
    when (change) {
        is TopBarChange -> change.toData() as D
        is DrawerChange -> change.toData() as D
    }
// endregion mapping utils

inline fun <T: Any, R: Any> Optional<T>?.convertTo(converter: (T) -> R): Optional<R> =
    Optional.ofNullable(this?.getOrNull()?.let { converter(it) })
