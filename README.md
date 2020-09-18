# ClothesStore (Android)
[![kotlin](https://img.shields.io/badge/Kotlin-1.3.xx-blue)](https://kotlinlang.org/) [![MVVM ](https://img.shields.io/badge/Architecture-MVVM-brightgreen)](https://developer.android.com/jetpack/guide) [![coroutines](https://img.shields.io/badge/Kotlin-Coroutines-orange)](https://developer.android.com/kotlin/coroutines) [![Dagger](https://img.shields.io/badge/Dagger-Hilt-orange)](https://dagger.dev/hilt)

Android client for an online catalogue.

![App Demo Gif](https://user-images.githubusercontent.com/13585693/86788698-5b555800-c05e-11ea-880f-3a0afcb85f1f.gif)


#### Setup:
Create a `serverSecrets.properties` file in the root directory of your project

![alt text](https://user-images.githubusercontent.com/13585693/86789145-e3d3f880-c05e-11ea-8525-03ff1879d45f.png "serverSecrets setup")

```kotlin
API_KEY="XXXXXXX_API_KEY_HERE_XXXXX"
BASE_URL="XXXXXXX_PROJECT_BASE_URL_HERE_XXXXX"
HEADER_AUTH_KEY="XXXXXXX_HEADER_AUTH_KEY_XXXXX"
```

The private keys above can be provided by Melissa.


- Icons sourced from the vector icons catalogue in Android Studio, Licensed available under[ Apache license version 2.0.](https://www.apache.org/licenses/LICENSE-2.0.html)
- Theme was customized by a free tool called [MaterialPalette](https://www.materialpalette.com/brown/grey)


*P.S:* The backend was designed in a way that requests had to be chained, hence the refresh after certain GET/POST requests
