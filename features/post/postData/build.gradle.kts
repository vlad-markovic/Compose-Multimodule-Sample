/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

configureDataModule()

androidLibrary {
    namespace = "com.vladmarkovic.sample.post_data"
}

dependencies {
    implementationProject(Project.Feature.Post.domain)
}
