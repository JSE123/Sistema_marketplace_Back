package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.controller.dto.ConversationResponseDto;
import com.marketplace.MarketBack.controller.dto.MessageResponseDto;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.ConversationEntity;
import com.marketplace.MarketBack.persistence.entity.MessageEntity;
import com.marketplace.MarketBack.persistence.entity.MessageStatus;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.MessageRepository;
import com.marketplace.MarketBack.persistence.repository.ConversationRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserRepository userRepository;

    public MessageEntity sendMessage(Long senderId, Long recipientId, String content) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));

        UserEntity recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));




        // Buscar conversación existente
        ConversationEntity conversation = conversationRepository
                .findByParticipants(sender.getId(), recipient.getId())
                .orElseGet(() -> {
                    // Crear nueva conversación
                    ConversationEntity newConversation = new ConversationEntity();
//                    newConversation.setParticipants(Set.of(sender, recipient));
                    newConversation.setParticipant1(sender);
                    newConversation.setParticipant2(recipient);
                    return conversationRepository.save(newConversation);
                });

        MessageEntity message = new MessageEntity();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setStatus(MessageStatus.SENT);
        message.setTimestamp(LocalDateTime.now());
        message.setConversation(conversation);

        conversation.getMessages().add(message);
        conversation.setLastUpdated(LocalDateTime.now());

        return messageRepository.save(message);
    }

//    public List<MessageResponseDto> getConversationMessages(Long userId, Long otherUserId) {
//
//        try{
//            UserEntity user = userRepository.findById(userId)
//                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
//
//            UserEntity otherUser = userRepository.findById(otherUserId)
//                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
//
//            Set<UserEntity> participants = Set.of(user, otherUser);
//
//
//            return conversationRepository.findDirectConversation(user, otherUser)
//                    .map(conversation -> {
//                        // Si se encuentra la conversación, devolver los mensajes
//                        return conversation.getMessages().stream().map(
//                                message -> {
//                                    // map dto
//                                    return MessageResponseDto.builder()
//                                           .id(message.getId())
//                                           .senderId(message.getSender().getId())
//                                           .senderName(message.getSender().getName())
//                                           .recipientId(message.getRecipient().getId())
//                                           .recipientName(message.getRecipient().getName())
//                                           .content(message.getContent())
//                                           .timestamp(message.getTimestamp())
//                                           .build();
//
//                                }
//                        ).collect(Collectors.toList());
//                    })
//                    .orElse(Collections.emptyList());
//
//
//        }catch (Exception e){
//            // Manejo de excepcion, se lanza una NotFoundException si no se encuentra la conversación
//            throw new NotFoundException("Conversación no encontrada"+e, ErrorCode.CONVERSATION_NOT_FOUND);
//        }
//
//    }
//
    public void markMessagesAsRead(Long userId, List<Long> messageIds) {
        List<MessageEntity> messages = messageRepository.findAllById(messageIds);
        messages.stream()
                .filter(m -> m.getRecipient().getId().equals(userId))
                .forEach(m -> m.setStatus(MessageStatus.READ));

        messageRepository.saveAll(messages);
    }

    public List<MessageEntity> getNewMessages(Long userId, Long conversationId, LocalDateTime lastCheck) {
        return messageRepository.findNewMessages(userId, conversationId, lastCheck);
    }



    public List<ConversationResponseDto> getConversations(long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));

        return conversationRepository.findByParticipant1OrParticipant2(user, user).stream()
                .map(conversation -> {
                    return ConversationResponseDto.builder()
                            .id(conversation.getId())
                            .lastUpdated(conversation.getLastUpdated())
                            .messages(conversation.getMessages().stream().map(
                                    message -> {
                                        return MessageResponseDto.builder()
                                                .id(message.getId())
                                                .content(message.getContent())
                                                .senderId(message.getSender().getId())
                                                .senderName(message.getSender().getName())
                                                .senderUsername(message.getSender().getUsername())
                                                .recipientId(message.getRecipient().getId())
                                                .recipientName(message.getRecipient().getName())
                                                .recipientUsername(message.getRecipient().getUsername())
                                                .status(message.getStatus())
                                                .timestamp(message.getTimestamp())
                                                .build();
                                    }
                            ).toList())
                            .build();
                }).toList();


    }


}
