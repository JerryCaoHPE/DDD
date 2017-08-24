package ddd.develop;

 class Include
{
	public Include()
	{
	}
	/** 
	* @Title: Include 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param startIndex
	* @param endIndex
	* @param fileName 
	*/ 
	public Include(int startIndex, int endIndex, String command,String fileName)
	{
		super();
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.command = command;
		this.fileName = fileName;
	}
	public int startIndex;
	public int endIndex;
	public String command;
	public String fileName;
	@Override
	public String toString()
	{
		return "Include [startIndex=" + startIndex + ", endIndex=" + endIndex + ", command=" + command + ", fileName=" + fileName + "]";
	}
}