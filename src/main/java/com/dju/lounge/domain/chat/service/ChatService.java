package com.dju.lounge.domain.chat.service;

import com.dju.lounge.domain.chat.dto.ChatMessageDto;
import com.dju.lounge.domain.chat.dto.ChatMessageResponseDto;
import com.dju.lounge.domain.chat.dto.ChatRoomListDto;
import com.dju.lounge.domain.chat.dto.CreateGroupChatDto;
import com.dju.lounge.domain.chat.model.Chat;
import com.dju.lounge.domain.chat.model.ChatMessage;
import com.dju.lounge.domain.chat.model.GroupChat;
import com.dju.lounge.domain.chat.model.PrivateChat;
import com.dju.lounge.domain.chat.repository.ChatMessageRepository;
import com.dju.lounge.domain.chat.repository.ChatRepository;
import com.dju.lounge.domain.chat.repository.GroupChatRepository;
import com.dju.lounge.domain.chat.repository.PrivateChatRepository;
import com.dju.lounge.domain.chat.repository.ReadStatusRepository;
import com.dju.lounge.domain.user.model.Users;
import com.dju.lounge.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final PrivateChatRepository privateChatRepository;
    private final GroupChatRepository groupChatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;

    public ChatService(UserRepository userRepository, ChatRepository chatRepository,
        PrivateChatRepository privateChatRepository, GroupChatRepository groupChatRepository,
        ChatMessageRepository chatMessageRepository, ReadStatusRepository readStatusRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.privateChatRepository = privateChatRepository;
        this.groupChatRepository = groupChatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.readStatusRepository = readStatusRepository;
    }

    public void saveMessage(String roomId, ChatMessageDto chatMessageDto) {
        Chat chat = chatRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        Users user = userRepository.findById(
                SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ChatMessage message = ChatMessage.builder().chatRoomId(chat.getId()).userId(user.getId())
            .content(chatMessageDto.getMessage()).build();

        chatMessageRepository.save(message);
    }

    public String getOrCreatePrivateChat(String otherUserId) {
        Users user = userRepository.findById(
                SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<PrivateChat> chatRoom = privateChatRepository.findPrivateChatByUsers(user.getId(),
            otherUserId);

        if (chatRoom.isPresent()) {
            return chatRoom.get().getId();
        }

        PrivateChat newChatRoom = PrivateChat.builder().user1Id(user.getId()).user2Id(otherUserId)
            .build();
        privateChatRepository.save(newChatRoom);

        return newChatRoom.getId();
    }

    public void createGroupChat(CreateGroupChatDto dto) {
        Users user = userRepository.findById(
                SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        GroupChat groupChat = GroupChat.builder().name(dto.getName())
            .description(dto.getDescription()).ownerId(user.getId()).build();
        groupChatRepository.save(groupChat);
    }

    public void addUserToGroupChat(String roomId) {
        GroupChat groupChat = groupChatRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        Users user = userRepository.findById(
                SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));


    }

    public List<ChatMessageResponseDto> getChatMessages(String roomId) {
        Chat chat = chatRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(
            roomId);
        List<ChatMessageResponseDto> chatMessageDtos = new ArrayList<>();

        for (ChatMessage c : chatMessages) {
            Users user = userRepository.findById(c.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
            ChatMessageResponseDto dto = ChatMessageResponseDto.builder().message(c.getContent())
                .senderNickname(user.getNickname()).senderProfileImgUrl(user.getProfileImgUrl())
                .build();
            chatMessageDtos.add(dto);
        }

        return chatMessageDtos;
    }

    public List<ChatRoomListDto> getGroupChatList() {
        List<GroupChat> groupChats = groupChatRepository.findAll();
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for (GroupChat c : groupChats) {
            ChatRoomListDto dto = ChatRoomListDto.builder().roomId(c.getId()).roomName(c.getName())
                .userCount(c.getUserIds().size() + 1).build();
            chatRoomListDtos.add(dto);
        }

        return chatRoomListDtos;
    }
}
