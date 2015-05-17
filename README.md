travel-log
==========

Place a config file named 'images.conf' next to 'application.conf'.
'images.conf' should look like this:

```
at.droelf.travel-log.images.rootDir=
at.droelf.travel-log.tracks.rootDir=

at.droelf.travel-log.db.mode=

at.droelf.travel-log.user=
at.droelf.travel-log.password=

at.droelf.travel-log.analytics=

at.droelf.travel-log.email.recipient=
at.droelf.travel-log.email.sender=
at.droelf.travel-log.email.hold=
at.droelf.travel-log.email.alternative=

at.droelf.travel-log.disqus.shortname=
```

```sbt run```


Features
=========
* Document you travels with images, gpx tracks and texts.
* Automatically calculate the position where pictuers were taken.
* Disqus/Google Analytics integration
* Secures image and track upload
* Android App for uploading media --> https://github.com/makubi/travel-log-app/

Technologies
=========
* Scala
* Play Framework
* Slick ORM (Postgres/H2)
* Javascript
* WebJars



Demo
=====
http://pacific2014.at

[![Build Status](https://travis-ci.org/MrGradient/travel-log.svg?branch=master)](https://travis-ci.org/b-a-s-t-i/travel-log)
