# AeroGear Android Security [![Build Status](https://travis-ci.org/aerogear/aerogear-android-security.png)](https://travis-ci.org/aerogear/aerogear-android-security)

AeroGear's Android libraries were built as jar and aar packages using [Maven](http://maven.apache.org/) and the [android-maven-plugin](https://github.com/jayway/maven-android-plugin). The project follows the standard Maven layout so it can be imported directly into most IDEs as a Maven project.

## Security

AeroGear Android Security is an Android API under [AeroGear Crypto Java](https://github.com/aerogear/aerogear-crypto-java) to provide easy way to use cryptography.

|                 | Project Info  |
| --------------- | ------------- |
| License:        | Apache License, Version 2.0  |
| Build:          | Maven  |
| Documentation:  | http://aerogear.org/docs/guides/aerogear-android/  |
| Issue tracker:  | https://issues.jboss.org/browse/AGDROID  |
| Mailing lists:  | [aerogear-users](http://aerogear-users.1116366.n5.nabble.com/) ([subscribe](https://lists.jboss.org/mailman/listinfo/aerogear-users))  |
|                 | [aerogear-dev](http://aerogear-dev.1069024.n5.nabble.com/) ([subscribe](https://lists.jboss.org/mailman/listinfo/aerogear-dev))  |

## Building

Please take a look at the [step by step guide](http://aerogear.org/docs/guides/aerogear-android/how-to-build-aerogear-android/) on our website.

*The following dependencies are required to build this project:*

* [aerogear-android-core](http://github.com/aerogear/aerogear-android-core) 

## Usage

There are two supported ways of developing apps using AeroGear for Android: Android Studio and Maven.

### Android Studio

Add to your application's `build.gradle` file

```
dependencies {
  compile 'org.jboss.aerogear:aerogear-android-security:2.1.0'
}
```

### Maven

Include the following dependencies in your project's `pom.xml`

```
<dependency>
  <groupId>org.jboss.aerogear</groupId>
  <artifactId>aerogear-android-security</artifactId>
  <version>2.1.0</version>
  <scope>provided</scope>
  <type>jar</type>
</dependency>

<dependency>
  <groupId>org.jboss.aerogear</groupId>
  <artifactId>aerogear-android-security</artifactId>
  <version>2.1.0</version>
  <type>aar</type>
</dependency>
```

## Documentation

For more details about the current release, please consult [our documentation](http://aerogear.org/docs/guides/aerogear-android/).

## Demo apps

Take a look in our demo apps

* [OTP Demo](https://github.com/aerogear/aerogear-otp-android-demo)
* [Crypto Demo](https://github.com/aerogear/aerogear-crypto-android-demo)

## Development

If you would like to help develop AeroGear you can join our [developer's mailing list](https://lists.jboss.org/mailman/listinfo/aerogear-dev), join #aerogear on Freenode, or shout at us on Twitter @aerogears.

Also takes some time and skim the [contributor guide](http://aerogear.org/docs/guides/Contributing/)

## Questions?

Join our [user mailing list](https://lists.jboss.org/mailman/listinfo/aerogear-users) for any questions or help! We really hope you enjoy app development with AeroGear!

## Found a bug?

If you found a bug please create a ticket for us on [Jira](https://issues.jboss.org/browse/AGDROID) with some steps to reproduce it.

