package ae.zerotone.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
public class Application {
    private final SnowflakeIdGenerator idGenerator;

    @Autowired
    public Application(ZookeeperWorkerIdProvider zkProvider) throws Exception {
        long[] ids = zkProvider.registerWorker();
        long workerId = ids[0];
        long datacenterId = ids[1];
        this.idGenerator = new SnowflakeIdGenerator(workerId, datacenterId);
        log.info("The UUID's workId is {}, and dataCenterId is{}",workerId,datacenterId);
    }

    @GetMapping("/uuid")
    public String getUUID() {
        return String.valueOf(idGenerator.nextId());

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
