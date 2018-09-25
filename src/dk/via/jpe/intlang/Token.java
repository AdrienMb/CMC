/*
 * 16.08.2016 Minor editing
 * 29.10.2009 New package structure
 * 22.10.2006 isAssignOp(), isAddOp(), isMulOp()
 * 28.09.2006 New keyword: return
 * 22.09.2006 Keyword recoqnition in constructor
 * 22.09.2006 ERROR added
 * 17.09.2006 Original Version (based on Example 4.2 in Watt&Brown)
 */
 
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
	
	public static final byte NEW = 3;
	public static final byte ASSIGN = 4;
	public static final byte IF = 5;
	public static final byte DO = 6;
	public static final byte ELSE = 7;
	public static final byte WHILE = 8;
	public static final byte DECLARE = 9;
	public static final byte RESULT = 10;
	
	public static final byte QUOTE = 11;
	public static final byte COLONS = 12;
	public static final byte COMMA = 15;
	public static final byte SEMICOLON = 16;
	public static final byte LEFTPARAN = 17;
	public static final byte RIGHTPARAN = 18;
	
	public static final byte EOT = 19;
	
	public static final byte ERROR = 20;
	
	
	private static final String SPELLINGS[] =
	{
		"<identifier>",
		"<integerliteral>",
		"<operator>",
		
		"new",
		"<-",
		"if",
		"do",
		"else",
		"while",
		"declare",
		"result",
		

		"\"",
		":",
		",",
		";",
		"(",
		")",
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