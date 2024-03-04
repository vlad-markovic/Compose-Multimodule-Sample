package com.vladmarkovic.sample.shared_presentation.compose

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

sealed interface ScreenPartData
sealed interface ScreenPartChange

// region TopBar
data class TopBarData(
    val title: StrOrRes?,
    val upButton: UpButton? = null,
    val menuItems: List<MenuItem>? = null,
) : ScreenPartData

data class TopBarChange(
    val screen: Screen? = null,
    val title: Optional<StrOrRes>? = null,
    val upButton:  Optional<UpButton>? = null,
    val menuItems:  Optional<List<MenuItem>>? = null,
) : ScreenPartChange {
    companion object {
        fun default(screen: Screen? = null, title: StrOrRes? = null): TopBarChange = TopBarChange(
            screen = screen,
            title = Optional.ofNullable(title),
            upButton = null,
            menuItems = null,
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
    title = title?.getOrNull(),
    upButton = upButton?.getOrNull(),
    menuItems = menuItems?.getOrNull(),
)

/**
 * For non-nullable values, null change means no change, change value means replace.
 * For nullable values, change null Optional means no change, empty Optional means remove, Optional value means replace.
 */
fun TopBarData.updateWith(change: TopBarChange): TopBarData = copy(
    title = title.updateWithOptional(change.title),
    upButton = upButton.updateWithOptional(change.upButton),
    menuItems = menuItems.updateWithOptional(change.menuItems),
)
// endregion TopBar

// region Drawer
data class DrawerData(val items: List<DrawerItem>) : ScreenPartData
/**
 * For non-nullable values, null means no change, value means replace.
 * For nullable values, null Optional means no change. Empty optional means remove. Optional with value means replace.
 */
data class DrawerChange(val items: List<DrawerItem>?) : ScreenPartChange

fun DrawerChange.toData(): DrawerData = DrawerData(items = items!!,)

fun DrawerData.updateWith(change: DrawerChange): DrawerData = copy(
    items = change.items ?: items
)
// endregion Drawer

// region ScreenChange
data class ScreenChange(
    val screenChange: ScreenData,
    val topBarChange: Optional<TopBarChange>? = null,
    val drawerChange: Optional<DrawerChange>? = null,
) : BriefAction {
    constructor(screenChange: ScreenData, drawerChange: DrawerChange) :
            this(screenChange = screenChange, drawerChange = Optional.of(drawerChange))
    constructor(screenChange: ScreenData, topBarChange: TopBarChange, drawerChange: DrawerChange? = null) :
            this(screenChange = screenChange, topBarChange = Optional.of(topBarChange), drawerChange = Optional.ofNullable(drawerChange))
}

data class ScreenData(val screen: Screen, val holderType: ScreenHolderType)
// endregion ScreenChange

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

inline fun <reified C : ScreenPartChange, reified D : ScreenPartData> Optional<C>?.toData(current: D?): D? =
    if (this == null) current
    else {
        val change = (this as Optional<*>).getOrNull() as C?
        if (change == null) null
        else if (current != null) updateDataWithChange(current, change)
        else changeToData(change)
    }

inline fun <reified D : ScreenPartData> updateDataWithChange(data: D, change: ScreenPartChange): D =
    when (change) {
        is TopBarChange -> (data as TopBarData).updateWith(change) as D
        is DrawerChange -> (data as DrawerData).updateWith(change) as D
    }

inline fun <reified D : ScreenPartData> changeToData(change: ScreenPartChange): D =
    when (change) {
        is TopBarChange -> change.toData() as D
        is DrawerChange -> change.toData() as D
    }
// endregion mapping utils

inline fun <T: Any, R: Any> Optional<T>?.convertTo(converter: (T) -> R): Optional<R> =
    Optional.ofNullable(this?.getOrNull()?.let { converter(it) })
