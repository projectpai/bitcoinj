### PAIcoin Demo App

## Build

paidemo is built into self containing runnable jar file. It can be run with:

	java -jar paidemo.jar
	
## Usage

paidemo is made as simple CLI Shell like application. When it is started small shell will be shown. In shell different command can be entered.

List of all commands can be print with following command:

	pai_demo> ?list
	
For each command detailed information can be provided with command:

	pai_demo> ?help <command_name>
	
Exit of programm is possible with:

	pai_demo> exit

Commands are executed by entering command name and arguments of commands, for example:

	pai_demo> send lk9834yrwueh9hfsd 0.09
	
Will send 0.09 coins to provided address.