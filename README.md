# Hilt Compose Sample App

## _Sample project, sampling Hilt injection with Compose views and Hilt-Compose navigation_

## Project Structure

Project is modularised. The project structure and modules are described within the `sealed interface Project` in `buildSrc` module.

- `app` module has dependency to all other modules. It defines the `App` Application class and is responsible for dependency injection for "cross-module" classes: if an interface has an implementation in another module, the module to provide/bind it can be specified in the `app` module, since the modules have not "visibility" (access) to each other.
- `shared` module - all modules can "access" it. Contains shared and common code, utilities, base classes and shared resources.
- "feature" modules - all other modules are feature modules and each feature should be contained within its own module and have "visibility" only to that module and `shared` modules. Data and Presentation can "see" Domain module of the same feature.

### Layers

Most feature modules would be split into "layers": "_data_", "_domain_" and "_presentation_" (and the`shared` module)

- "_data_" layer is an Android module, and is responsible for everything "data" related (fetching data with api calls, storing and processing data)
- "_domain_" layer is a plain Kotlin module. It contains business model representations and logic.
- "_presentation_" layer as an Android module and it is responsible for presenting the UI and handling related interactions. It is organised in an MVVM presentational pattern, using Android ViewModel-s; and Jetpack Compose for showing views.

## Branches

There are three branches:

- `fragments` branch, with fragments holding compose views, with fragment per screen, and custom navigation via view models using BriefAction pattern.
  
- `compose` branch, several commits on top of the `fragments` branch, refactoring to remove fragments, and use compose screens only, with compose navigation. There are benefits to this, such as being able to apply animations, but a lot of the Compose features don't seem fully mature yet.

- `tabs` branch, several commits on top of the `compose` branch, adds full scaffold structure (bottom tabs - adding another tab, drawer and menu).

## BriefAction

- `LiveAction` is custom `LiveData` which can "consume" value only once. Used for actions of non-persistent nature, such as:
    - `NavigationAction` - navigating to another screen or tab.
    - `DisplayAction` - like showing of a non persistent message.
- `BriefActioner` - holds `MutableLiveAction<BriefAction>` and a function for "sending" actions, and can be included in a `ViewModel` a pre-setup structure.
- `BriefActionable` is an interface holding a `BriefActioner` - can be used to decorate a `ViewModel` (if preferring composition over inheritance).
- `BriefActionViewModel` implements `BriefActionable` - a base `ViewModel` already including an implementation with `BriefActioner` instance.
- ViewModels (holding an instance of `BriefActioner`, by being decorated with `BriefActionable` or inheriting from `BriefActionViewModel`), are enabled to "send" `BriefAction`s.
- Setup of observing `BriefAction`s is done in `shared_presentation.util.BriefActionHandlingSetup.kt` (by use of `fun BriefActioner.setupWith(navController: NavHostController)`) for specific screens/tabs, and further in `CommonDisplayHandler`/`TopNavHandler` for common display actions and top (common) navigation actions.
    - Thus some of the `Screen`s and common `Display` actions need to be defined inside `shared_presentation` module - in `navigation` and `display` packages respectively.
        - This is to be able to navigate "cross-module" for modules which don't "see" each other, and send common actions from all feature modules.

### Navigation

- Navigation structure is defined in `shared_presentation.navigation`.
- `NavigationAction` is an `interface` for scoping navigation actions.
- `ToScreen` is a `NavigationAction` representing navigation to a screen.
- Compose navigation is setup in `shared_presentation.util.BriefActionHandler.kt` file with `fun BriefActioner.setupWith(navController: NavHostController)`, from which handling of `BriefAction`s starting, branching into handling of `NavigationAction`s.
- `NavigationAction`s are sent from `ViewModel`s, for which observing of the actions is auto set-up, as handling is in one place.
- In the case of fragments use on `fragments` branch, navigating is done using `supportFragmentManager` or `childFragmentManager`, using `Navigation` interface. 
- In the case of `compose branch` handling of `NavigationAction` to navigate from compose screen to compose screen is done simply by passing the String name of the screen to `NavHostController.navigate` function.

## `compose` branch

### Composition

- `ScreenComposer` represents all the UI parts for one screen with `@Composable fun Content` function for each overriding composer to override as a way to define how the screen needs to look like. The function passed the the `NavHostController`, `ScaffoldState`, so separate "screens" can handle what it needs to separately.
- `ScreenHolderComposer` represents either a "holder" of multiple screens. For multi-tab activity it could be a tab, or a logical representation of screen grouping, like a feature.
- `ViewModel`s are scoped with compose recomposition, using `Hilt` injection to inject them directly into `Composable` functions. 

