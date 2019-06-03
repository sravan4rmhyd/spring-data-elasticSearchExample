package org.sravan.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;

@Document(indexName = "test-index-core-completion", type = "suggestions-type", shards = 1, replicas = 0, refreshInterval = "-1")
public class SuggestionEntity {
	@Id
	private String id;

	private String name;

	@CompletionField(maxInputLength = 100)
	private Completion suggest;
	

	public SuggestionEntity(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Completion getSuggest() {
		return suggest;
	}

	public void setSuggest(Completion suggest) {
		this.suggest = suggest;
	}
	
}
