@echo off
javac -d bin src/module-info.java src/com/perisic/tomato/engine/*.java src/com/perisic/tomato/peripherals/*.java
copy /Y src\tomato1.png bin\ >nul
copy /Y src\tomato2.png bin\ >nul
java -cp bin com.perisic.tomato.peripherals.LoginGUI
