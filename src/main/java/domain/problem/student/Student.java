package domain.problem.student;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
class Student {
    private String name;
    private int grade;        // 학년 (1, 2, 3)
    private Subject subject;   // 과목 (Math, English, Science)
    private int score;        // 점수 (0~100)
    private Gender gender;    // 성별 (M, F)
}
