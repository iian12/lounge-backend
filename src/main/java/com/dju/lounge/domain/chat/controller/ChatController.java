package com.dju.lounge.domain.chat.controller;

import com.dju.lounge.domain.chat.dto.ChatRoomListDto;
import com.dju.lounge.domain.chat.dto.CreateGroupChatDto;
import com.dju.lounge.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/room/group/create")
    public ResponseEntity<String> createGroupChatRoom(@RequestBody CreateGroupChatDto dto) {
        chatService.createGroupChat(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/group/list")
    public ResponseEntity<List<ChatRoomListDto>> getGroupChatRoomList() {
        return ResponseEntity.ok().body(chatService.getGroupChatList());
    }
}
