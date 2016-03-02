rm -rf bin/*
javac -classpath coolserver.jar -sourcepath src -d bin src/*.java 

if [ $? -eq 0 ]; then
    cd bin
    mv CA4.class ../
    jar cvfm ../CA4.jar ../manifest *.class
fi
