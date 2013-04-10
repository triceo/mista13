#!/bin/sh

# Change directory to the directory of the script
cd `dirname $0`

echo "Usage: ./runMista.sh in out timeLimitInSeconds randomSeed"
echo "For example: ./runMista.sh data/projectscheduling/input/A-1.txt data/projectscheduling/output/A-1.txt 300 0"
echo "Some notes:"
echo "- Working dir should be the directory of this script."
echo "- Java is recommended to be JDK and java 7 for optimal performance"
echo "- The environment variable JAVA_HOME should be set to the JDK installation directory"
echo "  For example (linux): export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-i386"
echo "  For example (mac): export JAVA_HOME=/Library/Java/Home"
echo
echo "Starting mista..."

if [ -f $JAVA_HOME/bin/java ]; then
    $JAVA_HOME/bin/java -Xms512m -Xmx2048m -server -jar mista2013-6.0.0.Beta1-jar-with-dependencies.jar $*
else
    echo "Please set environment variable JAVA_HOME"
    # Prevent the terminal window to disappear before the user has seen the error message
    sleep 20
fi

if [ $? != 0 ] ; then
    echo
    echo "Error occurred. Check if \$JAVA_HOME ($JAVA_HOME) is correct."
    # Prevent the terminal window to disappear before the user has seen the error message
    sleep 20
fi
