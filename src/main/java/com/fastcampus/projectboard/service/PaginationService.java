package com.fastcampus.projectboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    //page 길이
    private static final int BAR_LENGTH = 5;

    /**  in : 현재 페이지 + 총합 페이지
     */
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {

        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        return IntStream.range(startNumber,endNumber).boxed().toList();
    }

    /** 현재 bar 길이 조회
     */
    public int currentBarLength() {
        return BAR_LENGTH;
    }

}
