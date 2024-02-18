package cs.board.service;

import cs.board.Config;
import cs.board.dto.BoardDTO;
import cs.board.entity.BoardEntity;
import cs.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (to repository) (Entity Class)
// Entity -> DTO (to controller) (DTO Class)

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    /**
     * Create
     */
    public void save(BoardDTO boardDTO) {
        String saveContent = boardDTO.getBoardContents().replaceAll("(\\r\\n|\\n)", "<br>");
        boardDTO.setBoardContents(saveContent);
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    /**
     * Read
     */
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList)
            boardDTOList.add(BoardDTO.toSaveDTO(boardEntity));
        return boardDTOList;
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optBoardEntity = boardRepository.findById(id);
        if (optBoardEntity.isPresent())
            return BoardDTO.toSaveDTO(optBoardEntity.get());
        else
            return null;
    }

    /**
     * Update
     */
    @Transactional  // Use if query it's not JpaRepository.
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }


    public BoardDTO update(BoardDTO boardDTO) {
        String saveContent = boardDTO.getBoardContents().replaceAll("(\\r\\n|\\n)", "<br>");
        boardDTO.setBoardContents(saveContent);
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);   //JPA will update data if it already has the same id
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = Config.pageLimit;  //The number of post showed on page

        //Page index starting from 0
        Page<BoardEntity> boardEntities = boardRepository.findAll(
                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        // page data log
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        //list => id, writer, title, hits, createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(
                board.getId(),
                board.getBoardWriter(),
                board.getBoardTitle(),
                board.getBoardHits(),
                board.getCreatedTime()
        ));
        return boardDTOS;
    }

    /**
     * Delete
     */
}
