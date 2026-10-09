# Tomato Game - CIS046-3

Java Swing game using the Marc Conrad Tomato API.
Login first (default `Ada` / `hello23`), then guess the missing number.

## setup

needs Java 17.

```bat
javac -d bin src/module-info.java src/com/perisic/tomato/engine/*.java src/com/perisic/tomato/peripherals/*.java
copy src\tomato1.png bin\
copy src\tomato2.png bin\
java -cp bin com.perisic.tomato.peripherals.LoginGUI
```

## notes

- offline fallback uses `tomato1.png` / `tomato2.png` if the api is down.
- users are stored hashed (SHA-256 + salt) in `users.txt`.
- session token in `session.cookie`, expires after 30 mins.

based on the TomatoFull example from the unit. see source comments for links used.
