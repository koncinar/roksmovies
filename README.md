# Movies Ro(c)k

This application is an implementation of TheMovieDB's API built with intention to display the usage
of the latest Android technologies and principles.

It contains 3 screens:
1. List of popular movies - first screen when you open the application
2. Search results - if you decide to search for movies by title
3. Movie details - More info about the movies if you clicked on one of them on screens 1 or 2

It gets all the data from TheMovieDB's APIs and it also saves the data locally so that it can also
be used offline.


# About the code

The app is built using MVVM + DataBinding principle. It has one Activity with 2 Fragments - one for
displaying the list of movies (popular or search results) and the other one for displaying the
details.
The data flow looks like this: View <-> ViewModel <-> Repository <-> Service or DB


# Libraries

These libraries are used in the project alongside with the Android SDK and Kotlin. The app is not
big and some of the libraries are not necessary but are added for the purpose of demoing the usage
of them together.
  * [Library Android Architecture Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) (provides ViewModel and LiveData) are used for the communication between View <-> ViewModel.
  * [DataBinding](https://developer.android.com/topic/libraries/data-binding) library is also used for that
  * [Glide](https://github.com/bumptech/glide) library is used for downloading and displaying movie thumbnails
  * [RxAndroid](https://github.com/ReactiveX/RxAndroid) and [RxJava](https://github.com/ReactiveX/RxJava) are used for threading and organizing the data reading from the API and DB
  * [Retrofit](https://square.github.io/retrofit/) is used for fetching the data from the API, in combination with [Gson](https://github.com/google/gson) that parses the response
  * [Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room) is used for storing (caching) the flights into the local database


# Suggested libraries

These libraries would be good to use but are not implemented (yet).
  * [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics) monitoring of the crashes and errors in the apps in production
  * [Firebase Analytics](https://firebase.google.com/docs/analytics) this would give us an insight of how users are interacting with our app
  * [Dagger](https://google.github.io/dagger) when project will grow this library would simplify the dependency injection


# Try it out

To try out the application:
1. Have Java installed on your computer
2. Download the sources
3. Open the Command Line or Terminal and navigate to the root of the project
4. Execute command: "gradlew assemble" on Windows or "./gradlew.sh assemble" on MacOS or Linux
5. You can find the app installation file in the subfolder app/build/outputs/apk/debug/app-debug.apk
