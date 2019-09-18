# Android Kotlin Sightseeing Forecast App

A simple Android Kotlin-based weather forecast app that can display current and 7 day forecasts.

## Duplicating My Dev Environment

Clone this repository to an empty directory of your choice and use Android Studio to build a project with it.

### Notes & Ideas on What Can be Improved

Some of the stranger design decisions I made are as a result of the peculiarities of the Dark Sky API:

```
*Dark Sky API's endpoint formatting is pretty non-standard. It is very rare to see an endpoint that does not consume an API_Key via query parameters. All that meant was that I wasn't able to do anything clever with query injections.

*The lack of a city name based alternative to the latitude & longitude severely restricted my abilities to design around user experience. I would have liked to have used a city name field instead of raw latitude & longitude fields for my choice of manual user fallback options.

*The response provided by Dark Sky API's endpoint was formatted a bit awkwardly, leading to less elegant Entity Models.
```

With that out of the way, here are some ways I would improve on my app design.

```
*More testsing. Nothing improves stability more than test oriented design.

*User options for handling localization and converting between systems of measurements.

*Improved user input sanitization. I sanitized user input pretty thoroughly with regexes but the feedback on failed user input could use some prettying up.

*Increase number of and quality of weather icons. Since Dark Sky API only covers a small number of weather phenomena with their icon response values, any additional icons would have to interpolated from secondary response data such as precipitation and wind speed.

*Add more infromation to the current forecast screen. As it currently is it feels a little bit sparse. Maybe roll the 7 day forecast into the main page to increase efficiency.

*Allow the user to see additional details by clicking on entries in the 7 day forecast list.

*Get somebody more artistically talented than me to design the ui.

*Make it possible for the user to manually search for a forecast using city names instead of latitude & longitude. Google's Geocoding library would probably help with that.
```