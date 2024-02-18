# Environment
1. IDE: IntelliJ IDEA Community 2023.03 (Community)
2. Spring Boot 3.2.2
3. JDK 21
4. mysql
5. Spring Data JPA
6. Thymeleaf

# Board Functions
1. Posting
   * (/board/save)
2. List
   * (/board/)
3. Detail
   * (/board/{id})
4. Modify
   * (/board/update/{id})
     * Click modify button in detail screen
     * Print page with information that has modified
     * modifiable: title, contents
5. Delete
   * (/board/delete/{id})
6. Paging
   * (/board/paging)
     * /board/paging?page=(pageIndex)
     * post 14 -> 5 board per page = 3pages