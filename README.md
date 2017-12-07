# commons
Commons and reusable Java libraries

Table of Contents
-----------------

* [cdi](#cdi)
* [logging](#logging)
* [ssl](#ssl)

cdi
============
 
* `CDIContainer` - simplifies selection of CDI beans from the current container
* `Events` - simplifies fire CDI events of a particular type
* `Startup` - annotation which marks beans for startup when the application context is initialized

logging
============

### slf4j

* `SimpleLoggerFactory` - factory which returns SLF4J logger for the class from which the call was made
* `ContextLogger` - logger for logging messages with context on the beginning of message content
* `ContextLoggerFactory` - factory of `ContextLogger` instances 

### logback

* `AppLevelProperty` - property for setting application logging level
* `LoggingPathProperty` - property for setting logging path
* `WebserviceLevelProperty` - property for setting webservice logging level

Examples of usage (default values in *logback.xml*):
```
<define name="APP_LEVEL" class="com.github.mstawowiak.commons.logging.logback.property.AppLevelProperty">
    <default>INFO</default>
</define>
<define name="WEBSERVICE_LEVEL" class="com.github.mstawowiak.commons.logging.logback.property.WebserviceLevelProperty">
    <default>OFF</default>
</define>
<define name="LOGGING_LEVEL" class="com.github.mstawowiak.commons.logging.logback.property.LoggingPathProperty">
    <default>logs/</default>
</define>
```

Examples of usage (by JVM Options):
```
-Dcom.github.mstawowiak.commons.logging.app.level=INFO
-Dcom.github.mstawowiak.commons.logging.webservice.level=OFF
-Dcom.github.mstawowiak.commons.logging.path=logs/
```

ssl
============
 
* `SSLKeystore` - keystore which can be build from file or system properties
* `SSLConfiguration` - SSL configuration from keystore and truststore. Provides builder to create SSLContext for TLS Certificate verification and Client Certificate authentication
* `AliasSelectorKeyManager` - custom key manager which allows specify alias to be chosen by SSLSocketFactory
* `NaiveHostnameVerifier` - a HostnameVerifier which always verifies a hostname as true
* `NaiveTrustManager` - truststore manager which accepts all certificates
