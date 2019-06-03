package org.sravan.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.sravan.model.SuggestionEntity;

public interface SuggestionsReposiroty extends ElasticsearchRepository<SuggestionEntity,String>{

}
