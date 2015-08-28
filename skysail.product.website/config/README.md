# Starting skysail:

## Prerequisites

You need a Java 8 runtime on your machine

## Why is the jar so big?

Apart from the java JRE, this jar contains everything you need to run a complete web application, including the database,
the server, an OSGi-framework, the RESTlet framework, JSON libraries and so on. 

## How to run it?

* Extract the zip file
* In the command line: navigate to the root directory of the created directory 
* run "java -jar skysail.website.jar"

## And now?

A javafx browser should start where you can access the skysail website application.

Alternatively, you can open your browser at http://localhost:2017

## Next steps

In the command line, you have access to the OSGi framework. Type "lb", for example, to list the installed bundles.
Or type "scr:list", to see the list of available services. In case something goes wrong, typing "log error" might 
give you some hints what went wrong.

## Tips

Navigate to http://localhost:2017/_login to login to the framework. The username is "admin", password is "skysail".

