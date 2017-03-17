NastyTweeter
============

Overview
--------

NastyTweeter is an application that automates a Twitter account to reply to mentions by fetching invectives using the free [Fuck Off As A Service](http://foaas.com/). Though the service only supports direct replies to mentions at the moment, it will also be able to help Twitter users spew nasty words at others, too.

This application is under development and not ready to deploy at the moment and will be slowly edging towards an official release.

[![nastytweet.png](https://s28.postimg.org/fo767ns19/nastytweet.png)](https://postimg.org/image/xr08yvnvt/)

Getting started
---------------
- Set up your app on [Twitter Application Management](https://apps.twitter.com) and get your authentication credentials.
- Use these credentials to set up Twitter4J, this app's underlying library, to communicate with the Twitter servers. [You can do it in multiple ways.](http://twitter4j.org/en/configuration.html)
- In NastyTweeter.java, change the query in the function *scanTweets()* to use your app's Twitter account for fetching tweets, and edit the URL under fetchCurse() if you want to sign the tweets with a different name. A systematic method will be introduced in the near future.
- Run the app. My preferred way is deploying the app to [Heroku](https://www.heroku.com). Since free Heroku applications go to sleep after 30 minutes of inactivity, it is recommended you set up [Kaffeine](http://kaffeine.herokuapp.com).

Contribute
----------
I am struggling with terrible time management, and any improvements/even completing the project will be a great boon. Your pull requests are greatly welcome!

Credits
-------
* Poorly coded main app by [Mayukh Nair](http://mayukhnair.com)
* [Twitter4J](https://github.com/yusuke/twitter4j) by [@yusuke](https://github.com/yusuke)
* [Apache HTTPComponents](https://hc.apache.org/)
