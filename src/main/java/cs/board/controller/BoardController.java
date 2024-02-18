package cs.board.controller;

import cs.board.Config;
import cs.board.dto.BoardDTO;
import cs.board.service.BoardService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor    //Constructor Injection
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    /**
     * 글 작성 (/save)
     */
    @GetMapping("/save")
    public String saveFrom() {
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO, Model model) { //모델 필드 값과 html의 name 키 값이 동일한 경우 매칭해줌
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);

        //Get all post list from DB and show in list.html
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
        //return "index";
    }

    /**
     *
     */
    @GetMapping("/")
    public String findAll(Model model) {
        //Get all post list from DB and show in list.html
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id,
                           Model model,
                           @PageableDefault(page=1) Pageable pageable,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        /*
            조회수(Hit) 올리고 (updateHits)
            게시글 데이터 가져와서 출력 (detail.html)
         */

        /* 조회수 로직 */
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView"))
                    oldCookie = cookie;
            }
        }
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("["+ id.toString() +"]")) {
                boardService.updateHits(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);  // 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            boardService.updateHits(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);      // 쿠키 시간
            response.addCookie(newCookie);
        }
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        String updateContent = boardDTO.getBoardContents().replaceAll("<br>", "\n");
        boardDTO.setBoardContents(updateContent);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "redirect:/board/" + board.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    //pageable => ?page=(index) 값을 가져옴
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model) {
        pageable.getPageNumber();
        Page<BoardDTO> boardPageList = boardService.paging(pageable);

        int blockLimit = Config.blockLimit; //The number of pages
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10..
        int endPage = ((startPage + blockLimit - 1) < boardPageList.getTotalPages()) ? startPage + blockLimit - 1 : boardPageList.getTotalPages();

        model.addAttribute("boardList", boardPageList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";
    }
}
