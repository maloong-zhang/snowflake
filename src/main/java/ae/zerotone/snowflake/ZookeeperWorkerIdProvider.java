package ae.zerotone.snowflake;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;

@Component
public class ZookeeperWorkerIdProvider {
    private static final String ZK_ADDRESS = "zookeeper:2181"; // ZooKeeper 的地址
    private static final String WORKER_NODE_PATH = "/worker-nodes"; // 节点路径

    private CuratorFramework client;

    public ZookeeperWorkerIdProvider() {
        client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new ExponentialBackoffRetry(1000, 3));
        client.start();
    }

    public long[] registerWorker() throws Exception {
        // 确保根路径存在
        if (client.checkExists().forPath(WORKER_NODE_PATH) == null) {
            client.create().forPath(WORKER_NODE_PATH);
        }

        // 创建一个临时顺序节点，为每个启动的工作节点分配唯一的 WORKER_ID 和 DATACENTER_ID
        String path = client.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(WORKER_NODE_PATH + "/worker-node-", new byte[0]);

        // 从创建的路径获取 WORKER_ID 和 DATACENTER_ID
        String[] splitPath = path.split("-");
        long workerId = Long.parseLong(splitPath[splitPath.length - 1]) % 32; // 取最后的数字作为 WORKER_ID，32 是最大节点数
        long datacenterId = (Long.parseLong(splitPath[splitPath.length - 1]) / 32) % 32; // 计算 DATACENTER_ID

        return new long[] { workerId, datacenterId };
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }
}
