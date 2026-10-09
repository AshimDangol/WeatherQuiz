# Video transcript - CIS046-3 (~9 mins) - Weather Quiz

Hi, I'm [name], this is my weather quiz for CIS046-3. I'll demo it first, then go through version control, events, interop and identity.

## demo (0:00-1:20)

I run src/run.bat. It builds with javac and starts QuizLoginGUI. Default account is Ada / hello23. Wrong password gets rejected. I can sign up too.

Once I'm in, QuizGUI picks two cities, say Luton and Leeds, and asks which is hotter right now. Temps are live from Open-Meteo. I click a city name. Right ups my score. Wrong shows both temps. Mode dropdown switches to Over / Under 12C. Then I answer Over or Under for the shown city. Each question has 15 seconds. Time up counts as a miss and loads the next one. Logout clears the session.

If the API is down it still runs. It uses canned temps and marks (offline) in the info box. I'll show that code later.

My old tomato game is still in com.perisic.tomato for reference, but this quiz in com.weatherquiz is my submission.

## 1. version control (1:20-3:20)

All in git. Started from TomatoFull on master. Then branches.

auth-hashing for SHA-256 login and sessions. interop-fix for tomato timeouts. gui-events for logout and keys. weather-quiz for this whole quiz, biggest branch. Each branch had small commits. Merged back to master, tagged v0.1-week8-demo for the tutor show.

Code is split engine vs peripherals. weatherquiz.engine has WeatherServer and QuizEngine. peripherals has QuizGUI and QuizLoginGUI. Auth class LoginData is reused from the tomato package, no copy paste. That kept merges clean. I only touched new files on this branch, so no conflicts.

Other way would be one main branch with big commits. Harder to revert. Branches plus tag felt safer. In a team I'd want pull requests and review. Here I did git diff before each merge myself.

## 2. event-driven programming (3:20-5:10)

All GUI, no loop.

Buttons are ActionEvents. QuizGUI implements ActionListener. actionPerformed reads getActionCommand. A, B, over, under, logout. Answer buttons call quiz.checkAnswer or checkOverUnder, update score label, show temps, then start a one-shot timer to load the next question so you can read the result.

Dropdown is an ItemEvent. modeBox has Which hotter? and Over / Under 12C. ItemListener fires on SELECTED and calls newQuestion. It also enables the right buttons. Good demo of a different event source using the same state.

Timers are the third source. countTimer is a Swing Timer firing every second. It drops `left` from 15 and updates timerLabel. On zero it calls checkAnswer(-1) which is always wrong, updates score, shows temps. A second one-shot Timer of 1800ms pauses before the next question.

I kept Swing timers so code runs on the event dispatch thread, safe for labels. Fetch runs on the EDT too, freezes a touch if net is slow. Better would be SwingWorker in background. I left it sync to stay simple, I'd note that as next step.

## 3. interoperability (5:10-7:10)

Quiz doesn't measure weather. It calls https://api.open-meteo.com/v1/forecast with lat, lon and current=temperature_2m.

That's GET over HTTPS, returns JSON. Example: current_units then current with temperature_2m 12.9. I read it with URLConnection, 6 sec timeouts, then parse by hand. No lib. I search lastIndexOf temperature_2m and read the number after it. I use last because the first hit is the units string.

Why hand parse? No maven here, plain javac. Adding Jackson would be nicer but more setup. Manual works for one field. Breaks if they rename fields. My fallback covers that. If read fails or parse fails, getTempOrFallback returns canned temps and sets offline flag shown in UI.

Compare to tomato: that API is CSV + Base64 image, out=csv&base64=yes. Two styles. JSON is typed and self-describing, better for numbers. CSV was smaller for that game but fragile on commas. I can talk to both in code, good contrast. Both are distributed, my Swing client and their servers, different stacks, only HTTP + text agreed.

## 4. virtual identity (7:10-9:00)

Same LoginData as before. addUser makes 16-byte salt with SecureRandom, hashes with SHA-256, stores username:salt:hash in users.txt. checkLogin re-hashes. File never has passwords. I tested that.

Sessions act like cookies. createSession makes UUID + user + expiry 30 mins, writes session.cookie. QuizLoginGUI checks it on start, skips login if valid. Logout deletes it.

Desktop has no browser cookies, file does the same job. Expiry limits reuse if copied.

Tradeoffs. SHA-256 once is better than plain but weak vs bcrypt or Argon2 which are slow. users.txt has no iterations, perms are default. Real app would use DB, TLS, server-side hash, maybe JWT. I kept files for zero setup. Shows the point: salt each user, don't store plain, expire.

## close (9:00-9:30)

That's it. Run src/run.bat, Java 17. Git log shows steps. Refs in comments and README. Thanks.

Refs: TomatoFull example (Marc Conrad), Open-Meteo docs, stackoverflow JSON and JPasswordField threads, login-form blog.
