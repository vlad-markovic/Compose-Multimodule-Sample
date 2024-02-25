/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

configurePresentationModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.covid_presentation"
}

androidLibrary {
    dependencies {
        implementationProject(Project.Feature.Covid.domain)
    }
}
