# Video transcript - CIS046-3 (~9 mins)

Hi, I'm [name], this is my Tomato game for CIS046-3. I'll demo it first, then go through version control, events, interop and identity.

## demo (0:00-1:20)

I run src/run.bat. It compiles with javac and starts LoginGUI. Default account is Ada / hello23. Wrong password gets rejected. I can sign up too, I made bob / secret1 for this demo.

Once I'm in, GameGUI shows a tomato puzzle image from the API. I click numbers 0-9. Right answer ups my score and loads a new puzzle. Wrong answer keeps the same puzzle and ups my tries. You can also type 0-9 on the keyboard. Timer at the top counts seconds. Logout clears the session and goes back to login.

If the API is down it still works. It falls back to tomato1.png and tomato2.png in src. I'll show that code later.

## 1. version control (1:20-3:20)

All my code is in git. I started from the TomatoFull example on master, commit 7aaa0bf. Then I worked on branches.

I used three branches. auth-hashing for login changes. interop-fix for the GameServer timeouts and fallback. gui-events for logout, keys and timer. Each branch had one or two small commits, like "wip hashing + signup and sessions" and "fix api timeouts and offline fallback". I merged them back into master with no fast-forward issues. I tagged v0.1-week8-demo when it first ran end to end. I showed that to my tutor.

The code is split into engine and peripherals. engine has Game, GameEngine, GameServer. peripherals has GameGUI, LoginGUI, LoginData. That made merges easier. I didn't touch Game.java at all, so no conflicts there. Small commits helped. If I broke hashing, I could revert just that branch.

Another way would be trunk-based with everyone pushing to main. That works for tiny coursework but gets messy fast. Branches plus a tag for the demo felt safer. If this was a team job I'd want pull requests and reviews before merge. I did self-review with git diff before each merge instead.

## 2. event-driven programming (3:20-5:10)

My GUI is all events. No polling loop.

Button clicks are ActionEvents. GameGUI implements ActionListener. In actionPerformed I read e.getActionCommand. If it's "logout" I clear the session and open LoginGUI. Else I parse it as int and call checkAnswer. The ten number buttons are made in a loop in initGame and all share the same listener. Login works the same way, blogin and bsignup each have an anonymous ActionListener.

Keys are a second source. I added a KeyAdapter. keyPressed checks if the char is 0-9 and calls the same checkAnswer method. So buttons and keys feed into one place. That saved duplicate code.

The third one is the timer. New Timer(1000, ...) fires an ActionEvent every second and updates topLabel with player and seconds. It's a Swing timer so it runs on the event dispatch thread. Safe to touch labels.

I picked Swing listeners because the example used them and they're simple. JavaFX properties would be cleaner for binding score to a label, but heavier to set up. One thing to watch: nextGame does network IO. Right now it runs on the EDT, so the UI freezes for a few seconds if the API is slow. Better would be SwingWorker to fetch in background. I kept it sync to stay close to the example, but I'd change that next.

## 3. interoperability (5:10-7:10)

The game doesn't make its own puzzles. It calls https://marcconrad.com/uob/tomato/api.php?out=csv&base64=yes.

That's a GET over HTTPS. Response is CSV, two fields. First is PNG bytes as Base64, second is the int solution. I split on comma, Base64 decode, wrap in ByteArrayInputStream, then ImageIO.read to get a BufferedImage. Game stores image + solution together.

My changes were around failure. Old code used url.openStream with no timeout and crashed on null with NullPointerException on split. I switched to URLConnection with 6 sec connect and read timeout. If dataraw is null or has no comma, or decode fails, I call getOfflineGame. That loads tomato1.png or tomato2.png from resources and returns solution 1. Same as the Basic example. It means the game still demos without wifi.

I thought about using out=json instead of csv. JSON would need a parser. CSV is fine here, two fields, no quotes. Base64 in the URL keeps it text-safe. Downside is no versioning or error codes. If Marc's server changes format, my split breaks. My fallback covers that a bit.

This is distributed. My Swing client and his PHP API run on different machines, different stacks. They only agree on HTTPS + CSV text. I didn't need his source, just the doc page.

## 4. virtual identity (7:10-9:00)

Old LoginData checked Ada/hello23 in plain text. I replaced that.

Now addUser makes a 16-byte salt with SecureRandom, hashes salt+password with SHA-256, stores username:salt:hash in users.txt. checkLogin re-hashes with the stored salt and compares. users.txt never has passwords. I checked that in testing.

Sessions work like cookies. createSession makes a UUID token plus username plus expiry, 30 mins from now, and writes it to session.cookie. LoginGUI on start calls checkSession. If the token is still valid it skips login and opens the game. Logout calls clearSession which deletes the file.

Why like this? Desktop Swing has no browser cookies, but the file does the same job - remembers who I am. Expiry limits damage if someone copies the file. Logout is explicit.

Tradeoffs. SHA-256 with one round is better than plain but weak vs bcrypt or Argon2 which are slow on purpose. My users.txt has no iterations, no pepper, and file perms are whatever Windows gives. A real app would use a DB, prepared statements, TLS, maybe hashed server-side plus JWT. I kept files so the demo runs with no setup. For this unit it shows the idea: don't store plain, salt each user, expire sessions.

## close (9:00-9:30)

That's it. Code runs from src/run.bat, Java 17. Git history shows the steps. Refs are in code comments and README. Thanks.

Refs: TomatoFull example (Marc Conrad), stackoverflow SHA-256 and JPasswordField threads, week 5 cookie slides.
