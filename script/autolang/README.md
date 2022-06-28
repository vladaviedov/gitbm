# Autolang

Script which automatically generates gitbm localization by using the minecraft language files.

## Installation
- Install yarn
- `yarn install`

## Running
`yarn run gen --lang=<code> --mcversion=<version> --location=<dir> --out=<dir>`
- `--lang` - language code
- `--mcversion` - minecraft version
- `--location` - root .minecraft directory (default: $HOME/.minecraft)
- `--out` - out directory (default: ./out)

## Languages
| :heavy_check_mark: | :white_check_mark: | :heavy_multiplication_x: |
| ------------------ | ------------------ | ------------------------ |
| Verified           | Looks okay         | Does not work            |

- `en_gb` :heavy_check_mark:
- `ru_ru` :heavy_check_mark:
- `zh_cn` :heavy_check_mark:
- `ja_jp` :white_check_mark:
- `fr_fr` :white_check_mark:
- `es_es` :white_check_mark:
- `uk_ua` :heavy_multiplication_x: (grammar: з/із/зі)

## Notes
- The version of the game specified needs to be launched at least once on the computer, so the assets are downloaded
- en\_us cannot be used as langugage code (does not exist in assets)
