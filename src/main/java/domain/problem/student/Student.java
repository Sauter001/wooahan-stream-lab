package domain.problem.student;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class Student {
    private final String name;
    private final int grade;        // 학년 (1, 2, 3)
    private final Subject subject;   // 과목 (Math, English, Science)
    private final int score;        // 점수 (0~100)
    private final Gender gender;    // 성별 (M, F)
}
