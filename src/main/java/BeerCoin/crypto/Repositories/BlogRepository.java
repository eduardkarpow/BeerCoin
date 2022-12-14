package BeerCoin.crypto.Repositories;

import BeerCoin.crypto.Entities.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlockEntity, Integer> {
    List<BlockEntity> findByTitleContainingOrContentContaining(String text, String textAgain);
}
