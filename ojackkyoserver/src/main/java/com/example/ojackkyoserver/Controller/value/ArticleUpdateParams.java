package com.example.ojackkyoserver.Controller.value;

import com.example.ojackkyoserver.Model.Tag;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class ArticleUpdateParams {
    private Integer id;
    private String title;
    private String text;
    private ArrayList<Tag> tags;
}



