package command;


public class RunScript {
	
	private String ScriptFilename;
	private String OutputFilename = "";
	
	public RunScript(String ScriptFilename,String[] Args) {
		this.ScriptFilename = ScriptFilename;
		if (Args.length >= 2)
			if (Args[0].equalsIgnoreCase("-o"))
				OutputFilename = Args[1];
	}

	public String GetScriptFilename() {
		return this.ScriptFilename;
	}
	public String GetOutputFilename() {
		return this.OutputFilename;
	}
}
