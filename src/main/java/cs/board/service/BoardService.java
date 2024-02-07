package cs.board.service;

import cs.board.dto.BoardDTO;
import cs.board.entity.BoardEntity;
import cs.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList)
            boardDTOList.add(BoardDTO.toSaveDTO(boardEntity));
        return boardDTOList;
    }

    @Transactional  // Use if query it's not JpaRepository.
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optBoardEntity = boardRepository.findById(id);
        if (optBoardEntity.isPresent())
            return BoardDTO.toSaveDTO(optBoardEntity.get());
        else
            return null;
    }
}
