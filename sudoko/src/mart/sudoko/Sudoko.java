package mart.sudoko;

public class Sudoko {
			
	private int fMaxLevel;
	private int fMaxValue;
	private int fVariants[];
	private int fRows;
	private int fCols;
	
	private boolean fRowValueUsed[][];
	private boolean fColValueUsed[][];
	
	public Sudoko() {
		fRows = 4;
		fCols = 4;

		fMaxValue = 4;
		fMaxLevel = fRows * fCols;

		fVariants = new int[fMaxLevel];
		fRowValueUsed = new boolean[fRows][fMaxValue+1];
		fColValueUsed = new boolean[fCols][fMaxValue+1];
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
		
		// boolean used = !fRowValueUsed[row][value] && !fColValueUsed[col][value];
		// If one, or both values are true, they are used!
		boolean used = fRowValueUsed[row][value] || fColValueUsed[col][value];

		// prettyPrintVariant(true);
		// System.out.println("checkValue() level=" + level + " value=" + value + " used = " + used);

		return !used;
	}	
	
	void setValue(int level, int value)
	{
		int row = getRow(level);
		int col = getCol(level);

		// Reset usage of old value
		int old = fVariants[level];
		fRowValueUsed[row][old] = false;
		fColValueUsed[col][old] = false;

		// Mark these values as used
		fRowValueUsed[row][value] = true;
		fColValueUsed[col][value] = true;
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
				// Need to check whether this value is fRowValueUsed[row][value]allowed
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
		sudoko.iterateVariants();

	}
}
