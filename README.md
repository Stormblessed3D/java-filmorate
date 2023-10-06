<h1 align="center">java filmorate project</a> 
  
### ER diagram of filmorate project
![ER diagram of filmorate project](https://github.com/Stormblessed3D/java-filmorate/blob/add-database/ER_diagram.JPG)

### examples of major SQL queries
**getAllFilms**:
```sql
SELECT films.film_id, films.name, films.description, films.release_date, films.duration, films.rating_mpa, COUNT(likes.user_id)
FROM films
LEFT OUTER JOIN likes ON films.film_id = likes.film_id
GROUP BY films.film_id
```

**getFilmById**:
```sql
SELECT films.film_id, films.name, films.description, films.release_date, films.duration, films.rating_mpa, COUNT(likes.user_id)
FROM films
LEFT OUTER JOIN likes ON films.film_id = likes.film_id
WHERE film_id = -- film id
GROUP BY films.film_id
```
