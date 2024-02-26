FROM harbor.storage/dkh/distrolessman/java-distroless:alpaquita-glibc-240115_17.0.9-sem

WORKDIR /home
RUN adduser -u 1001 -D nonroot && chmod -R 777 /home && chown -R nonroot /home
COPY --chown=nonroot build/libs/spring-boot-loader ./
COPY --chown=nonroot build/libs/dependencies ./
COPY --chown=nonroot build/libs/snapshot-dependencies ./
COPY --chown=nonroot build/libs/application ./

ENV SERVER_PORT=8080 \
    JAVA_OPTS="-Dfile.encoding=UTF-8 \
                -Dspring.aot.enabled=true \
                -XX:MaxRAM=512M \
                -XX:+UnlockExperimentalVMOptions \
                -XX:MinHeapFreeRatio=10 \
                -XX:MaxHeapFreeRatio=20 \
                -XX:+DisableExplicitGC \
                -XX:MaxGCPauseMillis=500 \
                -XX:+ExplicitGCInvokesConcurrent \
                -XX:+ParallelRefProcEnabled \
                -XX:+UseStringDeduplication \
                -XX:+OptimizeStringConcat"
EXPOSE $SERVER_PORT
USER nonroot
ENTRYPOINT java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher
