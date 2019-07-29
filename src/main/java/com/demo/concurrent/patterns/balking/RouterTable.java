package com.demo.concurrent.patterns.balking;

import java.util.Set;
import java.util.concurrent.*;

/**
 * 路由表的自动保存
 */
public class RouterTable {

    //key：接口名
    //value：路由集合
    ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> rt = new ConcurrentHashMap<>();

    //路由表是否发生变化
    volatile boolean changed;

    //将路由表写入本地文件的线程
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();


    public void startLocalServer() {
        ses.scheduleWithFixedDelay(this::autoSave, 1, 1, TimeUnit.MINUTES);
    }

    public void autoSave() {
        if (!changed) {
            return;
        }
        changed = false;
        //将路由表写入本地文件
        this.save2Local();
    }

    public void remove(Router router) {
        Set<Router> set = rt.get(router.getIface());
        if (set != null) {
            set.remove(router);
            changed = true;
        }
    }

    public void add(Router router) {
        CopyOnWriteArraySet<Router> set =
                rt.computeIfAbsent(router.getIface(), r -> new CopyOnWriteArraySet<>());
        set.add(router);
        changed = true;
    }

    private void save2Local() {

    }

    class Router {

        private String ip;
        private String port;
        private String iface;

        public Router(String ip, String port, String iface) {
            this.ip = ip;
            this.port = port;
            this.iface = iface;
        }

        public String getIp() {
            return ip;
        }

        public String getPort() {
            return port;
        }

        public String getIface() {
            return iface;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Router) {
                Router router = (Router) o;
                return iface.equals(router.getIface()) &&
                        ip.equals(router.getIp()) &&
                        port.equals(router.getPort());
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = ip != null ? ip.hashCode() : 0;
            result = 31 * result + (port != null ? port.hashCode() : 0);
            result = 31 * result + (iface != null ? iface.hashCode() : 0);
            return result;
        }
    }

}
