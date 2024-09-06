/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

/**
 * This class documents the modular project structure and is used to import modules as projects.
 *
 * Each module is of one of the module type: [Core], [Common], [Shared], or [Feature].
 *
 * These are organised in an "onion" like structure, where
 * - [Core] is in the centre, than
 * - [Common] is in the next layer, than
 * - [Shared] is in the next layer, and
 * - [Feature]s are in the outer layer
 *
 * A dependency arrow goes inward, which means that inner layers don't know about the outer layers.
 * Inner layer modules can only depend on the modules of the more inner layers.
 * Or in some cases they could depend on the modules within the same layer:
 *  - if an implementation module depended on the abstraction module (public api part), when they for a group
 *  - if a functionality depends on a subset functionality, when they form a further hierarchy granularity within a layer.
 *  For instance mvi module depending on the action module.
 *
 * So, a [Common] module could depend on a [Core] module and on another [Common], but could not
 * include any of the [Shared] modules.
 *
 * - [Core] modules should be thought of high level helper, which could be included in any project.
 * For pure Kotlin [Core] module, it should be useful for any Kotlin project.
 * This are things such as String or Coroutines extensions or for Android projects Context or View extensions, and similar.
 * [Core] could be extracted and managed as a separate repository, and reused between projects and [Common] modules.
 *
 * - [Common] modules should be thought of high level helper, on a specific tooling. Reusable in any project in which
 * such tool (library) should be used. And also which could find use of [Core] module helpers. For instance Compose extension,
 * Navigation Component extensions, and similar.
 * [Common] could be extracted and managed as a separate repository, and reused between projects.
 *
 * - [Shared] modules are code extracted to be shared by other modules of this project,
 * but not extractable to be reused by any other project. They can use [Core] and [Common] modules.
 *
 * - [Feature] modules hold distinctly separated features of this project, and can use (include)
 * any of the [Core], [Common] or [Shared] module. Many features are [Layered].
 * Kotlin only [Domain] feature modules can only include [Shared] [Domain] module.
 * All [Feature]s are [NameOrganised] and need to be placed into top directory "features".
 *
 * ### Organisational structure and decorator interfaces:
 *
 * Each module is named having to specify [shortName] and [fullName], which could include dir path.
 *
 * Organising decorator interfaces are used to help easier definition of modules when they are placed within
 * some directory structure, so that it can be descriptively defined within this class the structure for each module,
 * and that the directories don't need to be repeated for the modules in the same dir.
 *
 * - [Organised]- each module is [Organised], which specifies [Organised.dirs] (empty for [TopNameOrganised])
 * from which to form [Organised.dirPath], and [Organised.module] which is a full definition for the module,
 * including the dir [Organised.dirPath] and the [fullName] which modules specify by overwriting.
 *
 * - [NameOrganised] - module could also be [NameOrganised], which means that the module uses the name
 * of the immediate dir it is in for the module [shortName].
 * Thus for [NameOrganised], [Organised.dirs] don't contain the last dir named [shortName].
 *
 * - [TopNameOrganised] - [NameOrganised] at top level: within no dirs.
 *
 * ### Layering decorator interfaces:
 *
 * Layering decorator interfaces are used to easily and uniformly define modules of the same layer.
 * This means that all layer modules are named in the same name - suffixed with module layer.
 * It also enables for modules which define all three layer modules (data, domain and presentation)
 * to be included with one inclusion line at once.
 *
 * Modules can be decorated as [Data], [Domain], [Presentation] or [Layered] (all three).
 *
 * Use [implementationProject] for [Layered] and single module projects to implement
 *  by specifying the [Project], and not implementation(project(String)) using String. I.e.:
 *   - implementationProject(Project.Feature.Main) // not [Layered]
 *   - implementationProject(Project.Feature.Home) // is [Layered]
 *  instead of:
 *   - implementationProject(Project.Feature.Main.presentation) // not [Layered]
 *   - implementationProject(Project.Feature.Home.data)
 *   - implementationProject(Project.Feature.Home.domain)
 *   - implementationProject(Project.Feature.Home.presentation)
 *
 * If you need just to implement just one of the modules from the [Project.Layered], than use i.e.:
 *   implementationProject(Project.Feature.Home.domain)
 *
 * Examples:
 *
 *  [TopNameOrganised]:
 *  - :share:sharedPresentation ([Presentation] [NameOrganised] [shortName]=base)
 *
 *  [Organised] (into some dir) not [Layered]
 *  - :common:util ([Organised] [Organised.dirs]=listOf(common) and [shortName]=util)
 *  - :common:utilPresentation ([Presentation] [Organised.dirs]=listOf(common) and [shortName]=util)
 *
 *  [Organised] [Layered]
 *  - :common:shared:sharedData ([Layered] [Data] [NameOrganised] [Organised.dirs]=listOf(common) [Data] [shortName]=shared)
 *  - :common:shared:sharedDomain ([Layered] [Domain] [NameOrganised] [Organised.dirs]=listOf(common) [Domain] [shortName]=shared)
 *  - :common:shared:sharedPresentation ([Layered] [Presentation] [NameOrganised] [Organised.dirs]=listOf(common) [Presentation] [shortName]=shared)
 *
 *  [Feature] not [Layered] (each feature is [NameOrganised] and [Organised.dirs]=listOf(features)):
 *  - :features:f1:f1Presentation ([Feature] [Presentation] [Organised.dirs]=listOf(features) [shortName]=f1) (not layered)
 *
 *  [Feature] [Layered] (each feature is [NameOrganised] and [Organised.dirs]=listOf(features):
 *  - :features:f2:f2Data ([Layered] [Feature] [Data] [NameOrganised] [Organised.dirs]=listOf(features) [shortName]=f2)
 *  - :features:f2:f2Domain ([Layered] [Feature] [Domain] [NameOrganised] [Organised.dirs]=listOf(features) [shortName]=f2)
 *  - :features:f2:f2Presentation ([Layered] [Feature] [Presentation] [NameOrganised] [Organised.dirs]=listOf(features) [shortName]=f2)
 */