Main Compose structure is defined in the base `ScreenHolderComposer` and `ScreenHolderComposer`, which define base Compose functions (open for override if need to customise):

- `MainContent()`
    - which creates, and passes down remembered `NavHostController`,
    - calls standard `Scaffold` function passing to it the `scaffoldState`, and further defined different `Scaffold` "content" functions and `TopBar` (no op, and open to custom override):
        - `TopBar` - calling to default implementation for `TopAppBar`, with customisable options for title and up button.
        - `NavGraphBuilder.navGraph` function (passing down `NavHostController`, `ScaffoldState`, (main) `CoroutineScope`, and `NavBackStackEntry` all the way to the `Content` function of each `ScreenComposer`) - in `BaseTabNavActivity` calls `composeNavGraph` fun for each tab `ScreenHolderComposer`
        - Easily, further to this `Drawer` and `BottomBar` `Composable` functions to, with default implementation, with potential to override, could be set-up.
    
#### Compose Navigation
- Compose navigation is done in `NavigationComponent` style, using `NavHostControler.navigate(route: String)`, passing a `Screen.name` as the `route`, and it is setup via the Compose `NavHost` function, passing the "remembered" `NavHostControler`, and for building the nav graph calls the `NavGraphBuilder.navGraph` function, passing down the `NavHostController`.
- The navigation for each screen is setup using the `composable` Compose navBuilder extension function - in `ScreenHolderComposer`, overriding `NavGraphBuilder.composeNavGraph` function.
  Thinking about how this would look like if it was all written in one place flattened would be:

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
                    startDestination = holder1.initialScreen.name,
                    route = holder1.name
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
                    startDestination = holder3.initialScreen.name,
                    route = holder2.name
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
    
    @Composable
    fun BackHandler(navController: NavHostController, backStackEntry: NavBackStackEntry) {
        // Setup closing app on back press from any tab first screen.
        navController.enableOnBackPressed(false)
        BackHandler {
            // When at first screen, it's 2 (main, first screen),
            // When at first tab, it's 3 (main, first tab, first tab first screen),
            // and at any other next tab it's 5 (+tab, +tab first screen)
            // => < 6 prevents closing the app if tab first screen repeats in stack.
            if (backStackEntry.isFirstScreen && navController.backQueue.size < 6) {
                 (navController.context as Activity).finish()
            } else {
                navController.popBackStack()
            }
        }
    }
```

## `tabs` branch

Continues on top of the `compose` branch, adding another tab and full scaffold structure - bottom tabs, drawer and menu.

## Hilt Injection
- Application (`App`) class is annotated with `@HiltAndroidApp` which enables injection into Application class and initiates Hilt injection (in Application onCreate so dependencies are available after that point).
    - `AppModule` (in `app` module) defines Application (singleton) scoped dependencies.
- Activities are annotated with `@AndroidEntryPoint` annotation, which enables injection into activities; into which `ScreenHolderComposer`s are injected.
- ViewModels are annotated with `@HiltViewModel` annotation (except for `@AssistInject` ViewModels as Hilt doesn't support assisted injection), and injected directly in Compose functions (withing a `ScreenComposer`) using custom function defined on top of `hiltViewModel` Hilt function, which scopes ViewModel dependencies to `@ViewModelScope`.
    - For assisted injection of the `BriefActoin``ViewModel`s, use the `actionViewModels()` function util function, or `actionViewModel()` for Compose variant.
    - `ViewModelModule` (in `app` module) defines ViewModel scoped dependencies.
- Helper functions for injecting ViewModels are defined in `shared_presentation.di.ViewModelProviderHelper.kt` file.

### Custom EntryPoint-s
- Custom `EntryPoint`s are defined in `di.EntryPoints.kt` file.
- `EntryPointAccessor` is a helper class for getting dependencies from application defined with a custom `EntryPoint`.
- If a custom EntryPoint is defined in a plain Kotlin module (domain - which has no access to `@EntryPoint @InstallIn(SingletonComponent::class)`), is is defined in plain Kotlin module as a simple interface, which is then overridden in the `app` module, then annotated with `@EntryPoint @InstallIn(SingletonComponent::class)`.
- Custom `EntryPoint`s enable:
    - property injection into "non-Android" classes (which cannot be annotated with `@AndroidEntryPoint"`)
    - property injection into objects (see `Lumber` and `LoggerEntryPoint` for example).


Copyright (C) 2022 Vladimir Markovic - All Rights Reserved  