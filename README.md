# Project 2 - *Flixster*

**Flixster** shows the latest movies currently playing in theaters. The app utilizes the Movie Database API to display images and basic information about these movies to the user.

Time spent: **14** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **scroll through current movies** from the Movie Database API
* [x] Display a nice default [placeholder graphic](https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library#advanced-usage) for each image during loading
* [x] For each movie displayed, user can see the following details:
  * [x] Title, Poster Image, Overview (Portrait mode)
  * [x] Title, Backdrop Image, Overview (Landscape mode)
* [x] Allow user to view details of the movie including ratings and popularity within a separate activity

The following **stretch** features are implemented:

* [x] Improved the user interface by experimenting with styling and coloring
* [x] Apply rounded corners for the poster or background images using [Glide transformations](https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library#transformations)
* [x] Apply the popular [View Binding annotation library](http://guides.codepath.org/android/Reducing-View-Boilerplate-with-ViewBinding) to reduce boilerplate code
* [x] Allow video trailers to be played in full-screen using the YouTubePlayerView from the details screen

The following **additional** features are implemented:

* [x] Custom launcher icons
* [x] Custom heart rating bar to show popularity
* [x] Soft-coded links and stored YouTube keys so as to not waste time/resources to fetch again
* [x] Overlaid YouTube play button that changes when pressed
* [x] Splash screen added; however, we don't usually have many movies concurrently playing in theaters so this usually will not show up
* [x] Created a watchlist that users can add and remove from. Long-press a movie to add it to the watchlist, and press the upper-right icon to see the watchlist. Maintains persistence to allow the app to keep the watchlist data.

## Video Walkthrough

Here's some walkthroughs of implemented user stories.

Portrait mode:

![App Demo Portrait Link](screenshots/walkthroughportrait.gif)

Landscape mode:

![App Demo Landscape Link](screenshots/walkthroughlandscape.gif)

***NEW!*** Save movies to a watchlist by long-pressing to add and remove. Maintains persistence; closing the app and reopening it still keeps the data.

![App Demo Watchlist Link](screenshots/walkthroughwatchlist.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Initial app kept crashing; the error given was "Expected Android API level 21+, but was 30". Changing the Android API level to 29 and the miniSDK level to 21 fixed this issue; perhaps the functionalities used lacked compatibility with API level 30.

I wanted the play button to change to a different shape when clicked, and return to normal when the user exited the video player. However, the play button would change back so quickly that it looked like it didn't even change. Workaround: Adding a timer so that the image doesn't change immediately, but will change when the user sees the video player screen as to give the impression it changed correctly.

Spent a lot of time on the watchlist. Debated how to save the watchlist movies to a file: should I save the movie info directly? However, it was difficult to read all of this information from a file. Thus, I decided to just save the movie IDs from the DB and load them in from the database whenever the app starts; this leads to slightly longer loading times but nothing drastic. Also had to use a Set instead of a List because long-pressing sometimes responded in duplicates.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright 2020 Emily Park

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
