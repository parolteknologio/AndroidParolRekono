# LocalSTT

### [Esperanto]

Android Esperanto Parolrekono rekonas vian parolon en Esperanto kaj provizas la tekston.

Ĝi baziĝas sur la Vosk parolrekona modelo por Esperanto (vosk-model-small-eo-0.42) trovebla ĉe https://alphacephei.com/vosk/models.


Vi povas elŝuti de https://javabog.dk/privat/EsperantoParolRekono.apk


### [English]

> **Note: This application is just a proof of concept for now**

LocalSTT is an Android application that provides automatic speech recognition services without needing internet connection as all processing is done locally on your phone.

This is possible thanks to:
- a RecognitionService wrapping the Vosk library
- a RecognitionService wrapping Mozilla's DeepSpeech library
- an Activity that handles RECOGNIZE_SPEECH intents amongst others

The code is currently just a PoC strongly based on:
- [Kõnele](https://github.com/Kaljurand/K6nele)
- [Vosk Android Demo](https://github.com/alphacep/vosk-android-demo)

LocalSTT should work with all keyboards and applications implementing speech recognition through the RECOGNIZE_SPEECH intent or Android's SpeechRecognizer class. It has been successfully tested using the following applications on Android 9:
- [AnySoftKeyboard](https://github.com/AnySoftKeyboard/AnySoftKeyboard)
- [Kõnele](https://github.com/Kaljurand/K6nele)
- [SwiftKey](https://www.swiftkey.com)

You can download a pre-built binary with Vosk models for:
 - English: https://github.com/ewheelerinc/LocalSTT/releases / [LocalSTT-en-us.apk](https://github.com/ewheelerinc/LocalSTT/releases/download/2022-01-18-en-US/LocalSTT-en-us.apk)

and also with DeepSpeech models here:
 - Catalan: [here](https://github.com/ccoreilly/LocalSTT/releases/download/2020-12-03/localstt.apk).

If you want to use the application with your language just replace the models in `app/src/main/assets/sync/vosk-model/` with a package from https://alphacephei.com/vosk/models and rebuild the application.

#### Build notes:
 - git clone https://github.com/ewheelerinc/LocalSTT.git
- ./gradlew build
- ./repack-n-sign.sh ./app/build/outputs/apk/release/app-release-unsigned.apk
  - You might need to update paths and keys in this script for your use.

#### BUGS:

- Does not work with Google's keyboard "GBoard".
- Not all record applications read the voice text properly, there must be another way---and if you know how, it is probably a trivial fix.

#### Demo

![LocalSTT in action](./demo.gif)
