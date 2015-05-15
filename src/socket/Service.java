package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.Constant;

public class Service {
	
	private ServerSocket serverSocket;
	private ExecutorService executorService;//线程池
	private final int POOL_SIZE = 50;//单个CPU线程池大小
	
	public Service() throws IOException{
		
		serverSocket = new ServerSocket(Constant.SERVICE_PORT);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
		//Runtime.getRuntime().availableProcessors()返回当前系统的CPU数目
		System.out.println("Server Start");
		
	}
	
	public void service(){
		
		while( true ){	
			Socket socket = null;
			try{
				//接受客户端连接，只要客户端连接，就会触发accpet()
				socket = serverSocket.accept();
				executorService.execute(new Handler(socket));
				
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
		
	}
	
	class Handler implements Runnable{

		private Socket socket;
		public Handler(Socket socket) {
			// TODO Auto-generated constructor stub
			this.socket = socket;
		}
		
		private PrintWriter getWriter(Socket socket) throws IOException{
			OutputStream socketOut = socket.getOutputStream();
			return new PrintWriter(socketOut, true);
		}
		
		private BufferedReader getReader(Socket socket) throws IOException{
			InputStream socketIn = socket.getInputStream();
			return new BufferedReader(new InputStreamReader(socketIn));
		}
		
		public String echo(String msg){
			return "Service   echo : "+msg;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				System.out.println("Service    :   new connection accepted   :  "+socket.getInetAddress()+"      port  : "+socket.getPort());
				BufferedReader br = getReader(socket);
				PrintWriter pw = getWriter(socket);
				String msg = null;
				
				while( (msg=br.readLine())!=null ){
					System.out.println("Service   msg : "+msg);
					pw.println(msg);
					if (msg.equals("bye")) {
						break;
					}
				}	
				pw.close();
				br.close();
				
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				try {
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
			
		}
		
	}
	
}
