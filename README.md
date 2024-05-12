This library is meant to enable a centralized multi-stage and multi-region configuration for configurational parameters, using wildcard entries.

It adds to the following use-cases:
1. Enable easy deployment and configuration of parametrs and any relevant dependency such as remote endpoints without the need to resolve these in code or via system parameters.
2. Avoidance of introducing environment variables at runtime, which are prone to manual interference.

Steps to onboard:

1. Add Dependency to classpath:
```
    implementation("org.agraman4u.appconfig:app-config-parser-jvm:1.0")
```

2. Create a `configuration/app-config` directory in your project root folder.
```
mkdir configuration/app-config
```
3. Decide a APPLICATION_NAME (`SampleApp` for the explanation
```
APPLICATION_NAME: "SampleApp"
```

4. **Create configuration files**. Create a file with the filename in the format: `<any-value>.APPLICATION_NAME.conf`. File contents need to be in the following schema:

```
<stage>.<region>.<identifier> += {
    "<key1>": "<value1>",
    "<key2>": "<value2",
}
```
 a) `stage`: The deployment stage. Insert `*` for wildcard
 b) `region`: The deployment region. Insert `*` for wildcard
 c) `identifier`: Custom identifier for the set of configurations. Wildcard noy supported

Example Config (also in `configuration/config.AppConfigTest.conf`:

```
*.*.databaseDriverConfig += {
    "name": "hello world",
    "password": "hello",
    "id": "40"
}

prod.*.databaseDriverConfig += {
    "name": "hello world prod",
    "password": "hello prod",
    "id": "41"
}

prod.WestUs2.databaseDriverConfig += {
    "name": "hello world prod west us",
    "password": "hello prod west us",
    "id": "42"
}
```

5. Add a copy task in gradle to move the configuration folder to project's build directory.
```
tasks.register<Copy>("copy-configuration") {
    println("Copying config files")
    from(layout.projectDirectory.dir("configuration"))
    into(layout.buildDirectory.dir("configuration"))
    mustRunAfter("compileJava")
}
```

6. Import and initialize the AppConfig
```
    import org.agraman4u.appconfig.AppConfig
    AppConfig.initAppConfig(APPLICATION_NAME, DEPLOYMENT_STAGE, DEPLOYMET_REGION)
```

7. Fetch values as required anywhere in code.
```
val databaseName=AppConfig.get<String>("databaseDriverConfig", "name")
```

Thats it. You are good to go! Hurraay!
