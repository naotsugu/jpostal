[![Build](https://github.com/naotsugu/jpostal/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/naotsugu/jpostal/actions/workflows/gradle-build.yml)


# JPostal

郵便番号辞書(いわゆる ken_all.csv)のユーティリティです.

![jpostal](doc/images/search.gif)

<br/>

## Feature

* japanpost.jp から辞書 csv を自動ダウンロード
* 辞書データは以下を選択可能
  * 「住所の郵便番号（1レコード1行、UTF-8形式）（CSV形式）」(`utf_all.csv`)
  * 「読み仮名データの促音・拗音を小書きで表記するもの」(`ken_all.zip`)
  * 「事業所の個別郵便番号」(`jigyosyo.zip`)
* 住所情報の整形
* 辞書の自動アップデート(月次)
* REST web サーバ付属 
* CSV 出力
* 依存ライブラリ無し

<br/>

## Usage

以下の依存を追加します.

```groovy
dependencies {
    implementation 'com.mammb:jpostal:0.5.1'
}
```

`Postal` を生成して初期化します.

```java
Postal postal = Postal.of();
postal.initialize();
```

生成したインスタンスに郵便番号のクエリを渡すことで、候補の郵便番号と住所が取得できます.

```java
String code = "105001";
Collection<Address> addresses = postal.get(code);
//[{"code": "1050011", "prefecture": "東京都", "city": "港区", "town": "芝公園", "street": ""},
// {"code": "1050012", "prefecture": "東京都", "city": "港区", "town": "芝大門", "street": ""},
// {"code": "1050013", "prefecture": "東京都", "city": "港区", "town": "浜松町", "street": ""}, 
// {"code": "1050014", "prefecture": "東京都", "city": "港区", "town": "芝", "street": ""}]
```

<br/>

## CSV file

整形済みのCSVファイルを出力することができます.

`-o` オプションで出力ファイル名を指定します。

```
$ java -jar jpostal-0.5.0.jar -o out.csv
```

`郵便番号,地方公共団体コード,都道府県名,市区町村名,町域名` の並びのCSVファイルを出力します。


<br/>

## PostalServer

REST サーバが必要な場合は jpostal.jar を実行します.


直接ビルドして実行するか、`https://github.com/naotsugu/jpostal/releases` から `jpostal.jar` をダウンロードして実行します.

```
$ git clone https://github.com/naotsugu/jpostal.git
$ cd jpostal
$ ./gradlew jar
$ java -jar build/libs/jpostal-0.5.0.jar
```

または、`PostalServer` を使います. 


```java
PostalServer server = PostalServer.of(postal);
server.start();
```

`http://localhost:8080/postal/105001` のようにアクセスすることで json フォーマットで結果を取得することができます.

`http://localhost:8080/postal/console.html` にアクセスすれば簡易的な住所チェック用のコンソールが表示されます.

<br/>

## Options

以下のオプションがあります.

```java
Postal postal = Postal.of()
    .useLegacySource(false)
    .fineAddressSupport(true)
    .leftMatchSupport(true)
    .leftMatchLimitCount(20)
    .officeSourceSupport(false)
    .autoUpdateSupport(true);
```


| Option                 | Default | Description                                                                                         |
| ---------------------- |---------|-----------------------------------------------------------------------------------------------------|
| `useLegacySource`   | `false` | 2023年6月から提供開始された、1レコード1行、UTF-8形式ファイルを使用するか、旧来形式ファイルを使用するかを指定します。`true` とした場合、旧来形式のファイルをソースとして使用します。 |
| `fineAddressSupport`   | `true`  | 詳細な住所加工を行うかどうかを指定します                                                                                |
| `leftMatchSupport`     | `true`  | 住所の前方一致検索を有効にします                                                                                    |
| `leftMatchLimitCount`  | `20`    | 前方一致検索時の検索結果数を指定します                                                                                 |
| `officeSourceSupport`  | `false` | 事業所の個別郵便番号をサポートするかどうかを指定します                                                                         |
| `autoUpdateSupport`    | `false` | 郵便番号辞書の自動更新を有効にするかどうかを指定します                                                                         |

<br/>

## Details

### 辞書のダウンロード

`jpostal.jar` 実行時のディレクトリに `ken_all.zip` または `utf_all.csv` が存在する場合は、このファイルを利用します。

ファイルが存在しない場合は、日本郵政の辞書ファイルを自動でダウンロードします.
ダウンロードしたファイルは `jpostal.jar` 実行時のディレクトリにダウンロードされるため、次回起動時にはこのファイルを使うようになります.

ダウンロードするファイルは「住所の郵便番号（1レコード1行、UTF-8形式）（CSV形式）」です。
`useLegacySource` に `true` を指定した場合には「読み仮名データの促音・拗音を小書きで表記するもの」の全国版を使用します.

オプションで `officeSourceSupport` が有効化されていた場合は「事業所の個別郵便番号」`jigyosyo.zip` を加えて扱います.


### 辞書の更新

郵政郵便番号は月末に更新分が公開されます。

`autoUpdateSupport` を有効にすることで月初(0時〜1時の間のランダムな時刻)に自動更新されます。 


### 郵便番号のマッチモード

`leftMatchSupport` を有効にした場合、前方一致で郵便番号を検索します.
`leftMatchSupport` を無効にした場合は完全一致検索となります.

前方一致検索で取得する結果件数は `leftMatchLimitCount` で指定します.


### 住所情報の整形

日本郵政の公開する郵便番号辞書はシステムでそのまま利用できる代物ではないため、各種の整形処理を行っています.

複数行に分割されたレコードを合成した後、[`com.mammb.code.jpostal.source.TownEditor`](src/main/java/com/mammb/code/jpostal/source/TownEditor.java) にある変換処理を行います.


例えば以下のようなレコードは、

```
"0580343",..,"北海道","幌泉郡えりも町","東洋（油駒、南東洋、１３２～１５６、１５８～３５４、３６６、３６７番地）"
```

以下のような住所情報として整形します.

```
"0580343",..,"北海道","幌泉郡えりも町","東洋","油駒"
"0580343",..,"北海道","幌泉郡えりも町","東洋","南東洋"
"0580343",..,"北海道","幌泉郡えりも町","東洋","３６６番地"
"0580343",..,"北海道","幌泉郡えりも町","東洋","３６７番地"
"0580343",..,"北海道","幌泉郡えりも町","東洋",""
```

`fineAddressSupport` オプションを無効にした場合は以下のように編集されます.

```
"0580343",..,"北海道","幌泉郡えりも町","東洋",""
```

なお、「事業所の個別郵便番号」については住所情報の編集は行いません.


