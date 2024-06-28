/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

/**
 * This class also represents a document for the project structure.
 *
 * Each is [Named] having to specify [Named.shortName] and [Named.fullName] is depending on type.
 * Each has to be either [Top], [Data], [Domain], [Presentation] or [Layered] (all three).
 * If [Layered] then it is comprised of three modules [Data], [Domain] and [Presentation].
 * [Organised] is into dirs specified by [Organised.dirs], which form the [Organised.path].
 * If [NameOrganised] it means it is placed in a directory named the same as [Named.shortName]
 *  (mainly used for layered - to contain three data, domain and presentation),
 *  but that directory can also sit within other directories, if specified with [Organised.dirs].
 *  For [NameOrganised] [Organised.dirs] don't contain the last dir named [Named.shortName].
 * All [Feature]s are [NameOrganised] and need to be placed into directory "features".
 *
 * Use [implementationProject] for [Layered] and single module projects to implement
 *  by specifying the [Project], and not implementation(project(String)) using String. I.e.:
 *   implementationProject(Project.Feature.Main) // not [Layered]
 *   implementationProject(Project.Feature.Home) // is [Layered]
 *  instead of:
 *   implementationProject(Project.Feature.Main.presentation) // not [Layered]
 *   implementationProject(Project.Feature.Home.data)
 *   implementationProject(Project.Feature.Home.domain)
 *   implementationProject(Project.Feature.Home.presentation)
 *
 * If you need just to implement just one of the modules from the [Project.Layered], than use i.e.:
 *   implementationProject(Project.Feature.Home.domain)
 *
 * Examples:
 *
 *  [Top] level:
 *  - :app (top level: [Top] and [Named.shortName]=app)
 *
 *  [TopNameOrganised]:
 *  - :base:basePresentation ([Presentation] [NameOrganised] [Named.shortName]=base)
 *
 *  [Organised] (into some dir) not [Layered]
 *  - :common:util ([Organised] [Organised.dirs]=listOf(common) and [Named.shortName]=util)
 *  - :common:utilPresentation ([Presentation] [Organised.dirs]=listOf(common) and [Named.shortName]=util)
 *
 *  [Organised] [Layered]
 *  - :common:shared:sharedData ([Layered] [Data] [NameOrganised] [Organised.dirs]=listOf(common) [Data] [Named.shortName]=shared)
 *  - :common:shared:sharedDomain ([Layered] [Domain] [NameOrganised] [Organised.dirs]=listOf(common) [Domain] [Named.shortName]=shared)
 *  - :common:shared:sharedPresentation ([Layered] [Presentation] [NameOrganised] [Organised.dirs]=listOf(common) [Presentation] [Named.shortName]=shared)
 *
 *  [Feature] not [Layered] (each feature is [NameOrganised] and [Organised.dirs]=listOf(features)):
 *  - :features:f1:f1Presentation ([Feature] [Presentation] [Organised.dirs]=listOf(features) [Named.shortName]=f1) (not layered)
 *
 *  [Feature] [Layered] (each feature is [NameOrganised] and [Organised.dirs]=listOf(features):
 *  - :features:f2:f2Data ([Layered] [Feature] [Data] [NameOrganised] [Organised.dirs]=listOf(features) [Named.shortName]=f2)
 *  - :features:f2:f2Domain ([Layered] [Feature] [Domain] [NameOrganised] [Organised.dirs]=listOf(features) [Named.shortName]=f2)
 *  - :features:f2:f2Presentation ([Layered] [Feature] [Presentation] [NameOrganised] [Organised.dirs]=listOf(features) [Named.shortName]=f2)
 */
sealed interface Project: Named {

    val modules: List<String> get() = when (this) {
        is Layered -> listOf(data.module, domain.module, presentation.module)
        is NameOrganised -> listOf(module)
        is Organised -> listOf(module)
        is Top -> listOf(module)
    }

    // region specific project modules

    /** Included into all other projects (modules), per required layer. */
    object Shared : TopNameOrganised("shared"), Layered
    /** Included into all other data and presentation projects (with testImplementation). */
    object SharedTest : TopNameOrganised("shared") {
        override val fullName: String = "${shortName}Test"
    }
    object SharedAndroidTest : TopNameOrganised("shared") {
        override val fullName: String = "${shortName}AndroidTest"
    }

    object Main : TopNameOrganised("main"), Presentation

    sealed class Feature(override val shortName: String) : NameOrganised {
        override val dirs: List<String> = listOf("features")

        object Settings : Feature("settings"), Presentation

        object Post : Feature("post"), Layered
        object Covid : Feature("covid"), Layered
    }
    // endregion specific project modules

    // region project kinds decorator interfaces
    sealed interface Layered : NameOrganised {
        val data: Data get() = DataLayer(shortName, path, dirs)
        val domain: Domain get() = DomainLayer(shortName, path, dirs)
        val presentation: Presentation get() = PresentationLayer(shortName, path, dirs)
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

    private data class DataLayer(override val shortName: String, override val path: String, override val dirs: List<String>) : Data
    private data class DomainLayer(override val shortName: String, override val path: String, override val dirs: List<String>) : Domain
    private data class PresentationLayer(override val shortName: String, override val path: String, override val dirs: List<String>) : Presentation
    // endregion project kinds decorator interfaces

    // region project top level subtypes
    /** Top level, such as :app */
    sealed class Top(override val shortName: String) : Project {
        val module: String get() = ":$fullName"
    }

    /**
     * Inside one top level dir and prefixed with that dir name,
     * such as base or shared (base:baseData, shared:sharedDomain, etc.)
     */
    sealed class TopNameOrganised(override val shortName: String): NameOrganised {
        override val dirs: List<String> get() = listOf()
    }
    // endregion project top level subtypes

    // region project organising decorator interfaces
    /**
     * Defines last dir placed inside named the same as the module [shortName].
     * Purpose is for auto-adding, to remove boilerplate; instead just decorate with this.
     * For [NameOrganised] [Organised.dirs] don't contain the last dir named [Named.shortName].
     */
    interface NameOrganised : Organised {
        override val path: String get() = "${super.path}:$shortName"
    }

    /**
     * [dirs] - in which module is placed in.
     * [path] - used to "auto" prefix module with [path] such as ":dir1:dir2"
     */
    interface Organised : Project {
        val dirs: List<String>
        val path: String get() = if (dirs.isEmpty()) "" else ":${dirs.joinToString(":")}"
        val module: String get() = "$path:$fullName"
    }
    // endregion project organising decorator interfaces
}

interface Named {
    val shortName: String
    val fullName: String get() = shortName
}
