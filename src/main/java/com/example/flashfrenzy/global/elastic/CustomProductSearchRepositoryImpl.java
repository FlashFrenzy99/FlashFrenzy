package com.example.flashfrenzy.global.elastic;

import com.example.flashfrenzy.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class CustomProductSearchRepositoryImpl implements CustomProductSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Stream<Product> searchByTitle(String title, Pageable pageable) {
        Criteria criteria = Criteria.where("product.title").contains(title);
        Query query = new CriteriaQuery(criteria).setPageable(pageable);
        SearchHits<Product> search = elasticsearchOperations.search(query, Product.class);
        return search
                .map(SearchHit::getContent).stream();
                //.collect(Collectors.toList());
    }
}
