
package be.jedi.jvspherecontrol;
import java.util.Enumeration;
import java.util.Hashtable;

import be.jedi.jvspherecontrol.commands.*;


public class JVsphereControl {

	Hashtable<String, Class> commands;

	String mainArgs[];
	String subCommand;
	String commandClass;
	AbstractCommand command;


	public JVsphereControl(String[] args) {
		mainArgs=args;
			registerCommands();
	}

	public static void main(String[] args) {

		JVsphereControl jmachineControl=new JVsphereControl(args);

		try {
			jmachineControl.validateArgs();
			jmachineControl.execute();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	 void execute() {
			

				if (command!=null) {
					 System.out.println("executing");
					command.execute();
				}
		
	}

	void validateArgs() {
	
		String subArgs[];
		
		if (mainArgs.length>0) {
			//We need the subcommand 
			subCommand=mainArgs[0];
	
			commandClass=commands.get(subCommand).getName();
			System.out.println("Executing class = "+commandClass);
			
			if (mainArgs.length>1) {
				subArgs=new String[mainArgs.length-1];
				System.arraycopy(mainArgs, 1, subArgs, 0,mainArgs.length-1);
			} else {
				subArgs=null;
				//we should print the commands and say <command> help
			}
			
			//prepare a command

			try {
				command = (AbstractCommand) Class.forName(commandClass).newInstance();
				command.init(subArgs);
				
				if (command!=null) {
					command.validateArgs();
				}

			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.err.println("We need at least one arg");
			printAvailableCommands();
		}		
	}
	
	void registerCommands() {
		commands=new Hashtable<String, Class>();

		this.registerCommand(ListVsphereCommand.class);

		this.registerCommand(CreateVmCommand.class);
		this.registerCommand(SendVncTextCommand.class);
		this.registerCommand(ActivateVncInVmCommand.class);
		this.registerCommand(DeActivateVncInVmCommand.class);

		
		//	this.registerCommand(KickstartVmCommand.class);
		//	this.registerCommand(OmapiRegisterCommand.class);	

	}

	void printAvailableCommands() {
		System.out.println("The following commands are available:");
		
		Enumeration<String> e = commands.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			// do whatever you need with the pair, like
			System.out.println("'" + key + "'");
		}
	}
	
	
	void registerCommand(Class commandClass) {
			try {
				try {
					commands.put((String)commandClass.getDeclaredField("keyword").get(null), commandClass);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
	}	


}