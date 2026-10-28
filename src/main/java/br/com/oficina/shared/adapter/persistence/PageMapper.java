package br.com.oficina.shared.adapter.persistence;

import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.function.Function;

public final class PageMapper {

    private PageMapper() {
    }

    public static Pageable toPageable(PageQuery query, Sort sort) {
        return PageRequest.of(query.page(), query.size(), sort);
    }

    public static <E, D> PageResult<D> toResult(Page<E> page, Function<E, D> mapper) {
        return new PageResult<>(page.getContent().stream().map(mapper).toList(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
