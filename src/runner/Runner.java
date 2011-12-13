package runner;

import connect.SendReceive;

public class Runner {

	
	
	public static void main(String[] args) {
//		String projectPath = "";
//		String externPath = "C:/Users/BlueDragon/Desktop/Studium SS11/VS/Aufgaben/externalModules/";
//		
//		String classQuelle = "datasource.DataSource.class";
//		String classSenke = "datasink.DataSink.class";
//		String classSendRceive = "connect.SendReceive.class";
		
		
		//args={dataSource, dataSink, MC_address, MC_port}
		args = new String[]{"11001","11002","225.10.1.2","15022"};
		
		
		SendReceive.main(args);
		
		
	}
	
}
