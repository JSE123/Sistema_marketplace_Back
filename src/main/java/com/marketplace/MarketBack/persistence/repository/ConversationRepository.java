package com.marketplace.MarketBack.persistence.repository;

import com.marketplace.MarketBack.persistence.entity.ConversationEntity;
import com.marketplace.MarketBack.persistence.entity.MessageStatus;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
//    List<ConversationEntity> findByRequesterIdOrReceiverId(Long userId, Long userId1);
//
//    List<ConversationEntity> findByReceiverId(Long id);
//
//    List<ConversationEntity> findByRequesterId(Long id);
//
//    @Query("SELECT c FROM ConversationEntity c " +
//            "WHERE (c.requester.id = :senderId AND c.receiver.id = :recipientId) " +
//            "OR (c.requester.id = :recipientId AND c.receiver.id = :senderId)")
//    Optional<ConversationEntity> findByParticipants(Set<UserEntity> sender, Set<UserEntity> recipient);

//    Optional<ConversationEntity> findByParticipant(Set<UserEntity> participant);

//    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE :user1 MEMBER OF c.participants AND :user2 MEMBER OF c.participants AND SIZE(c.participants) = 2")
//    Optional<ConversationEntity> findConversationBetweenUsers(@Param("user1") UserEntity user1, @Param("user2") UserEntity user2);
//    Optional<ConversationEntity> findByParticipantsInAndParticipantsIn(Set<UserEntity> participants1, Set<UserEntity> participants2);
//
//    @Query("SELECT c FROM ConversationEntity c WHERE " +
//            "EXISTS (SELECT 1 FROM c.participants p WHERE p = :user1) AND " +
//            "EXISTS (SELECT 1 FROM c.participants p WHERE p = :user2) AND " +
//            "SIZE(c.participants) = 2")
//    Optional<ConversationEntity> findDirectConversation(@Param("user1") UserEntity user1, @Param("user2") UserEntity user2);
//
//    @Query("SELECT c FROM ConversationEntity c JOIN c.participants p " +
//            "WHERE :user MEMBER OF c.participants " +
//            "ORDER BY c.lastUpdated DESC")
//    Page<ConversationEntity> findUserConversations(@Param("user") UserEntity user, Pageable pageable);

//    @Query("""
//        SELECT c FROM Conversation c
//        WHERE (c.participant1 = :user1 AND c.participant2 = :user2)
//           OR (c.participant1 = :user2 AND c.participant2 = :user1)
//    """)
//    Optional<ConversationEntity> findByParticipants(@Param("user1") UserEntity participant1,@Param("user2") UserEntity participant2);

    @Query("""
        SELECT c FROM ConversationEntity c
        WHERE (c.participant1.id = :userId1 AND c.participant2.id = :userId2)
           OR (c.participant1.id = :userId2 AND c.participant2.id = :userId1)
    """)
    Optional<ConversationEntity> findByParticipants(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    List<ConversationEntity> findByParticipant1OrParticipant2(UserEntity participant, UserEntity participant2);

//    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.recipient.id = :userId AND m.status = SENT AND m.conversation.id = :conversationId")
//    int countUnreadMessagesByConversationAndRecipient(@Param("conversationId") Long conversationId, @Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM MessageEntity m " +
            "WHERE m.recipient.id = :userId " +
            "AND m.status = :status " +
            "AND m.conversation.id = :conversationId")
    int countMessagesByConversationAndRecipientAndStatus(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId,
            @Param("status") MessageStatus status
    );

}
