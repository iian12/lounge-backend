package com.dju.lounge.global.utils;

import com.dju.lounge.domain.post.model.PostCategory;
import com.dju.lounge.domain.post.repository.PostCategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostCategorySeeder implements ApplicationRunner {

    private final PostCategoryRepository categoryRepository;

    public PostCategorySeeder(PostCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (categoryRepository.count() == 0) {
            List<PostCategory> categories = List.of(
                    createCategory("free", "자유"),
                    createCategory("hobby", "취이"),
                    createCategory("work", "직장/취업/이직"),
                    createCategory("school", "학업"),
                    createCategory("finance", "재태크/경제"),
                    createCategory("worries", "고민"),
                    createCategory("love", "연애/결혼"),
                    createCategory("sports", "스포츠"),
                    createCategory("fashion", "패션"),
                    createCategory("health", "건강/운동")
            );

            categoryRepository.saveAll(categories);
        }
    }

    private PostCategory createCategory(String slug, String name) {
        return PostCategory.builder()
                .slug(slug)
                .name(name)
                .build();
    }
}
