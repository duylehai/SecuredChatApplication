package com.FinalProject.SecuredChaApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecuredChaApplication {

	public static void main(String[] args) {
//		Thread wsThread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				Map<String, Object> map = new HashMap<String, Object>();
//				Server server = new Server("localhost", 8081,"/ws", map , ChatEndpoint.class);
//				try {
//		            server.start();
//		            Thread.currentThread().join();
//		            System.out.println("Press any key to stop the server..");
//		            new Scanner(System.in).nextLine();
//		        } catch (Exception e) {
//		            throw new RuntimeException(e);
//		        } finally {
//		            server.stop();
//		        }
//			}
//			
//		});
		SpringApplication.run(SecuredChaApplication.class, args);
	}

}
