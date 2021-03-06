package dbfit.fixture;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import dbfit.environment.DbEnvironmentFactory;
import dbfit.environment.DBEnvironment;
import dbfit.util.*;
import fit.Binding;
import fit.Parse;

public class Insert extends fit.Fixture {
	private DBEnvironment environment;
	private PreparedStatement statement;
	private String tableName;
	private DbParameterAccessor[] accessors;
	private Binding[] columnBindings;
    public Insert()
    {
        this.environment = DbEnvironmentFactory.getDefaultEnvironment();
    }
    public Insert(DBEnvironment dbEnvironment)
    {
        this.environment=dbEnvironment;
    }
	public Insert(DBEnvironment dbEnvironment, String tableName) {
		this.tableName= tableName;
		this.environment = dbEnvironment;		
	}
	public PreparedStatement BuildInsertCommand(String tableName, DbParameterAccessor[] accessors) throws SQLException {
		String ins=environment.buildInsertCommand(tableName, accessors);
		PreparedStatement cs=
				(environment.supportsOuputOnInsert())?
							environment.getConnection().prepareCall(ins):
							environment.getConnection().prepareStatement(ins,Statement.RETURN_GENERATED_KEYS);
		for (int i=0; i<accessors.length; i++){
			accessors[i].bindTo(this, cs, i+1);
		}
		return cs;
	}
	public void doRows(Parse rows) {
		// if table not defined as parameter, read from fixture argument; if still not defined, read from first row
        if ((tableName==null || tableName.trim().length()==0) && args.length > 0)
        {
            tableName = args[0];
        }
		else if (tableName == null) {
				tableName=rows.parts.text();
				rows = rows.more;
		}
        try {
			initParameters(rows.parts);//init parameters from the first row			
	        statement= BuildInsertCommand(tableName, accessors);
	        Parse row = rows;
			while ((row = row.more) != null) {				
				runRow(row);
			}			
		//rows.Parts.Last.More=new Parse("td",Gray("Generated Query: " + command.CommandText),null,null);
        }
        catch (Throwable e){
        	e.printStackTrace();
        	exception(rows.parts,e);
        }
      }
	
	private void initParameters(Parse headerCells) throws SQLException {			
		Map<String, DbParameterAccessor> allParams=
			environment.getAllColumns(tableName);
		if (allParams.isEmpty()){
			throw new SQLException("Cannot retrieve list of columns for "+tableName+" - check spelling and access rights");
		}
		accessors = new DbParameterAccessor[headerCells.size()];		
		columnBindings=new Binding[headerCells.size()];
		for (int i = 0; headerCells != null; i++, headerCells = headerCells.more) {
			String name=headerCells.text();
			String paramName= NameNormaliser.normaliseName(name);
			accessors[i] = allParams.get(paramName);			
			if (accessors[i]==null){
				wrong(headerCells);
				throw new SQLException("Cannot find column "+paramName);
			}
			if (headerCells.text().endsWith("?")){
				// if output parameters on insert are supported by the db environment, just switch
				// direction to output
				if (environment.supportsOuputOnInsert()){
						accessors[i].setDirection(DbParameterAccessor.OUTPUT);	
				}
				// else, use the autokey accessor
				else{
					accessors[i]=new DbAutoGeneratedKeyAccessor(accessors[i]);
				}
				columnBindings[i]=new SymbolAccessQueryBinding();
			}
			else
				columnBindings[i]=new SymbolAccessSetBinding();
        	columnBindings[i].adapter=accessors[i];
		}
	}
	private void runRow(Parse row)  throws Throwable{
		statement.clearParameters();
		Parse cell = row.parts;
		//first set input params
		for(int column=0; column<accessors.length; column++,	cell = cell.more){
			if (accessors[column].getDirection()==DbParameterAccessor.INPUT) {
				columnBindings[column].doCell(this, cell);
			}
		} 
		statement.execute();
		cell = row.parts;
		//next evaluate output params
		for(int column=0; column<accessors.length; column++, cell = cell.more){
			if (accessors[column].getDirection()==DbParameterAccessor.OUTPUT||
					accessors[column].getDirection()==DbParameterAccessor.RETURN_VALUE) {
				columnBindings[column].doCell(this, cell);
			}
		}							
	}
}
