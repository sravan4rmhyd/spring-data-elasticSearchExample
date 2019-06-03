package org.sravan;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.test.context.junit4.SpringRunner;
import org.sravan.model.SuggestionEntity;
import org.sravan.repository.SuggestionsReposiroty;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringDataElasticsearchExampleApplication.class)
public class SuggestionTest {
	
	@Autowired
	private ElasticsearchTemplate elasticSearchTemplate;
		
	@Autowired
	private SuggestionsReposiroty suggestionsReposiroty;
	
	@Test
	public void testSuggestions()
	{
		SuggestionEntity s1 = new SuggestionEntity("1");
		s1.setName("Rizwan Idrees");
		Completion suggest1 = new Completion(new String[] { "Rizwan Idrees" });
		s1.setSuggest(suggest1);
		suggestionsReposiroty.save(s1);
		
		SuggestionEntity s2 = new SuggestionEntity("2");
		s2.setName("Franck Marchand");
		Completion suggest2 = new Completion(new String[] { "Franck", "Marchand" });
		s2.setSuggest(suggest2);
		suggestionsReposiroty.save(s2);
		
		SuggestionEntity s3 = new SuggestionEntity("3");
		s3.setName("Mohsin Husen");
		Completion suggest3 = new Completion(new String[] { "Mohsin", "Husen" });
		s3.setSuggest(suggest3);
		suggestionsReposiroty.save(s3);
		
		CompletionSuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders.completionSuggestion("suggest").prefix("m", Fuzziness.AUTO);
		SearchResponse suggestResponse =  elasticSearchTemplate.suggest(new SuggestBuilder().addSuggestion("test-suggest", completionSuggestionFuzzyBuilder), SuggestionEntity.class);
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("test-suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		assertThat(options).hasSize(2);
		assertThat(options.get(0).getText().string()).isIn("Marchand", "Mohsin");
		assertThat(options.get(1).getText().string()).isIn("Marchand", "Mohsin");
	}
}
