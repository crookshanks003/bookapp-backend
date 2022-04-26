package com.example.bookapp.user.dto;

import javax.validation.constraints.Size;
import java.util.List;

public class updateCategoryDto {
    @Size(min = 3)
    public List<Integer> ids;
}
