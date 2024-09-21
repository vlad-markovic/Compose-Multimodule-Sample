# Hilt Compose Sample App

## _Sample project, sampling Modular full Compose app with Hilt injection and Hilt-Compose navigation_

## Project Structure

Project is modularised. 

For details about the project modularisation structure, see [Project](https://github.com/vlad-markovic/Compose-Multimodule-Sample/blob/main/buildSrc/src/main/kotlin/Project.kt) in `buildSrc`, which describes and documents the project structure.

Modules are organised by "type", which are:

- `app` glue module, having access (dependency) to all other modules. 
   It defines the `App` Application class and is responsible for dependency injection for "cross-module" classes: 
   if an interface has an implementation in another module, the module to provide/bind it can be specified in the `app` module, 
   since the modules have no "visibility" (access) to each other.
- `core` - core helpers / extensions (extractable - could be shared with other projects)
- `common` - common functionality (extractable - could be shared with other projects)
- `shared` - shared between feature module of this project (not extractable). Separate shared modules for domain, data and presentation.
- `feature` modules - self contained and layered to domain, data and presentation layers

![modules onion structure](https://github.com/vlad-markovic/Compose-Multimodule-Sample/blob/main/assets/modules_onion.png)

### Layers

Most feature modules would be split into "layers": "_domain_" , "_data_" and "_presentation_" (and the`shared` module)

- "_domain_" layer is a plain Kotlin module. It contains business model representations and logic.
- "_data_" layer is an Android module, and is responsible for everything "data" related (fetching data with api calls, storing and processing data)
- "_presentation_" layer as an Android module and it is responsible for presenting the UI and handling related interactions. It is organised in an MVVM/MVI presentational patterns (sampling both),
using Android ViewModel-s; and Jetpack Compose for showing views.

## Action

- `Action` "consumed" by the view only once. Used for actions of non-persistent nature, such as:
    - `NavigationAction` - navigating to another screen or tab.
    - `DisplayAction` - like showing of a non persistent message.
- `Actioner` - holds `MutableSharedFlow<Action>` and a function for "sending" actions, and can be included in a `ViewModel` a pre-setup structure.
- `Actionable` is an interface holding a `Actioner` - can be used to decorate a `ViewModel` (if preferring composition over inheritance).
- `ActionViewModel` implements `Actionable` - a base `ViewModel` already including an implementation with `Actioner` instance.
- ViewModels (holding an instance of `Actioner`, by being decorated with `Actionable` or inheriting from `ActionViewModel`), are enabled to "send" `Action`s.
- Setup of observing `Action`s is done in `common.mv.action.compose.ActionsHandler` 
- `common.navigation.screen.compose.action.ComposeActionHandling.kt` (by use of Composable `fun ActionMonitorable<Action>.SetupWith(actionHandler: (Action) -> Unit)`) for specific screens/tabs, and further in `TopNavHandler` for common display actions and top navigation actions.
    - Thus `Screen`s and common `Display` actions need to be defined inside shared (domain and presentation) modules.
        - This is to be able to navigate "cross-module" for modules which don't "see" each other, and send common actions from all feature modules.

### Navigation

- Navigation structure is defined in `common.navigation` `screen` and `tabs` modules respectivelly.
- `NavigationAction` is an `interface` for scoping navigation actions.
- `ToNavGraphScreen` is a `NavigationAction` representing navigation to a screen using Compose Navigation Component.
- Compose navigation is setup in compose navigation module/s, in `ComposeActionHandling.kt`.
- `NavigationAction`s are sent from `ViewModel`s, for which observing of the actions is auto set-up, as handling definition is done in one place.
- ~In the case of fragments use on `fragments` branch, navigating is done using `supportFragmentManager` or `childFragmentManager`, using `Navigation` interface.~ N/A

### Composition

- `common.navigation.screen.compose.content.ComposeScreenContentResolver` is used to provide a content for each screen, to be able to use one top Scaffold with mutual bottom bar.
- it enables definition of each screen to be contained withits own feature module. 

Compose structure is defined in the base `ScreensNavScaffold` and `TabsNavScaffold`

The project tries to use composition-over-inheritance to provde functionality.
    
#### Compose Navigation

- Compose navigation is done in `NavigationComponent` style, using `NavHostControler.navigate(route: String)`, passing a `Screen.route`, and it is setup via the Compose `NavHost` function, passing the "remembered" `NavHostControler`, and for building the nav graph calls the `NavGraphBuilder.navGraph` function, passing down the `NavHostController`.
- The navigation for each screen is setup using the `composable` Compose navBuilder extension function `NavGraphBuilder.composeNavGraph`.
  Thinking about how this would look like if it was all written in one place flattened out, it would be:

```kotlin
    @Composable
    fun MainContent() {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = { Drawer(scaffoldState) },
            topBar = { TopBar(scaffoldState) },
            bottomBar = { BottomBar() }
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = initialGraphModifier,
                route = graphRoute
            ) {
                // setup tab navigation
                observeNonNull(tab.drop(1)) { tab ->
                    currentTab.value = tab
                    navigate(tab.name) {
                        // Separate stacks per tab.
                        popUpTo(graph.findStartDestination().id) {
                            // Save state to be able to restore when re-selecting..
                            saveState = true
                        }
                        // Avoid multiple instances when re-selecting the same tab.
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected tab.
                        restoreState = true
                    }
                }
                // tab one nav
                navigation(
                    startDestination = resolver1.initialScreen.name,
                    route = resolver1.name
                ) {
                    // screen1 nav
                    composable(screen1.name) { backStackEntry ->
                        BackHandler(navController, backStackEntry)
                        currentScreen.value = screen1
                        Screen1Content {
                            // Compose View
                        }
                    }
                    // screen2 nav
                    composable(screen2.name) { backStackEntry ->
                        BackHandler(navController, backStackEntry)
                        currentScreen.value = screen2
                        Screen2Content {
                            // Compose View
                        }
                    }
                    // ...
                }
                // tab two nav
                navigation(
                    startDestination = resolver2.initialScreen.name,
                    route = resolver2.name
                ) {
                    // screen3 nav
                    composable(screen3.name) { backStackEntry ->
                        BackHandler(navController, backStackEntry)
                        currentScreen.value = screen3
                        Screen3Content {
                            // Compose View
                        }
                    }
                    // screen4 nav
                    composable(screen4.name) { backStackEntry ->
                        BackHandler(navController, backStackEntry)
                        currentScreen.value = screen4
                        Screen4Content {
                            // Compose View
                        }
                    }
                    // ...
                }
                // ...
            }
        }
    }
```

### Custom EntryPoint-s
- Custom `EntryPoint`s are defined in `di.EntryPoints.kt` file.
- `EntryPointAccessor` is a helper class for getting dependencies from application defined with a custom `EntryPoint`.
- If a custom EntryPoint is defined in a plain Kotlin module (domain - which has no access to `@EntryPoint @InstallIn(SingletonComponent::class)`), is is defined in plain Kotlin module as a simple interface, which is then overridden in the `app` module, then annotated with `@EntryPoint @InstallIn(SingletonComponent::class)`.
- Custom `EntryPoint`s enable:
    - property injection into "non-Android" classes (which cannot be annotated with `@AndroidEntryPoint"`)
    - property injection into objects (see `Lumber` and `LoggerEntryPoint` for example).


Copyright (C) 2022 Vladimir Markovic - All Rights Reserved  
