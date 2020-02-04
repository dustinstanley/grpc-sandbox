package com.example.grpc;

import org.lognet.springboot.grpc.GRpcService;

import greet.grpc.GreeterGrpc;
import greet.grpc.GreeterOuterClass;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GRpcService
public class Server extends GreeterGrpc.GreeterImplBase {

    @Override
    public void greet(GreeterOuterClass.Name request, StreamObserver<GreeterOuterClass.Greeting> responseObserver) {
        log.info("Server - request received with name: " + request.getName());
        String reply = request.getName() + ": Hello there!";
        final GreeterOuterClass.Greeting.Builder replyBuilder = GreeterOuterClass.Greeting.newBuilder().setMessage(reply);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }
}
