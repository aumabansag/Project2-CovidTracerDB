
public class ProcessReport{
	
	int code = 0;
	String message = "Test";
	boolean status = false;

	public ProcessReport(){}

	public ProcessReport(int code, boolean status, String message){
		this.code = code;
		this.status = status;
		this.message = message;
	}

	public int getCode(){
		return code;
	}

	public String getMessage(){
		return message;
	}

	public boolean getStatus(){
		return status;
	}
}