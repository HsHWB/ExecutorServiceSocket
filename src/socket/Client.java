package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.Constant;

public class Client {

	public static void main(String[] args){
		int runTasks = 10;
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		for (int i = 0; i < runTasks; i++) {
			executorService.execute(createTask(i));
		}
	}
	
	//定义一个监听任务
	private static Runnable createTask(final int taskID){
		
		return new Runnable() {
			
			private Socket socket = null;
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Client   :   Task  :  "+taskID+"    start");
				
				try {
					socket  = new Socket(Constant.SERVICE_HOST, Constant.SERVICE_PORT);
					//发送指令
					OutputStream socketOut = socket.getOutputStream();
					socketOut.write("hello ".getBytes());
					
					//接收服务器反馈
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String msg = null;
					while( (msg = br.readLine()) != null ){
						System.out.println("Client     :    msg    :"+msg);
					}
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
		
	}
	
}
