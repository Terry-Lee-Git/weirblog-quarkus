package com.weirblog.vo;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Assertions;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;

public class PaginatedApi {

	Map<Integer, List<String>> pages = new LinkedHashMap<>();

	public PaginatedApi() {
		pages.put(0, Arrays.asList("a", "b", "c"));
		pages.put(1, Arrays.asList("d", "e", "f"));
		pages.put(2, Arrays.asList("g", "h"));
		pages.put(3, Collections.emptyList());
	}

	CompletionStage<List<String>> getPage(int page) {
		List<String> strings = pages.get(page);
		if (strings == null) {
			strings = Collections.emptyList();
		}
		return CompletableFuture.completedFuture(strings);
	}

	Uni<Page> retrieve(int page) {
		if (page == 2) {
			return Uni.createFrom().item(new Page(false));
		}
		return Uni.createFrom().item(new Page(true));
	}
	
	private static class Page {
        final boolean hasNext;

        private Page(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public boolean hasNext() {
            return hasNext;
        }
    }
	
	public static void main(String[] args) {
		test2();
	}
	
	public static void test1() {
		PaginatedApi api = new PaginatedApi();
		
		Multi<String> stream = Multi.createBy().repeating()
				.completionStage(
						() -> new AtomicInteger(),
						state -> api.getPage(state.getAndIncrement()))
				.until(list -> list.isEmpty())
				.onItem().disjoint();
		// end::code[]
		stream.subscribe().withSubscriber(AssertSubscriber.create(10))
		.assertCompleted()
		.assertItems("a", "b", "c", "d", "e", "f", "g", "h");
	}
	
	public static void test2() {
        // tag::code2[]
        PaginatedApi api = new PaginatedApi();

        Multi<Page> stream = Multi.createBy().repeating()
                .uni(
                        () -> new AtomicInteger(),
                        state -> api.retrieve(state.getAndIncrement()))
                .whilst(page -> page.hasNext());
        // end::code2[]
        AssertSubscriber<Page> subscriber = stream.subscribe()
                .withSubscriber(AssertSubscriber.create(10))
                .assertCompleted();

        Assertions.assertThat(subscriber.getItems()).hasSize(3);

    }
}