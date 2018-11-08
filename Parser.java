/*
 * 30.08.2016 IParse gone, IScanner gone, minor editing
 * 24.09.2010 IParser
 * 07.10.2009 New package structure
 * 02.10.2006 Small fix in parsePrimary()
 * 28.09.2006 Original version (based on Watt&Brown)
 */

package dk.via.jpe.intlang;


public class Parser
{
	private Scanner scan;


	private Token currentTerminal;


	public Parser( Scanner scan )
	{
		this.scan = scan;

		currentTerminal = scan.scan();
	}


	public void parseProgram()
	{
		parseBlock();

		if( currentTerminal.kind != Token.EOT )
			System.out.println( "Tokens found after end of program" );
	}


	private void parseBlock()
	{
		accept( Token.QUOTE );
		accept( Token.VARIABLES );
		accept( Token.COLONS );
		parseDeclarations();
		if(currentTerminal.kind == Token.FUNCTIONS  ) {
			accept( Token.FUNCTIONS );
			accept( Token.COLONS );
			parseFunctions();
		}
		accept( Token.EXECUTE );
		accept( Token.COLONS );
		parseStatements();
		accept( Token.QUOTE );
	}


	private void parseDeclarations()
	{
		removeNewLine();
		while( currentTerminal.kind == Token.NEW  )
			parseOneDeclaration();
	}


	private void parseOneDeclaration()
	{
		if( currentTerminal.kind==Token.NEW ) {
			accept( Token.NEW );
			if(currentTerminal.kind==Token.INT) {
				accept( Token.INT );
				accept( Token.IDENTIFIER );
				accept( Token.NEWLINE );
			}
			else if(currentTerminal.kind==Token.BOOLEAN) {
				accept( Token.BOOLEAN );
				accept( Token.IDENTIFIER );
				accept( Token.NEWLINE );
			}
			else if(currentTerminal.kind==Token.TAB) {
				accept( Token.TAB );
				accept( Token.IDENTIFIER );
				accept( Token.NEWLINE );
			}
			else {
				System.out.println( "int, boolean or tab expected" );
			}
		}
		else {
			System.out.println( "new expected" );
		}
	}

	private void parseFunctions()
	{
		removeNewLine();
		while( currentTerminal.kind == Token.NEW )
			parseOneFunction();
	}


	private void parseOneFunction()
	{
		if( currentTerminal.kind==Token.NEW ) {
			accept( Token.NEW );
			accept( Token.IDENTIFIER );
			accept( Token.COLONS );
			parseBlock();
			accept( Token.NEWLINE );
			accept( Token.RESULT );
			accept( Token.IDENTIFIER );
			accept( Token.NEWLINE );
		}

		else {
			System.out.println( "var or func expected" );
		}
	}




	/*private void parseIdList()
	{
		accept( Token.IDENTIFIER );

		while( currentTerminal.kind == Token.COMMA ) {
			accept( Token.COMMA );
			accept( Token.IDENTIFIER );
		}
	} */


	private void parseStatements()
	{
		removeNewLine();
		while( currentTerminal.kind == Token.IDENTIFIER ||
				currentTerminal.kind == Token.OPERATOR ||
				currentTerminal.kind == Token.INTEGERLITERAL ||
				currentTerminal.kind == Token.LEFTPARAN ||
				currentTerminal.kind == Token.IF ||
				currentTerminal.kind == Token.WHILE ||
				currentTerminal.kind==Token.DISPLAY)
			parseOneStatement();
	}


	private void parseOneStatement()
	{
		switch( currentTerminal.kind ) {
		case Token.IDENTIFIER:
		case Token.INTEGERLITERAL:
		case Token.OPERATOR:
		case Token.LEFTPARAN:
			parseExpression();
			accept(Token.NEWLINE);
			break;

		case Token.IF:
			accept( Token.IF );
			parseExpression();
			accept( Token.DO );
			accept( Token.COLONS);
			accept( Token.QUOTE );
			parseStatements();
			accept( Token.QUOTE );
			removeNewLine();
			if( currentTerminal.kind == Token.ELSE ) {
				accept( Token.ELSE );
				accept( Token.COLONS );
				accept( Token.QUOTE );
				parseStatements();
				accept( Token.QUOTE );
			}
			removeNewLine();
			break;

		case Token.WHILE:
			accept( Token.WHILE );
			parseExpression();
			accept( Token.DO );
			accept( Token.COLONS );
			accept( Token.QUOTE );
			parseStatements();
			accept( Token.QUOTE );
			accept(Token.NEWLINE);
			break;

		case Token.DISPLAY:
			accept( Token.DISPLAY);
			parseExpression();
			accept(Token.NEWLINE);
			break;
			
		default:
			System.out.println( "Error in statement" );
			break;
		}
	}


	private void parseExpression()
	{
		parsePrimary();
		while( currentTerminal.kind == Token.OPERATOR || currentTerminal.kind==Token.QUESTION) {
			if(currentTerminal.kind==Token.QUESTION) {
				accept(Token.QUESTION);}
			else {
				accept( Token.OPERATOR );
				parsePrimary();
			}
		}
	}


	private void parsePrimary()
	{
		switch( currentTerminal.kind ) {
		case Token.IDENTIFIER:
			accept( Token.IDENTIFIER );

			if( currentTerminal.kind == Token.LEFTPARAN ) {
				accept( Token.LEFTPARAN );

				if( currentTerminal.kind == Token.IDENTIFIER ||
						currentTerminal.kind == Token.INTEGERLITERAL ||
						currentTerminal.kind == Token.OPERATOR ||
						currentTerminal.kind == Token.LEFTPARAN )
					parseExpressionList();


				accept( Token.RIGHTPARAN );
				/*if (currentTerminal.kind==Token.QUOTE)
					accept( Token.QUOTE );*/
			}
			break;

		case Token.INTEGERLITERAL:
			accept( Token.INTEGERLITERAL );
			/*if (currentTerminal.kind==Token.QUOTE)
				accept( Token.QUOTE );*/
			break;

		case Token.OPERATOR:
			accept( Token.OPERATOR );
			parsePrimary();
			/*if (currentTerminal.kind==Token.QUOTE)
				accept( Token.QUOTE );*/
			break;

		case Token.LEFTPARAN:
			accept( Token.LEFTPARAN );
			parseExpressionList();
			accept( Token.RIGHTPARAN );
			/*
			if (currentTerminal.kind==Token.QUOTE)
				accept( Token.QUOTE );*/
			break;

		/*case Token.QUESTION:
			accept(Token.QUESTION);
			if (currentTerminal.kind==Token.QUOTE)
				accept( Token.QUOTE );
			accept( Token.NEWLINE );
			break;*/
			
		default:
			System.out.println( "Error in primary" );
			break;
		}
	}


	private void parseExpressionList()
	{
		removeNewLine();
		parseExpression();
		while( currentTerminal.kind == Token.COMMA ) {
			accept( Token.COMMA );
			parseExpression();
		}
	}


	private void accept( byte expected )
	{
		System.out.println(currentTerminal.spelling);
		while (currentTerminal.kind == Token.NEWLINE && currentTerminal.kind != expected) {
			currentTerminal = scan.scan();
		}
		if( currentTerminal.kind == expected ) 
			currentTerminal = scan.scan();

		else
			System.out.println( "Expected token of kind " + expected + " instead of "+currentTerminal.kind);
	}
	
	
	private void removeNewLine() {
		while (currentTerminal.kind == Token.NEWLINE) 
			currentTerminal = scan.scan();
	}
}