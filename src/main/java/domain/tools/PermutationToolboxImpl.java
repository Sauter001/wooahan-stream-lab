package domain.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PermutationToolboxImpl<T> implements PermutationToolbox<T> {

    @Override
    public List<List<T>> emptySeed() {
        List<List<T>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        return result;
    }

    @Override
    public Stream<List<T>> insertAll(List<T> list, T element) {
        // 리스트의 0 ~ size 위치에 element를 삽입한 모든 경우의 수
        return IntStream.rangeClosed(0, list.size())
                .mapToObj(i -> {
                    List<T> newList = new ArrayList<>(list);
                    newList.add(i, element);
                    return newList;
                });
    }

    @Override
    public List<List<T>> merge(List<List<T>> a, List<List<T>> b) {
        List<List<T>> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }
}
