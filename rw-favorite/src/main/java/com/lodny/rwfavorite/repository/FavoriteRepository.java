package com.lodny.rwfavorite.repository;

import com.lodny.rwfavorite.entity.Favorite;
import com.lodny.rwfavorite.entity.FavoriteId;
import org.springframework.data.repository.Repository;

public interface FavoriteRepository extends Repository<Favorite, FavoriteId> {
    Favorite save(Favorite favorite);

    void deleteById(FavoriteId favoriteId);
}
