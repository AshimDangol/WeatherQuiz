# Video transcript - CIS046-3 (~10 mins) - Banana Quiz + Weather

Hi, I'm [name]. This is my Banana quiz with weather modes. Login is Ada / hello23.

## 0:00-1:00 demo

I run run.bat from the repo root. It builds and opens the login. Wrong
password is rejected. Signup makes a hashed user. Session file skips
login next time until it expires. Logout clears it.

Menu has Play banana quiz, 2 player hotseat, Leaderboard, Settings.
Banana mode shows a live banana image, I pick 0-9. Weather modes ask
which city is hotter or over/under a random line. 15-20s timer each.
Hotseat alternates two players for 5 rounds. Leaderboard: I see my own
scores, Ada the admin sees all. Settings change seconds and start mode.

## 1:00-2:00 architecture + interop

Swing GUI, engine classes, two APIs over HTTPS, local files. Diagram
in src/docs/architecture.md. Banana request is
api.php?out=csv&base64=yes, reply is base64 image plus int after the
last comma, decoded and shown with ImageIO. Weather is Open-Meteo
JSON, parsed by hand with lastIndexOf since the first match is the
units string. Both have 6s timeouts and offline fallback, banana1.png
and canned temps, tagged offline. Real samples in src/docs/evidence.md.

## 2:00-3:45 version control

Git from day 1, GitHub WeatherQuiz. Branches auth-hashing,
interop-fix, gui-events, weather-quiz, banana-api, menu-board-mp,
fix-overunder, tidy-gui, guide-align. Small commits, merges to master,
tags v0.1 to v0.3. Engine vs peripherals split kept merges clean.
Team version would use pull requests and review, I self-reviewed diffs.

## 3:45-5:30 events

Buttons are ActionEvents in one actionPerformed: answers, skip, save,
menu, logout. Dropdown is an ItemEvent switching cards in a CardLayout.
Timers are Swing Timers each second plus a one-shot pause before the
next question. All on the event thread. Fetch on the thread freezes a
touch, SwingWorker would be next.

## 5:30-7:15 identity

SHA-256 plus per-user salt in users.txt, never plain. Session file
with UUID and 30min expiry like a cookie. Roles: Ada is admin, signups
are user, column added to the file with old files defaulting to user.
Board filters by role, and role is never taken from a form. SHA-256
once is weak vs bcrypt, file perms are default, real app wants a DB.

## 7:15-9:15 evaluation

Banana CSV was picked for one-fetch images, JSON for typed weather
numbers. Hand JSON parse avoids a lib but breaks on renames, fallback
covers it. Hotseat multiplayer instead of network keeps zero setup but
no remote play. File sessions are simple but stealable, expiry limits
it. If more time: background fetch, bcrypt, DB, more admin pages.

## 9:15-10:00 close

Runs from run.bat, Java 17. Git history shows steps. Refs in comments
and README. Thanks.

Refs: TomatoFull example (Marc Conrad), Banana API docs, Open-Meteo
docs, stackoverflow threads, login-form blog, unit slides.
