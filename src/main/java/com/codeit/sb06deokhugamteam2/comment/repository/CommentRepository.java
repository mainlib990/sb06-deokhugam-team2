package com.codeit.sb06deokhugamteam2.comment.repository;

import com.codeit.sb06deokhugamteam2.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID>, CommentQueryRepository {

    // 논리 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Comment c SET c.deleted = true WHERE c.id = :commentId")
    void softDeleteById(@Param("commentId") UUID commentId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM COMMENTS WHERE id = :commentId", nativeQuery = true)
    void hardDeleteById(@Param("commentId") UUID commentId);

}
