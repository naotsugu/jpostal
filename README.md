[![Build](https://github.com/naotsugu/jpostal/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/naotsugu/jpostal/actions/workflows/gradle-build.yml)

# JPostal

Japan postal code dictionary(so‐called ken_all.csv) utility.

* Read this in other languages: [日本語](README.ja.md)

![jpostal](doc/images/search.gif)

<br/>

## Feature

* Automatically download japan postal code csv from japanpost.jp
* Format address pretty well
* Auto update support (monthly)
* Minimal REST web server included 
* No dependencies (only jdk)

<br/>

## Usage

Add dependencies

```groovy
dependencies {
    implementation 'com.mammb:jpostal:0.2.0'
}
```

Create and initialize `Postal`.

```java
Postal postal = Postal.of();
postal.initialize();
```

Get address by postal code query.

```java
String code = "105001";
Collection<Address> addresses = postal.get(code);
//[{"code": "1050011", "prefecture": "東京都", "city": "港区", "town": "芝公園", "street": ""},
// {"code": "1050012", "prefecture": "東京都", "city": "港区", "town": "芝大門", "street": ""},
// {"code": "1050013", "prefecture": "東京都", "city": "港区", "town": "浜松町", "street": ""}, 
// {"code": "1050014", "prefecture": "東京都", "city": "港区", "town": "芝", "street": ""}]
```

<br/>

## PostalServer

If you needs rest server, run jpostal.jar.

Get `jpostal.jar` from `https://github.com/naotsugu/jpostal/releases` and run it.

Or clone and build and run.

```
$ git clone https://github.com/naotsugu/jpostal.git
$ cd jpostal
$ ./gradlew jar
$ java -jar build/libs/jpostal.jar
```

Or use `PostalServer`. 


```
PostalServer server = PostalServer.of(postal);
server.start();
```

Go to `http://localhost:8080/postal/105001`, then you gets address by json format.

In addition, console page is `http://localhost:8080/postal/console.html`.

<br/>

## Options

```java
Postal postal = Postal.of()
    .fineAddressSupport(true)
    .leftMatchSupport(true)
    .leftMatchLimitCount(15)
    .officeSourceSupport(true)
    .autoUpdateSupport(true);
```


| Option                 | Default |
| ---------------------- | ------- |
| `fineAddressSupport`   | `true`  |
| `leftMatchSupport`     | `true`  |
| `leftMatchLimitCount`  |  `20`   |
| `officeSourceSupport`  | `false` |
| `autoUpdateSupport`    | `false` |

<br/>

## Details

See README.ja.md



