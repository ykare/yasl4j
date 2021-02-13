# YASL4J(Yet Another SL for Java)

究極のキータイプ矯正ソフト [SL](https://github.com/mtoyoda/sl/blob/master/README.ja.md) を Java に移植しました。
機能・アルゴリズムは、ほとんど元のプログラムのままですが、音が鳴るようになっています。

## ビルド

```bash
gradle fatJar
```

## 実行

```bash
java -jar build/libs/yasl4j-all-in-one-X.X.X-SNAPSHOT.jar # Linux or Mac OS
javaw  -jar build/libs/yasl4j-all-in-one-X.X.X-SNAPSHOT.jar # Windows
```

使えるオプションは、-h オプションでご確認ください。

Windows で WSL2 の環境であれば、javaw で無くても実行できます。WSL2 で音を鳴らすには[こちら](https://poniti.hatenablog.com/entry/2020/01/08/002536) が参考になります。

## GraalVM の native-image を使う

GraalVM の native-image を使うとオリジナルと同じ使用感で **sl** コマンドが使えるようになります。

```bash
native-image -jar build/libs/yasl4j-all-in-one-X.X.X-SNAPSHOT.jar
mv yasl4j-all-in-one-1.0-SNAPSHOT sl
./sl # yasl4j の実行
```

Copyright (c) 2021 Yoshiyuki Karezaki(y.karezaki@gmail.com)<br>
Original Copyright 1993,1998,2014 Toyoda Masashi (mtoyoda@acm.org) https://github.com/mtoyoda/sl/

![VSCode で実行中の画面](demo.gif)