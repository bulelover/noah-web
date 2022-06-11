package org.noah.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * Created by jframework on 2017/11/30.
 */
@Component
public class AppInit implements CommandLineRunner {
    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String path;

    @Value("${knife4j.production}")
    private boolean production;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("-- Start success -----------------------------------------");
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("  Application running at:\n" +
                "\t- Local: http://localhost:" + port + path + "/\n" +
                "\t- Network: http://" + ip + ":" + port + path + "/");
        if(!this.production) {
            System.out.println("\t- ApiDoc: http://" + ip + ":" + port + path + "/doc.html");
        }
        System.out.println("----------------------------------------------------------");
    }


}

