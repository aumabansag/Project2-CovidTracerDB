//package NCoV19TracerApp;

import javax.swing.JTable;

public class NCoV19TracerController{
	private NCoV19TracerUI ui;
	private NCoV19TracerModel model = null;

	public NCoV19TracerController(){}

	public NCoV19TracerController(NCoV19TracerUI ui){
		this.ui = ui;

		model = new NCoV19TracerModel();
	}

	public void addRow(String[] input){
		model.insertData(input);
	}

	public void updateRow(int id, String t_Out){
		model.updateData(id, t_Out);
	}

	public void getRows(){
		//get queried data by contact tracers
	}

	public int logIn(String name, String pass){
		return model.verifyLogin(name, pass);
	}

	public JTable tracerQuery(String contact, String type){
		int id;
		try{//id
			id = Integer.parseInt(contact);
		}catch(NumberFormatException ne){ //name
			id = getID(contact);
		}	
		if(id==0 || !model.personExists(id)){
			javax.swing.JOptionPane.showMessageDialog(null,"No person in the database!","Search Error", javax.swing.JOptionPane.ERROR_MESSAGE);
		}else{
			return model.getTable(getQueryType(type), id);
		}
		return null;
	}

	private void updateGUITable(){

	}

	//create an instance exists
	public boolean establishmentExists(String name){
		return model.estabExists(name);
	}

	public boolean userVisited(int id){
		return model.isIn(id);
	}

	private int getID(String name){
		return model.getID(name);
	}

	public NCoV19TracerModel getModel(){
		return model;
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