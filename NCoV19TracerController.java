//package NCoV19TracerApp;

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

	public boolean establishmentExists(String name){
		return model.estabExists(name);
	}

	public boolean userVisited(int id){
		return model.isIn(id);
	}

	public NCoV19TracerModel getModel(){
		return model;
	}
}