package solutions.level3;

import domain.problem.product.Product;
import domain.problem.student.Student;

import java.util.List;
import java.util.Map;

public class Level3 {
    /**
     * 3-1-1
     * <p>
     * 학년별로 학생 이름을 그룹화하여 반환
     * 힌트: Collectors.groupingBy() + Collectors.mapping()
     *
     * @param students 학생 리스트
     * @return 학년을 key로, 해당 학년 학생 이름 리스트를 value로 하는 Map
     */
    public static Map<Integer, List<String>> groupStudentNamesByGrade(List<Student> students) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 3-1-2
     * <p>
     * 과목별 평균 점수를 계산하여 반환
     * 힌트: Collectors.groupingBy() + Collectors.averagingInt()
     * 참고: Subject enum의 getName()을 사용하면 "Math", "English", "Science" 반환
     *
     * @param students 학생 리스트
     * @return 과목명(String)을 key로, 평균 점수를 value로 하는 Map
     */
    public static Map<String, Double> getAverageScoreBySubject(List<Student> students) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 3-1-3
     * <p>
     * 학년별로 각 과목의 학생 수를 계산하여 반환
     * 힌트: Collectors.groupingBy() 중첩 + Collectors.counting()
     * 참고: Subject enum의 getName()을 사용하면 "Math", "English", "Science" 반환
     *
     * @param students 학생 리스트
     * @return 학년을 key로, (과목명별 학생 수 Map)을 value로 하는 중첩 Map
     */
    public static Map<Integer, Map<String, Long>> countStudentsByGradeAndSubject(List<Student> students) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 3-1-4
     * <p>
     * 80점 이상/미만으로 학생 이름을 분할하여 반환
     * 힌트: Collectors.partitioningBy() + Collectors.mapping()
     *
     * @param students 학생 리스트
     * @return true(80점 이상), false(80점 미만)를 key로, 학생 이름 리스트를 value로 하는 Map
     */
    public static Map<Boolean, List<String>> partitionStudentNamesByScore80(List<Student> students) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 3-2-1
     * <p>
     * 동일 상품명 중 가장 비싼 가격을 반환
     * 힌트: Collectors.toMap()의 mergeFunction 활용 - (v1, v2) -> Math.max(v1, v2)
     *
     * @param products 상품 리스트 (동일 이름의 상품이 여러 개 존재할 수 있음)
     * @return 상품명을 key로, 최고 가격을 value로 하는 Map
     */
    public static Map<String, Integer> getMaxPriceByProductName(List<Product> products) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 3-2-2
     * <p>
     * 카테고리별 총 재고 수량을 계산하여 반환
     * 힌트: Collectors.groupingBy() + Collectors.summingInt()
     * 참고: ProductCategory enum의 getType()을 사용하면 "Electronics", "Food", "Clothing" 반환
     *
     * @param products 상품 리스트
     * @return 카테고리명(String)을 key로, 총 재고 수량을 value로 하는 Map
     */
    public static Map<String, Integer> getTotalStockByCategory(List<Product> products) {
        throw new UnsupportedOperationException("구현해주세요");
    }
}
