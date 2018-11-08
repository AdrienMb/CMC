/*
 * 13.09.2016 IParse gone, IScanner gone, minor editing
 * 11.10.2010 Small fix
 * 01.10.2010 Renamed
 * 21.10.2009 New folder structure
 * 24.10.2006 params in function declaration empty Declarations instead of null
 * 23.10.2006 IdList replaced by Declarations for function parameters
 * 22.10.2006 Error in parsePrimary fixed
 * 01.10.2006 AST introduced
 * 28.09.2006 Original version (based on Watt&Brown)
 */
 
package dk.via.jpe.intlang;


import dk.via.jpe.intlang.ast.*;


public class ParserAST
{
	private Scanner scan;
	
	
	private Token currentTerminal;
	
	
	public ParserAST( Scanner scan )
	{
		this.scan = scan;
		
		currentTerminal = scan.scan();
	}
	
	
	public AST parseProgram()
	{
		Block block = parseBlock();
		
		if( currentTerminal.kind != Token.EOT )
			System.out.println( "Tokens found after end of program" );
			
		return new Program( block );
	}
	
	
	private Block parseBlock()
	{
		accept( Token.DECLARE );
		Declarations decs = parseDeclarations();
		accept( Token.DO );
		Statements stats = parseStatements();
		accept( Token.OD );
		
		return new Block( decs, stats );
	}
	
	
	private Declarations parseDeclarations()
	{
		Declarations decs = new Declarations();
		
		while( currentTerminal.kind == Token.VAR ||
		       currentTerminal.kind == Token.FUNC )
			decs.dec.add( parseOneDeclaration() );
			
		return decs;
	}
	
	
	private Declaration parseOneDeclaration()
	{
		switch( currentTerminal.kind ) {
			case Token.VAR:
				accept( Token.VAR );
				Identifier id = parseIdentifier();
				accept( Token.SEMICOLON );
				
				return new VariableDeclaration( id );
				
			case Token.FUNC:
				accept( Token.FUNC );
				Identifier name = parseIdentifier();
				accept( Token.LEFTPARAN );
				
				Declarations params = null;
				if( currentTerminal.kind == Token.IDENTIFIER )
					params = parseIdList();
				else
					params = new Declarations();
					
				accept( Token.RIGHTPARAN );
				Block block = parseBlock();
				accept( Token.RETURN );
				Expression retExp = parseExpression();
				accept( Token.SEMICOLON );
				
				return new FunctionDeclaration( name, params, block, retExp );
				
			default:
				System.out.println( "var or func expected" );
				return null;
		}
	}
	
	
	private Declarations parseIdList()
	{
		Declarations list = new Declarations();
		
		list.dec.add( new VariableDeclaration( parseIdentifier() ) );
		
		while( currentTerminal.kind == Token.COMMA ) {
			accept( Token.COMMA );
			list.dec.add( new VariableDeclaration( parseIdentifier() ) );
		}
		
		return list;
	}
	
	
	private Statements parseStatements()
	{
		Statements stats = new Statements();
		
		while( currentTerminal.kind == Token.IDENTIFIER ||
		       currentTerminal.kind == Token.OPERATOR ||
		       currentTerminal.kind == Token.INTEGERLITERAL ||
		       currentTerminal.kind == Token.LEFTPARAN ||
		       currentTerminal.kind == Token.IF ||
		       currentTerminal.kind == Token.WHILE ||
		       currentTerminal.kind == Token.SAY )
			stats.stat.add( parseOneStatement() );
			
		return stats;
	}
	
	
	private Statement parseOneStatement()
	{
		switch( currentTerminal.kind ) {
			case Token.IDENTIFIER:
			case Token.INTEGERLITERAL:
			case Token.OPERATOR:
			case Token.LEFTPARAN:
				Expression exp = parseExpression();
				accept( Token.SEMICOLON );
				
				return new ExpressionStatement( exp );
				
			case Token.IF:
				accept( Token.IF );
				Expression ifExp = parseExpression();
				accept( Token.THEN );
				Statements thenPart = parseStatements();
				
				Statements elsePart = null;
				if( currentTerminal.kind == Token.ELSE ) {
					accept( Token.ELSE );
					elsePart = parseStatements();
				}
				
				accept( Token.FI );
				accept( Token.SEMICOLON );
				
				return new IfStatement( ifExp, thenPart, elsePart );
				
			case Token.WHILE:
				accept( Token.WHILE );
				Expression whileExp = parseExpression();
				accept( Token.DO );
				Statements stats = parseStatements();
				accept( Token.OD );
				accept( Token.SEMICOLON );
				
				return new WhileStatement( whileExp, stats );
				
			case Token.SAY:
				accept( Token.SAY );
				Expression sayExp = parseExpression();
				accept( Token.SEMICOLON );
				
				return new SayStatement( sayExp );
				
			default:
				System.out.println( "Error in statement" );
				return null;
		}
	}
	
	
	private Expression parseExpression()
	{
		Expression res = parsePrimary();
		while( currentTerminal.kind == Token.OPERATOR ) {
			Operator op = parseOperator();
			Expression tmp = parsePrimary();
			
			res = new BinaryExpression( op, res, tmp );
		}
		
	return res;
	}
	
	
	private Expression parsePrimary()
	{
		switch( currentTerminal.kind ) {
			case Token.IDENTIFIER:
				Identifier name = parseIdentifier();
				
				if( currentTerminal.kind == Token.LEFTPARAN ) {
					accept( Token.LEFTPARAN );
					
					ExpList args;
					
					if( currentTerminal.kind == Token.IDENTIFIER ||
					    currentTerminal.kind == Token.INTEGERLITERAL ||
					    currentTerminal.kind == Token.OPERATOR ||
					    currentTerminal.kind == Token.LEFTPARAN )
					    
						args = parseExpressionList();
					else
						args = new ExpList();
						
					
					accept( Token.RIGHTPARAN );
					
					return new CallExpression( name, args );
				} else
					return new VarExpression( name );
				
			case Token.INTEGERLITERAL:
				IntegerLiteral lit = parseIntegerLiteral();
				return new IntLitExpression( lit );
				
			case Token.OPERATOR:
				Operator op = parseOperator();
				Expression exp = parsePrimary();
				return new UnaryExpression( op, exp );
				
			case Token.LEFTPARAN:
				accept( Token.LEFTPARAN );
				Expression res = parseExpression();
				accept( Token.RIGHTPARAN );
				return res;
				
			default:
				System.out.println( "Error in primary" );
				return null;
		}
	}
	
	
	private ExpList parseExpressionList()
	{
		ExpList exps = new ExpList();
		
		exps.exp.add( parseExpression() );
		while( currentTerminal.kind == Token.COMMA ) {
			accept( Token.COMMA );
			exps.exp.add( parseExpression() );
		}
		
		return exps;
	}
	
	
	private Identifier parseIdentifier()
	{
		if( currentTerminal.kind == Token.IDENTIFIER ) {
			Identifier res = new Identifier( currentTerminal.spelling );
			currentTerminal = scan.scan();
			
			return res;
		} else {
			System.out.println( "Identifier expected" );
			
			return new Identifier( "???" );
		}
	}
	
	
	private IntegerLiteral parseIntegerLiteral()
	{
		if( currentTerminal.kind == Token.INTEGERLITERAL ) {
			IntegerLiteral res = new IntegerLiteral( currentTerminal.spelling );
			currentTerminal = scan.scan();
			
			return res;
		} else {
			System.out.println( "Integer literal expected" );
			
			return new IntegerLiteral( "???" );
		}
	}
	
	
	private Operator parseOperator()
	{
		if( currentTerminal.kind == Token.OPERATOR ) {
			Operator res = new Operator( currentTerminal.spelling );
			currentTerminal = scan.scan();
			
			return res;
		} else {
			System.out.println( "Operator expected" );
			
			return new Operator( "???" );
		}
	}
	
	
	private void accept( byte expected )
	{
		if( currentTerminal.kind == expected )
			currentTerminal = scan.scan();
		else
			System.out.println( "Expected token of kind " + expected );
	}
}