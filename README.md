Spray-Prerender
===============

Are you using backbone, angular, emberjs, etc, but you're unsure about the SEO implications?

Use this Spray directive that prerenders a javascript-rendered page using an external service and returns the HTML to the search engine crawler for SEO.

`Note:` If you are using a `#` in your urls, make sure to change it to `#!`. [View Google's ajax crawling protocol](https://developers.google.com/webmasters/ajax-crawling/docs/getting-started)

`Note:` Prerender service will make a request to your server to render the HTML.

1:add dependency on your 

    "com.github.jarlakxen" %% "spray-prerender" % "1.0"

2:Add this line to your code

    
    implicit val prerenderConfig = routePrerender("10.254.169.132:9290", actorRefFactory)


     get {
      prerender(prerenderConfig) {
        mustache("/index")
      }
    }


`Note:` The first parameter of routePrerender is the ip and port of the Prerender server.

## How it works
1. Check to make sure we should show a prerendered page
    1. Check if the request is from a crawler (agent string)
    2. Check to make sure we aren't requesting a resource (js, css, etc...)
2. Make a `GET` request to the [prerender service](https://github.com/prerender/prerender)(phantomjs server) for the page's prerendered HTML
3. Return that HTML to the crawler
