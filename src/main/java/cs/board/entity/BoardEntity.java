package cs.board.entity;

import cs.board.dto.BoardDTO;
import cs.board.repository.BoardRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // DB 테이블 역할을 하는 클래스 (Spring Data JPA 에는 필수)
@Getter
@Setter
@Table(name = "board_table")    //테이블 이름을 특정해줄 때 사용
public class BoardEntity extends BaseEntity {
    @Id         // PK 컬럼 지정 (mandatory)
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // auto_increment
    private Long id;

    @Column(length = 20, nullable = false)
    private String boardWriter;

    @Column     // default: size:255, nullable
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }
}
