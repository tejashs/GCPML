This is an Android Application that uses the service exposed by the `Brainiac` application to consume REST service
and predict images of Flowers and Digits.

Before making the changes to the android application make sure that `Brainiac` application is deployed and
running successfully on the Google Cloud platform.

1. Open the file `LandingPage.java` under the following location :
    `app/src/main/java/com/example/i861944/gcpml/`
2. Find the following line of code :
    `private static final String BASE_URL = "https://saphxe-168717.appspot.com/predict/"`
3. Change the line of code to provide the URL of your deployed `Brainiac` app.
4. Build the Android application and load it onto your Android phone.
5. Follow the steps on the Android appliction to predict Flower and Digit images.
