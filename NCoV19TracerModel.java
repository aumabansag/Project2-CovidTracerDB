//package NCoV19TracerApp;

import java.sql.*;
import javax.sql.rowset.*;
import javax.swing.*;
import java.text.SimpleDateFormat;

public class NCoV19TracerModel{
	
    Connection connection;
    CachedRowSet courseListRowSet; // Contains data
    //ResultSetMetaData metadata;
    PreparedStatement stmt;
    int numcols, numrows;
	//Constructor
	public NCoV19TracerModel(){
		try{
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception e) {
            System.err.println("Unable to find and load driver");
            System.exit(1);
        }
        
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/covidDB",
                    "samson", "t325gh9QR*");
        } catch (SQLException sqlerr) {
            System.out.println(sqlerr.getSQLState()+":"+sqlerr.getErrorCode());
        }
        
        System.out.println("Connected Successfully");
        
        // try {
        //     connection.setAutoCommit(false);
        //     courseListRowSet = RowSetProvider.newFactory().createCachedRowSet();
        //     courseListRowSet.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        //     courseListRowSet.setConcurrency(ResultSet.CONCUR_UPDATABLE);
        //     courseListRowSet.setCommand("SELECT * FROM courselist");
        //     courseListRowSet.execute(connection);
            
        //     metadata = courseListRowSet.getMetaData();
        //     numcols = metadata.getColumnCount();
        //     numrows = courseListRowSet.size();
        //     courseListRowSet.first();
        // } catch (SQLException exp) {
        //     exp.printStackTrace();
        // }
	}

    public void insertData(String[] input){
        if(!personExists(Integer.parseInt(input[1]))){//add new person to person relation
            try{
                String sql = "INSERT INTO person VALUES(?,?,?,?,?)";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(input[1])); //id
                stmt.setString(2, input[0]); //name
                stmt.setString(3, input[2]);//address
                stmt.setInt(4, Integer.parseInt(input[3]));//age
                stmt.setInt(5, Integer.parseInt(input[4]));//contact #
            
                stmt.executeUpdate();
                stmt.close();
            }catch(SQLException sqle){
                sqle.getMessage();
                sqle.printStackTrace();
            }
        }

        //add to the visited relation
        try{
            String sql = "INSERT INTO visited VALUES(?,?,?,?,null)";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(input[1]));//person_id
            stmt.setInt(2, Integer.parseInt(input[7]));//establishment_id
            stmt.setDate(3, java.sql.Date.valueOf(input[6]));//new java.sql.Date((new SimpleDateFormat("yyyy-MM-dd").parse(input[6])).getTime()));
            stmt.setTime(4, java.sql.Time.valueOf(input[5]));
            
            stmt.executeUpdate(); 
            stmt.close();
        }catch(SQLException sqle){
            sqle.getMessage();
                sqle.printStackTrace();
        }
    }

    public void updateData(int id, String t_Out){
        try{
            String sql = "UPDATE visited SET time_out = ? WHERE person_id ="+id;
            stmt = connection.prepareStatement(sql);
            stmt.setTime(1, java.sql.Time.valueOf(t_Out));
            
            stmt.executeUpdate(); 
            stmt.close();
        }catch(SQLException sqle){
            sqle.getMessage();
                sqle.printStackTrace();
        }
    }

    public void deleteData(){}

    //check if person exists in the db
    public boolean personExists(int id){
        try{
            Statement st = connection.createStatement();
            String sql = "SELECT * FROM person WHERE id="+id;
            ResultSet rs = st.executeQuery(sql);
    
            if(rs.next())
                return true;
        }catch(SQLException sqe){}

        return false;
    }

    //creaetea a helper function
    public boolean estabExists(String name){
        try{
            Statement st = connection.createStatement();
            String sql = "SELECT * from establishment where name= '"+name+"'";
            ResultSet rf = st.executeQuery(sql);

            if(rf.next())
                return true;
        }catch(SQLException sqle){
            sqle.getMessage();
            sqle.printStackTrace();
        }

        return false;
    }

    public int verifyLogin(String name, String pass){
         try{
            Statement st = connection.createStatement();
            String sql = "SELECT * from establishment where name= '"+name+"' AND password= '"+pass+"'";
            ResultSet rg = st.executeQuery(sql);

            if(rg.next())
                return rg.getInt("id");
        }catch(SQLException sqle){
            sqle.getMessage();
            sqle.printStackTrace();
        }

        return 0;
    }

    public boolean isIn(int id){
        try{
            Statement st = connection.createStatement();
            String sql = "SELECT * from visited where person_id= "+id+" AND date= '"+
                            Date.valueOf(java.time.LocalDate.now())+"' AND time_out is null;";
            ResultSet rg = st.executeQuery(sql);

            if(rg.next())
                return true;
        }catch(SQLException sqle){
            sqle.getMessage();
            sqle.printStackTrace();
        }
        return false;
    }

    public int getRowCount(){
        return numrows;
    }

    public int getColumnCount(){
        return numcols;
    }

    public Connection getConnection(){
        return connection;
    }

	public static void main(String[] args){
		new NCoV19TracerModel();
	}


}