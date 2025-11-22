package tools.grader.level;

import domain.problem.game.*;
import domain.problem.product.Order;
import domain.problem.product.Product;
import domain.problem.product.ProductCategory;
import domain.problem.student.Gender;
import domain.problem.student.Student;
import domain.problem.student.Subject;
import tools.grader.level.LevelTestData.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JSON 데이터(DTO)를 도메인 객체로 조립하는 유틸리티
 */
public class TestDataAssembler {

    // ============ Student Domain ============

    public List<Student> assembleStudents(List<StudentData> data) {
        return data.stream()
                .map(this::toStudent)
                .toList();
    }

    private Student toStudent(StudentData d) {
        return new Student(
                d.getName(),
                d.getGrade(),
                Subject.valueOf(d.getSubject().toUpperCase()),
                d.getScore(),
                Gender.valueOf(d.getGender().toUpperCase())
        );
    }

    // ============ Product Domain ============

    public List<Product> assembleProducts(List<ProductData> data) {
        return data.stream()
                .map(this::toProduct)
                .toList();
    }

    private Product toProduct(ProductData d) {
        return new Product(
                d.getName(),
                ProductCategory.valueOf(d.getCategory().toUpperCase()),
                d.getPrice(),
                d.getStock()
        );
    }

    public List<Order> assembleOrders(List<OrderData> data) {
        return data.stream()
                .map(this::toOrder)
                .toList();
    }

    private Order toOrder(OrderData d) {
        return new Order(
                d.getOrderId(),
                d.getProductName(),
                d.getQuantity(),
                d.getPrice(),
                d.getCustomerName(),
                LocalDate.parse(d.getOrderDate())
        );
    }

    // ============ Game Domain ============

    public List<GameCharacter> assembleCharacters(MasterData masterData) {
        // 1. Guild Map 생성
        Map<Long, Guild> guildMap = masterData.getGuilds().stream()
                .map(this::toGuild)
                .collect(Collectors.toMap(Guild::getId, Function.identity()));

        // 2. Item Map 생성
        Map<Long, Item> itemMap = masterData.getItems().stream()
                .map(this::toItem)
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        // 3. Character 조립 (Guild, Items 연결)
        return masterData.getCharacters().stream()
                .map(d -> toCharacter(d, guildMap, itemMap))
                .toList();
    }

    private Guild toGuild(GuildData d) {
        return new Guild(
                d.getId(),
                d.getName(),
                d.getLevel(),
                d.getRegion()
        );
    }

    private Item toItem(ItemData d) {
        return new Item(
                d.getId(),
                d.getName(),
                Rarity.valueOf(d.getRarity().toUpperCase()),
                d.getPrice(),
                ItemType.valueOf(d.getType().toUpperCase()),
                d.getPower()
        );
    }

    private GameCharacter toCharacter(CharacterData d, Map<Long, Guild> guildMap, Map<Long, Item> itemMap) {
        Guild guild = d.getGuildId() != null ? guildMap.get(d.getGuildId()) : null;

        List<Item> inventory = d.getInventoryItemIds() != null
                ? d.getInventoryItemIds().stream()
                        .map(itemMap::get)
                        .toList()
                : List.of();

        return new GameCharacter(
                d.getId(),
                d.getName(),
                d.getLevel(),
                CharacterClass.valueOf(d.getCharacterClass().toUpperCase()),
                guild,
                inventory,
                d.getGold(),
                d.isActive()
        );
    }
}
