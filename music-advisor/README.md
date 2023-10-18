# Music Advisor

My implementation of project 'Music Advisor' from JetBrains Academy on Hyperskill.

## What's here?
- Simple CLI app with Spotify API (by default) using OAuth 2.0
- user can specify server and resource access points with parameters at launch (system properties)
- HTTP classes for the server, client, responses & requests

## Commands
* `auth`

Authorize through Spotify API.

* `new`

Get newest releases. **Needs authorization.**

* `featured`

Get featured playlists. **Needs authorization.**

* `categories`

Get names of available categories. **Needs authorization.**

* `playlist {CATEGORY_NAME}`

Get playlists from a provided category. **Needs authorization.**

* `prev` & `next`

When looking through a list of entries, use these to switch to previous and next page accordingly.

* `exit`

Goodbye!

## Options
* `-Dpage`
Set number of entries per page.

**Default:** 5.

* `-Daccess` & `-Dresource`

If you're hardcore and have constructed Spotify-exact access and resource points (just like JB Academy did), you can provide them and use them.

**Default:** `https://accounts.spotify.com` & `https://api.spotify.com`.

## Installation

In order to get this project launched on your machine, see the instructions below.

1. Download this directory (it's not repository) through, for example, this link:
```shell
https://download-directory.github.io/?url=https%3A%2F%2Fgithub.com%2FIceMajor2%2FJetBrains_course%2Ftree%2Fmain%2FMusic%2520Advisor
```
2. Open project directory
3. Run using Gradle
```shell
gradle run [-Daccess][-Dresource][-Dpage]
```
Look at default values to match yours.
