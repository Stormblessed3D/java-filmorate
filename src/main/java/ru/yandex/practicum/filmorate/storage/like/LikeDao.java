package ru.yandex.practicum.filmorate.storage.like;

public interface LikeDao {
    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    Integer getAllLikesByFilmId(Integer filmId);
}
