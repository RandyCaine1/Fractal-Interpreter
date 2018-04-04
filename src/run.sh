# Commands to run
cd fractal/syntax
jflex FractalLexer.jflex
cup -parser FractalParser FractalParser.cup
cd ..
cd ..
#javac -classpath ".:/home/valkyrie/Desktop/src/:/usr/share/java/cup.jar" -cp cs34q-utils.jar fractal/sys/Repl.java
#java -classpath ".:/home/valkyrie/Desktop/src/:/usr/share/java/cup.jar" fractal/sys/Repl

export CLASSPATH=".:/home/valkyrie/Desktop/src/:/usr/share/java/cup.jar:/home/valkyrie/Desktop/src/cs34q-utils.jar"

javac fractal/sys/Repl.java
java fractal/sys/Repl -- koch.fal
