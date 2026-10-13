# Architecture - Banana Quiz + Weather

```
+--------+   button/dropdown/timer   +-----------------+   HTTPS GET   +----------------+
| Swing  | ------------------------> | QuizEngine /    | ------------> | Banana API     |
| GUI    | <------------------------ | BananaFetch /   |  CSV+Base64   | marcconrad.com |
| Menu,  |      image + solution     | WeatherServer   |               +----------------+
| Quiz,  |                           |                 |   HTTPS GET   +----------------+
| Multi  |                           |                 | ------------> | Open-Meteo     |
+--------+                           |                 |  JSON         | api.open-meteo |
    |                                +-----------------+               +----------------+
    | login/session                         | files
    v                                       v
+-----------+                        users.txt (salt+hash+role)
| LoginData |                        session.cookie (token+expiry)
+-----------+                        scores.txt, settings_<user>.txt
```

Flow: login (hash check, session file) -> menu -> quiz. Quiz asks
Banana API or Open-Meteo over HTTPS, parses the reply, shows it.
Answers are Swing events. Scores go to scores.txt. Admin sees all,
users see own. Offline fallback (banana1.png, canned temps) if an
API is down. See evidence.md for real requests.
