package dk.via.jpe.intlang;


public class Token
{
	public byte kind;
	public String spelling;
	
	
	public Token( byte kind, String spelling )
	{
		this.kind = kind;
		this.spelling = spelling;
		
		if( kind == IDENTIFIER )
			for( byte i = 0; i < SPELLINGS.length; ++i )
				if( spelling.equals( SPELLINGS[i] ) ) {
					this.kind = i;
					break;
				}
	}
	
	
	public boolean isAssignOperator()
	{
		if( kind == OPERATOR )
			return containsOperator( spelling, ASSIGNOPS );
		else
			return false;
	}
	
	public boolean isAddOperator()
	{
		if( kind == OPERATOR )
			return containsOperator( spelling, ADDOPS );
		else
			return false;
	}
	
	public boolean isMulOperator()
	{
		if( kind == OPERATOR )
			return containsOperator( spelling, MULOPS );
		else
			return false;
	}
	
	
	private boolean containsOperator( String spelling, String OPS[] )
	{
		for( int i = 0; i < OPS.length; ++i )
			if( spelling.equals( OPS[i] ) )
				return true;
				
		return false;
	}
	
	
	public static final byte IDENTIFIER = 0;
	public static final byte INTEGERLITERAL = 1;
	public static final byte OPERATOR = 2;
	
	public static final byte VARIABLES = 3;
	public static final byte FUNCTIONS = 4;
	public static final byte EXECUTE = 5;
	public static final byte NEW = 6;
	public static final byte IF = 7;
	public static final byte DO = 8;
	public static final byte ELSE = 9;
	public static final byte WHILE = 10;
	public static final byte DECLARE = 11;
	public static final byte RESULT = 12;
	public static final byte INT = 13;
	public static final byte BOOLEAN = 14;
	public static final byte TAB = 15;
	
	public static final byte QUOTE = 16;
	public static final byte COLONS = 17;
	public static final byte COMMA = 18;
	public static final byte NEWLINE = 19;
	public static final byte LEFTPARAN = 20;
	public static final byte RIGHTPARAN = 21;
	public static final byte QUESTION = 22;
	
	public static final byte DISPLAY = 23;
	
	public static final byte EOT = 24;
	
	public static final byte ERROR = 25;
	
	
	private static final String SPELLINGS[] =
	{
		"<identifier>",
		"<integerliteral>",
		"<operator>",
		
		"variables",
		"functions",
		"execute",
		"new",
		"if",
		"do",
		"else",
		"while",
		"declare",
		"result",
		"int",
		"boolean",
		"tab",
		

		"\"",
		":",
		",",
		"/n",
		"(",
		")",
		"?",
		"display",
		"<eot>",
		"<error>",
	};
	
	
	private static final String ASSIGNOPS[] =
	{
		"<-",
	};
	
	private static final String ADDOPS[] =
	{
		"+",
		"-",
	};
	
	private static final String MULOPS[] =
	{
		"*",
		"/",
		"%",
	};
}