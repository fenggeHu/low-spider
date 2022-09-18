package spider.base;

import lombok.Data;

/**
 * 格式：
 * socks5://user:passwd@127.0.0.1:1080
 * http://127.0.0.1:1080
 *
 * @author fengge.hu  @Date 2022/9/16
 **/
@Data
public class ProxyConfig {
    private String protocol;
    private String host;
    private int port;
    private String username;
    private String password;

    // 判断是否socks
    public boolean isSocks() {
        return this.protocol.toLowerCase().startsWith("socks");
    }

    // 解析proxy
    public static ProxyConfig getProxyConfig(String proxyUrl) {
        ProxyConfig proxyConfig = new ProxyConfig();
        int ipt = proxyUrl.indexOf("://");
        proxyConfig.setProtocol(proxyUrl.substring(0, ipt));
        String hpup = proxyUrl.substring(ipt + 3);
        int upippix = hpup.lastIndexOf("@");
        if (upippix < 0) {
            String[] hp = hpup.split(":");
            proxyConfig.setHost(hp[0]);
            proxyConfig.setPort(Integer.parseInt(hp[1]));
        } else {
            String up = hpup.substring(0, upippix);
            int uix = up.indexOf(":");
            proxyConfig.setUsername(up.substring(0, uix));
            proxyConfig.setPassword(up.substring(uix + 1));
            String ipp = hpup.substring(upippix + 1);
            String[] hp = ipp.split(":");
            proxyConfig.setHost(hp[0]);
            proxyConfig.setPort(Integer.parseInt(hp[1]));
        }

        return proxyConfig;
    }

}
