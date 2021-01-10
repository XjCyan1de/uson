[![GitHub](https://img.shields.io/github/license/unidpers/uson?style=for-the-badge)](../LICENSE)
![JitPack](https://img.shields.io/jitpack/v/github/unidpers/uson?style=for-the-badge)

Description
-----------
The simple fast json parser on pure java

Build the project
-----------------
> gradlew build

Add as dependency
-----------------
> Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.unidpers</groupId>
    <artifactId>uson</artifactId>
    <version>1.0.0-STABLE</version>
</dependency>
```
> Gradle
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.unidpers:uson:1.0.0-STABLE'
}
```
Usage
-----
> Read json from file
```java
Json json = Json.defaultInstance();

// Automatically replaces \\n to \n, \\u0000 to \u0000 and etc
//
// If you know that file contains characters like this
// you can to uncomment the following line
// 
// json.addOptions(Json.AUTO_UNESCAPE);

JsonNode node = json.fromJson(new File(filename));
JsonObject object = node.asObject();
String message = object.getString("message");
```
> Write json to file
```java
Json json = Json.defaultInstance();

// Enables pretty printing
//
// If you need more human-readable output
// you can to uncomment the following line
//
// json.addOptions(Json.PRETTY_PRINTING);

JsonObject object = new JsonObject();
object.put("message", "Hello world!");

json.toJson(object, new File(filename));
```

Contact with author
-------------------
 - VK: [Click to go to vk.com](https://vk.com/id623151994)
 - Telegram: [Click to go to t.me](https://t.me/unidpers)
 - Email: [Click to mail me](mailto:unidpers@gmail.com)

License
-------
```
Copyright 2021 Unidentified Person

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
