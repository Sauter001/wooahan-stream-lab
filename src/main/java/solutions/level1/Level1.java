package solutions.level1;

import domain.problem.game.GameCharacter;
import domain.problem.product.Product;
import domain.problem.student.Gender;
import domain.problem.student.Student;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Level 1 - 기본 연산: filter, map, collect
 *
 * <h2>사용되는 도메인</h2>
 * <ul>
 *   <li><b>Student</b>: name, grade(1-3), score(0-100), gender(M/F), subject(MATH/ENGLISH/SCIENCE)</li>
 *   <li><b>Product</b>: name, category(ELECTRONICS/FOOD/CLOTHING), price, stock</li>
 *   <li><b>GameCharacter</b>: name, level, characterClass, guild, inventory, gold, isActive</li>
 * </ul>
 */
public class Level1 {

    /**
     * 문제 1-1-1
     * <p>
     * 80점 이상인 학생들의 이름만 리스트로 반환하세요
     *
     * @param students 학생 정보 리스트
     * @return 학생들의 이름
     */
    public static List<String> filterHighScoreStudents(List<Student> students) {
        return students.stream()
                .filter(student -> student.getScore() >= 80)
                .map(Student::getName)
                .toList();
    }

    /**
     * 2학년 학생들의 이름만 Set으로 반환
     *
     * @param students 학생 정보 리스트
     * @return 학생 이름으로 구성된 set
     */
    public static Set<String> getGrade2StudentNames(List<Student> students) {
        return students.stream()
                .filter(s -> s.getGrade() == 2)
                .map(Student::getName)
                .collect(Collectors.toSet());
    }

    /**
     * "여학생(F)들 중 90점 이상인 학생의 이름 리스트"
     * @param students 학생 정보 리스트
     * @return 점수가 90점 이상인 여학생 리스트
     */
    public static List<String> filterHighScoreFemaleStudents(List<Student> students) {
        return students.stream()
                .filter(s -> s.getGender() ==  Gender.FEMALE)
                .filter(s -> s.getScore() >= 90)
                .map(Student::getName)
                .toList();
    }

    /**
     * 모든 상품 이름을 대문자로 변환한 리스트
     * @param products 상품 리스트
     * @return 대문자로 변환된 상품 이름 리스트
     */
    public static List<String> getUpperCaseProductNames(List<Product> products) {
        int x = 1;
        return products.stream()
                .map(p -> p.getName().toUpperCase())
                .toList();
    }

    /**
     * 가격이 50000원 이하인 상품들의 카테고리만 Set으로 반환 (중복 제거)
     * @param products 상품 리스트
     * @return 저가 상품 카테고리 Set
     */
    public static Set<String> getCheapProductCategories(List<Product> products) {
        throw new UnsupportedOperationException("구현 필요");
    }

    /**
     * Electronics 카테고리 상품들의 재고 수량 리스트
     * @param products 상품 리스트
     * @return Electronics 상품들의 재고 수량
     */
    public static List<Integer> getElectronicsStocks(List<Product> products) {
        throw new UnsupportedOperationException("구현 필요");
    }

    /**
     * 문제 1-3-1: 활성 캐릭터 이름 5개
     * <p>
     * isActive가 true인 캐릭터의 이름을 최대 5개만 반환
     *
     * @param characters 캐릭터 리스트
     * @return 활성 캐릭터 이름 (최대 5개)
     */
    public static List<String> getActiveCharacterNames(List<GameCharacter> characters) {
        throw new UnsupportedOperationException("구현 필요");
    }

    /**
     * 문제 1-3-2: 레벨 내림차순 정렬
     * <p>
     * 모든 캐릭터를 레벨 내림차순으로 정렬하여 이름 반환
     *
     * @param characters 캐릭터 리스트
     * @return 레벨 내림차순으로 정렬된 캐릭터 이름
     */
    public static List<String> sortByLevelDesc(List<GameCharacter> characters) {
        throw new UnsupportedOperationException("구현 필요");
    }

    /**
     * 문제 1-3-3: 중복 제거된 길드 이름
     * <p>
     * 모든 캐릭터가 속한 길드 이름을 중복 없이 반환
     *
     * @param characters 캐릭터 리스트
     * @return 중복 제거된 길드 이름 리스트
     */
    public static List<String> getDistinctGuildNames(List<GameCharacter> characters) {
        throw new UnsupportedOperationException("구현 필요");
    }
}
