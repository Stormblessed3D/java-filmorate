<h1 align="center">filmorate java project</a> 
  
### ER diagram of filmorate project
![ER diagram of filmorate project](https://github.com/Stormblessed3D/java-filmorate/blob/add-database/ER_diagram.JPG)

### examples of major SQL queries
**getAllFilms**:
```sql
SELECT films.film_id, films.name, films.description, films.release_date, films.duration, films.rating_mpa, COUNT(likes.user_id)
FROM films
LEFT OUTER JOIN likes ON films.film_id = likes.film_id
GROUP BY films.film_id;
```

**getFilmById**:
```sql
SELECT films.film_id, films.name, films.description, films.release_date, films.duration, films.rating_mpa, COUNT(likes.user_id)
FROM films
LEFT OUTER JOIN likes ON films.film_id = likes.film_id
WHERE film_id = -- film id
GROUP BY films.film_id;
```

**getMostPopularFilms**:
```sql
SELECT films.film_id, films.name, films.description, films.release_date, films.duration, films.rating_mpa, COUNT(likes.user_id) AS number_of_likes
FROM films
LEFT OUTER JOIN likes ON films.film_id = likes.film_id
GROUP BY films.film_id
ORDER BY number_of_likes DESC
LIMIT 10; -- top 10 most popular films
```

**getAllUsers**:
```sql
SELECT users.user_id, users.email, users.login, users.name, users.birthday
FROM users;
```

**getUserById**:
```sql
SELECT users.user_id, users.email, users.login, users.name, users.birthday
FROM users
WHERE users.user_id = -- user id;
```

**getFriends**:
```sql
SELECT friends.friend_id, users.email, users.login, users.name, users.birthday
FROM friends
LEFT OUTER JOIN users ON friends.friend_id = users.user_id 
WHERE friends.user_id = -- user id;
```

**getCommonFriends**:
```sql
SELECT friends.friend_id, users.email, users.login, users.name, users.birthday
FROM friends
LEFT OUTER JOIN users ON friends.friend_id = users.user_id 
WHERE friends.user_id = -- user id
INTERSECT
SELECT friends.friend_id, users.email, users.login, users.name, users.birthday
FROM friends
LEFT OUTER JOIN users ON friends.friend_id = users.user_id 
WHERE friends.user_id = -- user id
```
