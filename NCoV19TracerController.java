//package NCoV19TracerApp;

import javax.swing.JTable;
import java.util.regex.*;

public class NCoV19TracerController{
	private NCoV19TracerUI ui;
	private NCoV19TracerModel model = null;
	public int responseCode = 200;

	public NCoV19TracerController(){}

	public NCoV19TracerController(NCoV19TracerUI ui){
		this.ui = ui;
		model = new NCoV19TracerModel(this);
		while(responseCode==1045){
			String[] creden = ui.askCreden();
			model = new NCoV19TracerModel(this, creden[0], creden[1]);
		}
	}

	public void addRow(String[] input){
		model.insertData(input);
	}

	public boolean regEstablishment(String[] input){
		return model.regEst(input);
	}

	public void updateRow(int id, String t_Out){
		model.updateData(id, t_Out);
	}

	public int logIn(String name, String pass){
		if(model.getConnection()==null){
			javax.swing.JOptionPane.showMessageDialog(null,"Please try again later.","Database Error",
				 javax.swing.JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		return model.verifyLogin(name, pass);
	}

	public JTable tracerQuery(String contact, String type, String from, String to){
		try{
			int id = Integer.parseInt(contact);

			if(!model.personExists(id)){
				javax.swing.JOptionPane.showMessageDialog(null,"No person in the database!","Search Error", 
					javax.swing.JOptionPane.ERROR_MESSAGE);
			}else{
				return model.getTable(getQueryType(type), id, from, to);
			}
		}catch(NumberFormatException ne){ //name
			javax.swing.JOptionPane.showMessageDialog(null,"Please enter a valid ID number.","Search Error",
				 javax.swing.JOptionPane.ERROR_MESSAGE);
			return null;
		}	
		return null;
	}
	
	public boolean isIDValid(String id){
		try{
			Integer.parseInt(id);
			return true;
		}catch(NumberFormatException nfe){
			return false;
		}
	}

	public boolean isPhoneValid(String phone){
		if(Pattern.matches("^(09)\\d{9}|(\\+639)\\d{9}$",phone))
			return true;
		else
			return false;
	}

	public boolean establishmentExists(int id){
		return model.estabExists(id);
	}

	public boolean establishmentVerified(int id){
		return id>-1;
	}

	public boolean userVisited(int id){
		return model.isIn(id);
	}

	private int getID(String name){
		return model.getPersonID(name);
	}

	public NCoV19TracerModel getModel(){
		return model;
	}

	public int getCode(){
		return responseCode;
	}

	public void setCode(int code){
		this.responseCode = code;
	}

	private int getQueryType(String type){
		if(type.equals("1st-Level Contact"))
			return 0;
		else if(type.equals("2nd-Level Contact"))
			return 1;
		else
			return 2;
	}
}