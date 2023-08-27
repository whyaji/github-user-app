# Github-User-App

The GitHub User App is a mobile app that allows users to search for GitHub users, view their profiles, and save their favorite users to a local database. The app uses the GitHub User API to retrieve and store data.

The app is developed using Kotlin, a modern programming language that is known for its conciseness and readability. The layout of the app is created using XML

The app has the following features: Search for GitHub users, View user profile, including repositories, followers, and following and also save favorite users to local database.

![alt text](https://raw.githubusercontent.com/whyaji/github-user-app/main/ss-githubuserapp.jpg)


## Info
**In your build.gradle(app) modify the following line and fill it based on your github request API Token**

```bash
..
defaultConfig {
        ...
        buildConfigField "String", "KEY", '"Your Token Here"'
}
..
```