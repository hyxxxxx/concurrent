package com.demo.concurrent.patterns.cow;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

/**
 * Created by Yasin H on 2019/7/28.
 */
public class CopyOnWriteDemo {

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

    /**
     * 路由表
     */
    class RouterTable {

        ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> tr = new ConcurrentHashMap<>();

        public Set<Router> get(String iface) {
            return tr.get(iface);
        }

        public void remove(Router router) {
            Set<Router> set = tr.get(router.getIface());
            if (set != null) {
                set.remove(router);
            }
        }

        public void add(Router router) {
            Set<Router> set =
                    tr.computeIfAbsent(router.getIface(), s -> new CopyOnWriteArraySet<>());
            set.add(router);
        }

    }

}
