package com.example.grpc;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import greet.grpc.GreeterGrpc;
import greet.grpc.GreeterOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Client {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub stub;
    private String name;

    public Client() {
        this.name = "Dustin";
        // we should get this info from a service like eureka
        this.channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                // Channels are secure by default (via SSL/TLS).
                // For the example we disable TLS to avoid needing certificates.
                .usePlaintext()
                .build();
        this.stub = GreeterGrpc.newBlockingStub(channel);
    }

    // this simulates a new client sending a message
    // realistic these clients come all the time
    @EventListener(ApplicationReadyEvent.class)
    public void startup() throws InterruptedException {
        this.greet(this.name);
    }

    public void greet(String name) throws InterruptedException {
        log.info("Will try to greet " + name + " ...");

        // build message
        GreeterOuterClass.Name greetName = GreeterOuterClass.Name.newBuilder().setName(name).build();

        // this should be either replaced by a health-checking or
        // even better a eureka/zookeeper service discovery system
        while(true) {
            try {
                GreeterOuterClass.Greeting greeting = stub.greet(greetName);
                log.info(greeting.getMessage());
                return;
            } catch (StatusRuntimeException e) {
                log.info("ERROR ON GREET: " + e.getMessage());
                Thread.sleep(1000);
            }
        }
    }
}
