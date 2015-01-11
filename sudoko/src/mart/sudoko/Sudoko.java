package mart.sudoko;

public class Sudoko {
	
	// The Sudoko matrix holding all values
	private int fVariants[];
	
	// The array which defines the ordering
	// What cells to solve in which order
	private int fPathOrder[];

	// Maximum value of a cell (typically 9)
	private int fMaxValue;
	
	// Number of cells to resolve
	private int fMaxPath;
	
	// The size of our Sudoko
	private int fRows;
	private int fCols;
	
	// Number of Sudoko cells
	private int fSuds;
	
	private boolean fRowValueUsed[][];
	private boolean fColValueUsed[][];
	private boolean fSudValueUsed[][];
	
	// Statistics
	private int fChkVals;
	private int fSetVals;
	
	public Sudoko() {
		fRows = 9;
		fCols = 9;
		fSuds = 9; 

		fMaxValue = 9;
		fMaxPath  = 0;

		fVariants     = new int[fRows * fCols];
		fPathOrder    = new int[fRows * fCols];
		fRowValueUsed = new boolean[fRows][fMaxValue+1];
		fColValueUsed = new boolean[fCols][fMaxValue+1];
		fSudValueUsed = new boolean[fSuds][fMaxValue+1];
		
		fChkVals = 0;
		fSetVals = 0;
	}
	
	public void init(int matrix[])
	{
		// First init the Sudo with the Game values
		// int matrix[] = getMatrix();
		for (int i=0; i< matrix.length; i++)
		{
			int value = matrix[i];
			setValue(i, value);
			
			// Calculate a default path to solve that thing
			// This is really the most stupid one
			if(value==0) {
				// No value was set; Need to calculate it!#
				fPathOrder[fMaxPath++] = i;
			}
		}
	}

	// Print current processed variant
	public void prettyPrintVariant()
	{
		for(int i=0; i<fRows * fCols; i++)
		{
			if (i>0 && i%fCols==0)
				System.out.println();			
			System.out.print(fVariants[i]);
		}
		System.out.println();
		System.out.println();
	}

	// Check whether this value is ==allowed
	boolean checkValue(int level, int value)
	{
		++fChkVals;
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
		++fSetVals;
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
	
	// For a given cell index return the row value
	int getRow(int cellidx)
	{
		int row = cellidx / fCols;
		return row;
	}
	
	// For a given cell index return the column value
	int getCol(int cellidx)
	{
		int col = cellidx % fRows;
		return col;
	}
	
	// For a given cell index return the Sudoko identifier/number
	int getSud(int cellidx)
	{
		int row, col, sud;
		row = getRow(cellidx);
		col = getCol(cellidx);
		
		// ...I know only correct for certain constraints:
		int j = (int)Math.sqrt(fSuds);
		
		sud = col / j + row / j * j;
		return sud;
	}

	// Iterate over all variants
	boolean iterateVariants()
	{
		boolean done=false;
		int level=0;
		boolean found;
		
		// Iterate until all variants have been processed
		while(!done)
		{
			// Iterate to find the next allowed value
			found = false;
			int cellidx = fPathOrder[level];
			for (int value=fVariants[cellidx]+1; value<=fMaxValue && !found; value++)
			{
				// Need to check whether this value is allowed
				if (checkValue(cellidx, value))
				{
					// Great, we found a valid value on this level
					found = true;
					setValue(cellidx, value);
					
					// Debug
					prettyPrintVariant();
					
					// We need to process the next level, or are we done?
					++level;
					if(level==fMaxPath)  {
						// Yeah we are done!
						done = true;
					}
				}
			}
			
			if (!found) {
				// Reached the max value on this level
				// Must go one step up and proceed from there
				setValue(cellidx, 0);
				--level;
				if(level<0) {
					// OK, there is no solution!
					done = true;
				}
			}	
		}
		
		if (done) {
			System.out.println("!!!SUCCESS!!!");
			System.out.println("   Checked Values = " + fChkVals);
			System.out.println("   Set Values     = " + fSetVals);
		}
		return done;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int matrix0[] = new int[] {
				
				0, 0, 0,   0, 0, 0,   0, 0, 0,
				0, 0, 0,   0, 0, 0,   0, 0, 0,
				0, 0, 0,   0, 0, 0,   0, 0, 0,

				0, 0, 0,   0, 0, 0,   0, 0, 0,
				0, 0, 0,   0, 0, 0,   0, 0, 0,
				0, 0, 0,   0, 0, 0,   0, 0, 0,

				0, 0, 0,   0, 0, 0,   0, 0, 0,
				0, 0, 0,   0, 0, 0,   0, 0, 0,
				0, 0, 0,   0, 0, 0,   0, 0, 0
		};
		
		int matrix1[] = new int[] {
				
				0, 0, 1,   4, 0, 5,   2, 0, 0,
				0, 0, 7,   0, 0, 0,   1, 0, 0,
				2, 8, 0,   0, 0, 0,   0, 3, 6,
				
				1, 0, 0,   0, 4, 0,   0, 0, 7,
				0, 0, 0,   7, 0, 8,   0, 0, 0,
				9, 0, 0,   0, 6, 0,   0, 0, 8,
				
				4, 3, 0,   0, 0, 0,   0, 9, 2,
				0, 0, 5,   0, 0, 0,   3, 0, 0,
				0, 0, 9,   6, 0, 2,   7, 0, 0
		};
		
		int matrix2[] = new int[] {
				
				0, 7, 0,   0, 0, 3,   0, 6, 1,
				5, 0, 0,   8, 0, 0,   0, 0, 0,
				0, 0, 0,   0, 0, 6,   0, 2, 0,
				
				0, 3, 0,   0, 0, 0,   0, 0, 2,
				0, 0, 0,   0, 9, 7,   6, 0, 0,
				8, 0, 1,   0, 2, 0,   0, 0, 9,
				
				0, 0, 0,   0, 8, 0,   0, 0, 0,
				4, 0, 5,   0, 0, 0,   0, 0, 0,
				3, 0, 0,   9, 0, 1,   0, 0, 5
		};
		
		int matrix3[] = new int[] {
				
				0, 0, 0,   0, 0, 0,   6, 1, 9,
				0, 2, 3,   0, 9, 0,   0, 0, 0,
				0, 0, 0,   1, 4, 0,   0, 2, 0,

				0, 0, 9,   0, 0, 0,   0, 0, 0,
				7, 0, 8,   0, 0, 3,   0, 0, 0,
				0, 0, 0,   0, 0, 5,   3, 4, 0,

				0, 0, 0,   0, 0, 0,   4, 6, 7,
				8, 3, 0,   0, 0, 0,   0, 0, 0,
				0, 0, 0,   6, 1, 2,   0, 0, 0
		};
		
		int matrix4[] = new int[] {
				
				0, 0, 5,   8, 0, 0,   4, 0, 9,
				0, 0, 0,   0, 5, 0,   0, 0, 0,
				4, 0, 8,   2, 0, 0,   0, 0, 7,

				5, 0, 1,   0, 3, 0,   0, 0, 0,
				0, 3, 0,   5, 0, 8,   0, 6, 0,
				0, 0, 0,   0, 1, 0,   9, 0, 3,

				3, 0, 0,   0, 0, 9,   6, 0, 5,
				0, 0, 0,   0, 2, 0,   0, 0, 0,
				8, 0, 7,   0, 0, 5,   2, 0, 0
		};

		Sudoko sudoko = new Sudoko();
		sudoko.init(matrix4);
		sudoko.prettyPrintVariant();
		sudoko.iterateVariants();
	}
}
