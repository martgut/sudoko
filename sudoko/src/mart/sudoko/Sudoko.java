package mart.sudoko;

public class Sudoko {
			
	private int fMaxLevel;
	private int fMaxValue;
	private int fVariants[];
	private int fRows;
	private int fCols;
	private int fSuds;
	
	private boolean fRowValueUsed[][];
	private boolean fColValueUsed[][];
	private boolean fSudValueUsed[][];
	
	public Sudoko() {
		fRows = 9;
		fCols = 9;

		fMaxValue = 9;
		fMaxLevel = fRows * fCols;
		fSuds = 9; 

		fVariants = new int[fMaxLevel];
		fRowValueUsed = new boolean[fRows][fMaxValue+1];
		fColValueUsed = new boolean[fCols][fMaxValue+1];
		fSudValueUsed = new boolean[fSuds][fMaxValue+1];
	}

	public void init()
	{
		for (int i=0; i< (fRows * fCols); i++)
		{
			System.out.println("l=" + i + " s=" + getSud(i));
		}
	}
	// Print current processed variant
	public void printVariant()
	{
		for(int i=0; i<fMaxLevel; i++)
		{
			System.out.print(fVariants[i]);
		}
		System.out.println();
	}

	// Print current processed variant
	public void prettyPrintVariant(boolean deep)
	{
		for(int i=0; i<fMaxLevel; i++)
		{
			if (i>0 && i%fCols==0)
				System.out.println();			
			System.out.print(fVariants[i]);
		}
		System.out.println();
		
		if (deep)
		{
			for(int i=0;i<fRows;i++){
				System.out.print("Row[" + i + "]:");
				for (int j=1;j<=fMaxValue; j++){
					if(fRowValueUsed[i][j])
						System.out.print("T");
					else
						System.out.print("F");
				}
				System.out.println();
			}
		}
		System.out.println();			
	}

	// Check whether this value is ==allowed
	boolean checkValue(int level, int value)
	{
		int row = getRow(level);
		int col = getCol(level);
		int sud = getSud(level);
		
		// If one, or both values are true, they are used!
		boolean used = fRowValueUsed[row][value] || 
					fColValueUsed[col][value] || 
					fSudValueUsed[sud][value];
		return !used;
	}	
	
	void setValue(int level, int value)
	{
		int row = getRow(level);
		int col = getCol(level);
		int sud = getSud(level);

		// Reset usage of old value
		int old = fVariants[level];
		fRowValueUsed[row][old] = false;
		fColValueUsed[col][old] = false;
		fSudValueUsed[sud][old] = false;

		// Mark these values as used
		fRowValueUsed[row][value] = true;
		fColValueUsed[col][value] = true;
		fSudValueUsed[sud][value] = true;
		fVariants[level] = value;	
	}
	
	int getRow(int level)
	{
		int row = level / fCols;
		return row;
	}
	
	int getCol(int level)
	{
		int col = level % fRows;
		return col;
	}
	
	int getSud(int level)
	{
		int row, col, sud;
		row = getRow(level);
		col = getCol(level);
		
		int j = (int)Math.sqrt(fSuds);
		
		sud = col / j + row / j * j;
		return sud;
	}

	// Iterate over all variants
	void iterateVariants()
	{
		boolean done=false;
		int level=0;
		boolean found;
		
		// Iterate until all variants have been processed
		while(!done)
		{
			// Iterate to find the next allowed value
			found = false;
			for (int value=fVariants[level]+1; value<=fMaxValue && !found; value++)
			{
				// Need to check whether this value is allowed
				if (checkValue(level, value))
				{
					// Great, we found a valid value on this level
					found = true;
					setValue(level, value);
					
					// Debug
					prettyPrintVariant(false);
					
					// We need to process the next level, or are we done?
					++level;
					if(level==fMaxLevel)  {
						// Yeah we are done!
						System.out.println("!!!SUCCESS!!!");
						done = true;
					}
				}
			}
			
			if (!found) {
				// Reached the max value on this level
				// Must go one step up and proceed from there
				setValue(level, 0);
				--level;
				if(level<0) {
					// OK, there is no solution!
					done = true;
				}
			}	
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello Sudoko");
		
		Sudoko sudoko = new Sudoko();
		sudoko.init();
		sudoko.iterateVariants();
	}
}
