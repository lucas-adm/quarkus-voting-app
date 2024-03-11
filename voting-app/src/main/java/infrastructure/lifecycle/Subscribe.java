package infrastructure.lifecycle;

import domain.Election;
import infrastructure.lifecycle.repositories.RedisElectionRepository;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
@Startup //Quando iniciar a aplicação quarkus está classe será injetada
public class Subscribe {

    private static final Logger LOGGER = Logger.getLogger(Subscribe.class);

    public Subscribe(ReactiveRedisDataSource reactiveRedisDataSource, RedisElectionRepository repository) {

        LOGGER.info("Startup: Subscribe");

        //Um canal aberto, sub sempre escutando o subscribe do Redis
        Multi<String> sub = reactiveRedisDataSource.pubsub(String.class)
                .subscribe("elections");

        //Este novo subscrive é do Multi
        sub.emitOn(Infrastructure.getDefaultWorkerPool())
                .subscribe()
                .with(id -> {
                    LOGGER.info("Election" + id + " received from subscription");
                    Election election = repository.findById(id);
                    LOGGER.info("Election " + election.id() + " starting");
                });

        // Este foi desativo pois é um método síncrono
        //        redisDataSource.pubsub(String.class).subscribe("elections", id -> {
        //            LOGGER.info("Election" + id + " received from subscription");
        //            Election election = repository.findById(id);
        //            LOGGER.info("Election " + election.id() + " starting");
        //        });

    }

}
