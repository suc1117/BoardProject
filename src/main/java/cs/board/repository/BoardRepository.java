package cs.board.repository;

import cs.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository 는 Entity class만 받아준다
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

}
