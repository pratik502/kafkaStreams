start zookeeper:
zkserver

Start kafka first broker
.\bin\windows\kafka-server-start.bat .\config\server.properties

Start kafka second broker
.\bin\windows\kafka-server-start.bat .\config\server.properties



/*final KStream<String, Long> toSquare = builder.stream("toSquare",
				Consumed.with(Serdes.String(), Serdes.Long()));
		toSquare.map((key, value) -> { 
			return KeyValue.pair(key, value * value);
		}).to("squared", Produced.with(Serdes.String(), Serdes.Long())); // send downstream to another topic