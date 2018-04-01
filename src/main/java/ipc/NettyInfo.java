package ipc;

//记录每个netty的开启的端口和所在机器的ip

public class NettyInfo {
    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public NettyInfo(String ip, int port) {

        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "netty address if " + ip + ":" + port;
    }
}