sealed interface Project {

    val shortName: String
    /** To be able to distinguish from [shortName] by any suffixes */
    val fullName: String get() = shortName
    val module: String

    val modules: List<String> get() = when (this) {
        is Layered -> listOf(data.module, domain.module, presentation.module)
        is NameOrganised -> listOf(module)
        is Organised -> listOf(module)
    }

    // region specific project modules
    object Main : TopNameOrganised("main"), Presentation

    sealed class Feature(override val shortName: String) : NameOrganised {
        override val dirs: List<String> = listOf("features")

        object Settings : Feature("settings"), Presentation
        object Post : Feature("post"), Layered
        object Covid : Feature("covid"), Layered
    }
    // endregion specific project modules

    // region shared project modules
    /** Included into all other projects (modules), per required layer. */
    sealed class Shared : TopNameOrganised("shared") {

        object Layers : Shared(), Layered

        /** Included into all other data and presentation projects (with testImplementation). */
        object Test : Shared() {
            override val fullName: String = "${shortName}Test"
        }

        object AndroidTest : Shared() {
            override val fullName: String = "${shortName}AndroidTest"
        }
    }
    // region shared project modules

    // region common modules
    sealed class Common(override val shortName: String) : Organised {
        override val dirs: List<String> = listOf("common")

        sealed class Di(override val shortName: String) : Common(shortName) {
            override val dirs: List<String> = super.dirs + "di"

            object Model : Di("model")
            object ViewModel : Di("viewmodel")
            object Compose : Di("compose")
        }

        object Logging : Common("logging"), NameOrganised {
            object Android : Common("androidLogging") {
                override val dirs: List<String> = super.dirs + "logging"
            }
        }

        object Android : Common("android")

        object Compose : Common("compose")

        sealed class Mv(override val shortName: String) : Common(shortName) {
            override val dirs: List<String> = super.dirs + "mv"

            object Action : Mv("action")

            object ActionCompose : Mv("actionCompose")

            object State : Mv("state")

            object Mvi : Mv("mvi")
        }

        sealed class Navigation(override val shortName: String) : Common(shortName) {
            override val dirs: List<String> = super.dirs + "navigation"

            sealed class Screen(override val shortName: String) : Navigation(shortName) {
                override val dirs: List<String> = super.dirs + "screen"

                object Model : Screen("model")

                object NavComponent : Screen("navcomponent")

                object Compose : Screen("compose")
            }

            sealed class Tab(override val shortName: String) : Navigation(shortName) {
                override val dirs: List<String> = super.dirs + "tab"

                object Model : Tab("model")

                object NavComponent : Tab("navcomponent")

                object Compose : Tab("compose")
            }
        }
    }
    // end region common modules

    // region core modules
    sealed class Core(override val shortName: String) : Organised {
        override val dirs: List<String> = listOf("core")

        object Kotlin : Core("kotlin")

        object Coroutines : Core("coroutines")

        object Android : Core("android")
    }
    // end region core modules

    // region layer decorator interfaces
    sealed interface Layered : NameOrganised {
        val data: Data get() = DataLayer(shortName, dirPath, dirs)
        val domain: Domain get() = DomainLayer(shortName, dirPath, dirs)
        val presentation: Presentation get() = PresentationLayer(shortName, dirPath, dirs)
    }

    sealed interface Data : Organised {
        override val fullName: String get() = "${shortName}Data"
    }

    sealed interface Domain : Organised {
        override val fullName: String get() = "${shortName}Domain"
    }

    sealed interface Presentation : Organised {
        override val fullName: String get() = "${shortName}Presentation"
    }

    private data class DataLayer(override val shortName: String, override val dirPath: String, override val dirs: List<String>) : Data
    private data class DomainLayer(override val shortName: String, override val dirPath: String, override val dirs: List<String>) : Domain
    private data class PresentationLayer(override val shortName: String, override val dirPath: String, override val dirs: List<String>) : Presentation
    // endregion layer decorator interfaces

    // region project organising decorator interfaces
    /**
     * Inside one top level dir and prefixed with that dir name,
     * such as base or shared (base:baseData, shared:sharedDomain, etc.)
     */
    sealed class TopNameOrganised(override val shortName: String): NameOrganised {
        override val dirs: List<String> get() = emptyList()
    }

    // region project organising decorator interfaces
    /**
     * Defines last dir placed inside named the same as the module [shortName].
     * Purpose is for auto-adding, to remove boilerplate; instead just decorate with this.
     * For [NameOrganised] [Organised.dirs] don't contain the last dir named [shortName].
     */
    interface NameOrganised : Organised {
        override val dirPath: String get() = "${super.dirPath}:$shortName"
    }

    /**
     * [dirs] - in which module is placed in.
     * [dirPath] - used to "auto" prefix module with [dirPath] such as ":dir1:dir2"
     */
    interface Organised : Project {
        val dirs: List<String>
        val dirPath: String get() = if (dirs.isEmpty()) "" else ":${dirs.joinToString(":")}"
        override val module: String get() = "$dirPath:$fullName"
    }
    // endregion project organising decorator interfaces
}
