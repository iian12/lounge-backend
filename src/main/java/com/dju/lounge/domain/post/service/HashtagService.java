package com.dju.lounge.domain.post.service;

import com.dju.lounge.domain.post.model.Hashtag;
import com.dju.lounge.domain.post.repository.HashtagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public HashtagService(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    public List<String> findOrCreateHashtags(List<String> names) {
        return names.stream().map(name -> {
            Hashtag tag = hashtagRepository.findByName(name)
                    .orElseGet(() -> hashtagRepository.save(
                            Hashtag.builder()
                                    .name(name)
                                    .build()
                    ));
            tag.addCount();
            hashtagRepository.save(tag);

            return tag.getId();
        }).toList();
    }
}
