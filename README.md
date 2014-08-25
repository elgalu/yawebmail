## Deps

    sudo apt-get install maven2
    
    mvn install:install-file -DgroupId=myfaces -DartifactId=myfaces-api -Dversion=1.1.5 -Dpackaging=jar -Dfile=deps/myfaces-api-1.1.5.jar
    mvn install:install-file -DgroupId=org.gnu.inet -DartifactId=libidn -Dversion=0.6.7 -Dpackaging=jar -Dfile=deps/libidn-0.6.7.jar
    mvn install:install-file -DgroupId=myfaces -DartifactId=myfaces-impl -Dversion=1.1.5 -Dpackaging=jar -Dfile=deps/myfaces-impl-1.1.5.jar
    mvn install:install-file -DgroupId=myfaces -DartifactId=tomahawk -Dversion=1.1.6 -Dpackaging=jar -Dfile=deps/tomahawk-1.1.6.jar

### Build

    mvn package
