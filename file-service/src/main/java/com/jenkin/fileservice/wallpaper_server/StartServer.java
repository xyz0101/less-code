package com.jenkin.fileservice.wallpaper_server;


import com.jenkin.fileservice.wallpaper_server.server.Server;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 21:07
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Component
public class StartServer implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        synchronized (this){
            new Thread(()->{
                new Server().start(9010);
            }).start();
        }

    }

}
