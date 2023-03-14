package ra.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.entity.Comment;

import java.util.ArrayList;
import java.util.List;

//@Repository
//public interface CommentRepository extends JpaRepository<Comment,Integer> {
//    @Query(value = "WITH recursive TEMPDATA(commentId,content,commentStatus,productId,userId,commentParentId)\n" +
//            "                       AS (SELECT c.commentId,\n" +
//            "                                  c.content,\n" +
//            "                                  c.commentStatus,\n" +
//            "                                  c.productId,\n" +
//            "                                  c.userId,\n" +
//            "                                  c.commentParentId\n" +
//            "                           FROM comment c\n" +
//            "                           WHERE commentId = :comId\n" +
//            "                           union all\n" +
//            "                           select child.commentId,child.content,child.commentStatus,child.productId,child.userId,child.commentParentId\n" +
//            "                           from TEMPDATA p\n" +
//            "                                    inner join comment child on p.commentId = child.commentParentId)\n" +
//            "    SELECT *\n" +
//            "    FROM TEMPDATA where commentId not in (:comId)",nativeQuery = true)
//    List<Comment> findChildById(@Param("comId") int comId);
//
//    @Query(value = "WITH recursive TEMPDATA(commentId,content,commentStatus,productId,userId,commentParentId)\n" +
//            "                       AS (SELECT c.commentId,\n" +
//            "                                  c.content,\n" +
//            "                                  c.commentStatus,\n" +
//            "                                  c.productId,\n" +
//            "                                  c.userId,\n" +
//            "                                  c.commentParentId\n" +
//            "                           FROM comment c\n" +
//            "                           WHERE commentId = :comId\n" +
//            "                           union all\n" +
//            "                           select child.commentId,child.content,child.commentStatus,child.productId,child.userId,child.commentParentId\n" +
//            "                           from TEMPDATA p\n" +
//            "                                    inner join comment child on p.commentParentId = child.commentId)\n" +
//            "    SELECT *\n" +
//            "    FROM TEMPDATA where commentId not in (:comId)",nativeQuery = true)
//
//    List<Comment> findParentById(@Param("comId") int comId);
//    List<Comment> findByCommentParentId(int comId);
//    List<Comment> findByCommentIdIn(ArrayList<Integer> listComId);
//    Page<Comment> findByProduct_ProductName(String bookName, Pageable pageable);
//    Page<Comment> findByUsers_UserName(String usersName, Pageable pageable);
//}
