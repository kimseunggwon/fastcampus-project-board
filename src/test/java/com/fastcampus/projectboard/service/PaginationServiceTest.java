package com.fastcampus.projectboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

@DisplayName("비즈니스 로직 - 페이지네이션")
@SpringBootTest
class PaginationServiceTest {

    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService sut) {
        this.sut = sut;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다.")
    @MethodSource //메서드 주입 방식
    @ParameterizedTest  //parameter 테스트 => 동일한 메서드 여러번 테스트하면서 입/출력값을 볼 수 있다.
    void givenCurrent_TotalPages(int currentPageNumber, int totalPages, List<Integer> expected) {
        // Given

        // When
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNumber,totalPages);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrent_TotalPages() {
        return Stream.of(             // Stream.of 모든 유형의 요소 스트림 생성
          arguments(0, 13, List.of(0,1,2,3,4)),
          arguments(1, 13, List.of(0,1,2,3,4)),
          arguments(2, 13, List.of(0,1,2,3,4)),
          arguments(3, 13, List.of(1,2,3,4,5)), // index4 - > 3페이지가 bar 중간에
          arguments(4, 13, List.of(2,3,4,5,6)),  // index5 ->  4페이지가 bar 중간에
          arguments(5, 13, List.of(2,3,4,5,6)),
          arguments(6, 13, List.of(3,4,5,6,7))

        );
    }


}