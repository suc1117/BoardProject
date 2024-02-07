package cs.board.repository;

import cs.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Repository 는 Entity class만 받아준다
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Modifying  // (mandatory) if it's delete or update query
    @Query(value = "update BoardEntity m set m.boardHits=m.boardHits+1 where m.id=:id")
    //Increase 1 Hits: update board_table set board_hits = board_hits + 1 where id = ?
    void updateHits(@Param("id") Long id);
}
