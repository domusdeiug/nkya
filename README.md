# Nkya

Nkya is an Android launcher localised for Uganda, based on [Lawnchair](https://github.com/LawnchairLauncher/lawnchair).

## Languages

English, Luganda, Acholi, Runyankole, Ateso, Lugbara and Kiswahili.

The Ugandan-language strings are machine translated and need review by native speakers.
See `translations/needs_manual.csv` for strings that still need manual translation.
Pick a language in **Settings → General → Language** (Android 13+), or via Android's per-app language settings.

## Building

Each push to `main` builds an APK with GitHub Actions. Download it from the run's **Nkya-APK** artifact.

Local build (JDK 21, Android SDK):

```
git clone --recurse-submodules https://github.com/domusdeiug/nkya.git
./gradlew assembleLawnWithQuickstepGithubRelease
```

## License

Apache License 2.0, see `LICENSE.txt`. Based on Lawnchair and the Android Open Source Project.
