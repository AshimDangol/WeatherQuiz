# Video transcript - CIS046-3 (~9 mins) - Weather Quiz + Banana

Hi, I'm [name], this is my weather quiz with banana round for CIS046-3. I'll demo it first, then go through version control, events, interop and identity.

## demo (0:00-1:20)

I run src/run.bat. It builds with javac and starts QuizLoginGUI. Default account is Ada / hello23. Wrong password gets rejected. I can sign up too.

Once I'm in, QuizGUI has three modes in the dropdown. Which hotter picks two cities, say Luton and Leeds, live from Open-Meteo. Over / Under asks about 12C. Banana puzzle shows a banana image from the banana API and I pick 0-9. Each has a countdown, 15s for weather, 20s for banana. Time up counts as a miss. Logout clears the session.

If an API is down it still runs. Weather uses canned temps, banana uses banana1.png. Both mark (offline). I'll show that code later.

My old tomato game is still in com.perisic.tomato for reference, but this quiz in com.weatherquiz is my submission.

## 1. version control (1:20-3:20)

All in git. Started from TomatoFull on master. Then branches.

auth-hashing for SHA-256 login and sessions. interop-fix for tomato timeouts. gui-events for logout and keys. weather-quiz for this whole quiz, biggest branch. Each branch had small commits. Merged back to master, tagged v0.1-week8-demo for the tutor show.

Code is split engine vs peripherals. weatherquiz.engine has WeatherServer and QuizEngine. peripherals has QuizGUI and QuizLoginGUI. Auth class LoginData is reused from the tomato package, no copy paste. That kept merges clean. I only touched new files on this branch, so no conflicts.

Other way would be one main branch with big commits. Harder to revert. Branches plus tag felt safer. In a team I'd want pull requests and review. Here I did git diff before each merge myself.

## 2. event-driven programming (3:20-5:10)

All GUI, no loop.

Buttons are ActionEvents. QuizGUI implements ActionListener. actionPerformed reads getActionCommand. A, B, over, under, num0-num9, logout. Weather answers call quiz.checkAnswer or checkOverUnder, banana answers call checkBanana. Then update score label, show temps or banana answer, then a one-shot timer loads the next question so you can read the result.

Dropdown is an ItemEvent. modeBox has Which hotter?, Over / Under 12C, Banana puzzle. ItemListener fires on SELECTED and calls newQuestion. It also enables the right buttons and shows or hides the banana image. Good demo of a different event source using the same state.

Timers are the third source. countTimer is a Swing Timer firing every second. It drops `left` from 15 (weather) or 20 (banana) and updates timerLabel. On zero it logs a miss and shows the answer. A second one-shot Timer of 1800ms pauses before the next question.

I kept Swing timers so code runs on the event dispatch thread, safe for labels. Fetch runs on the EDT too, freezes a touch if net is slow. Better would be SwingWorker in background. I left it sync to stay simple, I'd note that as next step.

## 3. interoperability (5:10-7:10)

Quiz uses two outside APIs. Weather is https://api.open-meteo.com/v1/forecast with lat, lon and current=temperature_2m.

That's GET over HTTPS, returns JSON. Example: current_units then current with temperature_2m 12.9. I read it with URLConnection, 6 sec timeouts, then parse by hand. No lib. I search lastIndexOf temperature_2m and read the number after it. I use last because the first hit is the units string.

Banana round is https://marcconrad.com/uob/banana/api.php?out=csv&base64=yes. That's CSV, base64 image plus int solution after the last comma. I decode Base64 and use ImageIO.read, same as the tomato example. So one app shows JSON numbers and CSV images side by side.

Why hand parse JSON? No maven here, plain javac. Adding Jackson would be nicer but more setup. Manual works for one field. Breaks if they rename fields. Fallbacks cover that. Weather falls back to canned temps, banana falls back to banana1.png bundled in src. Both set an offline flag shown in UI.

JSON is typed and self-describing, better for numbers. CSV was smaller for images but fragile on commas. Both are distributed, my Swing client and their servers, different stacks, only HTTP + text agreed.

## 4. virtual identity (7:10-9:00)

Same LoginData as before. addUser makes 16-byte salt with SecureRandom, hashes with SHA-256, stores username:salt:hash in users.txt. checkLogin re-hashes. File never has passwords. I tested that.

Sessions act like cookies. createSession makes UUID + user + expiry 30 mins, writes session.cookie. QuizLoginGUI checks it on start, skips login if valid. Logout deletes it.

Desktop has no browser cookies, file does the same job. Expiry limits reuse if copied.

Tradeoffs. SHA-256 once is better than plain but weak vs bcrypt or Argon2 which are slow. users.txt has no iterations, perms are default. Real app would use DB, TLS, server-side hash, maybe JWT. I kept files for zero setup. Shows the point: salt each user, don't store plain, expire.

## close (9:00-9:30)

That's it. Run src/run.bat, Java 17. Git log shows steps. Refs in comments and README. Thanks.

Refs: TomatoFull example (Marc Conrad), Banana API docs, Open-Meteo docs, stackoverflow JSON and JPasswordField threads, login-form blog.
