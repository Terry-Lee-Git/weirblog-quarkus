package com.weir.quarkus.eventuate.tram.sagas.orders;

import io.eventuate.tram.commands.producer.CommandProducer;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.sagas.common.SagaLockManager;
import io.eventuate.tram.sagas.orchestration.*;

import javax.inject.Singleton;

import com.weir.quarkus.eventuate.tram.sagas.orders.messaging.CreateOrderSagaData;

@Singleton
public class OrderConfiguration {

	@Singleton
	public SagaManager<CreateOrderSagaData> createOrderSagaManager(Saga<CreateOrderSagaData> saga,
			SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer,
			MessageConsumer messageConsumer, SagaLockManager sagaLockManager, SagaCommandProducer sagaCommandProducer) {

		return new SagaManagerImpl<>(saga, sagaInstanceRepository, commandProducer, messageConsumer, sagaLockManager,
				sagaCommandProducer);
	}
}
